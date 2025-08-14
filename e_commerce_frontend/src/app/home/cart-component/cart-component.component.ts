import { Component, OnInit } from '@angular/core';
import { CartItemDetail, CartResponse } from '../../model/cart.model';
import { CartService } from '../../service/cart.service';
import { AuthService } from '../../core/auth.service';
import { Route, Router, RouterLink } from '@angular/router';
import { CommonModule, NgFor, NgIf } from '@angular/common';

@Component({
  selector: 'app-cart-component',
  imports: [NgIf, NgFor, RouterLink],
  templateUrl: './cart-component.component.html',
  styleUrl: './cart-component.component.css'
})
export class CartComponentComponent implements OnInit{

  cart : CartResponse | null = null; 
  isLoading = false;
  imageBaseUrls: any;

  constructor(private cartService : CartService, private authService : AuthService, private router : Router){}  


  ngOnInit(): void {
    // if(!this.authService.isAuthenticated()){
    //   this.router.navigate(['/login']);
    //   return;
    // }
    this.loadCart();
  }
  
  loadCart(): void{
    this.isLoading = true;
    this.cartService.getCart().subscribe({
      next: (cart) => {
        this.cart = cart;
        this.isLoading = false
      },
      error: (err) => {
        console.error("Failed to load cart, Please try again.");
        this.isLoading= false;      
      }
    });
  }

  updateQuantity(item : CartItemDetail, newQuantity : number):void{
    if(newQuantity < 1) return;

    this.cartService.updateCartItem(item.productId, newQuantity).subscribe({
      next : (updateCart) => {
        this.cart = updateCart;
      },
      error : (err) => {
        console.error('Failed to update quantity', err);
      }
    });
  }

  removeItem(itemId : number): void{
    this.cartService.removeFromCart(itemId).subscribe({
      next : () => {
        this.loadCart();
      },
      error: (err) => {
        console.error('Failed to remove item', err);
      }
    })
  }

  checkout(): void {
    const checkoutRequest = {
      shippingAddressId : 1,
      paymentMethod : 'Credit Cart',
      notes : ''
    };

    this.cartService.checkout(checkoutRequest).subscribe({
      next : (order) => {
        this.router.navigate(['/order-confirmation', order.orderId]);
      },
      error: (err) => {
        console.error('Checkout failed', err);
      }
    })
  }
}
