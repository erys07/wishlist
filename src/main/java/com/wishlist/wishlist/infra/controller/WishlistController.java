package com.wishlist.wishlist.infra.controller;

import com.wishlist.wishlist.application.dto.*;
import com.wishlist.wishlist.application.usecase.AddItemUseCase;
import com.wishlist.wishlist.application.usecase.GetWishlistItemsUseCase;
import com.wishlist.wishlist.application.usecase.ItemExistsInWishlistUseCase;
import com.wishlist.wishlist.application.usecase.RemoveItemUseCase;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wishlist")
@RequiredArgsConstructor
@Validated
public class WishlistController {

    private final AddItemUseCase addItemUseCase;
    private final RemoveItemUseCase removeItemUseCase;
    private final GetWishlistItemsUseCase getWishlistItemsUseCase;
    private final ItemExistsInWishlistUseCase itemExistsInWishlistUseCase;

    @PostMapping("/item")
    public ResponseEntity<AddItemOutput> addItem(@Valid @RequestBody AddItemInput input) {
        AddItemOutput output = addItemUseCase.execute(input);
        return ResponseEntity.ok(output);
    }

    @DeleteMapping("/{userId}/items/{itemId}")
    public ResponseEntity<Void> removeItem(
            @PathVariable @NotBlank(message = "UserId is required") String userId,
            @PathVariable @NotBlank(message = "ItemId is required") String itemId
    ) {
        RemoveItemInput input = RemoveItemInput.builder()
                .userId(userId)
                .itemId(itemId)
                .build();
        removeItemUseCase.execute(input);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/items")
    public ResponseEntity<GetWishlistItemsOutput> getItems(
            @PathVariable @NotBlank(message = "UserId is required") String userId) {
        GetWishlistItemsInput input = new GetWishlistItemsInput();
        input.setUserId(userId);
        return ResponseEntity.ok(getWishlistItemsUseCase.execute(input));
    }

    @GetMapping("/{userId}/items/{itemId}")
    public ResponseEntity<ContainsItemOutput> contains(
            @PathVariable @NotBlank(message = "UserId is required") String userId,
            @PathVariable @NotBlank(message = "ItemId is required") String itemId
    ) {
        ContainsItemInput input = ContainsItemInput.builder()
                .userId(userId)
                .itemId(itemId)
                .build();
        return ResponseEntity.ok(itemExistsInWishlistUseCase.execute(input));
    }
}
