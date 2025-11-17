package com.wishlist.wishlist.application.dto;

import lombok.Data;

@Data
public class AddItemInput {
    private String userId;
    private String itemId;
    private String name;
}
