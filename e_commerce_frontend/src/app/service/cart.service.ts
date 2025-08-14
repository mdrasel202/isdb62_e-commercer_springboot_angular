import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AuthService } from '../core/auth.service';
import { Observable } from 'rxjs';
import { CartItem, CartResponse } from '../model/cart.model';
import { BuyNowRequest, CheckoutRequest, OrderResponse } from '../model/order.model';

@Injectable({
  providedIn: 'root'
})
export class CartService {

  private apiUrl = 'http://localhost:8080/api/cart';

  constructor(private http: HttpClient, private authService : AuthService) { }

  getCart():Observable<CartResponse>{
    return this.http.get<CartResponse>(this.apiUrl);
  }

  addCart( item : CartItem):Observable<CartResponse>{
    return this.http.post<CartResponse>(`${this.apiUrl}/add`, item);
  }

  updateCartItem(itemId : number, quantity : number):Observable<CartResponse>{
    return this.http.put<CartResponse>(`${this.apiUrl}/update/${itemId}`, null, 
      {params : {quantity : quantity.toString()}}
    );
  }

  removeFromCart(itemId : number):Observable<void>{
    return this.http.delete<void>(`${this.apiUrl}/delete/${itemId}`);
  }

  checkout(checkoutRequest : CheckoutRequest):Observable<OrderResponse>{
    return this.http.post<OrderResponse>(`${this.apiUrl}/checkout`, checkoutRequest);
  }

  buyNow(buyNowRequest : BuyNowRequest):Observable<OrderResponse>{
    return this.http.post<OrderResponse>(`${this.apiUrl}/buy-now`, buyNowRequest);
  }
}
