package dev.zsebel.bitcoin.api.model;

import dev.zsebel.bitcoin.api.error.ApiError;

import java.util.List;

public record ApiErrorResponse(
        int status,
        String path,
        List<ApiError> errors
) {}
