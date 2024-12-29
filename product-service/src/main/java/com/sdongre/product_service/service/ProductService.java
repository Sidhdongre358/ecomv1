package com.sdongre.product_service.service;


import com.sdongre.product_service.model.dto.ProductDto;
import reactor.core.publisher.Flux;

import java.util.List;

public interface ProductService {
    //    List<ProductDto> findAll();
    Flux<List<ProductDto>> findAll();

    ProductDto findById(final Integer productId);

    ProductDto save(final ProductDto productDto);

    ProductDto update(final ProductDto productDto);

    ProductDto update(final Integer productId, final ProductDto productDto);

    void deleteById(final Integer productId);
}
