package pl.matisiek.neptunedb

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class NeptunedbApplication

fun main(args: Array<String>) {
    runApplication<NeptunedbApplication>(*args)
}
