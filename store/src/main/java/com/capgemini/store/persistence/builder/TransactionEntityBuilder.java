package com.capgemini.store.persistence.builder;

import com.capgemini.store.persistence.entity.ClientEntity;
import com.capgemini.store.persistence.entity.ProductEntity;
import com.capgemini.store.persistence.entity.Status;
import com.capgemini.store.persistence.entity.TransactionEntity;

import java.time.LocalDateTime;

public final class TransactionEntityBuilder {
    private LocalDateTime date;
    private Status status;
    private Integer numberProducts;
    private Long id;
    private Long version;
    //(fetch = FetchType.LAZY)      //(mappedBy = "transactions")
    private ProductEntity product;
    private LocalDateTime createDate;
    private ClientEntity client;
    private LocalDateTime updateDate;

    public TransactionEntityBuilder() {
    }

    public static TransactionEntityBuilder aTransactionEntity() {
        return new TransactionEntityBuilder();
    }

    public TransactionEntityBuilder date(LocalDateTime date) {
        this.date = date;
        return this;
    }

    public TransactionEntityBuilder status(Status status) {
        this.status = status;
        return this;
    }

    public TransactionEntityBuilder numberProducts(Integer numberProducts) {
        this.numberProducts = numberProducts;
        return this;
    }

    public TransactionEntityBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public TransactionEntityBuilder version(Long version) {
        this.version = version;
        return this;
    }

    public TransactionEntityBuilder product(ProductEntity product) {
        this.product = product;
        return this;
    }

    public TransactionEntityBuilder createDate(LocalDateTime createDate) {
        this.createDate = createDate;
        return this;
    }

    public TransactionEntityBuilder client(ClientEntity client) {
        this.client = client;
        return this;
    }

    public TransactionEntityBuilder updateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
        return this;
    }

    public TransactionEntity build() {
        TransactionEntity transactionEntity = new TransactionEntity();
        transactionEntity.setDate(date);
        transactionEntity.setStatus(status);
        transactionEntity.setNumberProducts(numberProducts);
        transactionEntity.setId(id);
        transactionEntity.setVersion(version);
        transactionEntity.setProduct(product);
        transactionEntity.setCreateDate(createDate);
        transactionEntity.setClient(client);
        transactionEntity.setUpdateDate(updateDate);
        return transactionEntity;
    }
}
