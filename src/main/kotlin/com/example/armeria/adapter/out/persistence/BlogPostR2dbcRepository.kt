package com.example.armeria.adapter.out.persistence

import com.example.armeria.application.domain.BlogPost
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface BlogPostR2dbcRepository : CoroutineCrudRepository<BlogPost, Long> {

}