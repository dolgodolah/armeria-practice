package com.example.armeria.restapi.application.domain

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.linecorp.armeria.common.AggregatedHttpRequest
import com.linecorp.armeria.server.ServiceRequestContext
import com.linecorp.armeria.server.annotation.RequestConverterFunction
import java.lang.IllegalArgumentException
import java.lang.reflect.ParameterizedType
import java.util.concurrent.atomic.AtomicLong


data class BlogPost(
    val id: Long,
    val title: String,
    val content: String,
    val createdAt: Long,
    val modifiedAt: Long
) {

    @JsonCreator
    constructor(id: Long, title: String, content: String) : this(
        id = id,
        title = title,
        content = content,
        createdAt = System.currentTimeMillis(),
        modifiedAt = System.currentTimeMillis()
    )
}

class BlogPostRequestConverter : RequestConverterFunction {
    companion object {
        private val mapper = ObjectMapper()
        private val idGenerator = AtomicLong()
    }
    override fun convertRequest(
        ctx: ServiceRequestContext,
        request: AggregatedHttpRequest,
        expectedResultType: Class<*>,
        expectedParameterizedResultType: ParameterizedType?
    ): Any {
        if (expectedResultType == BlogPost::class.java) {
            val jsonNode = mapper.readTree(request.contentUtf8())
            val id = idGenerator.getAndIncrement()
            val title = stringValue(jsonNode, "title")
            val content = stringValue(jsonNode, "content")
            return BlogPost(id, title, content)
        }

        return RequestConverterFunction.fallthrough()
    }

    private fun stringValue(jsonNode: JsonNode, field: String): String {
        val value = jsonNode.get(field) ?: throw IllegalArgumentException("$field is missing!")
        return value.textValue()
    }

}