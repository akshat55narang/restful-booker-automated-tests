import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.When
import io.restassured.response.Response
import io.restassured.specification.RequestSpecification

open class RequestSenderImpl: RequestSender {
    override fun get(requestSpecification: RequestSpecification, path: String): Response =
        Given {
            spec(requestSpecification)
        } When {
            get(path)
        }

    override fun post(requestSpecification: RequestSpecification, path: String): Response =
        Given {
            spec(requestSpecification)
        } When {
            post(path)
        }

    override fun put(requestSpecification: RequestSpecification, path: String): Response =
        Given {
            spec(requestSpecification)
        } When {
            put(path)
        }

    override fun patch(requestSpecification: RequestSpecification, path: String): Response =
        Given {
            spec(requestSpecification)
        } When {
            patch(path)
        }

    override fun delete(requestSpecification: RequestSpecification, path: String): Response =
        Given {
            spec(requestSpecification)
        } When {
            delete(path)
        }

    override fun options(requestSpecification: RequestSpecification, path: String): Response =
        Given {
            spec(requestSpecification)
        } When {
            options(path)
        }
}
