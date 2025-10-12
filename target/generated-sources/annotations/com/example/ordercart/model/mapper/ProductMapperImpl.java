package com.example.ordercart.model.mapper;

import com.example.ordercart.entity.Product;
import com.example.ordercart.model.request.ProductRequest;
import com.example.ordercart.model.response.ProductResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-12T12:13:35+0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Amazon.com Inc.)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public Product productRequestToProduct(ProductRequest productRequest) {
        if ( productRequest == null ) {
            return null;
        }

        Product product = new Product();

        product.setName( productRequest.getName() );
        product.setDescription( productRequest.getDescription() );
        product.setPrice( productRequest.getPrice() );
        product.setStock( productRequest.getStock() );

        return product;
    }

    @Override
    public ProductResponse productToProductResponse(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductResponse.ProductResponseBuilder productResponse = ProductResponse.builder();

        productResponse.price( product.getPrice() );
        productResponse.stock( product.getStock() );
        productResponse.userId( product.getUserId() );
        productResponse.name( product.getName() );
        productResponse.description( product.getDescription() );

        return productResponse.build();
    }

    @Override
    public void updateProductFromRequest(ProductRequest request, Product product) {
        if ( request == null ) {
            return;
        }

        if ( request.getName() != null ) {
            product.setName( request.getName() );
        }
        if ( request.getDescription() != null ) {
            product.setDescription( request.getDescription() );
        }
        if ( request.getPrice() != null ) {
            product.setPrice( request.getPrice() );
        }
        product.setStock( request.getStock() );
    }
}
