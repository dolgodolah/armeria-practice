package com.example.armeria.restapi.application.port.`in`

import com.example.armeria.restapi.application.domain.BlogPost

interface BlogService {

    fun createPost(blogPost: BlogPost): Long
    fun getPost(id: Long): BlogPost
    fun getPosts(descending: Boolean): List<BlogPost>
    fun updatePost(id: Long, blogPost: BlogPost): BlogPost
    fun deletePost(id: Long): BlogPost
}