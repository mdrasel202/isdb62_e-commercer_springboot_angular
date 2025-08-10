package com.rasel.security_demo.GlobalExceptionHandler;

import com.rasel.security_demo.ResourceNotFoundException.ResourceNotFoundException;
import com.rasel.security_demo.dto.ErrorResponse;
import com.rasel.security_demo.exception.DiscountNotFoundException;
import com.rasel.security_demo.exception.FileStoreageException;
import com.rasel.security_demo.exception.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxSizeException(MaxUploadSizeExceededException exc) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "File size exceeds maximum limit");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globalExceptionHandler(Exception ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //product
    @ExceptionHandler(ProductNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleProductNotFoundException(ProductNotFoundException ex){
            return new ResponseEntity<>(
                    new ErrorResponse (HttpStatus.NOT_FOUND.value(), ex.getMessage()),
                            HttpStatus.NOT_FOUND);
        }

    //product discount
    @ExceptionHandler(DiscountNotFoundException.class)
    public ResponseEntity<ErrorResponse> hangleDiscountNotFoundException(DiscountNotFoundException ex){
        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    //file storage
    @ExceptionHandler(FileStoreageException.class)
    public ResponseEntity<ErrorResponse> hangleFileStorageException(FileStoreageException ex){
        return new ResponseEntity<>(
                new ErrorResponse(HttpStatus.NO_CONTENT.value(), ex.getMessage()),
                HttpStatus.NOT_FOUND);
    }
}
