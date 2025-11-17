package com.wishlist.wishlist.application.usecase;

import com.wishlist.wishlist.application.dto.AddItemInput;
import com.wishlist.wishlist.application.dto.AddItemOutput;
import com.wishlist.wishlist.application.service.WishlistService;
import com.wishlist.wishlist.domain.exception.WishlistLimitExceededException;
import com.wishlist.wishlist.domain.model.Wishlist;
import com.wishlist.wishlist.domain.model.WishlistItem;
import com.wishlist.wishlist.domain.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AddItemUseCaseImpl implements AddItemUseCase {

    private static final Logger log = LoggerFactory.getLogger(AddItemUseCaseImpl.class);
    private static final int MAX_ITEMS = 20;

    private final WishlistRepository wishlistRepository;
    private final WishlistService wishlistService;

    @Override
    @Transactional
    public AddItemOutput execute(AddItemInput input) {
        log.debug("Executing AddItemUseCase - userId: {}, itemId: {}", 
                input.getUserId(), input.getItemId());

        Wishlist wishlist = wishlistService.findWishlist(input.getUserId())
                .orElse(Wishlist.builder()
                        .userId(input.getUserId())
                        .build());

        if (wishlist.getId() == null) {
            log.debug("Wishlist not found, creating new wishlist for userId: {}", input.getUserId());
        }

        WishlistItem existingItem = wishlist.getItems().stream()
                .filter(i -> i.getItemId().equals(input.getItemId()))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            log.debug("Item already exists in wishlist - userId: {}, itemId: {}", 
                    input.getUserId(), input.getItemId());
            return AddItemOutput.builder()
                    .wishlistId(wishlist.getId())
                    .itemId(existingItem.getItemId())
                    .name(existingItem.getName())
                    .build();
        }

        if (wishlist.getItems().size() >= MAX_ITEMS) {
            log.warn("Wishlist limit exceeded for userId: {}, current size: {}", 
                    input.getUserId(), wishlist.getItems().size());
            throw new WishlistLimitExceededException(input.getUserId(), wishlist.getItems().size());
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
