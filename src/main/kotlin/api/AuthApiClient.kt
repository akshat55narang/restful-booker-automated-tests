package api

import ConfigManager.getDefaultPassword
import ConfigManager.getDefaultUsername
import RestConstants.AUTH_API
import io.restassured.http.ContentType
import io.restassured.response.Response
import models.Auth
import models.AuthResponse
import utils.Helpers.assertStatusCodeAndContentType
import utils.Helpers.extractBodyAs

class AuthApiClient(basePath: String = AUTH_API): BaseRequestSpecProvider(basePath = basePath) {

    fun generateAccessToken(): String =
        sendCreateTokenRequest()
            .assertStatusCodeAndContentType()
            .extractBodyAs(AuthResponse::class.java)
            .token


    private fun sendCreateTokenRequest(username: String = getDefaultUsername(), password: String = getDefaultPassword()): Response {
        val requestBody = Auth(username, password)
        val requestSpecification = baseRequestSpecificationWithoutToken()
            .contentType(ContentType.JSON)
            .body(requestBody)

        return post(requestSpecification)
    }
}