package com.wishlist.wishlist.infra.controller;

import com.wishlist.wishlist.application.dto.*;
import com.wishlist.wishlist.application.usecase.AddItemUseCase;
import com.wishlist.wishlist.application.usecase.GetWishlistItemsUseCase;
import com.wishlist.wishlist.application.usecase.ItemExistsInWishlistUseCase;
import com.wishlist.wishlist.application.usecase.RemoveItemUseCase;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wishlist")
@RequiredArgsConstructor
@Validated
public class WishlistController {

    private static final Logger log = LoggerFactory.getLogger(WishlistController.class);

    private final AddItemUseCase addItemUseCase;
    private final RemoveItemUseCase removeItemUseCase;
    private final GetWishlistItemsUseCase getWishlistItemsUseCase;
    private final ItemExistsInWishlistUseCase itemExistsInWishlistUseCase;

    @PostMapping("/item")
    public ResponseEntity<AddItemOutput> addItem(@Valid @RequestBody AddItemInput input) {
        log.info("Adding item to wishlist - userId: {}, itemId: {}, name: {}", 
                input.getUserId(), input.getItemId(), input.getName());
        AddItemOutput output = addItemUseCase.execute(input);
        log.info("Item added successfully - wishlistId: {}, itemId: {}", 
                output.getWishlistId(), output.getItemId());
        return ResponseEntity.status(HttpStatus.CREATED).body(output);
    }

    @DeleteMapping("/{userId}/items/{itemId}")
    public ResponseEntity<Void> removeItem(
            @PathVariable @NotBlank(message = "UserId is required") String userId,
            @PathVariable @NotBlank(message = "ItemId is required") String itemId
    ) {
        log.info("Removing item from wishlist - userId: {}, itemId: {}", userId, itemId);
        RemoveItemInput input = RemoveItemInput.builder()
                .userId(userId)
                .itemId(itemId)
                .build();
        removeItemUseCase.execute(input);
        log.info("Item removed successfully - userId: {}, itemId: {}", userId, itemId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{userId}/items")
    public ResponseEntity<GetWishlistItemsOutput> getItems(
            @PathVariable @NotBlank(message = "UserId is required") String userId) {
        log.debug("Getting wishlist items - userId: {}", userId);
        GetWishlistItemsInput input = new GetWishlistItemsInput();
        input.setUserId(userId);
        GetWishlistItemsOutput output = getWishlistItemsUseCase.execute(input);
        log.debug("Retrieved {} items for userId: {}", 
                output.getItems() != null ? output.getItems().size() : 0, userId);
        return ResponseEntity.ok(output);
    }

    @GetMapping("/{userId}/items/{itemId}")
    public ResponseEntity<ContainsItemOutput> contains(
            @PathVariable @NotBlank(message = "UserId is required") String userId,
            @PathVariable @NotBlank(message = "ItemId is required") String itemId
    ) {
        log.debug("Checking if item exists - userId: {}, itemId: {}", userId, itemId);
        ContainsItemInput input = ContainsItemInput.builder()
                .userId(userId)
                .itemId(itemId)
                .build();
        ContainsItemOutput output = itemExistsInWishlistUseCase.execute(input);
        log.debug("Item exists check result - userId: {}, itemId: {}, exists: {}", 
                userId, itemId, output.isExists());
        return ResponseEntity.ok(output);
    }
}
