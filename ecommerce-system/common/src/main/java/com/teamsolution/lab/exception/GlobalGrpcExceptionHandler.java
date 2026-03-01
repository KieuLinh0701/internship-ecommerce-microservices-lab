package com.teamsolution.lab.exception;

import io.grpc.Status;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

@GrpcAdvice
public class GlobalGrpcExceptionHandler {
    @GrpcExceptionHandler(ResourceNotFoundException.class)
    public Status handleNotFound(ResourceNotFoundException ex) {
        return Status.NOT_FOUND.withDescription(ex.getMessage());
    }

    @GrpcExceptionHandler(DuplicateResourceException.class)
    public Status handleDuplicate(DuplicateResourceException ex) {
        return Status.ALREADY_EXISTS.withDescription(ex.getMessage());
    }

    @GrpcExceptionHandler(IllegalArgumentException.class)
    public Status handleInvalidArgument(IllegalArgumentException ex) {
        return Status.INVALID_ARGUMENT.withDescription("Invalid input");
    }

    @GrpcExceptionHandler(Exception.class)
    public Status handleOther(Exception ex) {
        return Status.INTERNAL.withDescription("Internal server error");
    }
}
