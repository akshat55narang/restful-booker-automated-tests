package api

import ConfigManager.getBaseUri
import ConfigManager.getDefaultPassword
import ConfigManager.getDefaultUsername
import io.restassured.builder.RequestSpecBuilder
import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.filter.log.ResponseLoggingFilter
import io.restassured.http.ContentType
import models.Auth
import org.apache.http.HttpStatus
import org.slf4j.LoggerFactory

open class BaseApi(private val apiBasePath: String): RequestSenderImpl() {

    private val logger = LoggerFactory.getLogger(BaseApi::class.java)

    protected fun generateAccessToken(
        userName: String = getDefaultUsername(),
        password: String = getDefaultPassword(),
    ): String {
        val authRequestBody = Auth(userName, password)
        val requestSpecification = baseRequestWithoutToken(basePath = "/auth")
            .noFilters()
            .header("Content-Type", ContentType.JSON)
            .body(authRequestBody)

        val token: String = post(requestSpecification)
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_OK)
            .extract()
            .path("token")
        logger.info("Generated Access toke $token for user $userName")

        return token
    }

    protected fun baseRequestWithoutToken(
        baseUri: String = getBaseUri(),
        basePath: String = apiBasePath
    ) = RequestSpecBuilder()
        .setBaseUri(baseUri)
        .setBasePath(basePath)
        .setRelaxedHTTPSValidation()
        .addFilter(RequestLoggingFilter())
        .addFilter(ResponseLoggingFilter())
        .build()

    protected fun baseRequest(
        token: String = generateAccessToken()
    ) = baseRequestWithoutToken()
        .header("Cookie", "token=$token")

}
