import { Component, OnInit } from '@angular/core';
import { ProductResponse } from '../../model/product.model';
import { ProductService } from '../../service/product.service';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { CommonModule, NgFor } from '@angular/common';
import { ProductDiscountResponse } from '../../model/product-discount.model ';
import { NavBarComponent } from "../nav-bar/nav-bar.component";

@Component({
  selector: 'app-home',
  imports: [NgFor, CommonModule, RouterLink, NavBarComponent, RouterOutlet, RouterOutlet],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit{

  products : ProductResponse[] = [];

  constructor(private productService : ProductService, private router : Router){}

  ngOnInit(): void {
    this.loadAllProduct();
  }

  loadAllProduct() : void {
    this.productService.getAllProduct().subscribe({
      next : (data) => (this.products = data),
      error : (err) => console.error('Product error', err)
    })
  }

  // Discount calculation methods
  getCurrentPrice(product: ProductResponse): number {
    const activeDiscount = this.getActiveDiscount(product);
    return activeDiscount 
      ? product.price * (1 - activeDiscount.percentage / 100)
      : product.price;
  }

  hasActiveDiscount(product: ProductResponse): boolean {
    return !!this.getActiveDiscount(product);
  }

  getDiscountPercentage(product: ProductResponse): number {
    const activeDiscount = this.getActiveDiscount(product);
    return activeDiscount?.percentage || 0;
  }

  getSavingsAmount(product: ProductResponse): number {
    return product.price - this.getCurrentPrice(product);
  }

  getActiveDiscount(product: ProductResponse): ProductDiscountResponse | null {
    const now = new Date();
    return product.discounts.find(d =>
      d.active &&
      new Date(d.startDate) <= now &&
      new Date(d.endDate) >= now
    ) || null;
  }

}
