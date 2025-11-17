package com.wishlist.wishlist.domain.exception;

public class WishlistLimitExceededException extends RuntimeException {
    
    private static final int MAX_ITEMS = 20;
    
    public WishlistLimitExceededException(String userId) {
        super(String.format("Wishlist limit exceeded for user %s. Maximum of %d items allowed.", userId, MAX_ITEMS));
    }
    
    public WishlistLimitExceededException(String userId, int currentSize) {
        super(String.format("Wishlist limit exceeded for user %s. Current items: %d, maximum allowed: %d.", 
                userId, currentSize, MAX_ITEMS));
    }
}

