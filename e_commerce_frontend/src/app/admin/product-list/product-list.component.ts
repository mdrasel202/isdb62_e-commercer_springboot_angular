import { Component, OnInit } from '@angular/core';
import { ProductResponse } from '../../model/product.model';
import { ProductService } from '../../service/product.service';
import { ToastrService } from 'ngx-toastr';
import { CommonModule, NgFor, NgIf } from '@angular/common';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-product-list',
  imports: [NgIf, NgFor, CommonModule, RouterLink],
  templateUrl: './product-list.component.html',
  styleUrl: './product-list.component.css'
})
export class ProductListComponent implements OnInit{

  products : ProductResponse[] = [];
  isLoading = true;

  constructor(private productService : ProductService, private router : Router, private toastr : ToastrService) {}


  ngOnInit(): void {
    this.loadProduct();
  }

  loadProduct(): void {
    this.isLoading = true;
    this.productService.getAllProduct().subscribe({
      next : (products) => {
        this.products = products;
        this.isLoading = false;
      },
      // error : (err) => console.error('Error loading products: ' , err)
      error : (err) => {
        this.toastr.error('Failed to load products');
        this.isLoading = false;
      }
    });
  }

  deleteProduct(id : number):void{
    if(confirm('Are you sure you to delete this product?')){
      this.productService.deleteProduct(id).subscribe({
        next: () => {
          this.toastr.success('Product deleted successfully');
          this.loadProduct();
        },
        error : (err) =>{
          this.toastr.error('Failed to delete product');
        }
      });
    }
  }

  viewProduct(id: number): void {
    this.router.navigate(['/admin/product-detail', id]);
  }

    editProduct(id: number): void {
    this.router.navigate(['/admin/product-form', id, 'edit']);
  }

}
