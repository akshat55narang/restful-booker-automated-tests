package utils

import io.restassured.http.ContentType
import io.restassured.response.Response
import io.restassured.response.ValidatableResponse
import org.apache.commons.lang3.StringUtils
import org.apache.http.HttpStatus
import org.slf4j.LoggerFactory

object Helpers {
    private val logger = LoggerFactory.getLogger(Helpers::class.java)

    fun getParameterValue(variableName: String, defaultValue: String?): String {
        var resolvedValue = defaultValue
        val env = System.getenv(variableName)
        if (env != null) {
            resolvedValue = env
        }
        var result = System.getProperty(variableName, resolvedValue)
        if (StringUtils.isBlank(result)) {
            logger.info("empty value supplied for system property $variableName, reverting to default value")
            result = resolvedValue
        }
        return result
    }

    fun Response.assertStatusCode(statusCode: Int = HttpStatus.SC_OK): ValidatableResponse =
        then()
            .assertThat()
            .statusCode(statusCode)

    fun Response.assertStatusCodeAndContentType(
        statusCode: Int = HttpStatus.SC_OK,
        contentType: ContentType = ContentType.JSON
    ): ValidatableResponse = assertStatusCode(statusCode)
        .contentType(contentType)

    fun <T> ValidatableResponse.extractBodyAs(cls: Class<T>) =
        extract()
            .body()
            .`as`(cls)
}
