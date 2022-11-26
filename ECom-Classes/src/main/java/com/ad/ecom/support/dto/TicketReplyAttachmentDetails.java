package com.ad.ecom.support.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TicketReplyAttachmentDetails {
    private long id;
    private byte[] data;
}
