package com.nt.advice.Exception;

import java.time.LocalDateTime;

public class ApiError {


    private LocalDateTime timeStamp;
    private long status;
    private String error;
    private String message;

    public ApiError(LocalDateTime timeStamp, long status, String error, String message) {
        this.timeStamp = timeStamp;
        this.status = status;
        this.error = error;
        this.message = message;
    }
}
