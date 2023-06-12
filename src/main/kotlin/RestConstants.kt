import ConfigManager.getDefaultPassword
import ConfigManager.getDefaultUsername

object RestConstants {
    const val BOOKING_ENDPOINT = "/booking"
    const val AUTH_ENDPOINT = "/auth"
    const val HEALTHCHECK_ENDPOINT = "/ping"
    val defaultUsername = getDefaultUsername()
    val defaultPassword = getDefaultPassword()
}