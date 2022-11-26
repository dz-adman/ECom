package com.ad.ecom.service.impl;

import com.ad.ecom.common.dto.EComDate;
import com.ad.ecom.common.dto.ResponseMessage;
import com.ad.ecom.common.stub.ResponseType;
import com.ad.ecom.core.context.EComUserLoginContext;
import com.ad.ecom.service.TicketService;
import com.ad.ecom.support.dto.*;
import com.ad.ecom.support.persistance.entity.Ticket;
import com.ad.ecom.support.persistance.entity.TicketReply;
import com.ad.ecom.support.persistance.entity.TicketReplyAttachment;
import com.ad.ecom.support.persistance.repository.TicketRepository;
import com.ad.ecom.support.stubs.TicketStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Component
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketRepository ticketRepo;

    @Autowired
    private EComUserLoginContext loginContext;

    @Override
    public ResponseEntity<ResponseMessage> escalate(EscalateNewTicketDTO newTicketDto) {
        ResponseMessage responseMessage = new ResponseMessage();
        Optional<Ticket> ticket = covertToTicket(newTicketDto);
        if(ticket.isEmpty()) {
            responseMessage.addResponse(ResponseType.ERROR, "One or more Attachments");
            return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
        }
        ticketRepo.save(ticket.get());
        responseMessage.addResponse(ResponseType.SUCCESS, "TicketId: " + ticket.get().getId());
        return new ResponseEntity<>(responseMessage, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ResponseMessage> fetchAllTickets(int pageSize, int pageNum) {
        ResponseMessage responseMessage = new ResponseMessage();
        TicketDetailsList ticketDetailsList = new TicketDetailsList();
        Sort sort = Sort.by("openingDate").descending();
        PageRequest pageRequest = PageRequest.of(pageNum, pageSize, sort);
        Page<Ticket> page = ticketRepo.findAllByUserId(loginContext.getUserInfo().getId(), pageRequest);
        if(!page.hasContent())
            responseMessage.addResponse(ResponseType.INFO, "No Tickets Found");
        else {
            List<Ticket> userTickets = page.getContent();
            userTickets.stream()
                    .map(ticket -> convertToTicketDetails(ticket))
                    .forEach(td -> ticketDetailsList.getTicketDetailsList().add(td));
            responseMessage.addResponse(ResponseType.SUCCESS, "Ticket Details");
        }
        responseMessage.setResponseData(ticketDetailsList);
        return new ResponseEntity(responseMessage, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseMessage> ticketDetails(String ticketId) {
        ResponseMessage responseMessage = new ResponseMessage();
        Optional<Ticket> ticket = ticketRepo.findByTicketId(ticketId);
        if(ticket.isEmpty()) {
            responseMessage.addResponse(ResponseType.ERROR, "Invalid ticketId");
            return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
        }
        TicketDetails ticketDetails = convertToTicketDetails(ticket.get());
        responseMessage.addResponse(ResponseType.SUCCESS, "");
        responseMessage.setResponseData(ticketDetails);
        return ResponseEntity.ok(responseMessage);
    }

    private TicketDetails convertToTicketDetails(Ticket ticket) {
        // build from innermost entity to parent i.e. attachments -> replies -> ticket
        List<TicketReplyDetails> replies = new ArrayList<>();
        ticket.getReplies().stream().forEach(reply -> {
            // attachments for this ticket-reply
            List<TicketReplyAttachmentDetails> attachments = new ArrayList<>();
            reply.getAttachments().stream().forEach(attachment -> {
                TicketReplyAttachmentDetails attachmentDetails = TicketReplyAttachmentDetails.builder()
                        .id(attachment.getId())
                        .data(attachment.getData())
                        .build();
                attachments.add(attachmentDetails);
            });
            // replies for this ticket
            TicketReplyDetails replyDetails = TicketReplyDetails.builder()
                    .id(reply.getId())
                    .repliedOn(new EComDate(reply.getRepliedOn()))
                    .text(reply.getText())
                    .attachments(attachments)
                    .build();
            replies.add(replyDetails);
        });
        // ticket details
        TicketDetails ticketDetails = TicketDetails.builder()
                .ticketId(ticket.getTicketId())
                .status(ticket.getTicketStatus())
                .openingDate(new EComDate(ticket.getOpeningDate()))
                .closingDate(ticket.getClosingDate() == null ? new EComDate() : new EComDate(ticket.getClosingDate()))
                .category(ticket.getTicketCategory())
                .subCategory(ticket.getTicketSubCategory())
                .replies(replies)
                .build();

        return ticketDetails;
    }

    private Optional<Ticket> covertToTicket(EscalateNewTicketDTO newTicketDto) {
        Ticket ticket = new Ticket();
        ticket.setTicketId(UUID.randomUUID().toString());
        ticket.setTicketCategory(newTicketDto.getTicketCategory());
        ticket.setTicketSubCategory(newTicketDto.getSubCategory());
        ticket.setTitle(newTicketDto.getTitle());
        ticket.setOpeningDate(new Date(System.currentTimeMillis()));
        ticket.setTicketStatus(TicketStatus.OPEN);

        TicketReply ticketReply = TicketReply.builder()
                .ticket(ticket)
                .repliedOn(new Date(System.currentTimeMillis()))
                .role(loginContext.getUserInfo().getRole())
                .text(newTicketDto.getDescription())
                .build();

        List<TicketReplyAttachment> attachments = new ArrayList<>();
        try {
            if(newTicketDto.getTicketAttachments() != null) {
                for (MultipartFile file : newTicketDto.getTicketAttachments()) {
                    attachments.add(TicketReplyAttachment.builder()
                            .reply(ticketReply)
                            .data(file.getBytes())
                            .build());
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return Optional.empty();
        }

        ticketReply.setAttachments(attachments);
        if(ticket.getReplies() == null) ticket.setReplies(new ArrayList<>());
        ticket.getReplies().add(ticketReply);

        return Optional.of(ticket);
    }

}
