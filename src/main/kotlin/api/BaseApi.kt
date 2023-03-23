package api

import ConfigManager.getBaseUri
import io.restassured.builder.RequestSpecBuilder
import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.filter.log.ResponseLoggingFilter
import io.restassured.specification.RequestSpecification

abstract class BaseApi(private val baseUri: String = getBaseUri(), private val basePath: String) : RequestSenderImpl() {

    fun baseRequestWithoutToken(): RequestSpecification = RequestSpecBuilder()
        .setBaseUri(baseUri)
        .setBasePath(basePath)
        .setRelaxedHTTPSValidation()
        .addFilter(RequestLoggingFilter())
        .addFilter(ResponseLoggingFilter())
        .build()

    protected fun baseRequest(token: String) = baseRequestWithoutToken()
        .header("Cookie", "token=$token")
}
