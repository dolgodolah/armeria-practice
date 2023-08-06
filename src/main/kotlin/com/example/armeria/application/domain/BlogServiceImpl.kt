package com.example.armeria.application.domain

import com.example.armeria.application.port.`in`.BlogService
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException
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

    override fun getPost(id: Long): BlogPost {
        return posts[id] ?: throw IllegalArgumentException("not found post")
    }

    override fun getPosts(descending: Boolean): List<BlogPost> {
        return posts.values.sortedByDescending { it.id }
    }
}