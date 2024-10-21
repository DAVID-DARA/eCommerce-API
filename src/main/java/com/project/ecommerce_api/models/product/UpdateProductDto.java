package com.project.ecommerce_api.models.product;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
public class UpdateProductDto {
    private String name;
    private String description;
    private Double price;
    private Integer stockQuantity;
    private UUID categoryId;
    private String altText;
}
