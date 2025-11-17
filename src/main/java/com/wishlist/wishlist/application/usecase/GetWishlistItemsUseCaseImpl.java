package com.wishlist.wishlist.application.usecase;

import com.wishlist.wishlist.application.dto.GetWishlistItemsInput;
import com.wishlist.wishlist.application.dto.GetWishlistItemsOutput;
import com.wishlist.wishlist.domain.model.Wishlist;
import com.wishlist.wishlist.domain.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetWishlistItemsUseCaseImpl implements GetWishlistItemsUseCase {

    private final WishlistRepository wishlistRepository;

    @Override
    public GetWishlistItemsOutput execute(GetWishlistItemsInput input) {

        Wishlist wishlist = wishlistRepository.findByUserId(input.getUserId())
                .orElseThrow(() -> new RuntimeException("Wishlist not found"));

        List<GetWishlistItemsOutput.Item> items = wishlist.getItems() != null
                ? wishlist.getItems().stream()
                        .map(item -> GetWishlistItemsOutput.Item.builder()
                                .itemId(item.getItemId())
                                .name(item.getName())
                                .build())
                        .collect(Collectors.toList())
                : new ArrayList<>();

        return new GetWishlistItemsOutput(items);
    }
}
