package com.trendsellr.infrastructure.input.rest.controller;

import com.trendsellr.application.usecase.CreateProductUseCase;
import com.trendsellr.application.usecase.FindAllProductsUseCase;
import com.trendsellr.application.usecase.FindProductByIdUseCase;
import com.trendsellr.domain.model.Product;
import com.trendsellr.infrastructure.input.rest.api.ProductsApi;
import com.trendsellr.infrastructure.input.rest.dto.ProductDTO;
import com.trendsellr.infrastructure.input.rest.mapper.ProductDTOMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
        log.info("REST request to create Product: {}", productDto);

        var productToCreate = this.productDtoMapper.toDomain(productDto);
        Product createdProduct = this.createProductUseCase.dispatch(productToCreate);

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