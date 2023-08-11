package com.example.armeria.application.port.`in`

import com.example.armeria.application.domain.BlogPost

interface BlogService {

    suspend fun createPost(blogPost: BlogPost): Long
    suspend fun getPost(id: Long): BlogPost
    suspend fun getPosts(descending: Boolean): List<BlogPost>
    suspend fun updatePost(id: Long, blogPost: BlogPost): BlogPost
    suspend fun deletePost(id: Long): BlogPost
}