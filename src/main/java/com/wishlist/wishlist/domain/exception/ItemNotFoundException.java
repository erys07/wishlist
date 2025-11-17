package com.wishlist.wishlist.domain.exception;

public class ItemNotFoundException extends RuntimeException {
    
    public ItemNotFoundException(String itemId) {
        super("Item not found: " + itemId);
    }
    
    public ItemNotFoundException(String userId, String itemId) {
        super("Item " + itemId + " not found in wishlist for user: " + userId);
    }
}

