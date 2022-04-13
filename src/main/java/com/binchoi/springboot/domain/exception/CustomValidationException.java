package com.binchoi.springboot.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// cred: DongUk Lee (https://jojoldu.tistory.com/129)

@ResponseStatus(HttpStatus.BAD_REQUEST) // 400
public class CustomValidationException extends RuntimeException {
    private Error[] errors;

    public CustomValidationException(String defaultMessage, String field) {
        this.errors = new Error[] {new Error(defaultMessage, field)};
    }

    public CustomValidationException(Error[] errors) {
        this.errors = errors;
    }

    public Error[] getErrors() {
        return errors;
    }

    public static class Error {

        private String defaultMessage;
        private String field;

        public Error(String defaultMessage, String field) {
            this.defaultMessage = defaultMessage;
            this.field = field;
        }

        public String getDefaultMessage() {
            return defaultMessage;
        }

        public String getField() {
            return field;
        }
    }


}
