package com.ad.ecom.discounts.service;

import com.ad.ecom.common.dto.ResponseMessage;
import com.ad.ecom.discounts.dto.DiscountsDto;
import com.ad.ecom.discounts.dto.DiscountsFilter;
import com.ad.ecom.discounts.dto.DiscountsObjDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DiscountsService {
    public ResponseEntity<ResponseMessage> addDiscounts(List<DiscountsObjDto> discountObjects);
    public ResponseEntity<ResponseMessage> showAllDiscounts(DiscountsFilter filter, int pageSize, int pageNum);
    public ResponseEntity<ResponseMessage> updateDiscounts(List<DiscountsDto> discountObjects);
    public ResponseEntity<ResponseMessage> removeDiscounts(List<Long> discountCodes);
}
