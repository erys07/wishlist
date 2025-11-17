package com.wishlist.wishlist.infra.controller.exception;

import com.wishlist.wishlist.domain.exception.ItemNotFoundException;
import com.wishlist.wishlist.domain.exception.WishlistLimitExceededException;
import com.wishlist.wishlist.domain.exception.WishlistNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("GlobalExceptionHandler - Exception Handling Tests")
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler exceptionHandler;

    @Mock
    private HttpServletRequest request;

    @Test
    @DisplayName("handleWishlistNotFound - should return 404 with error response")
    void shouldHandleWishlistNotFound() {
        when(request.getRequestURI()).thenReturn("/wishlist/user123/items");
        WishlistNotFoundException ex = new WishlistNotFoundException("user123");

        ResponseEntity<?> response = exceptionHandler.handleWishlistNotFound(ex, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().toString()).contains("Wishlist not found for user: user123");
    }

    @Test
    @DisplayName("handleItemNotFound - should return 404 with error response")
    void shouldHandleItemNotFound() {
        when(request.getRequestURI()).thenReturn("/wishlist/user123/items/item001");
        ItemNotFoundException ex = new ItemNotFoundException("user123", "item001");

        ResponseEntity<?> response = exceptionHandler.handleItemNotFound(ex, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().toString()).contains("Item item001 not found in wishlist for user: user123");
    }

    @Test
    @DisplayName("handleWishlistLimitExceeded - should return 400 with error response")
    void shouldHandleWishlistLimitExceeded() {
        when(request.getRequestURI()).thenReturn("/wishlist/item");
        WishlistLimitExceededException ex = new WishlistLimitExceededException("user123", 20);

        ResponseEntity<?> response = exceptionHandler.handleWishlistLimitExceeded(ex, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().toString()).contains("Wishlist limit exceeded");
    }

    @Test
    @DisplayName("handleValidation - should return 400 with validation errors")
    void shouldHandleValidation() {
        when(request.getRequestURI()).thenReturn("/wishlist/item");
        
        MethodArgumentNotValidException ex = org.mockito.Mockito.mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = org.mockito.Mockito.mock(BindingResult.class);
        FieldError fieldError = new FieldError("addItemInput", "userId", "UserId is required");

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        ResponseEntity<?> response = exceptionHandler.handleValidation(ex, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().toString()).contains("Validation failed");
        assertThat(response.getBody().toString()).contains("userId");
    }

    @Test
    @DisplayName("handleConstraintViolation - should return 400 with constraint violations")
    void shouldHandleConstraintViolation() {
        when(request.getRequestURI()).thenReturn("/wishlist/user123/items");
        
        ConstraintViolation<?> violation = org.mockito.Mockito.mock(ConstraintViolation.class);
        when(violation.getMessage()).thenReturn("UserId is required");
        
        ConstraintViolationException ex = new ConstraintViolationException("Validation failed", Set.of(violation));

        ResponseEntity<?> response = exceptionHandler.handleConstraintViolation(ex, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().toString()).contains("Validation failed");
    }

    @Test
    @DisplayName("handleGenericException - should return 500 with error response")
    void shouldHandleGenericException() {
        when(request.getRequestURI()).thenReturn("/wishlist/item");
        Exception ex = new RuntimeException("Unexpected error occurred");

        ResponseEntity<?> response = exceptionHandler.handleGenericException(ex, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().toString()).contains("Unexpected error occurred");
    }
}

