package com.wishlist.wishlist.application.usecase;

import com.wishlist.wishlist.application.dto.ContainsItemInput;
import com.wishlist.wishlist.application.dto.ContainsItemOutput;
import com.wishlist.wishlist.application.service.WishlistService;
import com.wishlist.wishlist.domain.model.Wishlist;
import com.wishlist.wishlist.domain.model.WishlistItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ItemExistsInWishlistUseCase - BDD Tests")
class ItemExistsInWishlistUseCaseImplTest {

    @Mock
    private WishlistService wishlistService;

    @InjectMocks
    private ItemExistsInWishlistUseCaseImpl itemExistsInWishlistUseCase;

    private ContainsItemInput input;
    private String userId;
    private String itemId;

    @BeforeEach
    void setUp() {
        userId = "user123";
        itemId = "item001";
        
        input = ContainsItemInput.builder()
                .userId(userId)
                .itemId(itemId)
                .build();
    }

    @Test
    @DisplayName("Given an existing wishlist with the item, when checking if item exists, then should return true")
    void givenExistingWishlistWithItem_whenCheckingIfItemExists_thenShouldReturnTrue() {
        WishlistItem item = WishlistItem.builder()
                .itemId(itemId)
                .name("Item Test")
                .build();

        Wishlist wishlist = Wishlist.builder()
                .id("wishlist-id")
                .userId(userId)
                .items(new ArrayList<>())
                .build();
        wishlist.getItems().add(item);

        when(wishlistService.findWishlist(userId)).thenReturn(Optional.of(wishlist));

        ContainsItemOutput output = itemExistsInWishlistUseCase.execute(input);

        assertThat(output).isNotNull();
        assertThat(output.isExists()).isTrue();
        
        verify(wishlistService).findWishlist(userId);
    }

    @Test
    @DisplayName("Given an existing wishlist without the item, when checking if item exists, then should return false")
    void givenExistingWishlistWithoutItem_whenCheckingIfItemExists_thenShouldReturnFalse() {
        WishlistItem otherItem = WishlistItem.builder()
                .itemId("other-item")
                .name("Other Item")
                .build();

        Wishlist wishlist = Wishlist.builder()
                .id("wishlist-id")
                .userId(userId)
                .items(new ArrayList<>())
                .build();
        wishlist.getItems().add(otherItem);

        when(wishlistService.findWishlist(userId)).thenReturn(Optional.of(wishlist));

        ContainsItemOutput output = itemExistsInWishlistUseCase.execute(input);

        assertThat(output).isNotNull();
        assertThat(output.isExists()).isFalse();
        
        verify(wishlistService).findWishlist(userId);
    }

    @Test
    @DisplayName("Given a non-existent wishlist, when checking if item exists, then should return false")
    void givenNonExistentWishlist_whenCheckingIfItemExists_thenShouldReturnFalse() {
        when(wishlistService.findWishlist(userId)).thenReturn(Optional.empty());

        ContainsItemOutput output = itemExistsInWishlistUseCase.execute(input);

        assertThat(output).isNotNull();
        assertThat(output.isExists()).isFalse();
        
        verify(wishlistService).findWishlist(userId);
    }

    @Test
    @DisplayName("Given an empty wishlist, when checking if item exists, then should return false")
    void givenEmptyWishlist_whenCheckingIfItemExists_thenShouldReturnFalse() {
        Wishlist emptyWishlist = Wishlist.builder()
                .id("wishlist-id")
                .userId(userId)
                .items(new ArrayList<>())
                .build();

        when(wishlistService.findWishlist(userId)).thenReturn(Optional.of(emptyWishlist));

        ContainsItemOutput output = itemExistsInWishlistUseCase.execute(input);

        assertThat(output).isNotNull();
        assertThat(output.isExists()).isFalse();
        
        verify(wishlistService).findWishlist(userId);
    }
}

