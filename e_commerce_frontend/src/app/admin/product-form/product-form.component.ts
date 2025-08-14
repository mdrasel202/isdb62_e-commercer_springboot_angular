import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ProductService } from '../../service/product.service';
import { ToastrService } from 'ngx-toastr';
import { ActivatedRoute, Router } from '@angular/router';
import { ProcutRequest } from '../../model/product.model';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-product-form',
  imports: [ReactiveFormsModule , FormsModule , NgIf],
  templateUrl: './product-form.component.html',
  styleUrl: './product-form.component.css'
})
export class ProductFormComponent implements OnInit{

  productForm: FormGroup;
  isLoading = false;
  productId: number | null = null;
  isEditMode = false;

  constructor(
    private fb: FormBuilder,
    private productService: ProductService,
    private toastr: ToastrService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.productForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
      description: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(500)]],
      price: [0.01, [Validators.required, Validators.min(0.01)]],
      quantity: ['', [Validators.required, Validators.min(0)]],
      category: ['', Validators.required],
      sku: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      if(params['id']) {
        this.isEditMode =true;
        this.productId =+params['id'];
        this.loadProduct(this.productId);
      }
    })
  }



  loadProduct(id : number): void {
    this.isLoading = false;
    this.productService.getProductById(id).subscribe({
      next : (product) => {
        this.productForm.patchValue({
          name : product.name,
          description: product.description,
          price: product.price,
          quantity: product.quantity,
          category: product.category,
          sku: product.sku
        });
        this.isLoading = false;
      },
      error : (err) => {
        this.toastr.error('Failed to load product');
        this.isLoading = false;
      }
    });
  }

   onSubmit(): void {
    if (this.productForm.invalid) {
      return;
    }

    this.isLoading = true;
    const productData: ProcutRequest = this.productForm.value;

    if (this.isEditMode && this.productId) {
      this.productService.updateProduct(this.productId, productData).subscribe({
        next: (updatedProduct) => {
          this.toastr.success('Product updated successfully');
          this.router.navigate(['/product-form', updatedProduct.id]);
          this.isLoading = false;
        },
        error: (err) => {
          this.toastr.error('Failed to update product');
          this.isLoading = false;
        }
      });
    } else {
      this.productService.createProduct(productData).subscribe({
        next: (newProduct) => {
          this.toastr.success('Product created successfully');
          this.router.navigate(['/product-form', newProduct.id]);
          this.isLoading = false;
        },
        error: (err) => {
          this.toastr.error('Failed to create product');
          this.isLoading = false;
        }
      });
    }
  }

  onCancel(): void {
    this.router.navigate(['/product-form']);
  }
}
