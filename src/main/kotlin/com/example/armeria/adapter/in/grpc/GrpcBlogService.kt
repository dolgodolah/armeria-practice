package com.example.armeria.adapter.`in`.grpc

import com.example.armeria.grpc.BlogPost
import com.example.armeria.grpc.BlogServiceGrpc
import com.example.armeria.grpc.CreateBlogPostRequest
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
}