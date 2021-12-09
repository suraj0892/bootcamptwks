package com.tw.bootcamp.bookshop.error;

import com.tw.bootcamp.bookshop.book.exceptions.InvalidFileFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@ControllerAdvice
public class MissingServletPartHandler {

    @ExceptionHandler({ HttpMediaTypeNotSupportedException.class, MissingServletRequestPartException.class , MultipartException.class, InvalidFileFormatException.class})
    public ResponseEntity<ErrorResponse> handleMissingServletRequestPartException(Exception ex) {
        ErrorResponse apiError = new ErrorResponse(HttpStatus.BAD_REQUEST, "Missing File. Upload a valid csv file");
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
