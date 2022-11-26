package com.ad.ecom.support.persistance.entity;

import com.ad.ecom.ecomuser.stubs.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@Entity(name = "ECOM_TICKET_REPLY")
public class TicketReply {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private Role role;

    private String text;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Ticket ticket;

    private Date repliedOn;

    private List<TicketReplyAttachment> attachments;
}
