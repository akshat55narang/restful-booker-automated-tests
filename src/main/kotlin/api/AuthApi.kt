package api

import ConfigManager.getBaseUri
import ConfigManager.getDefaultPassword
import ConfigManager.getDefaultUsername
import api.RestConstants.AUTH_API
import io.restassured.http.ContentType
import models.Auth
import org.slf4j.LoggerFactory
import utils.Helpers.assertStatusCode

class AuthApi : BaseApi(uri = getBaseUri(), apiBasePath = AUTH_API) {
    private val logger = LoggerFactory.getLogger(AuthApi::class.java)

    fun generateAccessToken(
        userName: String = getDefaultUsername(),
        password: String = getDefaultPassword(),
    ): String {
        val authRequestBody = Auth(userName, password)
        val requestSpecification = baseRequestWithoutToken(basePath = "/auth")
            .noFilters()
            .header("Content-Type", ContentType.JSON)
            .body(authRequestBody)

        val token: String = post(requestSpecification)
            .assertStatusCode()
            .extract()
            .path("token")
        logger.info("Generated Access toke $token for user $userName")

        return token
    }
}
