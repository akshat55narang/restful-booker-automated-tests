import api.BookingApi
import fixtures.BookingFixture
import helpers.TestHelpers.assertBookingResponse
import models.Booking
import models.BookingDates
import models.BookingId
import models.BookingResponse
import org.apache.http.HttpStatus
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.slf4j.LoggerFactory

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookingApiTest : BaseTest() {
    private val logger = LoggerFactory.getLogger(BookingApiTest::class.java)

    private val bookingApi = BookingApi()
    private val defaultBookingRequestBody = BookingFixture.defaultBooking

    @BeforeAll
    fun setup() {
        logger.info("Cleaning up all bookings for user !!")
        val queryParams = mapOf(
            Pair("firstname", defaultBookingRequestBody.firstName),
            Pair("lastname", defaultBookingRequestBody.lastName)
        )
        val bookingApi = BookingApi()
        val bookingIds = bookingApi.getBookingIds(queryParams)
        bookingIds.forEach {
            bookingApi.deleteBooking(it.bookingId!!)
        }
    }


    @Test
    fun `should be able to create booking`() {

        val bookingResponseBody = bookingApi.createBookingResponse(defaultBookingRequestBody)
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_OK)
            .extract()
            .response()
            .`as`(BookingResponse::class.java)
            .booking

        assertBookingResponse(
            expectedFirstName = defaultBookingRequestBody.firstName,
            expectedLastName = defaultBookingRequestBody.lastName,
            expectedTotalPrice = defaultBookingRequestBody.totalPrice,
            expectedDepositPaid = defaultBookingRequestBody.depositPaid,
            expectedCheckInDate = defaultBookingRequestBody.bookingDates.checkIn,
            expectedCheckOutDate = defaultBookingRequestBody.bookingDates.checkOut,
            expectedAdditionalNeeds = defaultBookingRequestBody.additionalNeeds,
            actualBookingResponse = bookingResponseBody
        )

    }

    @Test
    fun `should be able to get booking by id`() {
        val bookingId = bookingApi.createBookingResponse(defaultBookingRequestBody)
            .then()
            .statusCode(HttpStatus.SC_OK)
            .extract()
            .response()
            .`as`(BookingResponse::class.java)
            .bookingId
        val bookingResponse = bookingApi.getBookingByIdResponse(bookingId.toString())
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_OK)
            .extract()
            .response()
            .`as`(Booking::class.java)

        assertBookingResponse(
            expectedFirstName = defaultBookingRequestBody.firstName,
            expectedLastName = defaultBookingRequestBody.lastName,
            expectedTotalPrice = defaultBookingRequestBody.totalPrice,
            expectedDepositPaid = defaultBookingRequestBody.depositPaid,
            expectedCheckInDate = defaultBookingRequestBody.bookingDates.checkIn,
            expectedCheckOutDate = defaultBookingRequestBody.bookingDates.checkOut,
            expectedAdditionalNeeds = defaultBookingRequestBody.additionalNeeds,
            actualBookingResponse = bookingResponse
        )

    }

    @Test
    fun `should be able to partially update booking`() {
        val bookingId = bookingApi.createAndGetBookingId()
        val bookingUpdateRequestBody = Booking(
            firstName = "Updated_Foo",
            lastName = "Updated_Bar",
            bookingDates = BookingDates(
                checkIn = "2022-08-11",
                checkOut = "2022-08-20"
            )
        )
        val response = bookingApi.partialUpdateBookingResponse(bookingId, bookingUpdateRequestBody)
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_OK)
            .extract()
            .response()
            .`as`(Booking::class.java)

        assertEquals("Updated_Foo", response.firstName)
        assertEquals("Updated_Bar", response.lastName)
        assertEquals("2022-08-11", response.bookingDates.checkIn)
        assertEquals("2022-08-20", response.bookingDates.checkOut)

    }

    @Test
    fun `should be able to delete a booking`() {
        val bookingId = bookingApi.createAndGetBookingId()
        bookingApi.deleteBookingResponse(bookingId)
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_CREATED)

        bookingApi.getBookingByIdResponse(bookingId)
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_NOT_FOUND)
    }

    @Test
    fun `should be able to get all booking ids`() {
        val bookingId = bookingApi.createAndGetBookingId()
        val response = bookingApi.getBookingIdsResponse()
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_OK)
            .extract()
            .body()
            .`as`(Array<BookingId>::class.java)
            .toList()

        assertTrue(
            response.any { it.bookingId == bookingId },
            "Booking id with matching criteria $bookingId not present in list of booking ids"
        )
    }

    @Test
    fun `should be able to get all booking ids by firstname and last name`() {
        val firstName = "Jonny"
        val lastName = "Swaak"
        val bookingRequestBody = defaultBookingRequestBody.copy(
            firstName = firstName,
            lastName = lastName,
        )
        val bookingId = bookingApi.createAndGetBookingId(bookingRequestBody)
        val queryParams = mapOf(Pair("firstname", firstName), Pair("lastname", lastName))
        val response = bookingApi.getBookingIdsResponse(queryParams)
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_OK)
            .extract()
            .body()
            .`as`(Array<BookingId>::class.java)
            .toList()

        assertTrue(
            response.any { it.bookingId == bookingId },
            "Booking id with matching criteria $bookingId not present in list of booking ids"
        )
    }
}
