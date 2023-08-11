package com.example.armeria.adapter.`in`.grpc

import com.example.armeria.ArmeriaApplication
import com.example.armeria.grpc.BlogServiceGrpc.BlogServiceBlockingStub
import com.example.armeria.grpc.CreateBlogPostRequest
import com.linecorp.armeria.client.grpc.GrpcClients
import org.slf4j.LoggerFactory
import org.springframework.boot.runApplication


class BlogClient {

    companion object {
        private val logger = LoggerFactory.getLogger(BlogClient::class.java)
    }

    fun createBlogPost(title: String, content: String) {
        val client = GrpcClients.newClient("http://127.0.0.1:8080/", BlogServiceBlockingStub::class.java)
        val request = CreateBlogPostRequest.newBuilder()
            .setTitle("My first blog")
            .setContent("Yay")
            .build()

        val response = client.createBlogPost(request)
        logger.info("[Create response] Title: ${response.title} Content: ${response.content}")
    }
}

fun main(args: Array<String>) {
//    runApplication<ArmeriaApplication>(*args)
    val blogClient = BlogClient()
    blogClient.createBlogPost("Another blog post", "Creating a post via createBlogPost().")
}