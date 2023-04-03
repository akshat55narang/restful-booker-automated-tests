package api

import RestConstants.BOOKING_API_PATH
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
import utils.Helpers.assertStatusCode
import utils.Helpers.assertStatusCodeAndContentType
import utils.Helpers.extractBodyAs

class BookingApiClient() : BaseRequestSpecProvider(basePath = BOOKING_API_PATH) {
    private val logger = LoggerFactory.getLogger(BookingApiClient::class.java)

    companion object {
        private val accessToken = AuthApiClient().generateAccessToken()
    }

    fun createBookingResponse(
        requestBody: Booking = BookingFixture.defaultBooking
    ): Response {
        val requestSpecification = baseRequestSpecificationWithoutToken()
            .header("Content-Type", ContentType.JSON)
            .body(requestBody)
        return post(requestSpecification)
    }

    fun createAndGetBookingId(
        requestBody: Booking = BookingFixture.defaultBooking
    ): String {
        val requestSpecification = baseRequestSpecificationWithoutToken()
            .header("Content-Type", ContentType.JSON)
            .body(requestBody)
        return post(requestSpecification)
            .assertStatusCodeAndContentType()
            .extractBodyAs(BookingResponse::class.java)
            .bookingId.toString()
    }

    fun getBookingByIdResponse(bookingId: String): Response = get(baseRequestSpecificationWithoutToken(), "/$bookingId")

    fun getBookingIdsResponse(queryParams: Map<String, String> = mapOf()): Response {
        val requestSpecification = baseRequestSpecificationWithoutToken()
            .queryParams(queryParams)
        return get(requestSpecification)
    }

    fun getBookingIds(queryParams: Map<String, String> = mapOf()): List<BookingId> =
        getBookingIdsResponse(queryParams)
            .assertStatusCodeAndContentType()
            .extractBodyAs(Array<BookingId>::class.java)
            .toList()


    fun partialUpdateBookingResponse(bookingId: String, updatedBooking: Booking): Response {
        val headers = Headers(
            listOf(
                Header("Accept", ContentType.JSON.toString()),
                Header("Content-Type", ContentType.JSON.toString())
            )
        )
        val requestSpecification = baseRequestSpecificationWithToken(token = accessToken)
            .headers(headers)
            .body(updatedBooking)
        return patch(requestSpecification, "/$bookingId")
    }

    fun deleteBookingResponse(bookingId: String): Response {
        logger.info("Deleting booking with id $bookingId")
        return delete(baseRequestSpecificationWithToken(token = accessToken), bookingId)
    }

    fun deleteBookingById(bookingId: String) =
        deleteBookingResponse(bookingId)
            .assertStatusCode(HttpStatus.SC_CREATED)

    fun deleteBookingsByName(firstName: String, lastName: String) {
        val bookingIds = getBookingIds(mapOf(Pair("firstname", firstName), Pair("lastname", lastName)))
        bookingIds.forEach { deleteBookingById(it.bookingId!!) }
    }

}
