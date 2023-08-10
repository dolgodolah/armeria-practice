package com.example.armeria.restapi.adapter.`in`.api

import com.example.armeria.restapi.application.domain.BlogPost
import com.example.armeria.restapi.application.domain.BlogPostRequestConverter
import com.example.armeria.restapi.application.port.`in`.BlogService
import com.linecorp.armeria.common.HttpResponse
import com.linecorp.armeria.server.annotation.Blocking
import com.linecorp.armeria.server.annotation.Default
import com.linecorp.armeria.server.annotation.Delete
import com.linecorp.armeria.server.annotation.ExceptionHandler
import com.linecorp.armeria.server.annotation.Get
import com.linecorp.armeria.server.annotation.Param
import com.linecorp.armeria.server.annotation.Post
import com.linecorp.armeria.server.annotation.ProducesJson
import com.linecorp.armeria.server.annotation.Put
import com.linecorp.armeria.server.annotation.RequestConverter
import com.linecorp.armeria.server.annotation.RequestObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller

@Controller
class BlogController(
    private val blogService: BlogService
) {

    companion object {
        val logger: Logger = LoggerFactory.getLogger(BlogController::class.java)
    }

    @Post("/blogs")
    @RequestConverter(BlogPostRequestConverter::class)
    suspend fun createPost(blogPost: BlogPost): HttpResponse {
        blogService.createPost(blogPost)

        return HttpResponse.ofJson(blogPost)
    }

    @Get("/blogs/:id")
    @ProducesJson
    @ExceptionHandler(BadRequestExceptionHandler::class)
    suspend fun getPost(@Param id: Long): BlogPost {
        return blogService.getPost(id)
    }

    @Get("/blogs")
    @ProducesJson
    suspend fun getPosts(@Param @Default("true") descending: Boolean): List<BlogPost> {
        return blogService.getPosts(descending)
    }

    @Put("/blogs/:id")
    @ExceptionHandler(BadRequestExceptionHandler::class)
    suspend fun updatePost(@Param id: Long, @RequestObject blogPost: BlogPost): HttpResponse {
        val updatedBlogPost = blogService.updatePost(id, blogPost)
        return HttpResponse.ofJson(updatedBlogPost)
    }

    // With real services, accessing and operating on a database takes time. We need to hand over such blocking tasks to blocking task executor so that the EventLoop isn't blocked.
    @Blocking
    @Delete("/blogs/:id")
    @ExceptionHandler(BadRequestExceptionHandler::class)
    suspend fun deletePost(@Param id: Long): HttpResponse {
        val deleteBlogPost = blogService.deletePost(id)
        return HttpResponse.ofJson(deleteBlogPost)
    }
}