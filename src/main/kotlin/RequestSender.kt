import io.restassured.response.Response
import io.restassured.specification.RequestSpecification

interface RequestSender {
    fun get(requestSpecification: RequestSpecification, path: String= ""): Response

    fun post(requestSpecification: RequestSpecification, path: String= ""): Response

    fun put(requestSpecification: RequestSpecification, path: String= ""): Response

    fun patch(requestSpecification: RequestSpecification, path: String= ""): Response

    fun delete(requestSpecification: RequestSpecification, path: String= ""): Response

    fun options(requestSpecification: RequestSpecification, path: String= ""): Response
}
