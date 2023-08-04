package com.example.armeria

import com.linecorp.armeria.common.HttpResponse
import com.linecorp.armeria.server.Server
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class ArmeriaApplication

fun main(args: Array<String>) {
	val server = newServer(8080)
	server.closeOnJvmShutdown()
	server.start().join()
}

fun newServer(port: Int): Server {
	val builder = Server.builder()
	return builder
		.http(port)
		.service("/") { ctx, req -> HttpResponse.of("Hello World")}
		.build()
}