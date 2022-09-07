package models

import com.fasterxml.jackson.annotation.JsonProperty

data class BookingDates(
    @get:JsonProperty("checkin") val checkIn: String = "",
    @get:JsonProperty("checkout") val checkOut: String = "",
)