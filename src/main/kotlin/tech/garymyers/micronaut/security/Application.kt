package tech.garymyers.micronaut.security

import io.micronaut.runtime.Micronaut

object Application {

    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.build()
                .packages("tech.garymyers.micronaut.security")
                .mainClass(Application.javaClass)
                .start()
    }
}