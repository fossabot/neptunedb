package pl.matisiek.neptunedb.controllers

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
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
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Query
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType


data class Dog constructor(@JsonProperty("_id") val _id: String?, @JsonProperty("name") var name: String, @JsonProperty("owner") val owner: String)

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ResourceControllerTests {

    @LocalServerPort
    private val port: Int = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Autowired
    private lateinit var mongoTemplate: MongoTemplate

    @Autowired
    private lateinit var controller: ResourceController

    private fun prune() {
        mongoTemplate.remove(Query(), "dogs")
    }

    @Test
    fun contextLoads() {
        assertThat(controller).isNotNull
    }

    @Test
    fun shouldNotFindAnyDocumentAndReturnEmptyJsonArray() {
        prune()
        assertThat(restTemplate.getForObject("http://localhost:$port/resources/dogs", String::class.java)).contains("[]")
    }

    @Test
    fun shouldInsertDocument() {
        prune()
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        print(ObjectMapper().writeValueAsString(Dog(null, "Boxy", "John")))
        val entity = HttpEntity(ObjectMapper().writeValueAsString(Dog("__test_document__", "Boxy", "John")), headers)
        assertThat(restTemplate.postForObject("http://localhost:$port/resources/dogs", entity, String::class.java))
        assertThat(restTemplate.getForObject("http://localhost:$port/resources/dogs", String::class.java)).contains("Boxy")
        assertThat(restTemplate.getForObject("http://localhost:$port/resources/dogs", String::class.java)).contains("John")
    }

    @Test
    fun shouldUpdateDocument() {
        shouldInsertDocument()
        val mapper = ObjectMapper()
        val dogs = mapper.readValue(restTemplate.getForObject("http://localhost:$port/resources/dogs", String::class.java), Array<Dog>::class.java)
        dogs[0].name = "Lil"
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val entity = HttpEntity(ObjectMapper().writeValueAsString(dogs[0]), headers)
        assertThat(restTemplate.exchange("http://localhost:$port/resources/dogs/${dogs[0]._id}", HttpMethod.PUT, entity, String::class.java).body).contains("Lil")
        assertThat(restTemplate.getForObject("http://localhost:$port/resources/dogs", String::class.java)).contains("Lil")
        assertThat(restTemplate.getForObject("http://localhost:$port/resources/dogs", String::class.java)).contains("John")
    }

    @Test
    fun shouldRemoveDocument() {
        shouldInsertDocument()
        val mapper = ObjectMapper()
        val dogs = mapper.readValue(restTemplate.getForObject("http://localhost:$port/resources/dogs", String::class.java), Array<Dog>::class.java)
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val entity = HttpEntity(ObjectMapper().writeValueAsString(dogs[0]), headers)
        assertThat(restTemplate.exchange("http://localhost:$port/resources/dogs/${dogs[0]._id}", HttpMethod.DELETE, entity, String::class.java).body).contains("{}")
        assertThat(restTemplate.getForObject("http://localhost:$port/resources/dogs", String::class.java)).doesNotContain("Boxy")
    }

}