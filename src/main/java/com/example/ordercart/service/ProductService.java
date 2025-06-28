package com.example.ordercart.service;

import com.example.ordercart.entity.Product;
import com.example.ordercart.exception.ServiceException;
import com.example.ordercart.model.mapper.ProductMapper;
import com.example.ordercart.model.request.ProductRequest;
import com.example.ordercart.model.response.ProductResponse;
import com.example.ordercart.repository.ProductRepository;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final UserService userService;
    private final CacheVersionService cacheVersionService;

    @Cacheable(
            value = "products",
            key = "T(io.micrometer.common.util.StringUtils).isBlank(#username) ? " +
                    "'global_page_' + #pageable.pageNumber + '_size_' + #pageable.pageSize + '_sort_' + #pageable.sort.toString() : " +
                    "'user_' + #username + '_V' + @cacheVersionService.getVersion(#username) + '_page_' + #pageable.pageNumber + '_size_' + #pageable.pageSize + '_sort_' + #pageable.sort.toString()"
    )
    public Page<ProductResponse> getAllProducts(Pageable pageable, String username) {
        log.info("[START] getAllProducts: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
        Page<Product> productPage;
        if (StringUtils.isBlank(username)) {
            productPage = productRepository.findAll(pageable);
        } else {
            final UUID userId = userService.fetchUserByUsername(username).getId();
            productPage = productRepository.findAllByUserId(userId, pageable);
        }
        return productPage.map(productMapper::productToProductResponse);
    }

    @CacheEvict(value = "products", allEntries = true)
    @Transactional
    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = productMapper.productRequestToProduct(productRequest);
        UUID userId = userService.fetchUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getId();
        product.setUserId(userId);
        log.info("[START] Save product");
        productRepository.save(product);
        log.info("[END] Save product successful: {}", product.getId());

        return productMapper.productToProductResponse(product);
    }

    @Transactional
    public ProductResponse updateProduct(UUID productId, ProductRequest request, String username) {
        if (StringUtils.isBlank(username)) throw new ServiceException("Username is blank");

        Product existing = productRepository.findById(productId)
                .orElseThrow(() -> new ServiceException("Product not found"));

        productMapper.updateProductFromRequest(request, existing);
        log.info("[START] Save updated product");
        Product saved = productRepository.save(existing);

        cacheVersionService.incrementVersion(username);

        return productMapper.productToProductResponse(saved);
    }
}
