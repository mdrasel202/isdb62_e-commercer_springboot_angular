// import { Component, OnInit } from '@angular/core';
// import { ProductService } from '../../service/product.service';
// import { ToastrService } from 'ngx-toastr';

// @Component({
//   selector: 'app-image-upload-dialog',
//   imports: [],
//   templateUrl: './image-upload-dialog.component.html',
//   styleUrl: './image-upload-dialog.component.css'
// })
// export class ImageUploadDialogComponent implements OnInit{
//   selectedFile: File | null = null;
//   isPrimary = false;
//   isLoading = false;

//   constructor(
//     public dialogRef: MatDialogRef<ImageUploadDialogComponent>,
//     @Inject(MAT_DIALOG_DATA) public data: { productId: number },
//     private productService: ProductService,
//     private toastr: ToastrService
//   ) { }

//   ngOnInit(): void {
//   }

//   onFileSelected(event: any): void {
//     this.selectedFile = event.target.files[0] as File;
//   }

//   uploadImage(): void {
//     if (!this.selectedFile) {
//       this.toastr.warning('Please select a file first');
//       return;
//     }

//     this.isLoading = true;
//     this.productService.uploadProductImage(this.data.productId, this.selectedFile, this.isPrimary)
//       .subscribe({
//         next: () => {
//           this.toastr.success('Image uploaded successfully');
//           this.dialogRef.close('success');
//           this.isLoading = false;
//         },
//         error: (err) => {
//           this.toastr.error('Failed to upload image');
//           this.isLoading = false;
//         }
//       });
//   }

//   closeDialog(): void {
//     this.dialogRef.close();
//   }
// }
