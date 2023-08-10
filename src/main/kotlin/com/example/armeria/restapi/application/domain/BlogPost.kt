package com.example.armeria.restapi.application.domain

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.linecorp.armeria.common.AggregatedHttpRequest
import com.linecorp.armeria.server.ServiceRequestContext
import com.linecorp.armeria.server.annotation.RequestConverterFunction
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.lang.IllegalArgumentException
import java.lang.reflect.ParameterizedType
import java.time.LocalDateTime
import java.util.concurrent.atomic.AtomicLong

@Table // `CoroutineCrudRepository<BlogPost, ID>`가 빈으로 등록되려면 테이블 매핑이 되어야 함.
data class BlogPost(
    @Id
    val id: Long = 0,
    val title: String,
    val content: String,
    val createdAt: LocalDateTime,
    val modifiedAt: LocalDateTime
) {

    @JsonCreator
    constructor(title: String, content: String) : this(
        title = title,
        content = content,
        createdAt = LocalDateTime.now(),
        modifiedAt = LocalDateTime.now()
    )
}

class BlogPostRequestConverter : RequestConverterFunction {
    companion object {
        private val mapper = ObjectMapper()
    }
    override fun convertRequest(
        ctx: ServiceRequestContext,
        request: AggregatedHttpRequest,
        expectedResultType: Class<*>,
        expectedParameterizedResultType: ParameterizedType?
    ): Any {
        if (expectedResultType == BlogPost::class.java) {
            val jsonNode = mapper.readTree(request.contentUtf8())
            val title = stringValue(jsonNode, "title")
            val content = stringValue(jsonNode, "content")
            return BlogPost(title, content)
        }

        return RequestConverterFunction.fallthrough()
    }

    private fun stringValue(jsonNode: JsonNode, field: String): String {
        val value = jsonNode.get(field) ?: throw IllegalArgumentException("$field is missing!")
        return value.textValue()
    }

}