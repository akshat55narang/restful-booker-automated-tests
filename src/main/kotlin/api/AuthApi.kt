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

class AuthApi(basePath: String = AUTH_API): BaseApi(basePath = basePath) {

    fun generateAccessToken(): String =
        sendCreateTokenRequest()
            .assertStatusCodeAndContentType()
            .extractBodyAs(AuthResponse::class.java)
            .token


    fun sendCreateTokenRequest(username: String = getDefaultUsername(), password: String = getDefaultPassword()): Response {
        val requestBody = Auth(username, password)
        val requestSpecification = baseRequestWithoutToken()
            .contentType(ContentType.JSON)
            .body(requestBody)

        return post(requestSpecification)
    }
}