package com.capgemini.store.persistence.builder;

import com.capgemini.store.persistence.entity.ProductEntity;
import com.capgemini.store.persistence.entity.TransactionEntity;

import java.time.LocalDateTime;
import java.util.Set;

public final class ProductEntityBuilder {
    private String name;
    private Double price;
    private Double margin;
    private Double weight;
    private Long id;
    private Set<TransactionEntity> transactions;
    private Long version;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public ProductEntityBuilder() {
    }

    public static ProductEntityBuilder aProductEntity() {
        return new ProductEntityBuilder();
    }

    public ProductEntityBuilder name(String name) {
        this.name = name;
        return this;
    }

    public ProductEntityBuilder price(Double price) {
        this.price = price;
        return this;
    }

    public ProductEntityBuilder margin(Double margin) {
        this.margin = margin;
        return this;
    }

    public ProductEntityBuilder weight(Double weight) {
        this.weight = weight;
        return this;
    }

    public ProductEntityBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public ProductEntityBuilder transactions(Set<TransactionEntity> transactions) {
        this.transactions = transactions;
        return this;
    }

    public ProductEntityBuilder version(Long version) {
        this.version = version;
        return this;
    }

    public ProductEntityBuilder createDate(LocalDateTime createDate) {
        this.createDate = createDate;
        return this;
    }

    public ProductEntityBuilder updateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
        return this;
    }

    public ProductEntity build() {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setName(name);
        productEntity.setPrice(price);
        productEntity.setMargin(margin);
        productEntity.setWeight(weight);
        productEntity.setId(id);
        productEntity.setTransactions(transactions);
        productEntity.setVersion(version);
        productEntity.setCreateDate(createDate);
        productEntity.setUpdateDate(updateDate);
        return productEntity;
    }
}
