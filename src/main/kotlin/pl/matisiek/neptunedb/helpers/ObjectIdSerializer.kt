package pl.matisiek.neptunedb.helpers

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.bson.types.ObjectId
import org.springframework.boot.jackson.JsonComponent

@JsonComponent
class ObjectIdSerializer : JsonSerializer<ObjectId>() {

    override fun serialize(value: ObjectId, jgen: JsonGenerator, provider: SerializerProvider) {
        jgen.writeString(value.toString())
    }

}