package com.ad.ecom.service;

import com.ad.ecom.common.dto.ResponseMessage;
import com.ad.ecom.support.dto.EscalateNewTicketDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface TicketService {
    ResponseEntity<ResponseMessage> escalate(EscalateNewTicketDTO escalateNewTicketDTO);

    ResponseEntity<ResponseMessage> fetchAllTickets(int pageSize, int pageNum);

    ResponseEntity<ResponseMessage> ticketDetails(String ticketId);
}
