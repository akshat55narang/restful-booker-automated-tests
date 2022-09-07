package api

import ConfigManager.getDefaultPassword
import ConfigManager.getDefaultUri
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

open class BaseApi(private val apiBasePath: String) {

    companion object {
        const val INVALID_TOKEN =
            "eyJraWQiOiJFOGdUVm82US1FQ2hIWFU4bzJ0OWNqb3pVRl96R1lHVGU2bGVhNllIY0VZIiwiYWxnIjoiUlMyNTYifQ.eyJ2ZXIiOjEsImp0aSI6IkFULkwyWWxLUE11T0F0dnBrUGRPZ1h2Y0VUMDZxdkFkRkI4ZDQtWUlqd2FYX3cub2FybGUwMDZ5SWRoUVo4Wkc0eDYiLCJpc3MiOiJodHRwczovL2Rldi00NTc5MzEub2t0YS5jb20vb2F1dGgyL2F1c2hkNGM5NVF0RkhzZld0NHg2IiwiYXVkIjoiYXBpIiwiaWF0IjoxNjQ2Nzc5ODA4LCJleHAiOjE2NDY3ODM0MDgsImNpZCI6IjBvYWhkaGprdXRhR2NJSzJNNHg2IiwidWlkIjoiMDB1aGVuaDFwVkRNZzJ1ZXg0eDYiLCJzY3AiOlsib2ZmbGluZV9hY2Nlc3MiXSwic3ViIjoiYXBpLXVzZXI0QGl3dC5uZXQifQ.eN2NiSBjEBRxxNMi-v3csXIrTOOc2STb1cnU-zz9Xp8UHl7VloeDGiTRetyNiRDDehHaE1M9EmB6fzWm0VZ4FjbycXZBVsj7JPnKVrJSDtZjelKQToIvgOo-xqM0bhCs7oDzFUBHzdtJ62V_e123VK3x-A29cdbr6WY1P45HoBHYMmNmdBHXaXCM7z8m-tsiyll8Qys-qLjNr6uYmWzvzH07cVOflZsbA58_ukHK5fCyz06JcHqoW-yFjmPcvDnucA2xeDrcZ2-eApzM51Z9vLTDHQ8Zp1ws_GdCLTef8nTOFBi2vs0wr_gGUPl4RXCa_W8eELkCBqYYn_e3stnBLA"
    }

    protected fun generateAccessToken(
        userName: String = getDefaultUsername(),
        password: String = getDefaultPassword(),
    ): String {
        val authRequestBody = Auth(userName, password)
        val requestSpecification = baseRequestWithoutToken(basePath = "/auth")
            .header("Content-Type", ContentType.JSON)
            .body(authRequestBody)

        return post(requestSpecification)
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_OK)
            .extract()
            .path("token")
    }

    protected fun baseRequestWithoutToken(
        baseUri: String = getDefaultUri(),
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
