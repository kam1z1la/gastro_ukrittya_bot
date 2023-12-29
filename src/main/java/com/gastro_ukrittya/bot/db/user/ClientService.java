package com.gastro_ukrittya.bot.db.user;

import com.gastro_ukrittya.bot.config.Order;
import com.gastro_ukrittya.bot.db.Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientService implements Mapper<Order, Client> {
    private final ClientRepository clientRepository;

    public void addClient(Order order) {
        clientRepository.save(toEntity(order));
        log.info("Client success add");
    }

    @Override
    public Client toEntity(Order dto) {
        return Client.builder()
                .id(dto.getChatId())
                .firstName(dto.getName())
                .phoneNumber(dto.getPhoneNumber())
                .build();
    }
}
