package com.example.armeria.application.domain

import com.example.armeria.application.port.`in`.BlogService
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

@Service
class BlogServiceImpl : BlogService {

    companion object {
        private val posts = ConcurrentHashMap<Long, BlogPost>()
    }

    override fun createPost(blogPost: BlogPost): Long {
        posts[blogPost.id] = blogPost
        return blogPost.id
    }
}