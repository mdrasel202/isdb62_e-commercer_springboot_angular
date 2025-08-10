package com.rasel.security_demo.Controller;

import com.rasel.security_demo.Service.ProductService;
import com.rasel.security_demo.dto.ProductRequestDTO;
import com.rasel.security_demo.dto.ProductResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product Management", description = "APIs for managing products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @Operation(summary = "Create a new product")
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductRequestDTO productRequestDTO){
        ProductResponseDTO responseDTO = productService.createProduct(productRequestDTO);
        return new ResponseEntity<>(responseDTO,HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by Id")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id){
        ProductResponseDTO responseDTO = productService.getProductById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/getall")
    @Operation(summary = "Get all products")
    public ResponseEntity<List<ProductResponseDTO>> getAllProduct(){
        List<ProductResponseDTO> responseDTO = productService.getAllProduct();
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update product by ID")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequestDTO requestDTO){
        ProductResponseDTO responseDTO = productService.updateProduct(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete product by ID")
    public ResponseEntity<Void> deleteByProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
