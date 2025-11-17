package com.wishlist.wishlist.application.usecase;

import com.wishlist.wishlist.application.dto.ContainsItemInput;
import com.wishlist.wishlist.application.dto.ContainsItemOutput;
import com.wishlist.wishlist.domain.model.Wishlist;
import com.wishlist.wishlist.domain.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemExistsInWishlistUseCaseImpl implements ItemExistsInWishlistUseCase {

    private final WishlistRepository wishlistRepository;

    @Override
    public ContainsItemOutput execute(ContainsItemInput input) {

        Optional<Wishlist> wishlistOpt = wishlistRepository.findByUserId(input.getUserId());

        if (wishlistOpt.isEmpty()) {
            return new ContainsItemOutput(false);
        }

        Wishlist wishlist = wishlistOpt.get();
        boolean exists = wishlist.getItems() != null &&
                wishlist.getItems().stream()
                        .anyMatch(item -> item.getItemId().equals(input.getItemId()));

        return new ContainsItemOutput(exists);
    }
}
