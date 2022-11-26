package com.ad.ecom.support.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TicketDetailsList {
    List<TicketDetails> ticketDetailsList = new ArrayList<>();
}
