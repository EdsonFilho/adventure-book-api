package com.edsonfilho.adventure.book.infrastructure.web;

import com.edsonfilho.adventure.book.domain.exception.BookNotFoundException;
import com.edsonfilho.adventure.book.domain.exception.BookValidationException;
import com.edsonfilho.adventure.book.domain.exception.CategoryAlreadyExistsException;
import com.edsonfilho.adventure.book.domain.exception.CategoryNotFoundException;
import com.edsonfilho.adventure.book.domain.exception.GameOverException;
import com.edsonfilho.adventure.book.domain.exception.GameSessionNotFoundException;
import com.edsonfilho.adventure.book.domain.exception.InvalidOptionException;
import com.edsonfilho.adventure.book.infrastructure.web.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Centralized exception-to-HTTP mapping for the web layer.
 *
 * <p>Domain exceptions bubble up from the service layer untouched.
 * This class catches them here — at the boundary — and translates them
 * into appropriate HTTP responses, keeping domain code free of HTTP concerns.</p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBookNotFound(BookNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("BOOK_NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler(BookValidationException.class)
    public ResponseEntity<ErrorResponse> handleBookValidation(BookValidationException ex) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ErrorResponse("BOOK_VALIDATION_ERROR", ex.getMessage()));
    }

    @ExceptionHandler(CategoryAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleCategoryAlreadyExists(CategoryAlreadyExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse("CATEGORY_ALREADY_EXISTS", ex.getMessage()));
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCategoryNotFound(CategoryNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("CATEGORY_NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler(GameSessionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleSessionNotFound(GameSessionNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("SESSION_NOT_FOUND", ex.getMessage()));
    }

    @ExceptionHandler(InvalidOptionException.class)
    public ResponseEntity<ErrorResponse> handleInvalidOption(InvalidOptionException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("INVALID_OPTION", ex.getMessage()));
    }

    @ExceptionHandler(GameOverException.class)
    public ResponseEntity<ErrorResponse> handleGameOver(GameOverException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse("GAME_OVER", ex.getMessage()));
    }
}
