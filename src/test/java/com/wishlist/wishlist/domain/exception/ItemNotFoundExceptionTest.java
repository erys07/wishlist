package com.wishlist.wishlist.domain.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ItemNotFoundException - Domain Exception Tests")
class ItemNotFoundExceptionTest {

    @Test
    @DisplayName("should create exception with itemId only")
    void shouldCreateExceptionWithItemIdOnly() {
        String itemId = "item001";
        ItemNotFoundException exception = new ItemNotFoundException(itemId);

        assertThat(exception.getMessage()).isEqualTo("Item not found: " + itemId);
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("should create exception with userId and itemId")
    void shouldCreateExceptionWithUserIdAndItemId() {
        String userId = "user123";
        String itemId = "item001";
        ItemNotFoundException exception = new ItemNotFoundException(userId, itemId);

        assertThat(exception.getMessage()).isEqualTo("Item " + itemId + " not found in wishlist for user: " + userId);
    }

    @Test
    @DisplayName("should create exception with different values")
    void shouldCreateExceptionWithDifferentValues() {
        String userId = "user456";
        String itemId = "item999";
        ItemNotFoundException exception = new ItemNotFoundException(userId, itemId);

        assertThat(exception.getMessage()).contains("item999");
        assertThat(exception.getMessage()).contains("user456");
    }
}

