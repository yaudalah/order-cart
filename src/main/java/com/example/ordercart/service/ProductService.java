package com.example.ordercart.service;

import com.example.ordercart.entity.Product;
import com.example.ordercart.model.mapper.ProductMapper;
import com.example.ordercart.model.request.ProductRequest;
import com.example.ordercart.model.response.ProductResponse;
import com.example.ordercart.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Cacheable(
            value = "products",
            key = "'page_' + #pageable.pageNumber + '_size_' + #pageable.pageSize + '_sort_' + #pageable.sort.toString()"
    )
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        log.info("[ProductService] getAllProducts: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.map(productMapper::productToProductResponse);
    }

    @CacheEvict(value = "products", allEntries = true)
    @Transactional
    public ProductResponse createProduct(ProductRequest productRequest) {
        final Product product = productMapper.productRequestToProduct(productRequest);
        log.info("[ProductService] Save product");
        productRepository.save(product);

        return productMapper.productToProductResponse(product);
    }
}
