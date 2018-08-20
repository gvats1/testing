package com.test.validreq

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.apache.http.HttpHost
import org.apache.http.client.config.RequestConfig
import org.apache.http.conn.routing.HttpRoute
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate
import java.text.SimpleDateFormat
import java.time.Instant

@Configuration
class GeotabConnectorConfig() {

    @Bean
    fun objectMapper() = ObjectMapper().apply {
        registerModule(SimpleModule().apply {
            addDeserializer(Instant::class.java, InstantDeserializer.INSTANT)
            addSerializer(Instant::class.java, InstantSerializer.INSTANCE)
        })
        registerModule(KotlinModule())
        configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
        dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    }

    /**
     * Customize how we interact with Geotab, allowing extra connections for authentication calls
     * and making sure the default spring json serializer supports kotlin
     */
    fun httpClient(timeOut : Int): CloseableHttpClient {
        val connectionManager = PoolingHttpClientConnectionManager().apply {
            maxTotal = 10
            defaultMaxPerRoute = 20
            setMaxPerRoute(HttpRoute(HttpHost("my.geotab.com")), 30)
        }

        val config = RequestConfig.custom()
                .setConnectTimeout(timeOut)
                .build()

        return HttpClientBuilder.create()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(config)
                .build()
    }

    fun httpRequestFactory(timeOut: Int) = HttpComponentsClientHttpRequestFactory(httpClient(timeOut))

    @Bean
    @Primary
    fun restTemplate(builder: RestTemplateBuilder) = RestTemplate(httpRequestFactory(2000)).apply {
        messageConverters.forEach {
            c -> if( c is MappingJackson2HttpMessageConverter) c.objectMapper = objectMapper()
        }
    }


    @Bean("authRestTemplate")
    fun authRestTemplate(builder: RestTemplateBuilder) = RestTemplate(httpRequestFactory(5000)).apply {
        messageConverters.forEach {
            c -> if( c is MappingJackson2HttpMessageConverter) c.objectMapper = objectMapper()
        }
    }
}

