package pl.finitas.app.core.http

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.jackson.jackson
import org.koin.core.module.Module

fun Module.httpClient() {
    single {
        HttpClient(Android) {
            install(Logging) {
                level = LogLevel.ALL
            }
            install(ContentNegotiation) {
                jackson()
            }
        }
    }
}