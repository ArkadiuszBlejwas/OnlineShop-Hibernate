package com.capgemini.store.persistence.dto;

import com.capgemini.store.persistence.entity.Address;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Setter
@Getter
@Builder
@EqualsAndHashCode
//@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO {

    private Long id;

    private Long version;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    private String firstName;

    private String surName;

    private Address address;

    private LocalDate birthDate;

    private Set<Long> transactions;
}
