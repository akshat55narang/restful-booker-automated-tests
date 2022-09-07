package fixtures

import models.Booking
import models.BookingDates

object BookingFixture {
    val defaultBooking = Booking(
        firstName = "Foo",
        lastName = "Bar",
        totalPrice = 100,
        depositPaid = true,
        bookingDates = BookingDates(
            checkIn = "2022-01-01",
            checkOut = "2022-01-10"
        ),
        additionalNeeds = "Lunch"
    )

    val defaultBookingDates = BookingDates(
        checkIn = "2022-01-01",
        checkOut = "2022-01-10"

    )
}