package com.capgemini.store.persistence.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Setter
@Getter
@Builder
@EqualsAndHashCode
//@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private Long id;

    private Long version;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    private String name;

    private Double price;

    private Double margin;

    private Double weight;

    private Set<Long> transactions;
}
