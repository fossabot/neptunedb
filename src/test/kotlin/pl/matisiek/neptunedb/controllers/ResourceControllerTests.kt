package pl.matisiek.neptunedb.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.bson.Document
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.getForObject
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.http.HttpEntity
import org.apache.catalina.manager.StatusTransformer.setContentType
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType


data class Dog(val name: String, val owner: String)

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ResourceControllerTests {

    @LocalServerPort
    private val port: Int = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Autowired
    private lateinit var controller: ResourceController

    @Test
    fun contextLoads() {
        assertThat(controller).isNotNull
    }

    @Test
    fun shouldNotFindAnyDocumentAndReturnEmptyJsonArray() {
        assertThat(restTemplate.getForObject("http://localhost:$port/resources/dogs", String::class.java)).contains("[]")
    }

    @Test
    fun shouldInsertDocument() {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val entity = HttpEntity(ObjectMapper().writeValueAsString(Dog("Boxy", "John")), headers)
        assertThat(restTemplate.postForObject("http://localhost:$port/resources/dogs", entity, String::class.java))
        assertThat(restTemplate.getForObject("http://localhost:$port/resources/dogs", String::class.java)).contains("Boxy")
        assertThat(restTemplate.getForObject("http://localhost:$port/resources/dogs", String::class.java)).contains("John")
    }

}