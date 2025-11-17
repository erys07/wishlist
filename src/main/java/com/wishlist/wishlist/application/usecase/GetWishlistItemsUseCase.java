package com.wishlist.wishlist.application.usecase;

import com.wishlist.wishlist.application.dto.GetWishlistItemsInput;
import com.wishlist.wishlist.application.dto.GetWishlistItemsOutput;

public interface GetWishlistItemsUseCase {
    GetWishlistItemsOutput execute(GetWishlistItemsInput input);
}
