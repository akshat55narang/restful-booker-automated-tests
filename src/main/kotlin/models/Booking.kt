package models

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
data class Booking(
    @get:JsonProperty("firstname") val firstName: String = "",
    @get:JsonProperty("lastname") val lastName: String = "",
    @get:JsonProperty("totalprice") val totalPrice: Int = 0,
    @get:JsonProperty("depositpaid") val depositPaid: Boolean = false,
    @get:JsonProperty("bookingdates") val bookingDates: BookingDates = BookingDates(),
    @get:JsonProperty("additionalneeds") val additionalNeeds: String = "",
)