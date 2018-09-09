package pl.matisiek.neptunedb.controllers

import lombok.Generated
import org.bson.Document
import org.jetbrains.annotations.NotNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.findAll
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.web.bind.annotation.*
import pl.matisiek.neptunedb.services.ResourceService

@RestController()
class ResourceController(@Autowired private val resourceService: ResourceService) {

    @GetMapping("resources/{collectionName}")
    fun read(@PathVariable("collectionName") collectionName: String): List<Any> = resourceService.read(collectionName)

    @PostMapping("resources/{collectionName}")
    fun create(@PathVariable("collectionName") collectionName: String, @RequestBody body: String) = resourceService.create(collectionName, Document.parse(body))

    @PutMapping("resources/{collectionName}/{id}")
    @PatchMapping("resources/{collectionName}/{id}")
    fun update(@PathVariable("collectionName") collectionName: String, @PathVariable("id") id: String, @RequestBody body: String): Document? = resourceService.update(collectionName, id, Document.parse(body))

    @DeleteMapping("resources/{collectionName}/{id}")
    fun delete(@PathVariable("collectionName") collectionName: String, @PathVariable("id") id: String): Document = resourceService.delete(collectionName, id)

}