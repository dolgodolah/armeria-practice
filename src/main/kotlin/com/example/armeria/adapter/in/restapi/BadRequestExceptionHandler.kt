package com.example.armeria.adapter.`in`.restapi

import com.fasterxml.jackson.databind.ObjectMapper
import com.linecorp.armeria.common.HttpRequest
import com.linecorp.armeria.common.HttpResponse
import com.linecorp.armeria.common.HttpStatus
import com.linecorp.armeria.server.ServiceRequestContext
import com.linecorp.armeria.server.annotation.ExceptionHandlerFunction
import java.lang.IllegalArgumentException

class BadRequestExceptionHandler : ExceptionHandlerFunction {

    companion object {
        private val mapper = ObjectMapper()
    }

    override fun handleException(ctx: ServiceRequestContext, req: HttpRequest, cause: Throwable): HttpResponse {
        if (cause is IllegalArgumentException) {
            val objectNode = mapper.createObjectNode()
            objectNode.put("error", cause.message)
            return HttpResponse.ofJson(HttpStatus.BAD_REQUEST, objectNode)
        }

        return ExceptionHandlerFunction.fallthrough()
    }
}