package com.capgemini.store.persistence.repo;

import com.capgemini.store.persistence.dto.ProductInRealizaton;
import com.capgemini.store.persistence.entity.ProductEntity;

import java.util.List;

public interface ProductRepoCustom {

    List<ProductEntity> find10BestSellingProducts();

    List<ProductEntity> findProductsWhichAreInRealization();

    List<ProductInRealizaton> findProductsWhichAreInRealizationInObject();
}
