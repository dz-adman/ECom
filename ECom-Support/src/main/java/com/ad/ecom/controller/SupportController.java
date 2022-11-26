package com.ad.ecom.controller;

import com.ad.ecom.common.dto.ResponseMessage;
import com.ad.ecom.service.TicketService;
import com.ad.ecom.support.dto.EscalateNewTicketDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/support")
@Validated
public class SupportController {

    @Autowired
    private TicketService ticketService;

    @RolesAllowed("USER")
    @PostMapping("/escalate")
    public ResponseEntity<ResponseMessage> escalateIssue(@RequestBody EscalateNewTicketDTO newTicketDto) {
        return ticketService.escalate(newTicketDto);
    }

    @RolesAllowed("USER")
    @GetMapping("/fetchAll/{pageSize}/{pageNum}")
    public ResponseEntity<ResponseMessage> fetchAllTickets(@PathVariable("pageSize") int pageSize, @PathVariable("pageNum") int pageNum) {
        return ticketService.fetchAllTickets(pageSize, pageNum);
    }

    @RolesAllowed({"USER", "ADMIN"})
    @GetMapping("/ticket/{ticketId}")
    public ResponseEntity<ResponseMessage> ticketDetails(@PathVariable("ticketId") String ticketId) {
        return ticketService.ticketDetails(ticketId);
    }
}
