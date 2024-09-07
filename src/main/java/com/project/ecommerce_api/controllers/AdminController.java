package com.project.ecommerce_api.controllers;

import com.project.ecommerce_api.models.CategoryInfo;
import com.project.ecommerce_api.models.CreateCategoryDto;
import com.project.ecommerce_api.models.UpdateCategoryDto;
import com.project.ecommerce_api.models.authDto.response.CustomResponse;
import com.project.ecommerce_api.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final CategoryService categoryService;

    @PostMapping("/categories")
    public ResponseEntity<?> createCategory (@RequestBody CreateCategoryDto categoryDto) {
        CustomResponse<CategoryInfo> response = null;
        try {
            response = categoryService.createCategory(categoryDto);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<?> updateCategory (@PathVariable UUID id,
                                             @RequestBody UpdateCategoryDto updateCategoryDto
    ) {
        CustomResponse<CategoryInfo> response = null;
        try {
            response = categoryService.updateCategory(id, updateCategoryDto);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<?> deleteCategory (@PathVariable UUID id) {
        CustomResponse<?> response = null;
        try {
            response = categoryService.deleteCategoryById(id);
            return ResponseEntity.status(response.getStatusCode()).body(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
