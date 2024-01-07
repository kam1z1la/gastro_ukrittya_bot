package com.gastro_ukrittya.bot.db.client;

import com.gastro_ukrittya.bot.dto.ReservationDto;
import com.gastro_ukrittya.bot.db.Mapper;
import com.gastro_ukrittya.bot.handler.reservation.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientService implements Mapper<ReservationDto, Client> {
    private final ClientRepository clientRepository;
    private final ValidationService validation;

    @Transactional
    public Client addClient(ReservationDto reservationDto) {
        Client client = toEntity(reservationDto);
        log.info("Client success add");
        return clientRepository.save(client);
    }

    public void updateClientById(ReservationDto dto) {
        Client client = toEntity(dto);
        clientRepository.updateClientById(client);
    }

    @Override
    public Client toEntity(ReservationDto dto) {
        return Client.builder()
                .id(dto.getChatId())
                .firstName(dto.getName())
                .phoneNumber(validation.getPhoneNumber(dto.getPhoneNumber()))
                .build();
    }
}
