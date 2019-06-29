package com.capgemini.store.persistence.entity;

import com.capgemini.store.persistence.builder.ProductEntityBuilder;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Setter
@Getter
@Entity
@EqualsAndHashCode
@NoArgsConstructor
@Table(name = "PRODUCTS")
public class ProductEntity extends AbstractEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, precision = 2)
    private Double price;

    @Column(nullable = false, precision = 2)
    private Double margin;

    @Column(nullable = false, precision = 2)
    private Double weight;

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<TransactionEntity> transactions;

    public static ProductEntityBuilder builder(){
        return new ProductEntityBuilder();
    }
}
