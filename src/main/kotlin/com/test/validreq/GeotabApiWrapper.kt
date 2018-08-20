package com.test.validreq


import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.ObjectNode
//import com.uptake.auth.exception.HttpException
//import com.uptake.geotab.config.ConnectorConfig
//import com.uptake.geotab.dto.*
//import com.uptake.geotab.service.AuthServiceFactoryBean
//import com.uptake.geotab.service.CredentialService
//import com.uptake.geotab.util.GeotabConnectorException
//import com.uptake.geotab.util.GeotabConstants
//import com.uptake.geotab.util.GeotabErrorResponseException
//import com.uptake.geotab.util.GeotabJsonProcessingException
//import com.uptake.geotab.util.MissingCredentialsException
//import com.uptake.geotab.util.UnauthorizedAuthTokenException
//import org.apache.commons.lang3.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import org.springframework.web.util.UriComponentsBuilder.fromHttpUrl
import java.net.URI
import java.time.Instant
import java.util.*
import javax.inject.Inject



@Service
class GeotabApiWrapper(@Autowired val objectMapper: ObjectMapper,
                       @Autowired val restTemplate: RestTemplate,
                       @Inject @Qualifier("authRestTemplate") val authRestTemplate: RestTemplate
                      ) {


    // API method for Engine Fault functionality


    fun getEngineFaultData(reqBodyEngineFaultUrl: reqBodyEngineFaultUrl,url:String): String {
        var json = ""
        try {
            json += ObjectMapper().writeValueAsString(reqBodyEngineFaultUrl.reqBodyEngineFault)
        } catch (jpe: JsonProcessingException) {
            throw RuntimeException("Failed to convert reqBodyEngineFault object to JSON")
        }
println("JSON Request:::::::::::::"+json)
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val httpEntity: HttpEntity<String> = HttpEntity(json, headers)
        val response = restTemplate.postForEntity(url, httpEntity, String::class.java)
        println("Response from server:::::::::::::::::"+response.toString())
        if (response.toString().contains("error")) {
            throw RuntimeException(response.toString())
        }
        println(response.toString())
        return response.toString()
    }



}
