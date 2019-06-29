package com.capgemini.store.persistence.entity;

import com.capgemini.store.persistence.builder.ClientEntityBuilder;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Setter
@Getter
@Entity
@EqualsAndHashCode
@NoArgsConstructor
@Table(name = "CLIENTS")
public class ClientEntity extends AbstractEntity{

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String surName;

    @Column(nullable = false)
    private Address address;

    @Column(nullable = true)
    private LocalDate birthDate;

    @OneToMany(mappedBy = "client", cascade = CascadeType.REMOVE)
    private Set<TransactionEntity> transactions;

    public static ClientEntityBuilder builder(){
        return new ClientEntityBuilder();
    }
}
