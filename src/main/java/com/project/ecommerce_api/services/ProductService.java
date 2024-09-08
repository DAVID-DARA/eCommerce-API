package com.project.ecommerce_api.services;

import com.project.ecommerce_api.entities.Category;
import com.project.ecommerce_api.entities.Product;
import com.project.ecommerce_api.entities.ProductImage;
import com.project.ecommerce_api.exceptions.CustomException;
import com.project.ecommerce_api.helpers.CloudinaryService;
import com.project.ecommerce_api.models.authDto.response.CustomResponse;
import com.project.ecommerce_api.models.product.CreateProductDto;
import com.project.ecommerce_api.models.product.ProductImageInfo;
import com.project.ecommerce_api.models.product.ProductInfo;
import com.project.ecommerce_api.repositories.CategoryRepository;
import com.project.ecommerce_api.repositories.ProductImageRepository;
import com.project.ecommerce_api.repositories.ProductRepository;
import com.project.ecommerce_api.utilities.ProductStatus;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;

    private final CloudinaryService cloudinaryService;
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    public CustomResponse<ProductInfo> addProduct (CreateProductDto createProductDto) {
        CustomResponse<ProductInfo> response = new CustomResponse<>();
        ProductInfo productInfo;

        // Check if category exists
        Optional<Category> categoryOptional = categoryRepository.findById(createProductDto.getCategoryId());
        if (categoryOptional.isEmpty()) {
            return createErrorResponse(response, HttpStatus.PRECONDITION_REQUIRED, "Category not found");
        }

        //Check if product exists
        Optional<Product> productOptional = productRepository.findByName(createProductDto.getName());
        if (productOptional.isPresent()) {
            return createErrorResponse(response, HttpStatus.CONFLICT, "product exists");
        }

        //Check if image file available
        if (createProductDto.getFile().isEmpty()) {
            return createErrorResponse(response, HttpStatus.BAD_REQUEST, "No image file");
        }


        Product product = new Product();
        ProductImage productImage = new ProductImage();

        product.setName(createProductDto.getName());
        product.setDescription(createProductDto.getDescription());
        product.setPrice(createProductDto.getPrice());
        product.setStockQuantity(createProductDto.getStockQuantity());
        product.setCategory(categoryOptional.get());

        if (product.getStockQuantity() == 0) {
            product.setStatus(ProductStatus.OUT_OF_STOCK);
        } else if (product.getStockQuantity() >= 1) {
            product.setStatus(ProductStatus.AVAILABLE);
        }

        try {
            String productImageUrl = cloudinaryService.uploadFile(createProductDto.getFile());

            productImage.setImageUrl(productImageUrl);
            productImage.setProduct(product);
            productImage.setAltText(createProductDto.getAltText());

            Product savedProduct = productRepository.save(product);
            productImageRepository.save(productImage);

            List<String> productImageUrls = productImageRepository.findAllByProduct(savedProduct)
                    .stream()
                    .map(ProductImage::getImageUrl)
                    .collect(Collectors.toList());


            productInfo = ProductInfo.builder()
                    .id(savedProduct.getId())
                    .name(savedProduct.getName())
                    .description(savedProduct.getDescription())
                    .price(savedProduct.getPrice())
                    .stockQuantity(savedProduct.getStockQuantity())
                    .category(savedProduct.getCategory())
                    .productImages(productImageUrls)
                    .status(savedProduct.getStatus())
                    .build();

            response.setSuccess(true);
            response.setMessage("Product Info");
            response.setStatusCode(HttpStatus.CREATED);
            response.setData(productInfo);

            logger.info("Product successfully created: {}", savedProduct.getId());
        } catch (Exception e) {
            logger.error("Error creating product: {}", e.getMessage());
            throw new CustomException("Error creating product");
        }
        return response;
    }

    private <T> CustomResponse<T> createErrorResponse(
            CustomResponse<T> response,
            HttpStatus status,
            String message
    ) {
        response.setSuccess(false);
        response.setStatusCode(status);
        response.setMessage(message);
        response.setData(null);

        return response;
    }
}
