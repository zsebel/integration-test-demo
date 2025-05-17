package com.epam.bitcoin.api.model;

import com.epam.bitcoin.api.error.ApiError;

import java.util.List;

public record ApiErrorResponse(
        int status,
        String path,
        List<ApiError> errors
) {}
