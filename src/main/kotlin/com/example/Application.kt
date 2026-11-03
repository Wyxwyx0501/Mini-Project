package com.example

import io.ktor.server.application.Application

fun Application.rootModule() {
    Database.init()

    configureStatusPages()
    configureSerialization()
    configureRouting()
}