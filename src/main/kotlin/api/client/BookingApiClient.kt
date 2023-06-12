package api.client

import RestConstants.BOOKING_ENDPOINT
import api.BaseRequestSpecProvider
import arrow.core.Either
import arrow.core.right
import fixtures.BookingFixture
import io.restassured.http.ContentType
import io.restassured.http.Header
import io.restassured.http.Headers
import io.restassured.response.Response
import models.Booking
import models.BookingId
import models.BookingResponse
import org.apache.http.HttpStatus
import org.slf4j.LoggerFactory
import types.BasicAuthToken
import types.UserRole
import utils.Helpers.assertStatusCode
import utils.Helpers.assertStatusCodeAndContentType
import utils.Helpers.extractBodyAs

class BookingApiClient : BaseRequestSpecProvider(basePath = BOOKING_ENDPOINT) {
    private val logger = LoggerFactory.getLogger(BookingApiClient::class.java)


    fun createBooking(
        requestBody: Booking = BookingFixture.defaultBooking
    ): Response {
        val requestSpecification = requestSpecWithoutAuthHeader()
            .header("Content-Type", ContentType.JSON)
            .body(requestBody)
        return post(requestSpecification)
    }

    fun createAndGetBookingId(
        requestBody: Booking = BookingFixture.defaultBooking
    ): String {
        val requestSpecification = requestSpecWithoutAuthHeader()
            .header("Content-Type", ContentType.JSON)
            .body(requestBody)
        return post(requestSpecification)
            .assertStatusCodeAndContentType()
            .extractBodyAs<BookingResponse>()
            .bookingId.toString()
    }

    fun getBookingById(bookingId: String): Response = get(requestSpecWithoutAuthHeader(), "/$bookingId")

    fun getBookingIdsResponse(queryParams: Map<String, String> = mapOf()): Response {
        val requestSpecification = requestSpecWithoutAuthHeader()
            .queryParams(queryParams)
        return get(requestSpecification)
    }

    fun getBookingIds(queryParams: Map<String, String> = mapOf()): List<BookingId> =
        getBookingIdsResponse(queryParams)
            .assertStatusCodeAndContentType()
            .extractBodyAs<Array<BookingId>>()
            .toList()


    fun partialUpdateBooking(
        authMechanism: Either<BasicAuthToken, UserRole> = UserRole.ADMIN.right(),
        bookingId: String,
        updatedBooking: Booking
    ): Response {
        val headers = Headers(
            listOf(
                Header("Accept", ContentType.JSON.toString()),
                Header("Content-Type", ContentType.JSON.toString())
            )
        )
        val requestSpecification = requestSpec(authMechanism = authMechanism)
            .headers(headers)
            .body(updatedBooking)
        return patch(requestSpecification, "/$bookingId")
    }

    fun deleteBooking(
        authMechanism: Either<BasicAuthToken, UserRole> = UserRole.ADMIN.right(),
        bookingId: String
    ): Response {
        logger.info("Deleting booking with id $bookingId")
        return delete(requestSpec(authMechanism = authMechanism), bookingId)
    }

    fun deleteBookingById(bookingId: String) =
        deleteBooking(bookingId = bookingId)
            .assertStatusCode(HttpStatus.SC_CREATED)

    fun deleteBookingsByName(firstName: String, lastName: String) {
        val bookingIds = getBookingIds(mapOf(Pair("firstname", firstName), Pair("lastname", lastName)))
        bookingIds.forEach { deleteBookingById(it.bookingId!!) }
    }

}
