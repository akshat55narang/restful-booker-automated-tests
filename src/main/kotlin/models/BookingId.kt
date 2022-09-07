package models

import com.fasterxml.jackson.annotation.JsonProperty

data class BookingId(
    @get:JsonProperty("bookingid") val bookingId: String?
)
