package api

import ConfigManager.getBaseUri
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import auth.TokenFactory.BASIC_AUTH_HEADER_TOKEN
import auth.TokenFactory.getTokenFor
import io.restassured.builder.RequestSpecBuilder
import io.restassured.specification.RequestSpecification
import types.BasicAuthToken
import types.UserRole

abstract class BaseRequestSpecProvider(private val baseUri: String = getBaseUri(), private val basePath: String) :
    RequestSenderImpl() {

    fun requestSpecWithoutAuthHeader(): RequestSpecification = RequestSpecBuilder()
        .setBaseUri(baseUri)
        .setBasePath(basePath)
        .setRelaxedHTTPSValidation()
        .build()

    protected fun requestSpec(
        authMechanism: Either<BasicAuthToken, UserRole> = UserRole.ADMIN.right()
    ): RequestSpecification {
        val requestSpecification = requestSpecWithoutAuthHeader()
        return authMechanism.fold(
            {
                if (it.value != null) requestSpecification.header("Authorization", "Basic $BASIC_AUTH_HEADER_TOKEN")
                else requestSpecification
            },
            { requestSpecification.header("Cookie", "token=${getTokenFor(userRole = it)}") }
        )
    }
}
