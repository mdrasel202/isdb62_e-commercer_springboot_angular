package com.rasel.security_demo.Service;

import com.rasel.security_demo.Repository.CardRepository;
import com.rasel.security_demo.Repository.OrderRepository;
import com.rasel.security_demo.Repository.ProductRepository;
import com.rasel.security_demo.Repository.UserRepository;
import com.rasel.security_demo.constants.OrderStatus;
import com.rasel.security_demo.dto.Cart_and_Order.*;
import com.rasel.security_demo.exception.ResourceNotFoundException;
import com.rasel.security_demo.model.*;
import com.rasel.security_demo.model.embeddable.PaymentInfo;
import com.rasel.security_demo.model.embeddable.ShippingAddress;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Value("${app.base.url}")
    private String baseUrl;

    @Value("${file.upload-dir}")
    private String productDirectory;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    public CartResponseDTO getCart(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Cart cart = cardRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        return mapToCartResponseDTO(cart);
    }

    public CartResponseDTO addToCart(CartItemRequestDTO cartItemRequestDTO, String email) {
       User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Product product = productRepository.findById(cartItemRequestDTO.getProductId()).orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Cart cart = cardRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            return cardRepository.save(newCart);
        });

        //Check if product already exists in cart
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();

        if(existingItem.isPresent()){
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + cartItemRequestDTO.getQuantity());
        }else {
            CartItem newitem = new CartItem();
            newitem.setProduct(product);
            newitem.setQuantity(cartItemRequestDTO.getQuantity());
            newitem.setPrice(product.getPrice());
            newitem.setDiscountedPrice(product.getCurrentPrice());
            cart.addItem(newitem);
        }
        cart.calculateTotals();
        cardRepository.save(cart);
        return mapToCartResponseDTO(cart);
    }

    public CartResponseDTO updateCartItem(Long itemId, Integer quantity, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Cart cart = cardRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        CartItem item = cart.getItems().stream()
                .filter(cartItem -> cartItem.getId().equals(itemId))
                .findFirst()
                .orElseThrow(()-> new ResourceNotFoundException("Cart item not found"));

        if(quantity <= 0){
            cart.removeItem(item);
        }else {
            item.setQuantity(quantity);
        }
        cart.calculateTotals();
        cardRepository.save(cart);

        return mapToCartResponseDTO(cart);
    }

    public void removeCart(Long itemId, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Cart cart = cardRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        CartItem item = cart.getItems().stream()
                .filter(cartItem -> cartItem.getId().equals(itemId))
                .findFirst()
                .orElseThrow(()-> new ResourceNotFoundException("Cart item not found"));

        cart.removeItem(item);

        cardRepository.save(cart);
    }

    public OrderResponseDTO checkOut(CheckoutRequestDTO checkoutRequestDTO, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Cart cart = cardRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

        if(cart.getItems().isEmpty()){
            throw new IllegalArgumentException("Cannot checkout with empty cart");
        }

        //Create order
        Order order = new Order();
        order.setUser(user);
        order.setOrderNumber(generateOrderNumber());
        order.setOrderstatus(OrderStatus.PENDING);

        ShippingAddress shippingAddress = new ShippingAddress();
        shippingAddress.setDoorNo(user.getDoorNo());
        shippingAddress.setStreet(user.getStreet());
        shippingAddress.setCity(user.getCity());
        shippingAddress.setDistrict(user.getDistrict());
        shippingAddress.setState(user.getState());

        order.setShippingAddress(shippingAddress);

        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setMethod(checkoutRequestDTO.getPaymentMethod());
        paymentInfo.setPaymentStatus("Pending");

        order.setPaymentInfo(paymentInfo);

        order.setNotes(checkoutRequestDTO.getNotes());

        //Add items to order
        for(CartItem cartItem : cart.getItems()){
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPrice());
            orderItem.setDiscountedPrice(cartItem.getDiscountedPrice());
            orderItem.setSubtotal(cartItem.getDiscountedPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
            order.getItem().add(orderItem);
            orderItem.setOrder(order);


            //Update product stock
            Product product = cartItem.getProduct();
            product.setQuantity(product.getQuantity() - cartItem.getQuantity());
            productRepository.save(product);
        }

        //Calculate order totals
        order.setSubtotal(cart.getSubtotal());
        order.setDiscount(cart.getDiscount());
        order.setTotal(cart.getTotal());

        //Save order
        cart.getItems().clear();
        cart.calculateTotals();
        cardRepository.save(cart);

        return mapToOrderResponseDTO(order);
    }

    private CartResponseDTO mapToCartResponseDTO(Cart cart){
        CartResponseDTO response = new CartResponseDTO();
        response.setCartId(cart.getId());
        response.setUserId(cart.getUser().getId());

        List<CartItemDetailDTO> itemDTOs = cart.getItems().stream()
                .map(this::mapToCartItemDetailesDTO)
                .collect(Collectors.toList());

        response.setItems(itemDTOs);
        response.setTotal(cart.getTotal());
        response.setDiscount(cart.getDiscount());
        response.setCreatedAt(cart.getCreatedAt());
        response.setUpdatedAt(cart.getUpdatedAt());

        return response;
    }

    public OrderResponseDTO buyNow(@NotNull CartItemRequestDTO cartItemRequestDTO, @NotNull CheckoutRequestDTO checkoutRequest, String email) {

        //add cart
        CartResponseDTO cartResponseDTO = addToCart(cartItemRequestDTO, email);

        //check out
        OrderResponseDTO orderResponseDTO = checkOut(checkoutRequest, email);

        return orderResponseDTO;

    }

    //auto generate number
    private String generateOrderNumber() {
        return "ORD-" + LocalDateTime.now().getYear() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private CartItemDetailDTO mapToCartItemDetailesDTO(CartItem item){
        CartItemDetailDTO detailsDTO = new CartItemDetailDTO();
        detailsDTO.setProductId(item.getProduct().getId());
        detailsDTO.setProductName(item.getProduct().getName());


        String imageUrl = item.getProduct().getImages() == null || item.getProduct().getImages().isEmpty()?
               baseUrl + "/uploads/product-images/flutter company.jpg" :
               baseUrl + "/" + productDirectory+ "/" + item.getProduct().getImages().get(0).getFilePath();

        detailsDTO.setProductImage(imageUrl);
        detailsDTO.setPrice(item.getPrice());
        detailsDTO.setDiscountedPrice(item.getDiscountedPrice());
        detailsDTO.setQuantity(item.getQuantity());
        detailsDTO.setItemTotal(item.getDiscountedPrice().multiply(BigDecimal.valueOf(item.getQuantity())));

        return detailsDTO;

    }

    private OrderResponseDTO mapToOrderResponseDTO(Order order){
        OrderResponseDTO response = new OrderResponseDTO();
        response.setOrderId(order.getId());
        response.setOrderNumber(order.getOrderNumber());
        response.setOrderstatus(order.getOrderstatus().toString());
        response.setTotalAmount(order.getTotal());
        response.setOrderDate(order.getOrderDate());

        List<OrderItemDTO> itemDTO = order.getItem().stream()
                .map(this::mapToOrderItemDTO)
                .collect(Collectors.toList());

        response.setItems(itemDTO);

        ShippingAddressDTO shippingAddress = new ShippingAddressDTO();
        shippingAddress.setDoorNo(order.getShippingAddress().getDoorNo());
        shippingAddress.setStreet(order.getShippingAddress().getStreet());
        shippingAddress.setCity(order.getShippingAddress().getCity());
        shippingAddress.setDistrict(order.getShippingAddress().getDistrict());
        shippingAddress.setState(order.getShippingAddress().getState());
        response.setShippingAddress(shippingAddress);

        PaymentInfoDTO paymentInfoDTO = new PaymentInfoDTO();
        paymentInfoDTO.setMethod(order.getPaymentInfo().getMethod());
        paymentInfoDTO.setPaymentStatus(order.getPaymentInfo().getPaymentStatus());
        paymentInfoDTO.setTransactionId(order.getPaymentInfo().getTransactionId());
        response.setPaymentInfo(paymentInfoDTO);

        return response;
    }

    private OrderItemDTO mapToOrderItemDTO(OrderItem item) {
        OrderItemDTO order = new OrderItemDTO();
        order.setProductId(item.getProduct().getId());
        order.setProductName(item.getProduct().getName());

        // Get first product image if available
        String imageUrl = item.getProduct().getImages() == null || item.getProduct().getImages().isEmpty()?
                baseUrl + "/uploads/product-images/flutter company.jpg" :
                baseUrl + "/" + productDirectory + "/" +item.getProduct().getImages().get(0).getFilePath();

        order.setProductImage(imageUrl);
        order.setPrice(item.getPrice());
        order.setQuantity(item.getQuantity());
        order.setSubtotal(item.getSubtotal());

        return order;
    }
}
