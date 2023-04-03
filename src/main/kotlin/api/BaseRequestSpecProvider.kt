package api

import ConfigManager.getBaseUri
import io.restassured.builder.RequestSpecBuilder
import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.filter.log.ResponseLoggingFilter
import io.restassured.specification.RequestSpecification

abstract class BaseRequestSpecProvider(private val baseUri: String = getBaseUri(), private val basePath: String) : RequestSenderImpl() {

    fun baseRequestSpecificationWithoutToken(): RequestSpecification = RequestSpecBuilder()
        .setBaseUri(baseUri)
        .setBasePath(basePath)
        .setRelaxedHTTPSValidation()
        .addFilter(RequestLoggingFilter())
        .addFilter(ResponseLoggingFilter())
        .build()

    protected fun baseRequestSpecificationWithToken(token: String): RequestSpecification = baseRequestSpecificationWithoutToken()
        .header("Cookie", "token=$token")
}
