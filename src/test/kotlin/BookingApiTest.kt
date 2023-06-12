import api.client.BookingApiClient
import arrow.core.left
import arrow.core.right
import fixtures.BookingFixture.defaultBooking
import fixtures.BookingFixture.updateRequestBody
import io.restassured.http.ContentType
import models.Booking
import models.BookingDates
import models.BookingId
import models.BookingResponse
import org.apache.http.HttpStatus
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.slf4j.LoggerFactory
import types.BasicAuthToken
import types.UserRole
import utils.Helpers.assertStatusCode
import utils.Helpers.assertStatusCodeAndContentType
import utils.Helpers.extractBodyAs
import kotlin.math.exp

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookingApiTest : BaseTest() {
    private val logger = LoggerFactory.getLogger(BookingApiTest::class.java)

    private val bookingApiClient = BookingApiClient()

    @BeforeAll
    fun setup() {
        logger.info(
            "Cleaning up all bookings default values with firstname ${defaultBooking.firstName} and "
                    + "lastname ${defaultBooking.lastName}!!"
        )
        val queryParams = mapOf(
            Pair("firstname", defaultBooking.firstName),
            Pair("lastname", defaultBooking.lastName)
        )
        val bookingIds = bookingApiClient.getBookingIds(queryParams)
        bookingIds.forEach {
            bookingApiClient.deleteBookingById(it.bookingId!!)
        }
    }

    @Test
    fun `should be able to create booking`() {
        val bookingCreationResponse = bookingApiClient.createBooking(defaultBooking)
            .assertStatusCode()
            .extractBodyAs<BookingResponse>()

        assertThat(bookingCreationResponse.booking)
            .usingRecursiveComparison()
            .isEqualTo(defaultBooking)

        assertThat(bookingCreationResponse.bookingId).isNotNull
    }

    @Test
    fun `should be able to get booking by id`() {
        val bookingId = bookingApiClient.createBooking(defaultBooking)
            .assertStatusCode()
            .extractBodyAs<BookingResponse>()
            .bookingId

        val bookingResponse = bookingApiClient.getBookingById(bookingId.toString())
            .assertStatusCode()
            .extractBodyAs<Booking>()

        assertThat(bookingResponse)
            .usingRecursiveComparison()
            .isEqualTo(defaultBooking)
    }

    @Test
    fun `should not be able to partially update booking without valid authentication header`() {
        val bookingId = bookingApiClient.createAndGetBookingId(defaultBooking)

        bookingApiClient.partialUpdateBooking(
            authMechanism = BasicAuthToken(null).left(),
            bookingId,
            updateRequestBody
        )
            .assertStatusCode(HttpStatus.SC_FORBIDDEN)
    }

    @Test
    fun `should be able to partially update booking using ADMIN user`() {
        val bookingId = bookingApiClient.createAndGetBookingId(requestBody = defaultBooking)

        val actualResponseBody =
            bookingApiClient.partialUpdateBooking(authMechanism = UserRole.ADMIN.right(), bookingId, updateRequestBody)
                .assertStatusCode()
                .extractBodyAs<Booking>()

        assertThat(actualResponseBody)
            .usingRecursiveComparison()
            .isEqualTo(updateRequestBody)
    }

    @Test
    fun `should be able to partially update booking using valid basic auth header`() {
        val bookingId = bookingApiClient.createAndGetBookingId(defaultBooking)

        val actualResponse =
            bookingApiClient.partialUpdateBooking(authMechanism = BasicAuthToken().left(), bookingId, updateRequestBody)
                .assertStatusCode()
                .extractBodyAs<Booking>()

        assertThat(actualResponse)
            .usingRecursiveComparison()
            .isEqualTo(updateRequestBody)
    }

    @Test
    fun `should be able to delete a booking using ADMIN user`() {
        val bookingId = bookingApiClient.createAndGetBookingId()
        bookingApiClient.deleteBooking(authMechanism = UserRole.ADMIN.right(), bookingId = bookingId)
            .assertStatusCodeAndContentType(statusCode = HttpStatus.SC_CREATED, contentType = ContentType.TEXT)

        bookingApiClient.getBookingById(bookingId)
            .assertStatusCode(statusCode = HttpStatus.SC_NOT_FOUND)
    }

    @Test
    fun `should be able to delete a booking using valid basic auth header`() {
        val bookingId = bookingApiClient.createAndGetBookingId()
        bookingApiClient.deleteBooking(authMechanism = BasicAuthToken().left(), bookingId)
            .assertStatusCodeAndContentType(statusCode = HttpStatus.SC_CREATED, contentType = ContentType.TEXT)

        bookingApiClient.getBookingById(bookingId)
            .assertStatusCode(statusCode = HttpStatus.SC_NOT_FOUND)
    }

    @Test
    fun `should be able to get all booking ids`() {
        val bookingId = bookingApiClient.createAndGetBookingId()
        val response = bookingApiClient.getBookingIdsResponse()
            .assertStatusCodeAndContentType()
            .extractBodyAs<Array<BookingId>>()
            .toList()

        assertTrue(
            response.any { it.bookingId == bookingId },
            "Expected Booking id $bookingId to be present in list of all booking ids, but not found!"
        )
    }

    @Test
    fun `should be able to get all booking ids by firstname and last name`() {
        val firstName = "Jonny"
        val lastName = "Swaak"
        val bookingRequestBody = defaultBooking.copy(
            firstName = firstName,
            lastName = lastName,
        )
        bookingApiClient.deleteBookingsByName(firstName, lastName)

        val bookingId = bookingApiClient.createAndGetBookingId(bookingRequestBody)
        val queryParams = mapOf("firstname" to firstName, "lastname" to lastName)

        val actualResponse = bookingApiClient.getBookingIdsResponse(queryParams)
            .assertStatusCodeAndContentType()
            .extractBodyAs<Array<BookingId>>()
            .toList()

        assertThat(actualResponse)
            .overridingErrorMessage(
                "Booking id with matching criteria $bookingId "
                        + "not present in list of booking ids"
            )
            .allMatch { it.bookingId == bookingId }
    }

    @AfterAll
    fun tearDown() {
        bookingApiClient.deleteBookingsByName(updateRequestBody.firstName, updateRequestBody.lastName)
        logger.info(
            "Deleted all bookings for user with first name ${updateRequestBody.firstName} "
                    + "and last name ${updateRequestBody.lastName}"
        )
    }
}
