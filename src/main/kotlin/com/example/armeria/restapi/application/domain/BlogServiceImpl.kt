package com.example.armeria.restapi.application.domain

import com.example.armeria.restapi.application.port.`in`.BlogService
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
        if (descending) {
            return posts.values.sortedByDescending { it.id }
        }

        return posts.values.toList()
    }

    override fun updatePost(id: Long, blogPost: BlogPost): BlogPost {
        val oldBlogPost = posts[id] ?: throw IllegalArgumentException("not found post")
        val newBlogPost = BlogPost(
            id = id,
            title = blogPost.title,
            content = blogPost.content,
            createdAt = oldBlogPost.createdAt,
            modifiedAt = blogPost.createdAt
        )

        posts[id] = newBlogPost
        return newBlogPost
    }

    override fun deletePost(id: Long): BlogPost {
        return posts.remove(id) ?: throw IllegalArgumentException("not found post")
    }
}