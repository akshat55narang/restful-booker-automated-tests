package api

import RequestSenderImpl
import io.restassured.builder.RequestSpecBuilder
import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.filter.log.ResponseLoggingFilter
import org.slf4j.LoggerFactory

open class BaseApi(private val uri: String, private val apiBasePath: String): RequestSenderImpl() {

    private val logger = LoggerFactory.getLogger(BaseApi::class.java)

    protected fun baseRequestWithoutToken(
        basePath: String = apiBasePath
    ) = RequestSpecBuilder()
        .setBaseUri(uri)
        .setBasePath(basePath)
        .setRelaxedHTTPSValidation()
        .addFilter(RequestLoggingFilter())
        .addFilter(ResponseLoggingFilter())
        .build()

    protected fun baseRequest(
        token: String
    ) = baseRequestWithoutToken()
        .header("Cookie", "token=$token")

}
