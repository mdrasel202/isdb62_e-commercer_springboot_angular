package com.rasel.security_demo.Controller;

import com.rasel.security_demo.Service.CartService;
import com.rasel.security_demo.dto.Cart_and_Order.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping
    public ResponseEntity<CartResponseDTO> getCart(@AuthenticationPrincipal UserDetails userDetails){
        CartResponseDTO cart = cartService.getCart(userDetails.getUsername());
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/add")
    public ResponseEntity<CartResponseDTO> addToCart(@RequestBody CartItemRequestDTO cartItemRequestDTO, @AuthenticationPrincipal UserDetails userDetails){
        CartResponseDTO cart = cartService.addToCart(cartItemRequestDTO, userDetails.getUsername());
        return ResponseEntity.ok(cart);
    }

    @PutMapping("/update/{itemId}")
    public ResponseEntity<CartResponseDTO> updateCartItem(@PathVariable Long itemId, @RequestParam Integer quantity, @AuthenticationPrincipal UserDetails userDetails){
        CartResponseDTO cart = cartService.updateCartItem(itemId, quantity, userDetails.getUsername());
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/delete/{itemId}")
    public ResponseEntity<Void> removeCart(@PathVariable Long itemId, @AuthenticationPrincipal UserDetails userDetails){
        cartService.removeCart(itemId, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/checkout")
    public ResponseEntity<OrderResponseDTO> checkOut(@RequestBody CheckoutRequestDTO checkoutRequestDTO, @AuthenticationPrincipal UserDetails userDetails){
        OrderResponseDTO order = cartService.checkOut(checkoutRequestDTO, userDetails.getUsername());
        return ResponseEntity.ok(order);
    }

    @PostMapping("/buy-now")
    public ResponseEntity<OrderResponseDTO> buyNow(@RequestBody BuyNowRequestDTO buyNowRequestDTO, @AuthenticationPrincipal UserDetails userDetails){
        OrderResponseDTO order = cartService.buyNow(buyNowRequestDTO.getCartItemRequestDTO(), buyNowRequestDTO.getCheckoutRequest(), userDetails.getUsername());
        return ResponseEntity.ok(order);
    }
}
