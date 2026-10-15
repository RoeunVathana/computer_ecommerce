package com.example.computer_ecommerce.product.service;

import com.example.computer_ecommerce.product.dto.ProductRequest;
import com.example.computer_ecommerce.product.dto.ProductResponse;
import com.example.computer_ecommerce.product.entity.Product;
import com.example.computer_ecommerce.product.mapper.ProductMapper;
import com.example.computer_ecommerce.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductService {

    private final ProductRepository products;
    private final ProductMapper mapper;

    public ProductService(ProductRepository products, ProductMapper mapper) {
        this.products = products;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> findAll() {
        return products.findAll().stream().map(mapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ProductResponse findById(Long id) {
        return mapper.toResponse(getProduct(id));
    }

    public ProductResponse create(ProductRequest request) {
        return mapper.toResponse(products.save(mapper.toEntity(request)));
    }

    public ProductResponse update(Long id, ProductRequest request) {
        Product product = getProduct(id);
        mapper.updateEntity(product, request);
        return mapper.toResponse(product);
    }

    public void delete(Long id) {
        products.delete(getProduct(id));
    }

    private Product getProduct(Long id) {
        return products.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));
    }
}
