package com.ad.ecom.support.dto;

import com.ad.ecom.common.dto.EComDate;
import com.ad.ecom.support.stubs.TicketCategory;
import com.ad.ecom.support.stubs.TicketStatus;
import com.ad.ecom.support.stubs.TicketSubCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
public class TicketDetails {
    private String ticketId;
    private TicketCategory category;
    private TicketSubCategory subCategory;
    private EComDate openingDate;
    private EComDate closingDate;
    private List<TicketReplyDetails> replies = new ArrayList<>();
    private TicketStatus status;
}
