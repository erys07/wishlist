package com.wishlist.wishlist.application.usecase;

import com.wishlist.wishlist.application.dto.GetWishlistItemsInput;
import com.wishlist.wishlist.application.dto.GetWishlistItemsOutput;
import com.wishlist.wishlist.application.service.WishlistService;
import com.wishlist.wishlist.domain.exception.WishlistNotFoundException;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("GetWishlistItemsUseCase - BDD Tests")
class GetWishlistItemsUseCaseImplTest {

    @Mock
    private WishlistService wishlistService;

    @InjectMocks
    private GetWishlistItemsUseCaseImpl getWishlistItemsUseCase;

    private GetWishlistItemsInput input;
    private String userId;

    @BeforeEach
    void setUp() {
        userId = "user123";
        input = new GetWishlistItemsInput();
        input.setUserId(userId);
    }

    @Test
    @DisplayName("Given an existing wishlist with items, when getting items, then should return all items")
    void givenExistingWishlistWithItems_whenGettingItems_thenShouldReturnAllItems() {
        WishlistItem item1 = WishlistItem.builder()
                .itemId("item001")
                .name("Item 1")
                .build();
        
        WishlistItem item2 = WishlistItem.builder()
                .itemId("item002")
                .name("Item 2")
                .build();

        Wishlist wishlist = Wishlist.builder()
                .id("wishlist-id")
                .userId(userId)
                .items(new ArrayList<>())
                .build();
        wishlist.getItems().add(item1);
        wishlist.getItems().add(item2);

        when(wishlistService.findWishlist(userId)).thenReturn(Optional.of(wishlist));

        GetWishlistItemsOutput output = getWishlistItemsUseCase.execute(input);

        assertThat(output).isNotNull();
        assertThat(output.getItems()).isNotNull();
        assertThat(output.getItems()).hasSize(2);
        assertThat(output.getItems().get(0).getItemId()).isEqualTo("item001");
        assertThat(output.getItems().get(0).getName()).isEqualTo("Item 1");
        assertThat(output.getItems().get(1).getItemId()).isEqualTo("item002");
        assertThat(output.getItems().get(1).getName()).isEqualTo("Item 2");
        
        verify(wishlistService).findWishlist(userId);
    }

    @Test
    @DisplayName("Given an existing empty wishlist, when getting items, then should return empty list")
    void givenExistingEmptyWishlist_whenGettingItems_thenShouldReturnEmptyList() {
        Wishlist emptyWishlist = Wishlist.builder()
                .id("wishlist-id")
                .userId(userId)
                .items(new ArrayList<>())
                .build();

        when(wishlistService.findWishlist(userId)).thenReturn(Optional.of(emptyWishlist));

        GetWishlistItemsOutput output = getWishlistItemsUseCase.execute(input);

        assertThat(output).isNotNull();
        assertThat(output.getItems()).isNotNull();
        assertThat(output.getItems()).isEmpty();
        
        verify(wishlistService).findWishlist(userId);
    }

    @Test
    @DisplayName("Given a non-existent wishlist, when getting items, then should throw WishlistNotFoundException")
    void givenNonExistentWishlist_whenGettingItems_thenShouldThrowException() {
        when(wishlistService.findWishlist(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> getWishlistItemsUseCase.execute(input))
                .isInstanceOf(WishlistNotFoundException.class)
                .hasMessageContaining("Wishlist not found")
                .hasMessageContaining(userId);

        verify(wishlistService).findWishlist(userId);
    }
}

