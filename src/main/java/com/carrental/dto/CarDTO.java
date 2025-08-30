package com.carrental.dto;

import com.carrental.model.CarStatus;

import java.math.BigDecimal;

public record CarDTO(Long id, String brand, String model, BigDecimal pricePerDay, CarStatus status) {}