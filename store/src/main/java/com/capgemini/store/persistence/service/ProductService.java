package com.capgemini.store.persistence.service;

import com.capgemini.store.persistence.dto.ProductDTO;
import com.capgemini.store.persistence.dto.ProductInRealizaton;
import com.capgemini.store.persistence.repo.ProductRepo;

import java.util.List;

public interface ProductService extends Service {

    List<ProductDTO> findAllProducts();

    ProductDTO findProductById(Long id);

    ProductDTO addProduct(ProductDTO productDTO);

    ProductDTO updateProduct(ProductDTO productDTO);

    void deleteProduct(Long id);

    ProductRepo getProductRepo();

    List<ProductDTO> find10BestSellingProducts();

    List<ProductDTO> findProductsWhichAreInRealization();

    List<ProductInRealizaton> findProductsWhichAreInRealizationInObject();
}
