package com.trendsellr.infrastructure.output.persistence.mongodb.repository;

import com.trendsellr.domain.model.Pagination;
import com.trendsellr.domain.model.product.Product;
import com.trendsellr.domain.model.product.ProductCollection;
import com.trendsellr.infrastructure.output.persistence.mongodb.entity.ProductEntity;
import com.trendsellr.infrastructure.output.persistence.mongodb.repository.mapper.PaginationRepositoryMapper;
import com.trendsellr.infrastructure.output.persistence.mongodb.repository.mapper.product.ProductCollectionRepositoryMapper;
import com.trendsellr.infrastructure.output.persistence.mongodb.repository.mapper.product.ProductRepositoryMapper;
import com.trendsellr.infrastructure.output.persistence.mongodb.repository.spring.SpringProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MongoProductRepositoryImplTest {

    @Mock
    private SpringProductRepository springProductRepository;

    @Mock
    private PaginationRepositoryMapper paginationRepositoryMapper;

    @Mock
    private ProductCollectionRepositoryMapper productCollectionRepositoryMapper;

    @Mock
    private ProductRepositoryMapper productRepositoryMapper;

    @InjectMocks
    private MongoProductRepositoryImpl mongoProductRepository;

    @Test
    @DisplayName("Given a Product, when save is called, then should map, save and return mapped domain product")
    void givenProduct_whenSave_thenShouldMapAndSave() {
        // Given
        final Product domainProduct = Product.builder().build();
        final ProductEntity entityToSave = new ProductEntity();
        final ProductEntity savedEntity = new ProductEntity();
        final Product expectedProduct = Product.builder().id(UUID.randomUUID().toString()).build();

        when(this.productRepositoryMapper.toEntity(domainProduct)).thenReturn(entityToSave);
        when(this.springProductRepository.save(entityToSave)).thenReturn(savedEntity);
        when(this.productRepositoryMapper.toDomain(savedEntity)).thenReturn(expectedProduct);

        // When
        final Product result = this.mongoProductRepository.save(domainProduct);

        // Then
        assertEquals(expectedProduct, result);
        verify(this.productRepositoryMapper).toEntity(domainProduct);
        verify(this.springProductRepository).save(entityToSave);
        verify(this.productRepositoryMapper).toDomain(savedEntity);
    }

    @Test
    @DisplayName("Given an existing product, when update is called, then should find, map, update, save and return domain product")
    void givenExistingProduct_whenUpdate_thenShouldFindAndUpdate() {
        // Given
        final String productId = UUID.randomUUID().toString();
        final Product domainProductToUpdate = Product.builder().name("New Name").build();

        final ProductEntity existingEntity = new ProductEntity();
        existingEntity.setProductId(productId);

        final ProductEntity mappedEntityFromUpdate = new ProductEntity();
        mappedEntityFromUpdate.setName("New Name");

        final Product finalDomainProduct = Product.builder().id(productId).name("New Name").build();

        when(this.springProductRepository.findByProductId(productId)).thenReturn(
                Optional.of(existingEntity));
        when(this.productRepositoryMapper.toEntity(domainProductToUpdate)).thenReturn(
                mappedEntityFromUpdate);
        when(this.springProductRepository.save(existingEntity)).thenReturn(existingEntity);
        when(this.productRepositoryMapper.toDomain(existingEntity)).thenReturn(finalDomainProduct);

        // When
        final Optional<Product> result = this.mongoProductRepository.update(productId,
                domainProductToUpdate);

        // Then
        assertTrue(result.isPresent());
        assertEquals(finalDomainProduct, result.get());

        verify(this.springProductRepository).findByProductId(productId);
        final ArgumentCaptor<ProductEntity> sourceCaptor = ArgumentCaptor.forClass(ProductEntity.class);
        final ArgumentCaptor<ProductEntity> targetCaptor = ArgumentCaptor.forClass(ProductEntity.class);
        verify(this.productRepositoryMapper).updateProduct(sourceCaptor.capture(),
                targetCaptor.capture());

        assertEquals(mappedEntityFromUpdate, sourceCaptor.getValue());
        assertEquals(existingEntity, targetCaptor.getValue());

        verify(this.springProductRepository).save(existingEntity);
    }

    @Test
    @DisplayName("Given a non-existing product, when update is called, then should return empty optional")
    void givenNonExistingProduct_whenUpdate_thenShouldReturnEmpty() {
        // Given
        final String productId = UUID.randomUUID().toString();
        final Product updatedProduct = Product.builder().build();

        when(this.springProductRepository.findByProductId(productId)).thenReturn(Optional.empty());

        // When
        final Optional<Product> result = this.mongoProductRepository.update(productId, updatedProduct);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Given an existing product ID, when findByProductId is called, then should return product")
    void givenExistingId_whenFindByProductId_thenShouldReturnProduct() {
        // Given
        final String productId = UUID.randomUUID().toString();
        final ProductEntity foundEntity = new ProductEntity();
        final Product expectedProduct = Product.builder().id(productId).build();

        when(this.springProductRepository.findByProductId(productId)).thenReturn(
                Optional.of(foundEntity));
        when(this.productRepositoryMapper.toDomain(foundEntity)).thenReturn(expectedProduct);

        // When
        final Optional<Product> result = this.mongoProductRepository.findByProductId(productId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(expectedProduct, result.get());
    }

    @Test
    @DisplayName("Given pagination params, when findAll is called, then should call repository and mappers and return collection")
    void givenPaginationParams_whenFindAll_thenShouldReturnCollection() {
        // Given
        final Long page = 0L;
        final Integer pageSize = 5;
        final Page<ProductEntity> entityPage = new PageImpl<>(List.of(new ProductEntity()));
        final Pagination pagination = Pagination.builder().build();
        final ProductCollection expectedCollection = ProductCollection.builder().build();

        when(this.springProductRepository.findAll(any(Pageable.class))).thenReturn(entityPage);
        when(this.paginationRepositoryMapper.toDomain(page, pageSize, entityPage.getTotalPages(),
                entityPage.getTotalElements())).thenReturn(pagination);
        when(this.productCollectionRepositoryMapper.toDomain(entityPage.getContent(),
                pagination)).thenReturn(expectedCollection);

        // When
        final ProductCollection result = this.mongoProductRepository.findAll(page, pageSize);

        // Then
        assertEquals(expectedCollection, result);
        verify(this.springProductRepository).findAll(any(Pageable.class));
        verify(this.paginationRepositoryMapper).toDomain(any(), any(), any(), any());
        verify(this.productCollectionRepositoryMapper).toDomain(any(), any());
    }

    @Test
    @DisplayName("When deleteAll is called, then should call deleteAll on Spring repository")
    void givenCallToDeleteAll_whenDeleteAll_thenShouldCallSpringRepository() {
        // When
        this.mongoProductRepository.deleteAll();

        // Then
        verify(this.springProductRepository).deleteAll();
    }
}