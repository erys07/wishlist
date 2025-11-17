package com.wishlist.wishlist.application.usecase;

import com.wishlist.wishlist.application.dto.RemoveItemInput;
import com.wishlist.wishlist.application.service.WishlistService;
import com.wishlist.wishlist.domain.exception.WishlistNotFoundException;
import com.wishlist.wishlist.domain.model.Wishlist;
import com.wishlist.wishlist.domain.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RemoveItemUseCaseImpl implements RemoveItemUseCase {

    private static final Logger log = LoggerFactory.getLogger(RemoveItemUseCaseImpl.class);

    private final WishlistRepository wishlistRepository;
    private final WishlistService wishlistService;

    @Override
    public void execute(RemoveItemInput input) {
        log.debug("Executing RemoveItemUseCase - userId: {}, itemId: {}", 
                input.getUserId(), input.getItemId());

        Wishlist wishlist = wishlistService.findWishlist(input.getUserId())
                .orElseThrow(() -> {
                    log.warn("Wishlist not found for userId: {}", input.getUserId());
                    return new WishlistNotFoundException(input.getUserId());
                });

        log.debug("Found wishlist - wishlistId: {}, items count before removal: {}", 
                wishlist.getId(), wishlist.getItems().size());

        boolean removed = wishlist.getItems().removeIf(
                item -> item.getItemId().equals(input.getItemId())
        );

        if (removed) {
            log.debug("Item removed from wishlist - userId: {}, itemId: {}", 
                    input.getUserId(), input.getItemId());
            wishlistRepository.save(wishlist);
        } else {
            log.warn("Item not found in wishlist - userId: {}, itemId: {}", 
                    input.getUserId(), input.getItemId());
        }
    }
}
