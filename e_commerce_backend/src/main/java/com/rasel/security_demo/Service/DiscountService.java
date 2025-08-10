package com.rasel.security_demo.Service;

import com.rasel.security_demo.Repository.ProductDiscountRepository;
import com.rasel.security_demo.Repository.ProductRepository;
import com.rasel.security_demo.dto.DiscountRequestDTO;
import com.rasel.security_demo.dto.DiscountResponseDTO;
import com.rasel.security_demo.exception.DiscountNotFoundException;
import com.rasel.security_demo.exception.ProductNotFoundException;
import com.rasel.security_demo.model.Product;
import com.rasel.security_demo.model.ProductDiscount;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiscountService {
    private final ProductDiscountRepository productDiscountRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public DiscountService(ProductDiscountRepository productDiscountRepository, ProductRepository productRepository, ModelMapper modelMapper) {
        this.productDiscountRepository = productDiscountRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }


    @Transactional
    public DiscountResponseDTO createDiscount(Long productId ,@Valid DiscountRequestDTO discountRequestDTO) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException("Product not found with id : " + productId));

        ProductDiscount discount = modelMapper.map(discountRequestDTO, ProductDiscount.class);
        discount.setProduct(product);

        ProductDiscount saveDiscount = productDiscountRepository.save(discount);
        return modelMapper.map(saveDiscount, DiscountResponseDTO.class);
        }


    @Transactional(readOnly = true)
    public List<DiscountResponseDTO> getAllDiscount(Long productId) {
        return productDiscountRepository.findByProductId(productId).stream()
                .map(discount -> modelMapper.map(discount, DiscountResponseDTO.class))
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public DiscountResponseDTO getDiscountById(Long discountId) {
        ProductDiscount discount = productDiscountRepository.findById(discountId).orElseThrow(() -> new DiscountNotFoundException("Discount not found with id: " + discountId));

        return modelMapper.map(discount, DiscountResponseDTO.class);
    }


    @Transactional
    public DiscountResponseDTO updateDiscount(Long discountId, @Valid DiscountRequestDTO discountRequestDTO) {
        ProductDiscount discount = productDiscountRepository.findById(discountId).orElseThrow(() -> new DiscountNotFoundException("Discount not found with id: " + discountId));

        modelMapper.map(discountRequestDTO, discount);
        ProductDiscount updateDiscount = productDiscountRepository.save(discount);
        return modelMapper.map(updateDiscount, DiscountResponseDTO.class);
    }


    @Transactional
    public void deleteById(Long discountId) {
        if(!productDiscountRepository.existsById(discountId)){
            throw new DiscountNotFoundException("Discount not found with id: " + discountId);
        }
        productDiscountRepository.deleteById(discountId);
    }


    @Transactional(readOnly = true)
    public List<DiscountResponseDTO> getActiveDiscount() {
        LocalDateTime now = LocalDateTime.now();
        return productDiscountRepository.findByActiveTrueAndStartDateBeforeAndEndDateAfter(now, now)
                .stream()
                .map(discount -> modelMapper.map(discount, DiscountResponseDTO.class))
                .collect(Collectors.toList());
    }


}
