package com.ad.ecom.support.persistance.repository;

import com.ad.ecom.support.persistance.entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long>, PagingAndSortingRepository<Ticket, Long> {
    Page findAllByUserId(long userId, PageRequest pageRequest);

    Optional<Ticket> findByTicketId(String ticketId);
}
