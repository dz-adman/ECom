package com.ad.ecom.support.dto;

import com.ad.ecom.common.dto.EComDate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
public class TicketReplyDetails {
    private long id;
    private String text;
    private EComDate repliedOn;
    private List<TicketReplyAttachmentDetails> attachments = new ArrayList<>();
}
