package com.wishlist.wishlist.application.usecase;

import com.wishlist.wishlist.application.dto.GetWishlistItemsInput;
import com.wishlist.wishlist.application.dto.GetWishlistItemsOutput;
import com.wishlist.wishlist.application.service.WishlistService;
import com.wishlist.wishlist.domain.exception.WishlistNotFoundException;
import com.wishlist.wishlist.domain.model.Wishlist;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetWishlistItemsUseCaseImpl implements GetWishlistItemsUseCase {

    private static final Logger log = LoggerFactory.getLogger(GetWishlistItemsUseCaseImpl.class);

    private final WishlistService wishlistService;

    @Override
    public GetWishlistItemsOutput execute(GetWishlistItemsInput input) {
        log.debug("Executing GetWishlistItemsUseCase - userId: {}", input.getUserId());

        Wishlist wishlist = wishlistService.findWishlist(input.getUserId())
                .orElseThrow(() -> {
                    log.warn("Wishlist not found for userId: {}", input.getUserId());
                    return new WishlistNotFoundException(input.getUserId());
                });

        log.debug("Found wishlist - wishlistId: {}, items count: {}", 
                wishlist.getId(), wishlist.getItems().size());

        List<GetWishlistItemsOutput.Item> items = wishlist.getItems().stream()
                .map(item -> GetWishlistItemsOutput.Item.builder()
                        .itemId(item.getItemId())
                        .name(item.getName())
                        .build())
                .collect(Collectors.toList());

        log.debug("Returning {} items for userId: {}", items.size(), input.getUserId());
        return new GetWishlistItemsOutput(items);
    }
}
