package com.example.armeria.application.port.`in`

import com.example.armeria.application.domain.BlogPost

interface BlogService {

    fun createPost(blogPost: BlogPost): Long
    fun getPost(id: Long): BlogPost
    fun getPosts(descending: Boolean): List<BlogPost>
    fun updatePost(id: Long, blogPost: BlogPost): BlogPost
    fun deletePost(id: Long): BlogPost
}