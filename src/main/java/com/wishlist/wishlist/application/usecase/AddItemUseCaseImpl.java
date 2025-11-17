package com.wishlist.wishlist.application.usecase;

import com.wishlist.wishlist.application.dto.AddItemInput;
import com.wishlist.wishlist.application.dto.AddItemOutput;
import com.wishlist.wishlist.domain.model.Wishlist;
import com.wishlist.wishlist.domain.model.WishlistItem;
import com.wishlist.wishlist.domain.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AddItemUseCaseImpl implements AddItemUseCase {

    private final WishlistRepository wishlistRepository;

    @Override
    public AddItemOutput execute(AddItemInput input) {

        Wishlist wishlist = wishlistRepository.findByUserId(input.getUserId())
                .orElse(Wishlist.builder()
                        .userId(input.getUserId())
                        .items(new ArrayList<>())
                        .build());

        if (wishlist.getItems() == null) {
            wishlist.setItems(new ArrayList<>());
        }

        boolean exists = wishlist.getItems().stream()
                .anyMatch(i -> i.getItemId().equals(input.getItemId()));

        if (exists) {
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

        WishlistItem item = WishlistItem.builder()
                .itemId(input.getItemId())
                .name(input.getName())
                .build();

        wishlist.getItems().add(item);

        Wishlist savedWishlist = wishlistRepository.save(wishlist);

        return AddItemOutput.builder()
                .wishlistId(savedWishlist.getId())
                .itemId(item.getItemId())
                .name(item.getName())
                .build();

    }
}
