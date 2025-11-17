package com.wishlist.wishlist.domain.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("WishlistNotFoundException - Domain Exception Tests")
class WishlistNotFoundExceptionTest {

    @Test
    @DisplayName("should create exception with correct message")
    void shouldCreateExceptionWithCorrectMessage() {
        String userId = "user123";
        WishlistNotFoundException exception = new WishlistNotFoundException(userId);

        assertThat(exception.getMessage()).isEqualTo("Wishlist not found for user: " + userId);
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("should create exception with different userId")
    void shouldCreateExceptionWithDifferentUserId() {
        String userId = "user456";
        WishlistNotFoundException exception = new WishlistNotFoundException(userId);

        assertThat(exception.getMessage()).isEqualTo("Wishlist not found for user: " + userId);
    }
}

