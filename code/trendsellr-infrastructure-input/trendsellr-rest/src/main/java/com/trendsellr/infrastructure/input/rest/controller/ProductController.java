package com.trendsellr.infrastructure.input.rest.controller;

import com.trendsellr.application.usecase.CreateProductUseCase;
import com.trendsellr.application.usecase.FindAllProductsUseCase;
import com.trendsellr.application.usecase.FindProductByIdUseCase;
import com.trendsellr.infrastructure.input.rest.api.ProductsApi;
import com.trendsellr.infrastructure.input.rest.dto.ProductDTO;
import com.trendsellr.infrastructure.input.rest.mapper.ProductDTOMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProductController implements ProductsApi {

    private final CreateProductUseCase createProductUseCase;
    private final FindAllProductsUseCase findAllProductsUseCase;
    private final FindProductByIdUseCase findProductByIdUseCase;

    private final ProductDTOMapper productDtoMapper;

    @Override
    public ResponseEntity<ProductDTO> createProduct(ProductDTO productDto) {
        log.info("REST request to create Product: {}", productDTO);

        var productToCreate = this.productDtoMapper.toDomain(productDTO);
        Product createdProduct = this.createProductUseCase.dispatch(product);

        return new ResponseEntity<>(this.productDtoMapper.toDto(createdProduct), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ProductDTO> getProductById(String id) {
        log.info("REST request to get Product by id: {}", id);

        return findProductByIdUseCase.dispatch(id)
                .map(this.productDtoMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        log.info("REST request to get all Products");
        List<ProductDTO> products = findAllProductsUseCase.dispatch().stream()
            .map(this.productDtoMapper::toDto)
            .toList();

        return ResponseEntity.ok(products);
    }
}