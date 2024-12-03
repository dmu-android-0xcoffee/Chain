package net.wh64.chain.util

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*

val client = HttpClient(OkHttp) {
	install(ContentNegotiation) {
		json()
	}
	engine {
		config {
			followRedirects(true)
		}
	}
}
