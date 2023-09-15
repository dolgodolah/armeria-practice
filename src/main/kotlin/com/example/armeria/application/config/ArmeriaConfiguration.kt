package com.example.armeria.application.config

import com.example.armeria.adapter.`in`.grpc.GrpcBlogService
import com.example.armeria.adapter.`in`.grpc.GrpcExceptionHandler
import com.example.armeria.adapter.`in`.restapi.BlogController
import com.example.armeria.grpc.BlogServiceGrpc
import com.example.armeria.grpc.GBlogPost
import com.linecorp.armeria.common.grpc.GrpcJsonMarshaller
import com.linecorp.armeria.server.docs.DocService
import com.linecorp.armeria.server.docs.DocServiceFilter
import com.linecorp.armeria.server.grpc.GrpcService
import com.linecorp.armeria.spring.ArmeriaServerConfigurator
import io.grpc.BindableService
import io.grpc.Metadata
import io.grpc.ServerCall
import io.grpc.kotlin.CoroutineContextServerInterceptor
import io.grpc.reflection.v1alpha.ServerReflectionGrpc
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import kotlin.coroutines.CoroutineContext

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
            .intercept(ContextInterceptor)
            .enableUnframedRequests(true)
            .jsonMarshallerFactory { GrpcJsonMarshaller.ofGson() }
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

class TestContext(val value: String) : CoroutineContext.Element {
    companion object Key : CoroutineContext.Key<TestContext>
    override val key: CoroutineContext.Key<*>
        get() = Key

}
object ContextInterceptor : CoroutineContextServerInterceptor() {
    override fun coroutineContext(call: ServerCall<*, *>, headers: Metadata): CoroutineContext {
        val headers = headers.get(Metadata.Key.of("test", Metadata.ASCII_STRING_MARSHALLER)) ?: "default"
        return TestContext(headers)
    }
}