package com.carrental.service;

import com.carrental.exception.ResourceNotFoundException;
import com.carrental.model.CarEntity;
import com.carrental.model.CarStatus;
import com.carrental.repository.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class CarServiceTest {

    private CarRepository carRepository;
    private CarService carService;

    @BeforeEach
    void setUp() {
        carRepository = mock(CarRepository.class);
        carService = new CarService(carRepository);
    }

    @Test
    void shouldReturnAllCars() {
        // given
        CarEntity car1 = CarEntity.builder()
                .id(1L).brand("Toyota").model("Corolla")
                .pricePerDay(BigDecimal.valueOf(150))
                .status(CarStatus.DOSTEPNY)
                .build();

        CarEntity car2 = CarEntity.builder()
                .id(2L).brand("Ford").model("Focus")
                .pricePerDay(BigDecimal.valueOf(200))
                .status(CarStatus.WYPOZYCZONY)
                .build();

        when(carRepository.findAll()).thenReturn(List.of(car1, car2));

        // when
        List<CarEntity> result = carService.getAllCars();

        // then
        assertThat(result).hasSize(2);
        assertThat(result).extracting(CarEntity::getBrand)
                .containsExactly("Toyota", "Ford");
        verify(carRepository, times(1)).findAll();
    }

    @Test
    void shouldAddCarWithDefaultStatusDostepny() {
        // given
        CarEntity input = CarEntity.builder()
                .brand("Honda").model("Civic")
                .pricePerDay(BigDecimal.valueOf(180))
                .build();

        CarEntity saved = CarEntity.builder()
                .id(1L).brand("Honda").model("Civic")
                .pricePerDay(BigDecimal.valueOf(180))
                .status(CarStatus.DOSTEPNY)
                .build();

        when(carRepository.save(any(CarEntity.class))).thenReturn(saved);

        // when
        CarEntity result = carService.addCar(input);

        // then
        assertThat(result.getStatus()).isEqualTo(CarStatus.DOSTEPNY);
        assertThat(result.getId()).isEqualTo(1L);
        verify(carRepository).save(any(CarEntity.class));
    }

    @Test
    void shouldChangeStatusOfCar() {
        // given
        CarEntity car = CarEntity.builder()
                .id(1L).brand("Toyota").model("Corolla")
                .pricePerDay(BigDecimal.valueOf(150))
                .status(CarStatus.DOSTEPNY)
                .build();

        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(carRepository.save(any(CarEntity.class))).thenReturn(car);

        // when
        CarEntity updated = carService.changeStatus(1L, CarStatus.WYPOZYCZONY);

        // then
        assertThat(updated.getStatus()).isEqualTo(CarStatus.WYPOZYCZONY);
        verify(carRepository).findById(1L);
        verify(carRepository).save(car);
    }

    @Test
    void shouldThrowExceptionWhenCarNotFound() {
        // given
        when(carRepository.findById(99L)).thenReturn(Optional.empty());

        // when + then
        assertThatThrownBy(() -> carService.changeStatus(99L, CarStatus.WYPOZYCZONY))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Car not found with id 99");

        verify(carRepository).findById(99L);
        verify(carRepository, never()).save(any());
    }
}
