package com.wishlist.wishlist.application.usecase;

import com.wishlist.wishlist.application.dto.ContainsItemInput;
import com.wishlist.wishlist.application.dto.ContainsItemOutput;

public interface ItemExistsInWishlistUseCase {
    ContainsItemOutput execute(ContainsItemInput input);
}
