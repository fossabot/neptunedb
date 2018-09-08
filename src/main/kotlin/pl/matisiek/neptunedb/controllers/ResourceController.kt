package pl.matisiek.neptunedb.controllers

import org.bson.Document
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.findAll
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.web.bind.annotation.*

@RestController()
class ResourceController {

    @Autowired
    private val mongoTemplate: MongoTemplate? = null

    @GetMapping("resources/{collectionName}")
    fun read(@PathVariable("collectionName") collectionName: String): List<Any> = mongoTemplate!!.findAll(collectionName)

    @PostMapping("resources/{collectionName}")
    fun create(@PathVariable("collectionName") collectionName: String, @RequestBody body: String) = mongoTemplate!!.insert(Document.parse(body), collectionName)

    @PutMapping("resources/{collectionName}/{id}")
    @PatchMapping("resources/{collectionName}/{id}")
    fun update(@PathVariable("collectionName") collectionName: String, @PathVariable("id") id: String, @RequestBody body: String): Document? {
        mongoTemplate!!.updateFirst(Query().addCriteria(Criteria.where("_id").`is`(id)), Update.fromDocument(Document.parse(body)), collectionName)
        return Document.parse(body).append("_id", id)
    }

    @DeleteMapping("resources/{collectionName}/{id}")
    fun delete(@PathVariable("collectionName") collectionName: String, @PathVariable("id") id: String): Document {
        mongoTemplate!!.remove(Query().addCriteria(Criteria.where("_id").`is`(id)), collectionName)
        return Document()
    }

}