package com.wishlist.wishlist.application.usecase;

import com.wishlist.wishlist.application.dto.RemoveItemInput;
import com.wishlist.wishlist.domain.model.Wishlist;
import com.wishlist.wishlist.domain.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RemoveItemUseCaseImpl implements RemoveItemUseCase {

    private final WishlistRepository wishlistRepository;

    @Override
    public void execute(RemoveItemInput input) {
        Wishlist wishlist = wishlistRepository.findByUserId(input.getUserId())
                .orElseThrow(() -> new RuntimeException("Wishlist not found"));

        if (wishlist.getItems() != null) {
            wishlist.getItems().removeIf(
                    item -> item.getItemId().equals(input.getItemId())
            );
        }

        wishlistRepository.save(wishlist);

    }
}
