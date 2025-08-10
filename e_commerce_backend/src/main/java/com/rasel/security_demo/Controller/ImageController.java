package com.rasel.security_demo.Controller;

import com.rasel.security_demo.Service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;

@RestController
@RequestMapping("/api/products/{productId}/images")
@Tag(name = "Product Image Management", description = "APIs for managing images")
public class ImageController {
    private final ProductService productService;

    public ImageController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload an image for a product")
    public ResponseEntity<Void> uploadImage(
            @PathVariable Long productId,
            @RequestParam("file")MultipartFile file,
            @RequestParam(required = false, defaultValue = "false") boolean isPrimary){
        productService.uploadProductImage(productId, file, isPrimary);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{imageId}")
    @Operation(summary = "Delete an Image for a product")
    public ResponseEntity<Void> deleteImage(
            @PathVariable Long productId,
            @PathVariable Long imageId){
        productService.deleteProductImage(productId, imageId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{imageId}/primary")
    @Operation(summary = "Set an image as primary for a product")
    public ResponseEntity<Void> setPrimaryImage(
            @PathVariable Long productId,
            @PathVariable Long imageId){
     productService.setPrimaryImage(productId, imageId);
     return ResponseEntity.ok().build();
    }
}
