package com.joyero.base.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseErrorApi {
    private Date timestamp;
    private String status;
    private String error;
    private ErrorApi[] errors;
    private String message;
    private String trace;
    private String path;
}
