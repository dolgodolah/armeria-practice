package com.example.armeria.application.domain

import com.example.armeria.adapter.out.persistence.BlogPostR2dbcRepository
import com.example.armeria.application.port.`in`.BlogService
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException

@Service
class BlogServiceImpl(
    private val blogPostRepository: BlogPostR2dbcRepository
) : BlogService {

    override suspend fun createPost(blogPost: BlogPost): Long {
        return blogPostRepository.save(blogPost).id
    }

    override suspend fun getPost(id: Long): BlogPost {
        return  blogPostRepository.findById(id) ?: throw IllegalArgumentException("not found post")
    }

    override suspend fun getPosts(descending: Boolean): List<BlogPost> {
        if (descending) {
            return blogPostRepository.findAll().toList().sortedByDescending { it.id }
        }

        return blogPostRepository.findAll().toList()
    }

    override suspend fun updatePost(id: Long, blogPost: BlogPost): BlogPost {
        val oldBlogPost = blogPostRepository.findById(id) ?: throw IllegalArgumentException("not found post")
        val newBlogPost = BlogPost(
            id = id,
            title = blogPost.title,
            content = blogPost.content,
            createdAt = oldBlogPost.createdAt,
            modifiedAt = blogPost.createdAt
        )

        return blogPostRepository.save(newBlogPost)
    }

    override suspend fun deletePost(id: Long): BlogPost {
        val blogPost = blogPostRepository.findById(id) ?: throw IllegalArgumentException("not found post")
        blogPostRepository.delete(blogPost)
        return blogPost
    }
}