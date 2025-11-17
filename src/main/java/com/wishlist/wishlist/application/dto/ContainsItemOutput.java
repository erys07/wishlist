package com.wishlist.wishlist.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ContainsItemOutput {
    private boolean exists;
}
