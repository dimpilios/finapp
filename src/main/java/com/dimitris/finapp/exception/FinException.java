package com.dimitris.finapp.exception;

import org.springframework.http.HttpStatus;

import java.util.Objects;

/**
 * Wrapper exception class used to wrap exceptions produced when application functionalities acceptance criteria are
 * not met.
 */
public class FinException extends Exception {

    private HttpStatus httpStatus;

    public FinException(HttpStatus httpStatus, String msg) {
        super(msg);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FinException that = (FinException) o;
        return httpStatus == that.httpStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(httpStatus);
    }
}
