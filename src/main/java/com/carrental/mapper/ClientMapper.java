package com.carrental.mapper;

import com.carrental.dto.ClientDTO;
import com.carrental.model.ClientEntity;

public class ClientMapper {
    public static ClientDTO toDto(ClientEntity entity) {
        return new ClientDTO(entity.getId(), entity.getFirstName(), entity.getLastName(), entity.getEmail());
    }

    public static ClientEntity toEntity(ClientDTO dto) {
        return ClientEntity.builder()
                .id(dto.id())
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .email(dto.email())
                .build();
    }
}
