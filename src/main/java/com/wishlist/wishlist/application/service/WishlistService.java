package com.wishlist.wishlist.application.service;

import com.wishlist.wishlist.domain.model.Wishlist;
import com.wishlist.wishlist.domain.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private static final Logger log = LoggerFactory.getLogger(WishlistService.class);

    private final WishlistRepository wishlistRepository;

    public Optional<Wishlist> findWishlist(String userId) {
        log.debug("Finding wishlist for userId: {}", userId);
        Optional<Wishlist> wishlistOpt = wishlistRepository.findByUserId(userId);
        wishlistOpt.ifPresent(wishlist -> {
            log.debug("Found wishlist - wishlistId: {}, items count: {}", 
                    wishlist.getId(), wishlist.getItems().size());
        });
        return wishlistOpt;
    }
}

