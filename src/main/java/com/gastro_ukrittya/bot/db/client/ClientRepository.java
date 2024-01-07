package com.gastro_ukrittya.bot.db.client;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    @Modifying
    @Transactional
    @Query(value = """
        update Client c set first_name = :#{#client.firstName},
        phone_number = :#{#client.phoneNumber}
        where c.id = :#{#client.id}
        """, nativeQuery = true)
    void updateClientById(@Param("client") Client client);
}
