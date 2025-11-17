package com.wishlist.wishlist.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddItemInput {
    @NotBlank(message = "UserId is required")
    private String userId;
    
    @NotBlank(message = "ItemId is required")
    private String itemId;
    
    @NotBlank(message = "Name is required")
    private String name;
}
