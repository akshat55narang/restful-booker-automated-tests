package helpers

import io.restassured.http.ContentType
import io.restassured.response.Response
import io.restassured.response.ValidatableResponse
import models.Booking
import org.apache.http.HttpStatus
import org.assertj.core.api.SoftAssertions

object TestHelpers {

    fun assertBookingResponse(
        expectedFirstName: String,
        expectedLastName: String,
        expectedTotalPrice: Int,
        expectedDepositPaid: Boolean,
        expectedCheckInDate: String,
        expectedCheckOutDate: String,
        expectedAdditionalNeeds: String,
        actualBookingResponse: Booking
    ) {
        val softly = SoftAssertions()
        softly.assertThat(actualBookingResponse.firstName).`as`("First Name").isEqualTo(expectedFirstName)
        softly.assertThat(actualBookingResponse.lastName).`as`("Last Name").isEqualTo(expectedLastName)
        softly.assertThat(actualBookingResponse.totalPrice).`as`("Total Price").isEqualTo(expectedTotalPrice)
        softly.assertThat(actualBookingResponse.depositPaid).`as`("Deposit Paid").isEqualTo(expectedDepositPaid)
        softly.assertThat(actualBookingResponse.bookingDates.checkIn).`as`("Check in Date")
            .isEqualTo(expectedCheckInDate)
        softly.assertThat(actualBookingResponse.bookingDates.checkOut).`as`("Check out Date")
            .isEqualTo(expectedCheckOutDate)
        softly.assertThat(actualBookingResponse.additionalNeeds).`as`("Additional Needs")
            .isEqualTo(expectedAdditionalNeeds)
        softly.assertAll()
    }

    fun Response.assertStatusCode(statusCode: Int = HttpStatus.SC_OK): ValidatableResponse = then()
        .assertThat()
        .statusCode(statusCode)

    fun Response.assertStatusCodeAndContentType(
        statusCode: Int = HttpStatus.SC_OK,
        contentType: ContentType = ContentType.JSON
    ): ValidatableResponse = assertStatusCode(statusCode)
        .contentType(contentType)

    fun <T> ValidatableResponse.extractBodyAs(cls: Class<T>) = extract()
        .body()
        .`as`(cls)
}
