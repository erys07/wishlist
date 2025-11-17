package com.wishlist.wishlist.domain.exception;

public class WishlistNotFoundException extends RuntimeException {
    
    public WishlistNotFoundException(String userId) {
        super("Wishlist not found for user: " + userId);
    }
}

