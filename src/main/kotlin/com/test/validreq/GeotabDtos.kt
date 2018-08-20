package com.test.validreq

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.JsonNode
import java.io.Serializable
import java.time.Instant

// For Engine Fault Status

data class deviceSearch(val id: String? = null)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class search(val deviceSearch: deviceSearch,
                  val fromDate: String? = null,
                  val includeOverlappedTrips: Boolean? = null,
                  val toDate: String? = null
)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class params(val typeName: String,
                  val search : search,
                  val credentials: Credentials
)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class reqBodyEngineFault(val method: String,
                              val params : params
)

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class reqBodyEngineFaultUrl(val url: String,
                              val reqBodyEngineFault : reqBodyEngineFault
)



@JsonInclude(JsonInclude.Include.NON_NULL)
data class Credentials(val database: String? = null,
                       val password: String? = null,
                       val sessionId: String? = null,
                       val userName: String = "")
