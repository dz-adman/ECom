package com.ad.ecom.support.persistance.entity;

import com.ad.ecom.support.stubs.TicketCategory;
import com.ad.ecom.support.stubs.TicketStatus;
import com.ad.ecom.support.stubs.TicketSubCategory;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "ECOM_TICKET")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.NONE)
    private long id;

    @Column(unique = true)
    private String ticketId;

    @Column(nullable = false)
    private long userId;

    @Column(nullable = false)
    private Date openingDate;

    private Date closingDate;

    @Column(nullable = false)
    private TicketCategory ticketCategory;

    @Column(nullable = false)
    private TicketSubCategory ticketSubCategory;

    private String title;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    private List<TicketReply> replies = new ArrayList<>();

    private TicketStatus ticketStatus;
}
