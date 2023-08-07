package com.example.armeria.adapter.`in`.api

import com.example.armeria.application.domain.BlogPost
import com.example.armeria.application.domain.BlogPostRequestConverter
import com.example.armeria.application.port.`in`.BlogService
import com.linecorp.armeria.common.HttpResponse
import com.linecorp.armeria.common.HttpStatus
import com.linecorp.armeria.server.annotation.Blocking
import com.linecorp.armeria.server.annotation.Default
import com.linecorp.armeria.server.annotation.Delete
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
import java.lang.IllegalArgumentException

@Controller
class BlogController(
    private val blogService: BlogService
) {

    companion object {
        val logger: Logger = LoggerFactory.getLogger(BlogController::class.java)
    }

    @Post("/blogs")
    @RequestConverter(BlogPostRequestConverter::class)
    fun createPost(blogPost: BlogPost): HttpResponse {
        blogService.createPost(blogPost)

        return HttpResponse.ofJson(blogPost)
    }

    @Get("/blogs/:id")
    @ProducesJson
    fun getPost(@Param id: Long): BlogPost {
        return blogService.getPost(id)
    }

    @Get("/blogs")
    @ProducesJson
    fun getPosts(@Param @Default("true") descending: Boolean): List<BlogPost> {
        return blogService.getPosts(descending)
    }

    @Put("/blogs/:id")
    fun updatePost(@Param id: Long, @RequestObject blogPost: BlogPost): HttpResponse {
        return try {
            val updatedBlogPost = blogService.updatePost(id, blogPost)
            HttpResponse.ofJson(updatedBlogPost)
        } catch (e: IllegalArgumentException) {
            HttpResponse.of(HttpStatus.NOT_FOUND)
        }
    }

    // With real services, accessing and operating on a database takes time. We need to hand over such blocking tasks to blocking task executor so that the EventLoop isn't blocked.
    @Blocking
    @Delete("/blogs/:id")
    fun deletePost(@Param id: Long): HttpResponse {
        return try {
            val deleteBlogPost = blogService.deletePost(id)
            HttpResponse.ofJson(deleteBlogPost)
        } catch (e: IllegalArgumentException) {
            HttpResponse.of(HttpStatus.NOT_FOUND)
        }
    }
}