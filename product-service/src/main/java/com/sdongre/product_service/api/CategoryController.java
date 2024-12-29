package com.sdongre.product_service.api;

import com.sdongre.product_service.model.dto.CategoryDto;
import com.sdongre.product_service.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/categories")
@Tag(name = "Category API", description = "Endpoints for managing product categories")
public class CategoryController {

    @Autowired
    private final CategoryService categoryService;

    @Operation(summary = "Get all categories", description = "Retrieve all categories without pagination")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of categories")
    @GetMapping
    public ResponseEntity<Flux<List<CategoryDto>>> findAll() {
        log.info("CategoryDto List, controller; fetch all categories");
        return ResponseEntity.ok(categoryService.findAll());
    }

    @Operation(summary = "Get categories with pagination", description = "Retrieve paginated categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved categories"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
    })
    @GetMapping("/paging")
    public ResponseEntity<Page<CategoryDto>> getAllCategories(
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        Page<CategoryDto> categoryPage = categoryService.findAllCategory(page, size);
        return new ResponseEntity<>(categoryPage, HttpStatus.OK);
    }

    @Operation(summary = "Get categories with pagination and sorting", description = "Retrieve paginated and sorted categories")
    @GetMapping("/paging-and-sorting")
    public ResponseEntity<List<CategoryDto>> getAllEmployees(
            @RequestParam(defaultValue = "0") @Parameter(description = "Page number") Integer pageNo,
            @RequestParam(defaultValue = "10") @Parameter(description = "Page size") Integer pageSize,
            @RequestParam(defaultValue = "categoryId") @Parameter(description = "Sort by field") String sortBy) {
        List<CategoryDto> list = categoryService.getAllCategories(pageNo, pageSize, sortBy);
        return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
    }

    @Operation(summary = "Get category by ID", description = "Retrieve detailed information of a specific category by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category found"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> findById(
            @PathVariable("categoryId")
            @NotBlank(message = "Input must not be blank")
            @Valid final String categoryId) {
        log.info("CategoryDto, resource; fetch category by id");
        return ResponseEntity.ok(categoryService.findById(Integer.parseInt(categoryId)));
    }

    @Operation(summary = "Create a new category", description = "Add a new category to the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<Mono<CategoryDto>> save(
            @RequestBody @NotNull(message = "Input must not be NULL") @Valid final CategoryDto categoryDto) {
        log.info("CategoryDto, resource; save category");
        return ResponseEntity.ok(categoryService.save(categoryDto));
    }

    @Operation(summary = "Update a category", description = "Update category information by providing the category ID and data")
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> update(
            @PathVariable("categoryId") @NotBlank(message = "Input must not be blank") @Valid final String categoryId,
            @RequestBody @NotNull(message = "Input must not be NULL") @Valid final CategoryDto categoryDto) {
        log.info("CategoryDto, resource; update category with categoryId");
        return ResponseEntity.ok(categoryService.update(Integer.parseInt(categoryId), categoryDto));
    }

    @Operation(summary = "Delete category by ID", description = "Remove a category from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Boolean> deleteById(@PathVariable("categoryId") final String categoryId) {
        log.info("Boolean, resource; delete category by id");
        categoryService.deleteById(Integer.parseInt(categoryId));
        return ResponseEntity.ok(true);
    }
}
