package api.client

import RestConstants.HEALTHCHECK_ENDPOINT
import api.BaseRequestSpecProvider

class HealthCheckClient : BaseRequestSpecProvider(basePath = "") {
    fun pingServer() = get(requestSpecification = requestSpecWithoutAuthHeader(), path = HEALTHCHECK_ENDPOINT)

}