package com.ad.ecom.support.persistance.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "ECOM_TICKET_REPLY_ATTACHMENT")
public class TicketReplyAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.NONE)
    public long id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    public TicketReply reply;

    @Lob
    private byte[] data;
}
