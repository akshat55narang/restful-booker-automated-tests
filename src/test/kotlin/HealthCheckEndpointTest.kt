import api.client.HealthCheckClient
import org.apache.http.HttpStatus
import org.junit.jupiter.api.Test
import utils.Helpers.assertStatusCode

class HealthCheckEndpointTest: BaseTest() {
    private val healthCheckClient = HealthCheckClient()

    @Test
    fun `should be able to check api health status`() {
        healthCheckClient.pingServer()
            .assertStatusCode(HttpStatus.SC_CREATED)
    }
}