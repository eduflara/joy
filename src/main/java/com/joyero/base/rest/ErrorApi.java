package com.joyero.base.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorApi {
    private String[] codes;
    private String defaultMessage;
    private String objectName;
    private String field;
    private String rejectedValue;
    private String bindingFailure;
    private String code;

    public String getErrorMessage() {
        return field + " : " + defaultMessage;
    }

}
