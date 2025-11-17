package com.wishlist.wishlist.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ListItemsOutput {
    private List<String> itemsIds;
}
