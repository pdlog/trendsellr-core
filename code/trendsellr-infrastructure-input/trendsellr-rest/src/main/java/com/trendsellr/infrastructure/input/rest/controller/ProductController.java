package com.trendsellr.infrastructure.input.rest.controller;

import com.trendsellr.application.usecase.CreateProductUseCase;
import com.trendsellr.application.usecase.FindAllProductsUseCase;
import com.trendsellr.application.usecase.FindProductByIdUseCase;
import com.trendsellr.application.usecase.UpdateProductUseCase;
import com.trendsellr.domain.model.Product;
import com.trendsellr.domain.model.ProductCollection;
import com.trendsellr.infrastructure.input.rest.api.ProductsApi;
import com.trendsellr.infrastructure.input.rest.dto.ProductCollectionDTO;
import com.trendsellr.infrastructure.input.rest.dto.ProductCreateRequestDTO;
import com.trendsellr.infrastructure.input.rest.dto.ProductDTO;
import com.trendsellr.infrastructure.input.rest.dto.ProductIdDTO;
import com.trendsellr.infrastructure.input.rest.dto.ProductResponseDTO;
import com.trendsellr.infrastructure.input.rest.dto.ProductUpdateRequestDTO;
import com.trendsellr.infrastructure.input.rest.mapper.ProductCollectionDTOMapper;
import com.trendsellr.infrastructure.input.rest.mapper.ProductDTOMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
  public ResponseEntity<Void> updateProduct(String id,
      ProductUpdateRequestDTO productUpdateRequestDto) {
    log.info("REST request to update Product with id '{}': {}", id, productUpdateRequestDto);

    final var updatedProduct = this.productDtoMapper.toDomain(productUpdateRequestDto.getProduct());
    this.updateProductUseCase.dispatch(id, updatedProduct);

    return ResponseEntity.noContent().build();
  }

  @Override
  public ResponseEntity<ProductResponseDTO> getProductById(String id) {
    log.info("REST request to get Product by id: {}", id);
    final Product product = this.findProductByIdUseCase.dispatch(id);

    final ProductDTO productDTO = this.productDtoMapper.toDto(product);

    final ProductResponseDTO response = new ProductResponseDTO();
    response.setProduct(productDTO);

    return ResponseEntity.ok(response);
  }

  @Override
  public ResponseEntity<ProductCollectionDTO> getAllProducts(Long page, Integer pageSize) {
    log.info("REST request to get all Products");
    final ProductCollection productCollection = this.findAllProductsUseCase.dispatch(page,
        pageSize);

    return ResponseEntity.ok(this.productCollectionDTOMapper.toDto(productCollection));
  }
}