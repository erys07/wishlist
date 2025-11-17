package com.wishlist.wishlist.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GetWishlistItemsInput {
    @NotBlank(message = "UserId is required")
    private String userId;
}