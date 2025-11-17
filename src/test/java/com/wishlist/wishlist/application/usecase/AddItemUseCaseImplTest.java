package com.wishlist.wishlist.application.usecase;

import com.wishlist.wishlist.application.dto.AddItemInput;
import com.wishlist.wishlist.application.dto.AddItemOutput;
import com.wishlist.wishlist.application.service.WishlistService;
import com.wishlist.wishlist.domain.exception.WishlistLimitExceededException;
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
@DisplayName("AddItemUseCase - BDD Tests")
class AddItemUseCaseImplTest {

    @Mock
    private WishlistRepository wishlistRepository;

    @Mock
    private WishlistService wishlistService;

    @InjectMocks
    private AddItemUseCaseImpl addItemUseCase;

    private AddItemInput input;
    private String userId;
    private String itemId;
    private String itemName;

    @BeforeEach
    void setUp() {
        userId = "user123";
        itemId = "item001";
        itemName = "Produto Teste";
        
        input = new AddItemInput();
        input.setUserId(userId);
        input.setItemId(itemId);
        input.setName(itemName);
    }

    @Test
    @DisplayName("Given a new wishlist, when adding an item, then should create wishlist and add item successfully")
    void givenNewWishlist_whenAddingItem_thenShouldCreateWishlistAndAddItem() {
        Optional<Wishlist> emptyWishlist = Optional.empty();
        Wishlist savedWishlist = Wishlist.builder()
                .id("wishlist-id")
                .userId(userId)
                .items(new ArrayList<>())
                .build();
        savedWishlist.getItems().add(WishlistItem.builder()
                .itemId(itemId)
                .name(itemName)
                .build());

        when(wishlistService.findWishlist(userId)).thenReturn(emptyWishlist);
        when(wishlistRepository.save(any(Wishlist.class))).thenReturn(savedWishlist);

        AddItemOutput output = addItemUseCase.execute(input);

        assertThat(output).isNotNull();
        assertThat(output.getWishlistId()).isEqualTo("wishlist-id");
        assertThat(output.getItemId()).isEqualTo(itemId);
        assertThat(output.getName()).isEqualTo(itemName);
        
        verify(wishlistService).findWishlist(userId);
        verify(wishlistRepository).save(any(Wishlist.class));
    }

    @Test
    @DisplayName("Given an existing wishlist, when adding a new item, then should add item to existing wishlist")
    void givenExistingWishlist_whenAddingNewItem_thenShouldAddItemToWishlist() {
        Wishlist existingWishlist = Wishlist.builder()
                .id("wishlist-id")
                .userId(userId)
                .items(new ArrayList<>())
                .build();
        existingWishlist.getItems().add(WishlistItem.builder()
                .itemId("existing-item")
                .name("Item Existente")
                .build());

        Wishlist savedWishlist = Wishlist.builder()
                .id("wishlist-id")
                .userId(userId)
                .items(new ArrayList<>(existingWishlist.getItems()))
                .build();
        savedWishlist.getItems().add(WishlistItem.builder()
                .itemId(itemId)
                .name(itemName)
                .build());

        when(wishlistService.findWishlist(userId)).thenReturn(Optional.of(existingWishlist));
        when(wishlistRepository.save(any(Wishlist.class))).thenReturn(savedWishlist);

        AddItemOutput output = addItemUseCase.execute(input);

        assertThat(output).isNotNull();
        assertThat(output.getWishlistId()).isEqualTo("wishlist-id");
        assertThat(output.getItemId()).isEqualTo(itemId);
        assertThat(output.getName()).isEqualTo(itemName);
        
        verify(wishlistService).findWishlist(userId);
        verify(wishlistRepository).save(any(Wishlist.class));
    }

    @Test
    @DisplayName("Given an existing wishlist with the same item, when adding duplicate item, then should return existing item without saving")
    void givenExistingWishlistWithItem_whenAddingDuplicateItem_thenShouldReturnExistingItem() {
        WishlistItem existingItem = WishlistItem.builder()
                .itemId(itemId)
                .name(itemName)
                .build();
        
        Wishlist existingWishlist = Wishlist.builder()
                .id("wishlist-id")
                .userId(userId)
                .items(new ArrayList<>())
                .build();
        existingWishlist.getItems().add(existingItem);

        when(wishlistService.findWishlist(userId)).thenReturn(Optional.of(existingWishlist));

        AddItemOutput output = addItemUseCase.execute(input);

        assertThat(output).isNotNull();
        assertThat(output.getWishlistId()).isEqualTo("wishlist-id");
        assertThat(output.getItemId()).isEqualTo(itemId);
        assertThat(output.getName()).isEqualTo(itemName);
        
        verify(wishlistService).findWishlist(userId);
        verify(wishlistRepository, never()).save(any(Wishlist.class));
    }

    @Test
    @DisplayName("Given a wishlist with 20 items, when adding a new item, then should throw WishlistLimitExceededException")
    void givenWishlistWithMaxItems_whenAddingNewItem_thenShouldThrowException() {
        Wishlist fullWishlist = Wishlist.builder()
                .id("wishlist-id")
                .userId(userId)
                .items(new ArrayList<>())
                .build();
        
        for (int i = 1; i <= 20; i++) {
            fullWishlist.getItems().add(WishlistItem.builder()
                    .itemId("item" + i)
                    .name("Item " + i)
                    .build());
        }

        when(wishlistService.findWishlist(userId)).thenReturn(Optional.of(fullWishlist));

        assertThatThrownBy(() -> addItemUseCase.execute(input))
                .isInstanceOf(WishlistLimitExceededException.class)
                .hasMessageContaining("Wishlist limit exceeded")
                .hasMessageContaining(userId)
                .hasMessageContaining("20");

        verify(wishlistService).findWishlist(userId);
        verify(wishlistRepository, never()).save(any(Wishlist.class));
    }
}

