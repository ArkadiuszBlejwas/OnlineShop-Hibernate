package com.capgemini.store.persistence.mapper;

import com.capgemini.store.persistence.dto.ClientDTO;
import com.capgemini.store.persistence.entity.ClientEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ClientMapper implements Mapper<ClientEntity, ClientDTO> {

    @Override
    public ClientDTO mapToDTO(ClientEntity clientEntity){
        if (clientEntity == null){
            return null;
        }
        if (clientEntity.getTransactions() == null){
            clientEntity.setTransactions(new HashSet<>());
        }
        return ClientDTO.builder()
                .id(clientEntity.getId())
                .version(clientEntity.getVersion())
                .createDate(clientEntity.getCreateDate())
                .updateDate(clientEntity.getUpdateDate())
                .address(clientEntity.getAddress())
                .birthDate(clientEntity.getBirthDate())
                .transactions(clientEntity.getTransactions().stream().map(t -> t.getId()).collect(Collectors.toSet()))
                .firstName(clientEntity.getFirstName())
                .surName(clientEntity.getSurName())
                .build();
    }

    @Override
    public ClientEntity mapToEntity(ClientDTO clientDTO){
        if (clientDTO == null){
            return null;
        }
        return ClientEntity.builder()
                .id(clientDTO.getId())
                .version(clientDTO.getVersion())
                .createDate(clientDTO.getCreateDate())
                .updateDate(clientDTO.getUpdateDate())
                .address(clientDTO.getAddress())
                .birthDate(clientDTO.getBirthDate())
                .firstName(clientDTO.getFirstName())
                .surName(clientDTO.getSurName())
                .build();
    }

    public Set<ClientDTO> mapToSetDTO(Set<ClientEntity> ClientEntitySet) {
        if (ClientEntitySet == null) {
            return new HashSet<>();
        }
        return ClientEntitySet.stream().map(this::mapToDTO).collect(Collectors.toSet());
    }

    public Set<ClientEntity> mapToSetEntity(Set<ClientDTO> ClientDTOSet) {
        if (ClientDTOSet == null) {
            return new HashSet<>();
        }
        return ClientDTOSet.stream().map(this::mapToEntity).collect(Collectors.toSet());
    }

    public List<ClientDTO> mapToDTOList(List<ClientEntity> ClientEntityList) {
        if (ClientEntityList == null){
            return new ArrayList<>();
        }
        return ClientEntityList.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<ClientEntity> mapToEntityList(List<ClientDTO> ClientDTOList) {
        if (ClientDTOList == null){
            return new ArrayList<>();
        }
        return ClientDTOList.stream().map(this::mapToEntity).collect(Collectors.toList());
    }
}
