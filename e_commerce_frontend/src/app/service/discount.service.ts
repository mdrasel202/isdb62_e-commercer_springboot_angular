import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ProductDiscountRequest, ProductDiscountResponse } from '../model/product-discount.model ';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DiscountService {

  private discountApiUrl = 'http://localhost:8080/api/products';

  constructor(private http:HttpClient) { }

  createDiscount(productId : number, discount : ProductDiscountRequest):Observable<ProductDiscountResponse>{
    return this.http.post<ProductDiscountResponse>(`${this.discountApiUrl}/${productId}/discounts`, discount);
  }

  getAllDiscounts(productId: number): Observable<ProductDiscountResponse[]> {
    return this.http.get<ProductDiscountResponse[]>(`${this.discountApiUrl}/${productId}/discounts`);
  }

  getDiscountById(productId: number, discountId: number): Observable<ProductDiscountResponse> {
    return this.http.get<ProductDiscountResponse>(`${this.discountApiUrl}/${productId}/discounts/${discountId}`);
  }

  updateDiscount(productId: number, discountId: number, discount: ProductDiscountRequest): Observable<ProductDiscountResponse> {
    return this.http.put<ProductDiscountResponse>(`${this.discountApiUrl}/${productId}/discounts/${discountId}`, discount);
  }

  deleteDiscount(productId: number, discountId: number): Observable<void> {
    return this.http.delete<void>(`${this.discountApiUrl}/${productId}/discounts/${discountId}`);
  }

  getActiveDiscounts(): Observable<ProductDiscountResponse[]> {
    return this.http.get<ProductDiscountResponse[]>(`${this.discountApiUrl}/active`);
  }
}
