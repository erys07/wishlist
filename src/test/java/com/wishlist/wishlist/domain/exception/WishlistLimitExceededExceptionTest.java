package com.wishlist.wishlist.domain.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("WishlistLimitExceededException - Domain Exception Tests")
class WishlistLimitExceededExceptionTest {

    @Test
    @DisplayName("should create exception with userId only")
    void shouldCreateExceptionWithUserIdOnly() {
        String userId = "user123";
        WishlistLimitExceededException exception = new WishlistLimitExceededException(userId);

        assertThat(exception.getMessage()).contains("Wishlist limit exceeded");
        assertThat(exception.getMessage()).contains(userId);
        assertThat(exception.getMessage()).contains("20");
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("should create exception with userId and currentSize")
    void shouldCreateExceptionWithUserIdAndCurrentSize() {
        String userId = "user123";
        int currentSize = 20;
        WishlistLimitExceededException exception = new WishlistLimitExceededException(userId, currentSize);

        assertThat(exception.getMessage()).contains("Wishlist limit exceeded");
        assertThat(exception.getMessage()).contains(userId);
        assertThat(exception.getMessage()).contains("Current items: 20");
        assertThat(exception.getMessage()).contains("maximum allowed: 20");
    }

    @Test
    @DisplayName("should create exception with different currentSize")
    void shouldCreateExceptionWithDifferentCurrentSize() {
        String userId = "user456";
        int currentSize = 25;
        WishlistLimitExceededException exception = new WishlistLimitExceededException(userId, currentSize);

        assertThat(exception.getMessage()).contains("Current items: 25");
        assertThat(exception.getMessage()).contains("maximum allowed: 20");
    }
}

