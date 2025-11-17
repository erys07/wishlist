package com.wishlist.wishlist.application.usecase;

import com.wishlist.wishlist.application.dto.ContainsItemInput;
import com.wishlist.wishlist.application.dto.ContainsItemOutput;
import com.wishlist.wishlist.domain.model.Wishlist;
import com.wishlist.wishlist.domain.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemExistsInWishlistUseCaseImpl implements ItemExistsInWishlistUseCase {

    private static final Logger log = LoggerFactory.getLogger(ItemExistsInWishlistUseCaseImpl.class);

    private final WishlistRepository wishlistRepository;

    @Override
    public ContainsItemOutput execute(ContainsItemInput input) {
        log.debug("Executing ItemExistsInWishlistUseCase - userId: {}, itemId: {}", 
                input.getUserId(), input.getItemId());

        Optional<Wishlist> wishlistOpt = wishlistRepository.findByUserId(input.getUserId());

        if (wishlistOpt.isEmpty()) {
            log.debug("Wishlist not found for userId: {}, returning false", input.getUserId());
            return new ContainsItemOutput(false);
        }

        Wishlist wishlist = wishlistOpt.get();
        boolean exists = wishlist.getItems() != null &&
                wishlist.getItems().stream()
                        .anyMatch(item -> item.getItemId().equals(input.getItemId()));

        log.debug("Item exists check completed - userId: {}, itemId: {}, exists: {}", 
                input.getUserId(), input.getItemId(), exists);
        return new ContainsItemOutput(exists);
    }
}
