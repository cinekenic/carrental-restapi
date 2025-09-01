package com.carrental.service;

import com.carrental.dto.RentalRequestDTO;
import com.carrental.dto.RentalResponseDTO;
import com.carrental.exception.ResourceNotFoundException;
import com.carrental.model.CarEntity;
import com.carrental.model.CarStatus;
import com.carrental.model.ClientEntity;
import com.carrental.model.RentalEntity;
import com.carrental.repository.CarRepository;
import com.carrental.repository.ClientRepository;
import com.carrental.repository.RentalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class RentalServiceTest {

    private RentalRepository rentalRepository;
    private CarRepository carRepository;
    private ClientRepository clientRepository;
    private RentalService rentalService;

    @BeforeEach
    void setUp() {
        rentalRepository = mock(RentalRepository.class);
        carRepository = mock(CarRepository.class);
        clientRepository = mock(ClientRepository.class);
        rentalService = new RentalService(rentalRepository, carRepository, clientRepository);
    }

    @Test
    void shouldRentCarSuccessfully() {
        // given
        RentalRequestDTO request = new RentalRequestDTO(1L, 1L);

        CarEntity car = CarEntity.builder()
                .id(1L).brand("Toyota").model("Corolla")
                .pricePerDay(BigDecimal.valueOf(150))
                .status(CarStatus.DOSTEPNY)
                .build();

        ClientEntity client = ClientEntity.builder()
                .id(1L).firstName("Jan").lastName("Kowalski").email("jan@test.pl")
                .build();

        RentalEntity rental = RentalEntity.builder()
                .id(10L).car(car).client(client)
                .rentalDate(LocalDateTime.now())
                .build();

        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(rentalRepository.save(any(RentalEntity.class))).thenReturn(rental);

        // when
        RentalResponseDTO response = rentalService.rentCar(request);

        // then
        assertThat(response.carId()).isEqualTo(1L);
        assertThat(response.clientId()).isEqualTo(1L);
        assertThat(car.getStatus()).isEqualTo(CarStatus.WYPOZYCZONY);

        verify(carRepository).save(car);
        verify(rentalRepository).save(any(RentalEntity.class));
    }

    @Test
    void shouldThrowWhenCarAlreadyRented() {
        // given
        RentalRequestDTO request = new RentalRequestDTO(1L, 1L);
        CarEntity car = CarEntity.builder().id(1L).status(CarStatus.WYPOZYCZONY).build();

        when(carRepository.findById(1L)).thenReturn(Optional.of(car));

        // expect
        assertThatThrownBy(() -> rentalService.rentCar(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Car is already rented");
    }

    @Test
    void shouldThrowWhenClientNotFound() {
        // given
        RentalRequestDTO request = new RentalRequestDTO(1L, 99L);
        CarEntity car = CarEntity.builder().id(1L).status(CarStatus.DOSTEPNY).build();

        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(clientRepository.findById(99L)).thenReturn(Optional.empty());

        // expect
        assertThatThrownBy(() -> rentalService.rentCar(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Client not found");
    }

    @Test
    void shouldReturnCarSuccessfully() {
        // given
        CarEntity car = CarEntity.builder().id(1L).status(CarStatus.WYPOZYCZONY).build();
        ClientEntity client = ClientEntity.builder().id(1L).email("jan@test.pl").build();
        RentalEntity rental = RentalEntity.builder()
                .id(10L).car(car).client(client)
                .rentalDate(LocalDateTime.now().minusDays(1))
                .build();

        when(rentalRepository.findById(10L)).thenReturn(Optional.of(rental));
        when(rentalRepository.save(any(RentalEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        // when
        RentalResponseDTO response = rentalService.returnCar(10L);

        // then
        assertThat(response.returnDate()).isNotNull();
        assertThat(car.getStatus()).isEqualTo(CarStatus.DOSTEPNY);

        verify(carRepository).save(car);
        verify(rentalRepository).save(rental);
    }

    @Test
    void shouldThrowWhenRentalNotFound() {
        // given
        when(rentalRepository.findById(99L)).thenReturn(Optional.empty());

        // expect
        assertThatThrownBy(() -> rentalService.returnCar(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Rental not found");
    }
}
