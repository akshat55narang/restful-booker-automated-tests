package api

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

class BookingApi : BaseApi(BOOKING_API_PATH) {
    private val logger = LoggerFactory.getLogger(BookingApi::class.java)

    companion object {
        private const val BOOKING_API_PATH = "/booking"
    }

    fun createBookingResponse(
        requestBody: Booking = BookingFixture.defaultBooking
    ): Response {
        val requestSpecification = baseRequestWithoutToken()
            .header("Content-Type", ContentType.JSON)
            .body(requestBody)
        return post(requestSpecification)
    }

    fun createAndGetBookingId(
        requestBody: Booking = BookingFixture.defaultBooking
    ): String {
        val requestSpecification = baseRequestWithoutToken()
            .header("Content-Type", ContentType.JSON)
            .body(requestBody)
        return post(requestSpecification)
            .assertStatusCodeAndContentType()
            .extractBodyAs(BookingResponse::class.java)
            .bookingId.toString()
    }

    fun getBookingByIdResponse(bookingId: String): Response = get(baseRequestWithoutToken(), "/$bookingId")

    fun getBookingIdsResponse(queryParams: Map<String, String> = mapOf()): Response {
        val requestSpecification = baseRequestWithoutToken()
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
        val requestSpecification = baseRequest()
            .headers(headers)
            .body(updatedBooking)
        return patch(requestSpecification, "/$bookingId")
    }

    fun deleteBookingResponse(bookingId: String): Response {
        logger.info("Deleting booking with id $bookingId")
        return delete(baseRequest(), bookingId)
    }

    fun deleteBookingById(bookingId: String) =
        deleteBookingResponse(bookingId)
            .assertStatusCode(HttpStatus.SC_CREATED)

    fun deleteBookingsByName(firstName: String, lastName: String) {
        val bookingIds = getBookingIds(mapOf(Pair("firstname", firstName), Pair("lastname", lastName)))
        bookingIds.forEach { deleteBookingById(it.bookingId!!) }
    }

}
