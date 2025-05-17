package dev.zsebel.bitcoin.api.error;

import com.fasterxml.jackson.annotation.JsonProperty;

public sealed interface ApiError permits BusinessError, InternalError, ValidationError {

    @JsonProperty("type")
    ErrorType type();
    String message();
}
