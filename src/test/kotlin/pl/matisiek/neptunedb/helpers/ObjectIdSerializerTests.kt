package pl.matisiek.neptunedb.helpers

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import org.assertj.core.api.Assertions.assertThat
import org.bson.types.ObjectId
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import java.io.StringWriter
import java.io.Writer

@RunWith(SpringRunner::class)
@SpringBootTest
class ObjectIdSerializerTests {

    @Test
    fun shouldReturnIdStringFromObjectId() {
        val jsonWriter: Writer = StringWriter()
        val jsonGenerator: JsonGenerator = JsonFactory().createGenerator(jsonWriter)
        val serializerProvider: SerializerProvider = ObjectMapper().serializerProvider
        ObjectIdSerializer().serialize(ObjectId("5b93cc4762421e1b60414753"), jsonGenerator, serializerProvider)
        jsonGenerator.flush()
        assertThat(jsonWriter.toString()).contains("5b93cc4762421e1b60414753")
    }

}