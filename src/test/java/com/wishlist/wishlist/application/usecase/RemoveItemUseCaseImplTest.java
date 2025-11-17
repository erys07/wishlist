package com.wishlist.wishlist.application.usecase;

import com.wishlist.wishlist.application.dto.RemoveItemInput;
import com.wishlist.wishlist.application.service.WishlistService;
import com.wishlist.wishlist.domain.exception.WishlistNotFoundException;
import com.wishlist.wishlist.domain.model.Wishlist;
import com.wishlist.wishlist.domain.model.WishlistItem;
import com.wishlist.wishlist.domain.repository.WishlistRepository;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RemoveItemUseCase - BDD Tests")
class RemoveItemUseCaseImplTest {

    @Mock
    private WishlistRepository wishlistRepository;

    @Mock
    private WishlistService wishlistService;

    @InjectMocks
    private RemoveItemUseCaseImpl removeItemUseCase;

    private RemoveItemInput input;
    private String userId;
    private String itemId;

    @BeforeEach
    void setUp() {
        userId = "user123";
        itemId = "item001";
        
        input = RemoveItemInput.builder()
                .userId(userId)
                .itemId(itemId)
                .build();
    }

    @Test
    @DisplayName("Given an existing wishlist with items, when removing an item, then should remove item and save wishlist")
    void givenExistingWishlistWithItems_whenRemovingItem_thenShouldRemoveItemAndSave() {
        WishlistItem itemToRemove = WishlistItem.builder()
                .itemId(itemId)
                .name("Item to Remove")
                .build();
        
        WishlistItem otherItem = WishlistItem.builder()
                .itemId("item002")
                .name("Other Item")
                .build();

        Wishlist wishlist = Wishlist.builder()
                .id("wishlist-id")
                .userId(userId)
                .items(new ArrayList<>())
                .build();
        wishlist.getItems().add(itemToRemove);
        wishlist.getItems().add(otherItem);

        Wishlist savedWishlist = Wishlist.builder()
                .id("wishlist-id")
                .userId(userId)
                .items(new ArrayList<>())
                .build();
        savedWishlist.getItems().add(otherItem);

        when(wishlistService.findWishlist(userId)).thenReturn(Optional.of(wishlist));
        when(wishlistRepository.save(any(Wishlist.class))).thenReturn(savedWishlist);

        removeItemUseCase.execute(input);

        verify(wishlistService).findWishlist(userId);
        verify(wishlistRepository).save(any(Wishlist.class));
        assertThat(wishlist.getItems().stream()
                .anyMatch(item -> item.getItemId().equals(itemId))).isFalse();
        assertThat(wishlist.getItems().stream()
                .anyMatch(item -> item.getItemId().equals("item002"))).isTrue();
    }

    @Test
    @DisplayName("Given an existing wishlist, when removing a non-existent item, then should not save wishlist")
    void givenExistingWishlist_whenRemovingNonExistentItem_thenShouldNotSave() {
        Wishlist wishlist = Wishlist.builder()
                .id("wishlist-id")
                .userId(userId)
                .items(new ArrayList<>())
                .build();
        wishlist.getItems().add(WishlistItem.builder()
                .itemId("other-item")
                .name("Other Item")
                .build());

        when(wishlistService.findWishlist(userId)).thenReturn(Optional.of(wishlist));

        removeItemUseCase.execute(input);

        verify(wishlistService).findWishlist(userId);
        verify(wishlistRepository, never()).save(any(Wishlist.class));
    }

    @Test
    @DisplayName("Given a non-existent wishlist, when removing an item, then should throw WishlistNotFoundException")
    void givenNonExistentWishlist_whenRemovingItem_thenShouldThrowException() {
        when(wishlistService.findWishlist(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> removeItemUseCase.execute(input))
                .isInstanceOf(WishlistNotFoundException.class)
                .hasMessageContaining("Wishlist not found")
                .hasMessageContaining(userId);

        verify(wishlistService).findWishlist(userId);
        verify(wishlistRepository, never()).save(any(Wishlist.class));
    }

    @Test
    @DisplayName("Given an empty wishlist, when removing an item, then should not save wishlist")
    void givenEmptyWishlist_whenRemovingItem_thenShouldNotSave() {
        Wishlist emptyWishlist = Wishlist.builder()
                .id("wishlist-id")
                .userId(userId)
                .items(new ArrayList<>())
                .build();

        when(wishlistService.findWishlist(userId)).thenReturn(Optional.of(emptyWishlist));

        removeItemUseCase.execute(input);

        verify(wishlistService).findWishlist(userId);
        verify(wishlistRepository, never()).save(any(Wishlist.class));
    }
}

