package com.capgemini.store.persistence;

import com.capgemini.store.persistence.entity.Status;
import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

//@Builder
@Getter
@Setter
@NoArgsConstructor
@Component
//@AllArgsConstructor
public class TransactionSearchCriteria {

    private Status status;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Long idProduct;

    private Double totalPrice;

}
