package com.ad.ecom.support.dto;

import com.ad.ecom.support.stubs.TicketCategory;
import com.ad.ecom.support.stubs.TicketSubCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EscalateNewTicketDTO {
    @NotNull
    private TicketCategory ticketCategory;
    @NotNull
    private TicketSubCategory subCategory;
    @NotNull @NotBlank @NotEmpty
    private String title;
    @NotNull @NotBlank @NotEmpty
    private String description;
    @NotNull @NotBlank @NotEmpty
    private String message;
    private List<MultipartFile> ticketAttachments = new ArrayList<>();
}
