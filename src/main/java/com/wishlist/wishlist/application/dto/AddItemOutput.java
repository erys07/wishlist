package com.wishlist.wishlist.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddItemOutput {
    private String wishlistId;
    private String itemId;
    private String name;
}
