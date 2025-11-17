package com.wishlist.wishlist.application.service;

import com.wishlist.wishlist.domain.model.Wishlist;
import com.wishlist.wishlist.domain.model.WishlistItem;
import com.wishlist.wishlist.domain.repository.WishlistRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("WishlistService - Service Layer Tests")
class WishlistServiceTest {

    @Mock
    private WishlistRepository wishlistRepository;

    @InjectMocks
    private WishlistService wishlistService;

    @Test
    @DisplayName("findWishlist - should return wishlist when exists")
    void shouldReturnWishlistWhenExists() {
        String userId = "user123";
        Wishlist wishlist = Wishlist.builder()
                .id("wishlist123")
                .userId(userId)
                .items(new ArrayList<>())
                .build();

        when(wishlistRepository.findByUserId(userId)).thenReturn(Optional.of(wishlist));

        Optional<Wishlist> result = wishlistService.findWishlist(userId);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo("wishlist123");
        assertThat(result.get().getUserId()).isEqualTo(userId);
        verify(wishlistRepository).findByUserId(userId);
    }

    @Test
    @DisplayName("findWishlist - should return empty when wishlist does not exist")
    void shouldReturnEmptyWhenWishlistDoesNotExist() {
        String userId = "user123";

        when(wishlistRepository.findByUserId(userId)).thenReturn(Optional.empty());

        Optional<Wishlist> result = wishlistService.findWishlist(userId);

        assertThat(result).isEmpty();
        verify(wishlistRepository).findByUserId(userId);
    }

    @Test
    @DisplayName("findWishlist - should return wishlist with items")
    void shouldReturnWishlistWithItems() {
        String userId = "user123";
        WishlistItem item1 = WishlistItem.builder()
                .itemId("item001")
                .name("Product 1")
                .build();
        WishlistItem item2 = WishlistItem.builder()
                .itemId("item002")
                .name("Product 2")
                .build();

        Wishlist wishlist = Wishlist.builder()
                .id("wishlist123")
                .userId(userId)
                .items(new ArrayList<>())
                .build();
        wishlist.getItems().add(item1);
        wishlist.getItems().add(item2);

        when(wishlistRepository.findByUserId(userId)).thenReturn(Optional.of(wishlist));

        Optional<Wishlist> result = wishlistService.findWishlist(userId);

        assertThat(result).isPresent();
        assertThat(result.get().getItems()).hasSize(2);
        assertThat(result.get().getItems().get(0).getItemId()).isEqualTo("item001");
        assertThat(result.get().getItems().get(1).getItemId()).isEqualTo("item002");
    }

    @Test
    @DisplayName("findWishlist - should handle different userIds")
    void shouldHandleDifferentUserIds() {
        String userId1 = "user123";
        String userId2 = "user456";

        when(wishlistRepository.findByUserId(userId1)).thenReturn(Optional.empty());
        when(wishlistRepository.findByUserId(userId2)).thenReturn(Optional.empty());

        Optional<Wishlist> result1 = wishlistService.findWishlist(userId1);
        Optional<Wishlist> result2 = wishlistService.findWishlist(userId2);

        assertThat(result1).isEmpty();
        assertThat(result2).isEmpty();
        verify(wishlistRepository).findByUserId(userId1);
        verify(wishlistRepository).findByUserId(userId2);
    }
}

