package com.trendsellr.infrastructure.input.rest.controller;

import com.trendsellr.application.usecase.product.CreateProductUseCase;
import com.trendsellr.application.usecase.product.FindAllProductsUseCase;
import com.trendsellr.application.usecase.product.FindProductByIdUseCase;
import com.trendsellr.application.usecase.product.UpdateProductUseCase;
import com.trendsellr.domain.model.product.Product;
import com.trendsellr.domain.model.product.ProductCollection;
import com.trendsellr.infrastructure.input.rest.api.ProductsApi;
import com.trendsellr.infrastructure.input.rest.dto.*;
import com.trendsellr.infrastructure.input.rest.mapper.product.ProductCollectionDTOMapper;
import com.trendsellr.infrastructure.input.rest.mapper.product.ProductDTOMapper;
import com.trendsellr.infrastructure.input.rest.security.AuthenticationRoles;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProductController implements ProductsApi {

    private final CreateProductUseCase createProductUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final FindAllProductsUseCase findAllProductsUseCase;
    private final FindProductByIdUseCase findProductByIdUseCase;

    private final ProductDTOMapper productDtoMapper;

    private final ProductCollectionDTOMapper productCollectionDTOMapper;

    @Override
    @PreAuthorize(AuthenticationRoles.ANY_REGISTERED_USER)
    public ResponseEntity<ProductIdDTO> createProduct(
            ProductCreateRequestDTO productCreateRequestDto) {
        log.info("REST request to create Product: {}", productCreateRequestDto);

        final var productToCreate = this.productDtoMapper.toDomain(
                productCreateRequestDto.getProduct());
        final Product createdProduct = this.createProductUseCase.dispatch(productToCreate);

        final ProductIdDTO productIdDto = new ProductIdDTO();
        productIdDto.setId(createdProduct.getId());

        return new ResponseEntity<>(productIdDto, HttpStatus.CREATED);
    }

    @Override
    @PreAuthorize(AuthenticationRoles.ANY_REGISTERED_USER)
    public ResponseEntity<Void> updateProduct(String id,
                                              ProductUpdateRequestDTO productUpdateRequestDto) {
        log.info("REST request to update Product with id '{}': {}", id, productUpdateRequestDto);

        final var updatedProduct = this.productDtoMapper.toDomain(productUpdateRequestDto.getProduct());
        this.updateProductUseCase.dispatch(id, updatedProduct);

        return ResponseEntity.noContent().build();
    }

    @Override
    @PreAuthorize(AuthenticationRoles.ANY_REGISTERED_USER)
    public ResponseEntity<ProductResponseDTO> getProductById(String id) {
        log.info("REST request to get Product by id: {}", id);
        final Product product = this.findProductByIdUseCase.dispatch(id);

        final ProductDTO productDTO = this.productDtoMapper.toDto(product);

        final ProductResponseDTO response = new ProductResponseDTO();
        response.setProduct(productDTO);

        return ResponseEntity.ok(response);
    }

    @Override
    @PreAuthorize(AuthenticationRoles.ANY_REGISTERED_USER)
    public ResponseEntity<ProductCollectionDTO> getAllProducts(Long page, Integer pageSize) {
        log.info("REST request to get all Products");
        final ProductCollection productCollection = this.findAllProductsUseCase.dispatch(page,
                pageSize);

        return ResponseEntity.ok(this.productCollectionDTOMapper.toDto(productCollection));
    }
}