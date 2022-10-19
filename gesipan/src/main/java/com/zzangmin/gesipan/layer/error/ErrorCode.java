package com.zzangmin.gesipan.layer.error;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(400, "C001", "Invalid Input Value"),
    MISSING_INPUT_VALUE(400, "C003", "Missing Input Value"),
    METHOD_NOT_ALLOWED(405, "C002", "Invalid Method Value"),
    HANDLE_ACCESS_DENIED(403, "C006", "Access is Denied"),
    INTERNAL_SERVER_ERROR(500, "C007", "Server Error"),
    ENUM_TYPE_MISSMATCH(400, "C008", "Enum type not matched"),
    // Member
    EMAIL_DUPLICATION(400, "M001", "Email is Duplication"),
    LOGIN_INPUT_INVALID(400, "M002", "Login input is invalid"),
    // Query
    INVALID_QUERY_ARGUMENT(404, "D001", "Data not found"),
    INVALID_QUERY_STATE(400, "D002", "Data state not valid"),
            ;

    private final String code;
    private final String message;
    private int status;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }
}
