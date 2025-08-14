import { Component, Input, OnInit } from '@angular/core';
import { ProductResponse } from '../../model/product.model';
import { CartService } from '../../service/cart.service';
import { AuthService } from '../../core/auth.service';
import { ActivatedRoute, Router, RouterOutlet } from '@angular/router';
import { CartItem } from '../../model/cart.model';
import { BuyNowRequest } from '../../model/order.model';
import { CommonModule, NgIf } from '@angular/common';
import { ProductService } from '../../service/product.service';
import { ToastrService } from 'ngx-toastr';
import { NavBarComponent } from "../nav-bar/nav-bar.component";

@Component({
  selector: 'app-product-component',
  imports: [NgIf, CommonModule, NavBarComponent, RouterOutlet],
  templateUrl: './product-component.component.html',
  styleUrl: './product-component.component.css'
})
export class ProductComponentComponent implements OnInit{

  @Input() product! : ProductResponse;

  // product : ProductResponse [] = [];

  quantity = 1;

  constructor(
    private route : ActivatedRoute, 
    private cartService : CartService, 
    private authService : AuthService, 
    private router : Router, 
    private productService : ProductService,
    private toastr : ToastrService
  ){}


  ngOnInit(): void {
     const id = Number(this.route.snapshot.paramMap.get('id'));
    if (id) this.loadProduct(id);
  }

    loadProduct(id: number): void {
    this.productService.getProductById(id).subscribe({
      next: (data) => (this.product = data),
      error: (err) => console.error('Failed to load product', err)
    });
  }


  addToCart(): void{
    // if(!this.authService.isAuthenticated()){
    //   // this.router.navigate(['/login']);
    //   this.toastr.warning('Please login first!');
    //   return;
    // }

    const cartItem : CartItem = {
      productId : this.product.id,
      quantity : this.quantity
    };

    this.cartService.addCart(cartItem).subscribe({
      next: () => {
        this.router.navigate(['/cart-component']);
      },
      error: (err) => {
        console.error('Failed to add to cart', err);
      }
    });
  }

  buyNow(): void{
    //  if(!this.authService.isAuthenticated()){
    //   this.router.navigate(['/login']);
    //   return;
    // }

    const buyNowRequest : BuyNowRequest = {
      cartItem: {
        productId: this.product.id,
        quantity: this.quantity
      },

      checkoutRequest: {
        shippingAddressId : 1,
        paymentMethod : 'Credit Cart',
        notes : ''
      }
    };
    this.cartService.buyNow(buyNowRequest).subscribe({
      next: (order) => {
        this.router.navigate(['/order-confirmation', order.orderId]);
      },
      error: (err) => {
        console.error('Buy now failed', err);
      }
    });
  }

  decreaseQuantity(): void {
  if (this.quantity > 1) {
    this.quantity--;
  }
}

increaseQuantity(): void {
  this.quantity++;
}

 get hasDiscount(): boolean {
    return this.product.currentPrice < this.product.price;
  }



  getTotalAmount(): number {
  return this.quantity * (this.product.currentPrice ?? this.product.price);
}
}
