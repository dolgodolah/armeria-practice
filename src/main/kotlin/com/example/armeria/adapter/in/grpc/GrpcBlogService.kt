package com.example.armeria.adapter.`in`.grpc

import com.example.armeria.grpc.*
import io.grpc.Status
import io.grpc.stub.StreamObserver
import java.time.Instant
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

class GrpcBlogService : BlogServiceGrpc.BlogServiceImplBase() {

    companion object {
        private val idGenerator = AtomicLong()
        private val posts = ConcurrentHashMap<Long, BlogPost>()
    }

    override fun createBlogPost(request: CreateBlogPostRequest, responseObserver: StreamObserver<BlogPost>) {
        val id = idGenerator.getAndIncrement()
        val now = Instant.now()
        val updated = BlogPost.newBuilder()
            .setId(id)
            .setTitle(request.title)
            .setContent(request.content)
            .setModifiedAt(now.toEpochMilli())
            .setCreatedAt(now.toEpochMilli())
            .build()

        posts[id] = updated
        responseObserver.onNext(updated)
        responseObserver.onCompleted()
    }

    override fun getBlogPost(request: GetBlogPostRequest, responseObserver: StreamObserver<BlogPost>) {
        val blogPost = posts[request.id] ?: return onError(responseObserver)

        responseObserver.onNext(blogPost)
        responseObserver.onCompleted()
    }

    override fun listBlogPosts(request: ListBlogPostsRequest, responseObserver: StreamObserver<ListBlogPostsResponse>) {
        val blogPosts = if (request.descending) {
            posts.values.sortedByDescending { it.id }
        } else {
            posts.values
        }

        responseObserver.onNext(ListBlogPostsResponse.newBuilder().addAllBlogs(blogPosts).build())
        responseObserver.onCompleted()
    }

    private fun onError(responseObserver: StreamObserver<BlogPost>) {
        responseObserver.onError(
            Status.NOT_FOUND.withDescription("not found post").asRuntimeException()
        )
    }
}