package com.example.armeria.adapter.`in`.grpc

import com.example.armeria.ArmeriaApplication
import com.example.armeria.grpc.BlogPost
import com.example.armeria.grpc.BlogServiceGrpc.BlogServiceBlockingStub
import com.example.armeria.grpc.CreateBlogPostRequest
import com.example.armeria.grpc.DeleteBlogPostRequest
import com.example.armeria.grpc.GetBlogPostRequest
import com.example.armeria.grpc.ListBlogPostsRequest
import com.example.armeria.grpc.UpdateBlogPostRequest
import com.linecorp.armeria.client.grpc.GrpcClients
import org.slf4j.LoggerFactory
import org.springframework.boot.runApplication
import java.util.concurrent.atomic.AtomicInteger


class BlogClient {

    companion object {
        private val logger = LoggerFactory.getLogger(BlogClient::class.java)
        private val client = GrpcClients.newClient("http://127.0.0.1:8080/", BlogServiceBlockingStub::class.java)
        private val requestCounter = AtomicInteger()
    }

    fun createBlogPost(title: String, content: String) {
        val request = CreateBlogPostRequest.newBuilder()
            .setTitle(title)
            .setContent(content)
            .build()

        val response = client.createBlogPost(request)
        logger.info("${requestCounter.incrementAndGet()}. [Create response] Title: ${response.title}, Content: ${response.content}")
    }

    fun getBlogPost(id: Long) {
        val request = GetBlogPostRequest.newBuilder().setId(id).build()
        val response = client.getBlogPost(request)
        logger.info("${requestCounter.incrementAndGet()}. [Get response] Title: ${response.title}, Content: ${response.content}")
    }

    fun listBlogPosts(descending: Boolean = true) {
        val requestCount = requestCounter.incrementAndGet()
        val request = ListBlogPostsRequest.newBuilder()
            .setDescending(descending)
            .build()

        val response = client.listBlogPosts(request)
        response.blogsList.forEach { blogPost ->
            logger.info("${requestCount}. [Get response] Title: ${blogPost.title}, Content: ${blogPost.content}")
        }
    }

    fun updateBlogPost(id: Long, title: String, content: String) {
        val request = UpdateBlogPostRequest.newBuilder()
            .setId(id)
            .setTitle(title)
            .setContent(content)
            .build()

        val response = client.updateBlogPost(request)
        logger.info("${requestCounter.incrementAndGet()}. [Update response] Title: ${response.title}, Content: ${response.content}")
    }

    fun deleteBlogPost(id: Long) {
        val request = DeleteBlogPostRequest.newBuilder()
            .setId(id)
            .build()

        val response = client.deleteBlogPost(request)
        logger.info("${requestCounter.incrementAndGet()}. [Delete response] delete success!")
    }
}

fun main(args: Array<String>) {
//    runApplication<ArmeriaApplication>(*args)
    val blogClient = BlogClient()

    // CREATE
    blogClient.createBlogPost("First blog post", "ya ho~")
    blogClient.createBlogPost("Another blog post", "Creating a post via createBlogPost().")

    // READ
    blogClient.getBlogPost(0)

    blogClient.listBlogPosts()

    // UPDATE
    blogClient.updateBlogPost(0, "Updated blog post", "Updated blog content")
    blogClient.getBlogPost(0)

    // DELETE
    blogClient.deleteBlogPost(0)
    blogClient.listBlogPosts()
}