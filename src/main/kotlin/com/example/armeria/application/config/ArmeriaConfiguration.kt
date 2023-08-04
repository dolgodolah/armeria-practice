package com.example.armeria.application.config

import com.linecorp.armeria.server.docs.DocService
import com.linecorp.armeria.spring.ArmeriaServerConfigurator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ArmeriaConfiguration {

    @Bean
    fun armeriaServerConfigurator(blogController: BlogController): ArmeriaServerConfigurator {
        return ArmeriaServerConfigurator { serverBuilder ->
            serverBuilder.http(8080)
            serverBuilder.serviceUnder("/docs", docService())
        }
    }

    private fun docService(): DocService {
        return DocService.builder().build()
    }
}