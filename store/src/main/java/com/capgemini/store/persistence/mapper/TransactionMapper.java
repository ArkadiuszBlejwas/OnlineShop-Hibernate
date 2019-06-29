package com.capgemini.store.persistence.mapper;

import com.capgemini.store.persistence.dto.TransactionDTO;
import com.capgemini.store.persistence.entity.TransactionEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TransactionMapper implements Mapper<TransactionEntity, TransactionDTO> {

    @Override
    public TransactionDTO mapToDTO(TransactionEntity transactionEntity){
        if (transactionEntity == null){
            return null;
        }
        return TransactionDTO.builder()
                .id(transactionEntity.getId())
                .version(transactionEntity.getVersion())
                .createDate(transactionEntity.getCreateDate())
                .updateDate(transactionEntity.getUpdateDate())
                .date(transactionEntity.getDate())
                .status(transactionEntity.getStatus())
                .numberProducts(transactionEntity.getNumberProducts())
                .product(transactionEntity.getProduct().getId())
                .client(transactionEntity.getClient().getId())
                .build();
    }

    @Override
    public TransactionEntity mapToEntity(TransactionDTO transactionDTO){
        if (transactionDTO == null){
            return null;
        }
        return TransactionEntity.builder()
                .id(transactionDTO.getId())
                .version(transactionDTO.getVersion())
                .createDate(transactionDTO.getCreateDate())
                .updateDate(transactionDTO.getUpdateDate())
                .date(transactionDTO.getDate())
                .status(transactionDTO.getStatus())
                .numberProducts(transactionDTO.getNumberProducts())
                .build();
    }

    public Set<TransactionDTO> mapToSetDTO(Set<TransactionEntity> TransactionEntitySet) {
        if (TransactionEntitySet == null) {
            return new HashSet<>();
        }
        return TransactionEntitySet.stream().map(this::mapToDTO).collect(Collectors.toSet());
    }

    public Set<TransactionEntity> mapToSetEntity(Set<TransactionDTO> TransactionDTOSet) {
        if (TransactionDTOSet == null) {
            return new HashSet<>();
        }
        return TransactionDTOSet.stream().map(this::mapToEntity).collect(Collectors.toSet());
    }

    public List<TransactionDTO> mapToDTOList(List<TransactionEntity> TransactionEntityList) {
        if (TransactionEntityList == null){
            return new ArrayList<>();
        }
        return TransactionEntityList.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<TransactionEntity> mapToEntityList(List<TransactionDTO> TransactionDTOList) {
        if (TransactionDTOList == null){
            return new ArrayList<>();
        }
        return TransactionDTOList.stream().map(this::mapToEntity).collect(Collectors.toList());
    }
}
