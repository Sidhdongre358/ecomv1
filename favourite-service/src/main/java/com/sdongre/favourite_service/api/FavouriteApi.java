package com.sdongre.favourite_service.api;


import com.sdongre.favourite_service.constant.ConfigConstant;
import com.sdongre.favourite_service.model.dto.FavouriteDto;
import com.sdongre.favourite_service.model.dto.response.collection.DtoCollectionResponse;
import com.sdongre.favourite_service.model.entity.id.FavouriteId;
import com.sdongre.favourite_service.service.FavouriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RestController
@RequestMapping("/api/favourites")
@RequiredArgsConstructor
@Tag(name = "Favourite API", description = "API for managing user favourites")
public class FavouriteApi {

    @Autowired
    private final FavouriteService favouriteService;

    @Operation(summary = "Get all favourites", description = "Retrieve a list of all favourite items")
    @GetMapping
    public ResponseEntity<DtoCollectionResponse<FavouriteDto>> findAll() {
        return ResponseEntity.ok(new DtoCollectionResponse<>(this.favouriteService.findAll()));
    }

    @Operation(summary = "Find favourite by ID", description = "Retrieve a favourite item by userId, productId, and likeDate")
    @GetMapping("/{userId}/{productId}/{likeDate}")
    public ResponseEntity<FavouriteDto> findById(@PathVariable("userId") final String userId,
                                                 @PathVariable("productId") final String productId,
                                                 @PathVariable("likeDate") final String likeDate) {
        return ResponseEntity.ok(this.favouriteService.findById(
                new FavouriteId(Integer.parseInt(userId), Integer.parseInt(productId),
                        LocalDateTime.parse(likeDate, DateTimeFormatter.ofPattern(ConfigConstant.LOCAL_DATE_TIME_FORMAT)))));
    }

    @Operation(summary = "Find favourite by composite ID", description = "Retrieve a favourite item by composite ID in the request body")
    @GetMapping("/find")
    public ResponseEntity<FavouriteDto> findById(@RequestBody
                                                 @NotNull(message = "Input must not be NULL")
                                                 @Valid final FavouriteId favouriteId) {
        return ResponseEntity.ok(this.favouriteService.findById(favouriteId));
    }

    @Operation(summary = "Add new favourite", description = "Save a new favourite item")
    @PostMapping
    public ResponseEntity<FavouriteDto> save(@RequestBody
                                             @NotNull(message = "Input must not be NULL")
                                             @Valid final FavouriteDto favouriteDto) {
        return ResponseEntity.ok(this.favouriteService.save(favouriteDto));
    }

    @Operation(summary = "Update favourite", description = "Update an existing favourite item")
    @PutMapping
    public ResponseEntity<FavouriteDto> update(@RequestBody
                                               @NotNull(message = "Input must not be NULL")
                                               @Valid final FavouriteDto favouriteDto) {
        return ResponseEntity.ok(this.favouriteService.update(favouriteDto));
    }

    @Operation(summary = "Delete favourite by ID", description = "Delete a favourite item by userId, productId, and likeDate")
    @DeleteMapping("/{userId}/{productId}/{likeDate}")
    public ResponseEntity<Boolean> deleteById(@PathVariable("userId") final String userId,
                                              @PathVariable("productId") final String productId,
                                              @PathVariable("likeDate") final String likeDate) {
        favouriteService.deleteById(
                new FavouriteId(Integer.parseInt(userId), Integer.parseInt(productId),
                        LocalDateTime.parse(likeDate, DateTimeFormatter.ofPattern(ConfigConstant.LOCAL_DATE_TIME_FORMAT)))
        );
        return ResponseEntity.ok(true);
    }

    @Operation(summary = "Delete by composite ID", description = "Delete a favourite item by composite ID in the request body")
    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> deleteById(@RequestBody
                                              @NotNull(message = "Input must not be NULL")
                                              @Valid final FavouriteId favouriteId) {
        favouriteService.deleteById(favouriteId);
        return ResponseEntity.ok(true);
    }
}
