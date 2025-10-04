package com.trendsellr.infrastructure.output.persistence.mongodb.repository;

import com.trendsellr.domain.model.Pagination;
import com.trendsellr.domain.model.Product;
import com.trendsellr.domain.model.ProductCollection;
import com.trendsellr.domain.repository.ProductRepository;
import com.trendsellr.infrastructure.output.persistence.mongodb.entity.ProductEntity;
import com.trendsellr.infrastructure.output.persistence.mongodb.repository.mapper.PaginationRepositoryMapper;
import com.trendsellr.infrastructure.output.persistence.mongodb.repository.mapper.ProductCollectionRepositoryMapper;
import com.trendsellr.infrastructure.output.persistence.mongodb.repository.mapper.ProductRepositoryMapper;
import com.trendsellr.infrastructure.output.persistence.mongodb.repository.spring.SpringProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Implementation of the ProductRepository port using Spring Data MongoDB.
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class MongoProductRepositoryImpl implements ProductRepository {

    private final SpringProductRepository springProductRepository;

    private final PaginationRepositoryMapper paginationRepositoryMapper;

    private final ProductCollectionRepositoryMapper productCollectionRepositoryMapper;

    private final ProductRepositoryMapper productRepositoryMapper;

    @Override
    public Product save(final Product product) {
        log.info("Saving product with id: {}", product.getId());
        ProductEntity productToSave = this.productRepositoryMapper.toEntity(product);
        ProductEntity savedEntity = this.springProductRepository.save(productToSave);

        return this.productRepositoryMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Product> update(final String productId, final Product updatedProduct) {
        log.info("Updating product with id: {}", productId);

        return this.springProductRepository.findByProductId(productId).map(existingProduct -> {
            ProductEntity productToUpdate = this.productRepositoryMapper.toEntity(updatedProduct);
            this.productRepositoryMapper.updateProduct(productToUpdate, existingProduct);
            ProductEntity updatedEntity = this.springProductRepository.save(existingProduct);

            return this.productRepositoryMapper.toDomain(updatedEntity);
        });
    }

    @Override
    public Optional<Product> findByProductId(final String productId) {
        log.info("Finding product by id: {}", productId);

        return this.springProductRepository.findByProductId(productId).map(this.productRepositoryMapper::toDomain);
    }

    @Override
    public ProductCollection findAll(final Long page, final Integer pageSize) {
        log.info("Finding all products with pagination - page: {}, pageSize: {}", page, pageSize);
        Pageable pageable = Pageable.ofSize(pageSize).withPage(page.intValue());
        final Page<ProductEntity> paginatedProducts = this.springProductRepository.findAll(pageable);
        final Pagination pagination = this.paginationRepositoryMapper.toDomain(page, pageSize, paginatedProducts.getTotalPages(),
                paginatedProducts.getTotalElements());

        return this.productCollectionRepositoryMapper.toDomain(paginatedProducts.getContent(), pagination);
    }
}