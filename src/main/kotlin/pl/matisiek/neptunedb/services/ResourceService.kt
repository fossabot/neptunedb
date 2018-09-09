package pl.matisiek.neptunedb.services

import org.bson.Document
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ResourceService(@Autowired private val databaseService: DatabaseService) {

    fun read(collectionName: String): List<Any> = databaseService.read(collectionName)

    fun create(collectionName: String, document: Document) = databaseService.create(collectionName, document)

    fun update(collectionName: String, id: String, document: Document): Document? = databaseService.update(collectionName, id, document)

    fun delete(collectionName: String, id: String): Document = databaseService.delete(collectionName, id)

}