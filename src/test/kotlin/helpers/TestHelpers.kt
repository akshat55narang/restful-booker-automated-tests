package helpers

import models.Booking
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

}