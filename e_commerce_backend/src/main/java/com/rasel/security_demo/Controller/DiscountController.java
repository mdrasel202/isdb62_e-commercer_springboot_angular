package com.rasel.security_demo.Controller;

import com.rasel.security_demo.Service.DiscountService;
import com.rasel.security_demo.dto.DiscountRequestDTO;
import com.rasel.security_demo.dto.DiscountResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products/{productId}/discounts")
@Tag(name = "Discount Management", description = "APIs for managing product discounts")
public class DiscountController {
    private final DiscountService discountService;

    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }

    @PostMapping
    @Operation(summary = "Create a new discount for a product")
    public ResponseEntity<DiscountResponseDTO> createDiscount(@PathVariable Long productId, @Valid @RequestBody DiscountRequestDTO discountRequestDTO){
        DiscountResponseDTO discountResponseDTO = discountService.createDiscount(productId, discountRequestDTO);
        return new ResponseEntity<>(discountResponseDTO, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all discount for a product")
    public ResponseEntity<List<DiscountResponseDTO>> getAllDiscount(@PathVariable Long productId){
        List<DiscountResponseDTO> discountResponseDTOS = discountService.getAllDiscount(productId);
         return ResponseEntity.ok(discountResponseDTOS);
    }

    @GetMapping("/{discountId}")
    @Operation(summary = "Get discount by ID")
    public ResponseEntity<DiscountResponseDTO> getDsicountById(@PathVariable Long productId, @PathVariable Long discountId){
        DiscountResponseDTO discountResponseDTO = discountService.getDiscountById(discountId);
        return ResponseEntity.ok(discountResponseDTO);
    }

    @PutMapping("/{discountId}")
    @Operation(summary = "Update discount by ID")
    public ResponseEntity<DiscountResponseDTO> updateDiscount(@PathVariable Long productId, @PathVariable Long discountId, @Valid @RequestBody DiscountRequestDTO discountRequestDTO){
        DiscountResponseDTO discountResponseDTO = discountService.updateDiscount(discountId, discountRequestDTO);
        return ResponseEntity.ok(discountResponseDTO);
    }

    @DeleteMapping("/{discountId}")
    @Operation(summary = "Delete discount by ID")
    public ResponseEntity<Void> deleteDiscount(@PathVariable Long productId, @PathVariable Long discountId){
        discountService.deleteById(discountId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/active")
    @Operation(summary = "Get all active discounts")
    public ResponseEntity<List<DiscountResponseDTO>> getActiveDiscount(){
        List<DiscountResponseDTO> discountResponseDTO = discountService.getActiveDiscount();
        return ResponseEntity.ok(discountResponseDTO);
    }
}
