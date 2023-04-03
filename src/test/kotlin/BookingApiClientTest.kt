import api.BookingApiClient
import fixtures.BookingFixture
import io.restassured.http.ContentType
import models.Booking
import models.BookingDates
import models.BookingId
import models.BookingResponse
import org.apache.http.HttpStatus
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.slf4j.LoggerFactory
import utils.Helpers.assertStatusCode
import utils.Helpers.assertStatusCodeAndContentType
import utils.Helpers.extractBodyAs

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookingApiClientTest : BaseTest() {
    private val logger = LoggerFactory.getLogger(BookingApiClientTest::class.java)

    private val bookingApiClient = BookingApiClient()
    private val defaultBookingRequestBody = BookingFixture.defaultBooking

    @BeforeAll
    fun setup() {
        logger.info(
            "Cleaning up all bookings default values with firstname ${defaultBookingRequestBody.firstName} and "
                    + "lastname ${defaultBookingRequestBody.lastName}!!"
        )
        val queryParams = mapOf(
            Pair("firstname", defaultBookingRequestBody.firstName),
            Pair("lastname", defaultBookingRequestBody.lastName)
        )
        val bookingIds = bookingApiClient.getBookingIds(queryParams)
        bookingIds.forEach {
            bookingApiClient.deleteBookingById(it.bookingId!!)
        }
    }

    @Test
    fun `should be able to create booking`() {
        val bookingResponse = bookingApiClient.createBookingResponse(defaultBookingRequestBody)
            .assertStatusCode()
            .extractBodyAs(BookingResponse::class.java)

        assertThat(bookingResponse.booking)
            .usingRecursiveComparison()
            .isEqualTo(defaultBookingRequestBody)

        assertThat(bookingResponse.bookingId).isNotNull

    }

    @Test
    fun `should be able to get booking by id`() {
        val bookingId = bookingApiClient.createBookingResponse(defaultBookingRequestBody)
            .assertStatusCode()
            .extractBodyAs(BookingResponse::class.java)
            .bookingId

        val bookingResponse = bookingApiClient.getBookingByIdResponse(bookingId.toString())
            .assertStatusCode()
            .extractBodyAs(Booking::class.java)

        assertThat(bookingResponse)
            .usingRecursiveComparison()
            .isEqualTo(defaultBookingRequestBody)
    }

    @Test
    fun `should be able to partially update booking`() {
        val firstName = "Updated_Foo"
        val lastName = "Updated_Bar"

        bookingApiClient.deleteBookingsByName(firstName, lastName)
        val bookingId = bookingApiClient.createAndGetBookingId(defaultBookingRequestBody)

        val updateRequestBody = Booking(
            firstName = firstName,
            lastName = lastName,
            bookingDates = BookingDates(
                checkIn = "2022-08-11",
                checkOut = "2022-08-20"
            )
        )

        val expectedResponse = updateRequestBody.copy(
            totalPrice = defaultBookingRequestBody.totalPrice,
            depositPaid = defaultBookingRequestBody.depositPaid,
            additionalNeeds = defaultBookingRequestBody.additionalNeeds
        )

        val actualResponse = bookingApiClient.partialUpdateBookingResponse(bookingId, updateRequestBody)
            .assertStatusCode()
            .extractBodyAs(Booking::class.java)

        assertThat(actualResponse)
            .usingRecursiveComparison()
            .isEqualTo(expectedResponse)
    }

    @Test
    fun `should be able to delete a booking`() {
        val bookingId = bookingApiClient.createAndGetBookingId()
        bookingApiClient.deleteBookingResponse(bookingId)
            .assertStatusCodeAndContentType(statusCode = HttpStatus.SC_CREATED, contentType = ContentType.TEXT)

        bookingApiClient.getBookingByIdResponse(bookingId)
            .assertStatusCode(statusCode = HttpStatus.SC_NOT_FOUND)
    }

    @Test
    fun `should be able to get all booking ids`() {
        val bookingId = bookingApiClient.createAndGetBookingId()
        val response = bookingApiClient.getBookingIdsResponse()
            .assertStatusCodeAndContentType()
            .extractBodyAs(Array<BookingId>::class.java)
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
        bookingApiClient.deleteBookingsByName(firstName, lastName)

        val bookingId = bookingApiClient.createAndGetBookingId(bookingRequestBody)
        val queryParams = mapOf(Pair("firstname", firstName), Pair("lastname", lastName))

        val actualResponse = bookingApiClient.getBookingIdsResponse(queryParams)
            .assertStatusCodeAndContentType()
            .extractBodyAs(Array<BookingId>::class.java)
            .toList()

        assertThat(actualResponse)
            .overridingErrorMessage(
                "Booking id with matching criteria $bookingId "
                        + "not present in list of booking ids"
            )
            .hasSize(1)
            .allMatch { it.bookingId == bookingId }


    }
}
