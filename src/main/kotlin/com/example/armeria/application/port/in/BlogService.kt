package com.example.armeria.application.port.`in`

import com.example.armeria.application.domain.BlogPost

interface BlogService {

    fun createPost(blogPost: BlogPost): Long
}