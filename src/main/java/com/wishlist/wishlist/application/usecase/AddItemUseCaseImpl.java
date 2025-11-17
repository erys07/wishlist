package com.wishlist.wishlist.application.usecase;

import com.wishlist.wishlist.application.dto.AddItemInput;
import com.wishlist.wishlist.application.dto.AddItemOutput;
import com.wishlist.wishlist.domain.model.Wishlist;
import com.wishlist.wishlist.domain.model.WishlistItem;
import com.wishlist.wishlist.domain.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AddItemUseCaseImpl implements AddItemUseCase {

    private static final Logger log = LoggerFactory.getLogger(AddItemUseCaseImpl.class);

    private final WishlistRepository wishlistRepository;

    @Override
    public AddItemOutput execute(AddItemInput input) {
        log.debug("Executing AddItemUseCase - userId: {}, itemId: {}", 
                input.getUserId(), input.getItemId());

        Wishlist wishlist = wishlistRepository.findByUserId(input.getUserId())
                .orElse(Wishlist.builder()
                        .userId(input.getUserId())
                        .items(new ArrayList<>())
                        .build());

        if (wishlist.getId() == null) {
            log.debug("Wishlist not found, creating new wishlist for userId: {}", input.getUserId());
        } else {
            log.debug("Found existing wishlist - wishlistId: {}, items count: {}", 
                    wishlist.getId(), wishlist.getItems() != null ? wishlist.getItems().size() : 0);
        }

        if (wishlist.getItems() == null) {
            wishlist.setItems(new ArrayList<>());
        }

        boolean exists = wishlist.getItems().stream()
                .anyMatch(i -> i.getItemId().equals(input.getItemId()));

        if (exists) {
            log.debug("Item already exists in wishlist - userId: {}, itemId: {}", 
                    input.getUserId(), input.getItemId());
            WishlistItem existingItem = wishlist.getItems().stream()
                    .filter(i -> i.getItemId().equals(input.getItemId()))
                    .findFirst()
                    .orElseThrow();
            
            return AddItemOutput.builder()
                    .wishlistId(wishlist.getId())
                    .itemId(existingItem.getItemId())
                    .name(existingItem.getName())
                    .build();
        }

        log.debug("Adding new item to wishlist - userId: {}, itemId: {}, name: {}", 
                input.getUserId(), input.getItemId(), input.getName());
        WishlistItem item = WishlistItem.builder()
                .itemId(input.getItemId())
                .name(input.getName())
                .build();

        wishlist.getItems().add(item);

        Wishlist savedWishlist = wishlistRepository.save(wishlist);
        log.debug("Item saved successfully - wishlistId: {}, itemId: {}", 
                savedWishlist.getId(), item.getItemId());

        return AddItemOutput.builder()
                .wishlistId(savedWishlist.getId())
                .itemId(item.getItemId())
                .name(item.getName())
                .build();

    }
}
