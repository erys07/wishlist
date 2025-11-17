package com.wishlist.wishlist.application.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RemoveItemInput {
    private String userId;
    private String itemId;
}
