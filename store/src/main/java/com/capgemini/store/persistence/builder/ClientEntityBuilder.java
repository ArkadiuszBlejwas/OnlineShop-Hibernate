package com.capgemini.store.persistence.builder;

import com.capgemini.store.persistence.entity.Address;
import com.capgemini.store.persistence.entity.ClientEntity;
import com.capgemini.store.persistence.entity.TransactionEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public final class ClientEntityBuilder {
    private String firstName;
    private String surName;
    private Address address;
    private LocalDate birthDate;
    private Long id;
    private Long version;
    private LocalDateTime createDate;
    private Set<TransactionEntity> transactions;
    private LocalDateTime updateDate;

    public ClientEntityBuilder() {
    }

    public static ClientEntityBuilder aClientEntity() {
        return new ClientEntityBuilder();
    }

    public ClientEntityBuilder firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public ClientEntityBuilder surName(String surName) {
        this.surName = surName;
        return this;
    }

    public ClientEntityBuilder address(Address address) {
        this.address = address;
        return this;
    }

    public ClientEntityBuilder birthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public ClientEntityBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public ClientEntityBuilder version(Long version) {
        this.version = version;
        return this;
    }

    public ClientEntityBuilder createDate(LocalDateTime createDate) {
        this.createDate = createDate;
        return this;
    }

    public ClientEntityBuilder transactions(Set<TransactionEntity> transactions) {
        this.transactions = transactions;
        return this;
    }

    public ClientEntityBuilder updateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
        return this;
    }

    public ClientEntity build() {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setFirstName(firstName);
        clientEntity.setSurName(surName);
        clientEntity.setAddress(address);
        clientEntity.setBirthDate(birthDate);
        clientEntity.setId(id);
        clientEntity.setVersion(version);
        clientEntity.setCreateDate(createDate);
        clientEntity.setTransactions(transactions);
        clientEntity.setUpdateDate(updateDate);
        return clientEntity;
    }
}
