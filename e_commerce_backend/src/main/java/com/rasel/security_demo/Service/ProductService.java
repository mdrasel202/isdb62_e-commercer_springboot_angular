package com.rasel.security_demo.Service;

import com.rasel.security_demo.Repository.ProductDiscountRepository;
import com.rasel.security_demo.Repository.ProductImageRepository;
import com.rasel.security_demo.Repository.ProductRepository;
import com.rasel.security_demo.dto.DiscountResponseDTO;
import com.rasel.security_demo.dto.ImageResponseDTO;
import com.rasel.security_demo.dto.ProductRequestDTO;
import com.rasel.security_demo.dto.ProductResponseDTO;
import com.rasel.security_demo.exception.ProductNotFoundException;
import com.rasel.security_demo.model.Product;
import com.rasel.security_demo.model.ProductDiscount;
import com.rasel.security_demo.model.ProductImage;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Value("${app.base.url}")
    private String baseUrl;

    @Value("${file.upload-dir}")
    private String productDirectory;


    private final ProductRepository productRepository;
    private final ProductDiscountRepository productDiscountRepository;
    private final ProductImageRepository productImageRepository;
    private  final ImageStorageService imageStorageService;
    private final ModelMapper modelMapper;

    public ProductService(ProductRepository productRepository, ProductDiscountRepository productDiscountRepository, ProductImageRepository productImageRepository, ImageStorageService imageStorageService, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.productDiscountRepository = productDiscountRepository;
        this.productImageRepository = productImageRepository;
        this.imageStorageService = imageStorageService;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) {
        if(productRepository.existsBySku(productRequestDTO.getSku())){
            throw new IllegalArgumentException("Product with SKU " + productRequestDTO.getSku() + "already exists");
        }
        Product product = modelMapper.map(productRequestDTO, Product.class);
        Product saveProduct = productRepository.save(product);
        return modelMapper.map(saveProduct, ProductResponseDTO.class);
    }


    @Transactional(readOnly = true)
    public ProductResponseDTO getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found with id : " + id));

        ProductResponseDTO responseDTO = modelMapper.map(product, ProductResponseDTO.class);
        responseDTO.setImages(mapImagesToResponse(product.getImages()));
        responseDTO.setDiscounts(mapDiscountToResponse(product.getDiscounts()));
        responseDTO.setCurrentPrice(product.getCurrentPrice());
        return responseDTO;
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDTO> getAllProduct() {
        return productRepository.findAll().stream()
                .map(product -> {
                    ProductResponseDTO responseDTO = modelMapper.map(product, ProductResponseDTO.class);
                    responseDTO.setImages(mapImagesToResponse(product.getImages()));
                    responseDTO.setDiscounts(mapDiscountToResponse(product.getDiscounts()));
                    responseDTO.setCurrentPrice(product.getCurrentPrice());
                    return responseDTO;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public ProductResponseDTO updateProduct(Long id, @Valid ProductRequestDTO requestDTO) {
         Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found with id" + id));

         if(!product.getSku().equals(requestDTO.getSku())){
             if(productRepository.existsBySku(requestDTO.getSku())){
                 throw new IllegalArgumentException("Product with SKU " + requestDTO.getSku() + "already exists");
             }
         }
         modelMapper.map(requestDTO, product);
         Product updateProduct = productRepository.save(product);
         return modelMapper.map(updateProduct, ProductResponseDTO.class);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found with id" + id));

        product.getImages().forEach(image -> imageStorageService.deleteFile(image.getFilePath()));
        productRepository.delete(product);
    }



    @Transactional
    public void uploadProductImage(Long productId, MultipartFile file, boolean isPrimary) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException("Product not found with id" + productId));

        if(file.isEmpty()) throw new IllegalArgumentException("File is empty");

        String fileName = imageStorageService.storeFile(file);
        ProductImage image = new ProductImage();
        image.setFileName(file.getOriginalFilename());
        image.setFilePath(fileName);
        image.setFileType(file.getContentType());
        image.setFileSize(file.getSize());
        image.setPrimary(isPrimary);

        if(isPrimary) product.getImages().forEach(img -> img.setPrimary(false));
        product.addImage(image);
        productRepository.save(product);
    }

    @Transactional
    public void deleteProductImage(Long productId, Long imageId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException("Product not found with id" + productId));

        ProductImage image = productImageRepository.findById(imageId).orElseThrow(() -> new IllegalArgumentException("Image not found with id: " + imageId));

        if(!image.getProduct().getId().equals(productId)){
            throw  new IllegalArgumentException("Image does not belong to the specified product");
        }

        imageStorageService.deleteFile(image.getFilePath());
        product.removeImage(image);
        productImageRepository.delete(image);

        if(image.isPrimary() && !product.getImages().isEmpty()){
            product.getImages().get(0).setPrimary(true);
            productRepository.save(product);
        }
    }


    @Transactional
    public void setPrimaryImage(Long productId, Long imageId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException("Product not found with id" + productId));

        ProductImage newPrimaryImage = product.getImages().stream()
                .filter(img -> img.getId().equals(imageId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Image not found with id: " + imageId));

        product.getImages().forEach(img -> img.setPrimary(false));
        newPrimaryImage.setPrimary(true);
        productRepository.save(product);
    }

    private List<ImageResponseDTO> mapImagesToResponse(List<ProductImage> images){
        return images.stream()
                .map(image -> {
                    ImageResponseDTO responseDTO = new ImageResponseDTO();
                    responseDTO.setId(image.getId());
                    responseDTO.setFileName(image.getFileName());
                    responseDTO.setFileUrl(baseUrl + "/" + productDirectory + "/" + image.getFilePath());
                    responseDTO.setFileType(image.getFileType());
                    responseDTO.setFileSize(image.getFileSize());
                    responseDTO.setPrimary(image.isPrimary());
                    return responseDTO;
                })
                .collect(Collectors.toList());
    }


    private List<DiscountResponseDTO> mapDiscountToResponse(List<ProductDiscount> discounts){
        return discounts.stream()
                .map(discount -> modelMapper.map(discount, DiscountResponseDTO.class))
                .collect(Collectors.toList());
    }



}
