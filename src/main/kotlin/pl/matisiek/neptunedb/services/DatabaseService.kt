package pl.matisiek.neptunedb.services

import org.bson.Document
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.findAll
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*

@Service
class DatabaseService(@Autowired private val mongoTemplate: MongoTemplate) {

    fun read(collectionName: String): List<Any> = mongoTemplate.findAll(collectionName)

    fun create(collectionName: String, document: Document) = mongoTemplate.insert(document, collectionName)

    fun update(collectionName: String, id: String, document: Document): Document? {
        mongoTemplate.updateFirst(Query().addCriteria(Criteria.where("_id").`is`(id)), Update.fromDocument(document), collectionName)
        return document.append("_id", id)
    }

    fun delete(collectionName: String, id: String): Document {
        mongoTemplate.remove(Query().addCriteria(Criteria.where("_id").`is`(id)), collectionName)
        return Document()
    }

}