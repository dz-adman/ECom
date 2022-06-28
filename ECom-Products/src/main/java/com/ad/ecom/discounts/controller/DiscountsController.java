package com.ad.ecom.discounts.controller;

import com.ad.ecom.common.stub.ResponseMessage;
import com.ad.ecom.discounts.dto.DiscountsDto;
import com.ad.ecom.discounts.dto.DiscountsFilter;
import com.ad.ecom.discounts.dto.DiscountsObjDto;
import com.ad.ecom.discounts.service.DiscountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RolesAllowed({"ADMIN", "CUSTOMER"})
@RequestMapping("/discounts")
public class DiscountsController {

    @Autowired
    private DiscountsService discountsService;

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseMessage> addDiscounts(List<DiscountsObjDto> discountObjects) {
        return discountsService.addDiscounts(discountObjects);
    }

    // TODO check the filter json parse error
    @PostMapping(value = "/showAll/{pageSize}/{pageNum}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseMessage> showAllDiscounts(@RequestBody DiscountsFilter filter,
                                                               @PathVariable @Min(value = 1, message = "PageSize too short") @Max(value = 500, message = "Max PageSize limit is 500") int pageSize,
                                                               @PathVariable @Min(value = 0, message = "Invalid PageNUmber") int pageNum) {
        return discountsService.showAllDiscounts(filter, pageSize, pageNum);
    }

    @PatchMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseMessage> updateDiscounts(List<DiscountsDto> discountObjs) {
        return discountsService.updateDiscounts(discountObjs);
    }

    @DeleteMapping(value = "/delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseMessage> deleteDiscounts(List<Long> discountCodes) {
        return discountsService.removeDiscounts(discountCodes);
    }
}
