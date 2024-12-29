package com.sdongre.product_service.api;

import com.sdongre.product_service.model.dto.ProductDto;
import com.sdongre.product_service.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products")
@Tag(name = "Product API", description = "Endpoints for managing products")
public class ProductController {

    @Autowired
    private final ProductService productService;

    @Operation(summary = "Get all products", description = "Retrieve all products without pagination")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all products")
    @GetMapping
    public Flux<List<ProductDto>> findAll() {
        log.info("ProductDto List, controller; fetch all products");
        return productService.findAll();
    }

    @Operation(summary = "Get product by ID", description = "Retrieve detailed information of a specific product by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> findById(
            @PathVariable("productId")
            @NotBlank(message = "Input must not be blank!")
            @Valid final String productId) {
        log.info("ProductDto, resource; fetch product by id");
        return ResponseEntity.ok(productService.findById(Integer.parseInt(productId)));
    }

    @Operation(summary = "Create a new product", description = "Add a new product to the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<ProductDto> save(
            @RequestBody @NotNull(message = "Input must not be NULL!") @Valid final ProductDto productDto) {
        log.info("ProductDto, resource; save product");
        return ResponseEntity.ok(productService.save(productDto));
    }

    @Operation(summary = "Update product", description = "Update product information by providing the product ID and data")
    @ApiResponse(responseCode = "200", description = "Product updated successfully")
    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> update(
            @PathVariable("productId") @NotBlank(message = "Input must not be blank!") @Valid final String productId,
            @RequestBody @NotNull(message = "Input must not be NULL!") @Valid final ProductDto productDto) {
        log.info("ProductDto, resource; update product with productId");
        return ResponseEntity.ok(productService.update(Integer.parseInt(productId), productDto));
    }

    @Operation(summary = "Delete product by ID", description = "Remove a product from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @DeleteMapping("/{productId}")
    public ResponseEntity<Boolean> deleteById(@PathVariable("productId") final String productId) {
        log.info("Boolean, resource; delete product by id");
        productService.deleteById(Integer.parseInt(productId));
        return ResponseEntity.ok(true);
    }
}