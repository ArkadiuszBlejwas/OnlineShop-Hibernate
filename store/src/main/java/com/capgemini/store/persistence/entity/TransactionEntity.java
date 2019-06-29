package com.capgemini.store.persistence.entity;

import com.capgemini.store.persistence.builder.TransactionEntityBuilder;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
//@EqualsAndHashCode
@NoArgsConstructor
@Table(name = "TRANSACTIONS")
public class TransactionEntity extends AbstractEntity{

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false)
    private Integer numberProducts;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})//(fetch = FetchType.LAZY)      //(mappedBy = "transactions")
    private ProductEntity product;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private ClientEntity client;

    public static TransactionEntityBuilder builder(){
        return new TransactionEntityBuilder();
    }
}
