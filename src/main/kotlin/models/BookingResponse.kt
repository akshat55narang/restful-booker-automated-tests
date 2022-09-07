package models

import com.fasterxml.jackson.annotation.JsonProperty

data class BookingResponse(
    @get:JsonProperty("bookingid") val bookingId: Int,
    @get:JsonProperty("booking") val booking: Booking,
)