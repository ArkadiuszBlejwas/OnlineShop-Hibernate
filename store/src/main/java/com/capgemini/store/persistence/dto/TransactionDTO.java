package com.capgemini.store.persistence.dto;

import com.capgemini.store.persistence.entity.Status;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Setter
@Getter
@Builder
@EqualsAndHashCode
//@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {

    private Long id;

    private Long version;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    private LocalDateTime date;

    private Status status;

    private Integer numberProducts;

    private Long product;

    private Long client;
}
