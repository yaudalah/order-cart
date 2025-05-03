package com.example.ordercart.model.mapper;

import com.example.ordercart.entity.Product;
import com.example.ordercart.model.request.ProductRequest;
import com.example.ordercart.model.response.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

//    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    // Mapping from ProductRequest to Product
    Product productRequestToProduct(ProductRequest productRequest);

    // Mapping from Product to ProductResponse
    @Mapping(source = "price", target = "price")
    @Mapping(source = "stock", target = "stock")
    ProductResponse productToProductResponse(Product product);
}
