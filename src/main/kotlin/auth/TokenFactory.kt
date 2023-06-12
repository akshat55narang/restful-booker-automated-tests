package auth

import RestConstants.defaultPassword
import RestConstants.defaultUsername
import api.client.AuthApiClient
import api.RequestSenderImpl
import models.AuthResponse
import types.UserRole
import utils.Helpers.assertStatusCodeAndContentType
import utils.Helpers.extractBodyAs

object TokenFactory : RequestSenderImpl() {
    private val authApiClient = AuthApiClient()

    const val BASIC_AUTH_HEADER_TOKEN = "YWRtaW46cGFzc3dvcmQxMjM="
    fun getTokenFor(userRole: UserRole) =
        when(userRole) {
            UserRole.ADMIN -> generateAccessToken(defaultUsername, defaultPassword)
            UserRole.READER -> throw RuntimeException("Role ${UserRole.READER} not supported!")
        }

    private fun generateAccessToken(username: String, password: String): String =
        authApiClient.authenticateUser(username, password)
            .assertStatusCodeAndContentType()
            .extractBodyAs<AuthResponse>()
            .token

}