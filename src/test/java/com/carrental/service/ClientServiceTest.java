package com.carrental.service;


import com.carrental.dto.ClientDTO;
import com.carrental.model.ClientEntity;
import com.carrental.repository.ClientRepository;
import com.carrental.service.ClientService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ClientServiceTest {

    private final ClientRepository repo = mock(ClientRepository.class);
    private final ClientService service = new ClientService(repo);

    @Test
    void shouldAddClient() {
        ClientDTO dto = new ClientDTO("Jan", "Kowalski", "jan@test.pl");
        ClientEntity client = ClientEntity.builder()
                .id(1L)
                .firstName(dto.firstName())
                .lastName(dto.lastName())
                .email(dto.email())
                .build();

        when(repo.save(Mockito.any(ClientEntity.class))).thenReturn(client);

        ClientEntity saved = service.addClient(dto);

        assertThat(saved.getId()).isEqualTo(1L);
        assertThat(saved.getEmail()).isEqualTo("jan@test.pl");
    }
}