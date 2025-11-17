package com.wishlist.wishlist.infra.controller;

import com.wishlist.wishlist.application.dto.*;
import com.wishlist.wishlist.application.usecase.AddItemUseCase;
import com.wishlist.wishlist.application.usecase.GetWishlistItemsUseCase;
import com.wishlist.wishlist.application.usecase.ItemExistsInWishlistUseCase;
import com.wishlist.wishlist.application.usecase.RemoveItemUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("WishlistController - REST API Tests")
class WishlistControllerTest {

    @Mock
    private AddItemUseCase addItemUseCase;

    @Mock
    private RemoveItemUseCase removeItemUseCase;

    @Mock
    private GetWishlistItemsUseCase getWishlistItemsUseCase;

    @Mock
    private ItemExistsInWishlistUseCase itemExistsInWishlistUseCase;

    @InjectMocks
    private WishlistController wishlistController;

    @Test
    @DisplayName("POST /wishlist/item - should return 201 Created when item is added successfully")
    void shouldReturn201WhenItemAddedSuccessfully() {
        AddItemInput input = new AddItemInput();
        input.setUserId("user123");
        input.setItemId("item001");
        input.setName("Product Name");

        AddItemOutput output = AddItemOutput.builder()
                .wishlistId("wishlist123")
                .itemId("item001")
                .name("Product Name")
                .build();

        when(addItemUseCase.execute(any(AddItemInput.class))).thenReturn(output);

        ResponseEntity<AddItemOutput> response = wishlistController.addItem(input);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getWishlistId()).isEqualTo("wishlist123");
        assertThat(response.getBody().getItemId()).isEqualTo("item001");
        assertThat(response.getBody().getName()).isEqualTo("Product Name");
    }

    @Test
    @DisplayName("DELETE /wishlist/{userId}/items/{itemId} - should return 204 No Content when item is removed")
    void shouldReturn204WhenItemRemoved() {
        ResponseEntity<Void> response = wishlistController.removeItem("user123", "item001");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(response.getBody()).isNull();
    }

    @Test
    @DisplayName("GET /wishlist/{userId}/items - should return 200 OK with items list")
    void shouldReturn200WithItemsList() {
        GetWishlistItemsOutput.Item item1 = GetWishlistItemsOutput.Item.builder()
                .itemId("item001")
                .name("Product 1")
                .build();
        GetWishlistItemsOutput.Item item2 = GetWishlistItemsOutput.Item.builder()
                .itemId("item002")
                .name("Product 2")
                .build();

        GetWishlistItemsOutput output = new GetWishlistItemsOutput();
        output.setItems(Arrays.asList(item1, item2));

        when(getWishlistItemsUseCase.execute(any(GetWishlistItemsInput.class))).thenReturn(output);

        ResponseEntity<GetWishlistItemsOutput> response = wishlistController.getItems("user123");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getItems()).hasSize(2);
        assertThat(response.getBody().getItems().get(0).getItemId()).isEqualTo("item001");
        assertThat(response.getBody().getItems().get(0).getName()).isEqualTo("Product 1");
        assertThat(response.getBody().getItems().get(1).getItemId()).isEqualTo("item002");
        assertThat(response.getBody().getItems().get(1).getName()).isEqualTo("Product 2");
    }

    @Test
    @DisplayName("GET /wishlist/{userId}/items - should return 200 OK with empty list when wishlist is empty")
    void shouldReturn200WithEmptyList() {
        GetWishlistItemsOutput output = new GetWishlistItemsOutput();
        output.setItems(List.of());

        when(getWishlistItemsUseCase.execute(any(GetWishlistItemsInput.class))).thenReturn(output);

        ResponseEntity<GetWishlistItemsOutput> response = wishlistController.getItems("user123");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getItems()).isEmpty();
    }

    @Test
    @DisplayName("GET /wishlist/{userId}/items/{itemId} - should return 200 OK with exists true when item exists")
    void shouldReturn200WithExistsTrue() {
        ContainsItemOutput output = new ContainsItemOutput(true);

        when(itemExistsInWishlistUseCase.execute(any(ContainsItemInput.class))).thenReturn(output);

        ResponseEntity<ContainsItemOutput> response = wishlistController.contains("user123", "item001");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isExists()).isTrue();
    }

    @Test
    @DisplayName("GET /wishlist/{userId}/items/{itemId} - should return 200 OK with exists false when item does not exist")
    void shouldReturn200WithExistsFalse() {
        ContainsItemOutput output = new ContainsItemOutput(false);

        when(itemExistsInWishlistUseCase.execute(any(ContainsItemInput.class))).thenReturn(output);

        ResponseEntity<ContainsItemOutput> response = wishlistController.contains("user123", "item001");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isExists()).isFalse();
    }
}

