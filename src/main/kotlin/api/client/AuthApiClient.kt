package api.client

import RestConstants.AUTH_ENDPOINT
import api.BaseRequestSpecProvider
import io.restassured.http.ContentType
import io.restassured.response.Response
import models.Auth

class AuthApiClient: BaseRequestSpecProvider(basePath = AUTH_ENDPOINT) {

    fun authenticateUser(username: String, password: String): Response {
        val requestBody = Auth(username, password)
        val requestSpecification = requestSpecWithoutAuthHeader()
            .contentType(ContentType.JSON)
            .body(requestBody)

        return post(requestSpecification)
    }
}