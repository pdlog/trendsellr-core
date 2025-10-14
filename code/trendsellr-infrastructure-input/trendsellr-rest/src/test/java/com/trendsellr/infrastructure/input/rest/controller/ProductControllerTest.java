package com.trendsellr.infrastructure.input.rest.controller;

import com.trendsellr.application.usecase.product.CreateProductUseCase;
import com.trendsellr.application.usecase.product.FindAllProductsUseCase;
import com.trendsellr.application.usecase.product.FindProductByIdUseCase;
import com.trendsellr.application.usecase.product.UpdateProductUseCase;
import com.trendsellr.domain.model.product.Product;
import com.trendsellr.domain.model.product.ProductCollection;
import com.trendsellr.infrastructure.input.rest.dto.*;
import com.trendsellr.infrastructure.input.rest.mapper.product.ProductCollectionDTOMapper;
import com.trendsellr.infrastructure.input.rest.mapper.product.ProductDTOMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private CreateProductUseCase createProductUseCase;

    @Mock
    private UpdateProductUseCase updateProductUseCase;

    @Mock
    private FindAllProductsUseCase findAllProductsUseCase;

    @Mock
    private FindProductByIdUseCase findProductByIdUseCase;

    @Mock
    private ProductDTOMapper productDtoMapper;

    @Mock
    private ProductCollectionDTOMapper productCollectionDTOMapper;

    @InjectMocks
    private ProductController productController;

    @Test
    @DisplayName("Given a create request, when createProduct is called, then should return 201 Created with product ID")
    void givenCreateRequest_whenCreateProduct_thenShouldReturnCreatedWithId() {
        // Given
        final ProductCreateDTO createDto = new ProductCreateDTO();
        final ProductCreateRequestDTO requestDto = new ProductCreateRequestDTO().product(createDto);
        final Product domainProduct = Product.builder().build();
        final Product createdDomainProduct = Product.builder().id(UUID.randomUUID().toString()).build();

        when(this.productDtoMapper.toDomain(createDto)).thenReturn(domainProduct);
        when(this.createProductUseCase.dispatch(domainProduct)).thenReturn(createdDomainProduct);

        // When
        final ResponseEntity<ProductIdDTO> response = this.productController.createProduct(requestDto);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(createdDomainProduct.getId(), response.getBody().getId());
        verify(this.createProductUseCase).dispatch(domainProduct);
    }

    @Test
    @DisplayName("Given an existing ID and update data, when updateProduct is called, then should return 204 No Content")
    void givenExistingId_whenUpdateProduct_thenShouldReturnNoContent() {
        // Given
        final String productId = UUID.randomUUID().toString();
        final ProductUpdateDTO updateDto = new ProductUpdateDTO();
        final ProductUpdateRequestDTO requestDto = new ProductUpdateRequestDTO().product(updateDto);
        final Product domainProduct = Product.builder().build();

        when(this.productDtoMapper.toDomain(updateDto)).thenReturn(domainProduct);

        // When
        final ResponseEntity<Void> response = this.productController.updateProduct(productId,
                requestDto);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(this.updateProductUseCase).dispatch(productId, domainProduct);
    }

    @Test
    @DisplayName("Given an existing ID, when getProductById is called, then should return 200 OK with product data")
    void givenExistingId_whenGetProductById_thenShouldReturnOkWithProductData() {
        // Given
        final String productId = UUID.randomUUID().toString();
        final Product domainProduct = Product.builder().id(productId).build();
        final ProductDTO productDto = new ProductDTO().id(productId);

        when(this.findProductByIdUseCase.dispatch(productId)).thenReturn(domainProduct);
        when(this.productDtoMapper.toDto(domainProduct)).thenReturn(productDto);

        // When
        final ResponseEntity<ProductResponseDTO> response = this.productController.getProductById(
                productId);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(productDto, response.getBody().getProduct());
        verify(this.findProductByIdUseCase).dispatch(productId);
    }

    @Test
    @DisplayName("Given pagination params, when getAllProducts is called, then should return 200 OK with a product collection")
    void givenPaginationParams_whenGetAllProducts_thenShouldReturnOkWithCollection() {
        // Given
        final Long page = 0L;
        final Integer pageSize = 10;
        final ProductCollection domainCollection = ProductCollection.builder().build();
        final ProductCollectionDTO collectionDto = new ProductCollectionDTO();

        when(this.findAllProductsUseCase.dispatch(page, pageSize)).thenReturn(domainCollection);
        when(this.productCollectionDTOMapper.toDto(domainCollection)).thenReturn(collectionDto);

        // When
        final ResponseEntity<ProductCollectionDTO> response = this.productController.getAllProducts(
                page,
                pageSize);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(collectionDto, response.getBody());
        verify(this.findAllProductsUseCase).dispatch(page, pageSize);
    }
}