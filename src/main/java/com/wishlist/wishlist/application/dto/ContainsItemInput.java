package com.wishlist.wishlist.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContainsItemInput {
    @NotBlank(message = "UserId is required")
    private String userId;
    
    @NotBlank(message = "ItemId is required")
    private String itemId;
}
