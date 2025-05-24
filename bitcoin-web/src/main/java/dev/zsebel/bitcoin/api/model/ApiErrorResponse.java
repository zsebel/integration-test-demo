package dev.zsebel.bitcoin.api.model;

import java.util.List;

import dev.zsebel.bitcoin.api.error.ApiError;

public record ApiErrorResponse(
    int status,
    String path,
    List<ApiError> errors
) {
}
