import { Component } from '@angular/core';
import { ProductResponse } from '../../model/product.model';
import { ProductService } from '../../service/product.service';
import { ToastrService } from 'ngx-toastr';
import { ActivatedRoute, Router } from '@angular/router';
import { DiscountService } from '../../service/discount.service';
import { CommonModule, NgFor, NgIf } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';

@Component({
  selector: 'app-product-details',
  imports: [NgIf, NgFor, CommonModule, MatSnackBarModule, ReactiveFormsModule],
  templateUrl: './product-details.component.html',
  styleUrl: './product-details.component.css'
})
export class ProductDetailsComponent {

  product: ProductResponse | null = null;
  discountForm: FormGroup;
  displayedColumns: string[] = ['id', 'name', 'percentage', 'startDate', 'endDate', 'active', 'actions'];
  imageColumns: string[] = ['id', 'fileName', 'fileUrl', 'isPrimary', 'actions'];
  isLoading = false;

  constructor(
    private productService: ProductService,
    private discountService : DiscountService,
    private route: ActivatedRoute,
    private snackBar: MatSnackBar,
    private fb: FormBuilder
  ) {
    this.discountForm = this.fb.group({
      name: ['', Validators.required],
      percentage: ['', [Validators.required, Validators.min(0.01), Validators.max(100)]],
      startDate: ['', Validators.required],
      endDate: ['', Validators.required],
      active: [false]
    });
  }

  ngOnInit(): void {
    const id = +this.route.snapshot.paramMap.get('id')!;
    this.isLoading = true;
    this.productService.getProductById(id).subscribe({
      next: (product) => {
        this.product = product;
        this.isLoading = false;
      },
      error: () => {
        this.snackBar.open('Error loading product details', 'Close', { duration: 3000 });
        this.isLoading = false;
      }
    });
  }


  uploadImage(event: Event): void {
  const input = event.target as HTMLInputElement;

  if (input.files && input.files.length > 0) {
    if (!this.product || !this.product.id || this.product.id <= 0) {
      this.snackBar.open('Invalid Product ID', 'Close', { duration: 3000 });
      return;
    }

    const file = input.files[0];
    this.isLoading = true;

    console.log('Uploading for Product ID:', this.product.id); // Debug log

    this.productService.uploadProductImage(this.product.id, file, false).subscribe({
      next: () => {
        this.snackBar.open('Image uploaded successfully', 'Close', { duration: 3000 });
        this.ngOnInit();
      },
      error: (err) => {
        console.error('Upload error:', err);
        this.snackBar.open('Error uploading image', 'Close', { duration: 3000 });
        this.isLoading = false;
      }
    });
  }
}


  // uploadImage(event: Event): void {
  //   const input = event.target as HTMLInputElement;
  //   if (input.files && input.files.length > 0) {
  //     const file = input.files[0];
  //     this.isLoading = true;
  //     this.productService.uploadProductImage(this.product!.id, file, false).subscribe({
  //       next: () => {
  //         this.snackBar.open('Image uploaded successfully', 'Close', { duration: 3000 });
  //         this.ngOnInit();
  //       },
  //       error: () => {
  //         this.snackBar.open('Error uploading image', 'Close', { duration: 3000 });
  //         this.isLoading = false;
  //       }
  //     });
  //   }
  // }

  setPrimaryImage(imageId: number): void {
    this.isLoading = true;
    this.productService.setPrimaryImage(this.product!.id, imageId).subscribe({
      next: () => {
        this.snackBar.open('Primary image set successfully', 'Close', { duration: 3000 });
        this.ngOnInit();
      },
      error: () => {
        this.snackBar.open('Error setting primary image', 'Close', { duration: 3000 });
        this.isLoading = false;
      }
    });
  }

  deleteImage(imageId: number): void {
    if (confirm('Are you sure you want to delete this image?')) {
      this.isLoading = true;
      this.productService.deleteProductImage(this.product!.id, imageId).subscribe({
        next: () => {
          this.snackBar.open('Image deleted successfully', 'Close', { duration: 3000 });
          this.ngOnInit();
        },
        error: () => {
          this.snackBar.open('Error deleting image', 'Close', { duration: 3000 });
          this.isLoading = false;
        }
      });
    }
  }

  createDiscount(): void {
    if (this.discountForm.valid) {
      this.isLoading = true;
      const discount = {
        ...this.discountForm.value,
        startDate: new Date(this.discountForm.value.startDate).toISOString(),
        endDate: new Date(this.discountForm.value.endDate).toISOString()
      };
      this.discountService.createDiscount(this.product!.id, discount).subscribe({
        next: () => {
          this.snackBar.open('Discount created successfully', 'Close', { duration: 3000 });
          this.ngOnInit();
          this.discountForm.reset();
        },
        error: () => {
          this.snackBar.open('Error creating discount', 'Close', { duration: 3000 });
          this.isLoading = false;
        }
      });
    }
  }

  deleteDiscount(discountId: number): void {
    if (confirm('Are you sure you want to delete this discount?')) {
      this.isLoading = true;
      this.discountService.deleteDiscount(this.product!.id, discountId).subscribe({
        next: () => {
          this.snackBar.open('Discount deleted successfully', 'Close', { duration: 3000 });
          this.ngOnInit();
        },
        error: () => {
          this.snackBar.open('Error deleting discount', 'Close', { duration: 3000 });
          this.isLoading = false;
        }
      });
    }
  }
}
