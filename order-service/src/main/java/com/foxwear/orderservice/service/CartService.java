package com.foxwear.orderservice.service;

import com.foxwear.orderservice.entity.Cart;
import com.foxwear.orderservice.repository.CartItemRepository;
import com.foxwear.orderservice.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Transactional
    public void createCart(Long userId) {
        log.info("Creating cart...");
        Cart cart = new Cart();
        cart.setUserId(userId);
        cartRepository.save(cart);
        log.info("Cart created successfully");
    }

}
