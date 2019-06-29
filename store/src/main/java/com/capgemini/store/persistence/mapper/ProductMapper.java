package com.capgemini.store.persistence.mapper;

import com.capgemini.store.persistence.dto.ProductDTO;
import com.capgemini.store.persistence.entity.ProductEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ProductMapper implements Mapper<ProductEntity, ProductDTO> {
    
    TransactionMapper transactionMapper = new TransactionMapper();

    @Override
    public ProductDTO mapToDTO(ProductEntity productEntity){
        if (productEntity == null){
            return null;
        }
        if (productEntity.getTransactions() == null){
            productEntity.setTransactions(new HashSet<>());
        }
        return ProductDTO.builder()
                .id(productEntity.getId())
                .version(productEntity.getVersion())
                .createDate(productEntity.getCreateDate())
                .updateDate(productEntity.getUpdateDate())
                .name(productEntity.getName())
                .margin(productEntity.getMargin())
                .price(productEntity.getPrice())
                .transactions(productEntity.getTransactions().stream().map(t -> t.getId()).collect(Collectors.toSet()))
                .weight(productEntity.getWeight())
                .build();
    }

    @Override
    public ProductEntity mapToEntity(ProductDTO productDTO){
        if (productDTO == null){
            return null;
        }
        return ProductEntity.builder()
                .id(productDTO.getId())
                .version(productDTO.getVersion())
                .createDate(productDTO.getCreateDate())
                .updateDate(productDTO.getUpdateDate())
                .name(productDTO.getName())
                .margin(productDTO.getMargin())
                .price(productDTO.getPrice())
                .weight(productDTO.getWeight())
                .build();
    }

    public Set<ProductDTO> mapToSetDTO(Set<ProductEntity> ProductEntitySet) {
        if (ProductEntitySet == null) {
            return new HashSet<>();
        }
        return ProductEntitySet.stream().map(this::mapToDTO).collect(Collectors.toSet());
    }

    public Set<ProductEntity> mapToSetEntity(Set<ProductDTO> ProductDTOSet) {
        if (ProductDTOSet == null) {
            return new HashSet<>();
        }
        return ProductDTOSet.stream().map(this::mapToEntity).collect(Collectors.toSet());
    }

    public List<ProductDTO> mapToDTOList(List<ProductEntity> ProductEntityList) {
        if (ProductEntityList == null){
            return new ArrayList<>();
        }
        return ProductEntityList.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<ProductEntity> mapToEntityList(List<ProductDTO> ProductDTOList) {
        if (ProductDTOList == null){
            return new ArrayList<>();
        }
        return ProductDTOList.stream().map(this::mapToEntity).collect(Collectors.toList());
    }
}
