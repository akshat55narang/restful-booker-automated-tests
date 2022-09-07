package api

import ConfigManager.getBaseUri
import ConfigManager.getDefaultPassword
import ConfigManager.getDefaultUsername
import io.restassured.builder.RequestSpecBuilder
import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.filter.log.ResponseLoggingFilter
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.When
import io.restassured.specification.RequestSpecification
import models.Auth
import org.apache.http.HttpStatus
import org.slf4j.LoggerFactory

open class BaseApi(private val apiBasePath: String) {

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

    protected fun post(requestSpecification: RequestSpecification) =
        Given {
            spec(requestSpecification)
        } When {
            post()
        }

    protected fun post(requestSpecification: RequestSpecification, path: String) =
        Given {
            spec(requestSpecification)
        } When {
            post(path)
        }

    protected fun put(requestSpecification: RequestSpecification) =
        Given {
            spec(requestSpecification)
        } When {
            put()
        }

    protected fun put(requestSpecification: RequestSpecification, path: String) =
        Given {
            spec(requestSpecification)
        } When {
            put(path)
        }

    protected fun patch(requestSpecification: RequestSpecification) =
        Given {
            spec(requestSpecification)
        } When {
            patch()
        }

    protected fun patch(requestSpecification: RequestSpecification, path: String) =
        Given {
            spec(requestSpecification)
        } When {
            patch(path)
        }

    protected fun get(requestSpecification: RequestSpecification) =
        Given {
            spec(requestSpecification)
        } When {
            get()
        }

    protected fun get(requestSpecification: RequestSpecification, path: String) =
        Given {
            spec(requestSpecification)
        } When {
            get(path)
        }

    protected fun delete(requestSpecification: RequestSpecification) =
        Given {
            spec(requestSpecification)
        } When {
            delete()
        }

    protected fun delete(requestSpecification: RequestSpecification, path: String) =
        Given {
            spec(requestSpecification)
        } When {
            delete(path)
        }
}
