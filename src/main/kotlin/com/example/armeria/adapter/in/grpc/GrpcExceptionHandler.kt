package com.example.armeria.adapter.`in`.grpc

import com.linecorp.armeria.common.RequestContext
import com.linecorp.armeria.common.grpc.GrpcStatusFunction
import io.grpc.Metadata
import io.grpc.Status
import java.lang.IllegalArgumentException

class GrpcExceptionHandler : GrpcStatusFunction {
    override fun apply(ctx: RequestContext, throwable: Throwable, metadata: Metadata): Status {
        if (throwable is IllegalArgumentException) {
            return Status.INVALID_ARGUMENT.withCause(throwable)
        }
        if (throwable is NotFoundException) {
            return Status.NOT_FOUND.withCause(throwable).withDescription(throwable.message)
        }

        return Status.INTERNAL
    }
}

class NotFoundException(
    override val message: String
) : IllegalStateException(message)