package com.example.armeria.application.config

import com.example.armeria.adapter.`in`.grpc.GrpcBlogService
import com.example.armeria.adapter.`in`.grpc.GrpcExceptionHandler
import com.example.armeria.adapter.`in`.restapi.BlogController
import com.example.armeria.grpc.BlogServiceGrpc
import com.example.armeria.grpc.GBlogPost
import com.linecorp.armeria.server.docs.DocService
import com.linecorp.armeria.server.docs.DocServiceFilter
import com.linecorp.armeria.server.grpc.GrpcService
import com.linecorp.armeria.spring.ArmeriaServerConfigurator
import io.grpc.BindableService
import io.grpc.reflection.v1alpha.ServerReflectionGrpc
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ArmeriaConfiguration {

    @Bean
    fun armeriaServerConfigurator(
        blogController: BlogController,
        grpcBlogService: GrpcBlogService
    ): ArmeriaServerConfigurator {
        return ArmeriaServerConfigurator { serverBuilder ->
            serverBuilder.http(8080)
            serverBuilder.annotatedService(blogController)
            serverBuilder.service(grpcService(listOf(grpcBlogService)))
            serverBuilder.serviceUnder("/docs", docService())
        }
    }

    private fun grpcService(grpcServices: List<BindableService>): GrpcService {
        return GrpcService.builder()
            .addServices(grpcServices)
            .enableUnframedRequests(true)
            .exceptionMapping(GrpcExceptionHandler())
            .useBlockingTaskExecutor(true) // By default, service methods are executed on the event loop and are expected to be implemented asynchronously. To implement blocking logic, call useBlockingTaskExecutor(true).
            .build()
    }

    private fun docService(): DocService {
        val grpcExampleRequest = GBlogPost.newBuilder()
            .setTitle("Example title")
            .setContent("Example content")
            .build()

        return DocService.builder()
            .exampleRequests(
                BlogServiceGrpc.SERVICE_NAME,
                "CreateBlogPost",
                grpcExampleRequest
            )
            .exclude(DocServiceFilter.ofServiceName(ServerReflectionGrpc.SERVICE_NAME))
            .exampleRequests(
                BlogController::class.java,
                "createPost",
                "{\"title\":\"My first blog\", \"content\":\"Hello Armeria!\"}"
            ).build()
    }
}