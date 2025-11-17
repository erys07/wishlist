package com.wishlist.wishlist.application.usecase;

import com.wishlist.wishlist.application.dto.AddItemInput;
import com.wishlist.wishlist.application.dto.AddItemOutput;

public interface AddItemUseCase {
    AddItemOutput execute(AddItemInput input);
}
