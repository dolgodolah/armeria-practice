package com.example.armeria.application.config

import com.example.armeria.adapter.`in`.grpc.GrpcBlogService
import com.example.armeria.adapter.`in`.restapi.BlogController
import com.linecorp.armeria.server.docs.DocService
import com.linecorp.armeria.server.grpc.GrpcService
import com.linecorp.armeria.spring.ArmeriaServerConfigurator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ArmeriaConfiguration {

    @Bean
    fun armeriaServerConfigurator(blogController: BlogController): ArmeriaServerConfigurator {
        val grpcService = GrpcService.builder().addService(GrpcBlogService()).build()
        return ArmeriaServerConfigurator { serverBuilder ->
            serverBuilder.http(8080)
            serverBuilder.annotatedService(blogController)
            serverBuilder.service(grpcService)
            serverBuilder.serviceUnder("/docs", docService())
        }
    }

    private fun docService(): DocService {
        return DocService.builder()
            .exampleRequests(
                BlogController::class.java,
                "createPost",
                "{\"title\":\"My first blog\", \"content\":\"Hello Armeria!\"}"
            ).build()
    }
}