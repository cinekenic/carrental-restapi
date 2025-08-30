package com.carrental.mapper;

import com.carrental.dto.CarDTO;
import com.carrental.model.CarEntity;

public class CarMapper {
    public static CarDTO toDto(CarEntity entity) {
        return new CarDTO(entity.getId(), entity.getBrand(), entity.getModel(),
                entity.getPricePerDay(), entity.getStatus());
    }

    public static CarEntity toEntity(CarDTO dto) {
        return CarEntity.builder()
                .id(dto.id())
                .brand(dto.brand())
                .model(dto.model())
                .pricePerDay(dto.pricePerDay())
                .status(dto.status())
                .build();
    }
}
