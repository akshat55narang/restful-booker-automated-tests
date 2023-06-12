import RestConstants.defaultPassword
import RestConstants.defaultUsername
import api.client.AuthApiClient
import models.AuthResponse
import models.InvalidAuthResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import utils.Helpers.assertStatusCodeAndContentType
import utils.Helpers.extractBodyAs

class AuthApiTest: BaseTest() {
    private val authApiClient = AuthApiClient()
    @Test
    fun `login should be allowed for ADMIN user`() {
        val accessToken = authApiClient.authenticateUser(defaultUsername, defaultPassword)
            .assertStatusCodeAndContentType()
            .extractBodyAs<AuthResponse>()
            .token

        assertThat(accessToken).isNotEmpty
    }

    /**
     * This test can be improved by using data provider with various combinations of
     * username and passwords. For demonstration, it only tests with invalid password
     */
    @Test
    fun `login should be allowed with invalid password`() {
        val actualResponse = authApiClient.authenticateUser(defaultUsername, "invalid_password")
            .assertStatusCodeAndContentType()
            .extractBodyAs<InvalidAuthResponse>()

        assertThat(actualResponse.reason).isEqualTo("Bad credentials")
    }

}