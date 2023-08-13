package com.example.armeria.adapter.`in`.grpc

import com.example.armeria.adapter.out.persistence.BlogPostR2dbcRepository
import com.example.armeria.application.domain.BlogPost
import com.example.armeria.application.domain.toGrpc
import com.example.armeria.grpc.*
import com.google.protobuf.Empty
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class GrpcBlogService(
    private val blogPostRepository: BlogPostR2dbcRepository
) : BlogServiceGrpcKt.BlogServiceCoroutineImplBase() {

    override suspend fun createBlogPost(request: CreateBlogPostRequest): GBlogPost {
        val now = Instant.now()
        val gBlogPost = GBlogPost.newBuilder()
            .setTitle(request.title)
            .setContent(request.content)
            .setModifiedAt(now.toEpochMilli())
            .setCreatedAt(now.toEpochMilli())
            .build()

        blogPostRepository.save(BlogPost.of(gBlogPost))
        return gBlogPost
    }

    override suspend fun getBlogPost(request: GetBlogPostRequest): GBlogPost {
        return blogPostRepository.findById(request.id)?.let {
            it.toGrpc()
        } ?: throw NotFoundException("not found post")
    }

    override suspend fun listBlogPosts(request: ListBlogPostsRequest): ListBlogPostsResponse {
        val blogPosts = if (request.descending) {
            blogPostRepository.findAll().toList().sortedByDescending { it.id }
        } else {
            blogPostRepository.findAll().toList()
        }

        return blogPosts.toGrpc()
    }

    override suspend fun updateBlogPost(request: UpdateBlogPostRequest): GBlogPost {
        val oldBlogPost = blogPostRepository.findById(request.id)?.let {
            it.toGrpc()
        } ?: throw NotFoundException("not found post")

        val updateBlogPost = GBlogPost.newBuilder(oldBlogPost)
            .setTitle(request.title)
            .setContent(request.content)
            .setModifiedAt(Instant.now().toEpochMilli())
            .build()

        blogPostRepository.save(BlogPost.of(updateBlogPost))

        return updateBlogPost
    }

    override suspend fun deleteBlogPost(request: DeleteBlogPostRequest): Empty {
        val blogPost = blogPostRepository.findById(request.id) ?: throw NotFoundException("not found post")

        blogPostRepository.delete(blogPost)

        return Empty.getDefaultInstance()
    }
}