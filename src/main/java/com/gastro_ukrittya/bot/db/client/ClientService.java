package com.gastro_ukrittya.bot.db.client;

import com.gastro_ukrittya.bot.config.ReservationDto;
import com.gastro_ukrittya.bot.db.Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientService implements Mapper<ReservationDto, Client> {
    private final ClientRepository clientRepository;

    public Long addClient(ReservationDto reservationDto) {
        Long id = clientRepository.save(toEntity(reservationDto)).getId();
        log.info("Client success add");
        return id;
    }

    public Client findClientById(Long id) {
        return clientRepository.findById(id).orElseThrow();
    }

    @Override
    public Client toEntity(ReservationDto dto) {
        return Client.builder()
                .id(dto.getChatId())
                .firstName(dto.getName())
                .phoneNumber(dto.getPhoneNumber())
                .build();
    }
}
