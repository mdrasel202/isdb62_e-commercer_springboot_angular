import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ProcutRequest, ProductResponse } from '../model/product.model';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private productUrl = 'http://localhost:8080/api/products';

  constructor(private http:HttpClient) { }

  createProduct(product: ProcutRequest):Observable<ProductResponse>{
    return this.http.post<ProductResponse>(`${this.productUrl}`, product);
  }

  getProductById(id : number):Observable<ProductResponse>{
    return this.http.get<ProductResponse>(`${this.productUrl}/${id}`);
  }

  getAllProduct():Observable<ProductResponse[]>{
    return this.http.get<ProductResponse[]>(`${this.productUrl}/getall`);
  }

  updateProduct(id : number, product : ProcutRequest):Observable<ProductResponse>{
    return this.http.put<ProductResponse>(`${this.productUrl}/${id}`, product);
  }

  deleteProduct(id : number):Observable<void>{
    return this.http.delete<void>(`${this.productUrl}/${id}`);
  }

  //image operations
  uploadProductImage(productId : number, file : File, isPrimary : boolean):Observable<void>{
    const formDate = new FormData();
    formDate.append('file', file);
    formDate.append('isPrimary', isPrimary.toString());

    return this.http.post<void>(`${this.productUrl}/${productId}/images`, formDate);
  }

  deleteProductImage(productId : number, imageId: number):Observable<void>{
    return this.http.delete<void>(`${this.productUrl}/${productId}/images/${imageId}/primary`);
  }

  setPrimaryImage(productId : number, imageId : number):Observable<void>{
    return this.http.patch<void>(`${this.productUrl}/${productId}/images/${imageId}/primary`, {});
  }
}
