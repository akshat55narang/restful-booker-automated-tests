package fixtures

import com.github.javafaker.Faker
import models.Booking
import models.BookingDates

object BookingFixture {
    private val faker = Faker()

    private val defaultBookingDates = BookingDates(
        checkIn = "2022-01-01",
        checkOut = "2022-01-10"
    )

    private val updatedBookingDates = BookingDates(
        checkIn = "2022-08-11",
        checkOut = "2022-08-20"
    )

    val defaultBooking = Booking(
        firstName = "Foo",
        lastName = "Bar",
        totalPrice = 100,
        depositPaid = true,
        bookingDates = defaultBookingDates,
        additionalNeeds = "Lunch"
    )

    val updateRequestBody = defaultBooking.copy(
        firstName = faker.name().firstName(),
        lastName = faker.name().lastName(),
        bookingDates = updatedBookingDates
    )
}
