package com.ad.ecom.discounts.service.impl;

import com.ad.ecom.common.ResponseMessage;
import com.ad.ecom.common.stub.ResponseType;
import com.ad.ecom.discounts.dto.DiscountsDto;
import com.ad.ecom.discounts.dto.DiscountsFilter;
import com.ad.ecom.discounts.dto.DiscountsObjDto;
import com.ad.ecom.discounts.persistance.Discount;
import com.ad.ecom.discounts.persistance.QDiscount;
import com.ad.ecom.discounts.repository.DiscountRepository;
import com.ad.ecom.discounts.service.DiscountsService;
import com.ad.ecom.discounts.stubs.DiscountStatus;
import com.ad.ecom.util.DateConverter;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class DiscountsServiceImpl implements DiscountsService {

    private final Logger LOGGER = LogManager.getLogger(DiscountsServiceImpl.class);

    @Autowired
    private DiscountRepository discountsRepo;

    @Override
    public ResponseEntity<ResponseMessage> addDiscounts(List<DiscountsObjDto> discountObjects) {
        ResponseMessage responseMessage = new ResponseMessage();
        if(!discountObjects.isEmpty()) {
            List<Discount> discounts = convertToDiscountsList(discountObjects);
            for(Discount d : discounts)    discountsRepo.save(d);
        }
        responseMessage.addResponse(ResponseType.SUCCESS, "Discounts Saved Successfully");
        return new ResponseEntity(responseMessage, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ResponseMessage> showAllDiscounts(DiscountsFilter filter, int pageSize, int pageNum) {
        ResponseMessage responseMessage = new ResponseMessage();
        Page<Discount> discountsPage = fetchFilteredDiscounts(filter, pageSize, pageNum);
        List<DiscountsDto> discounts = convertToDiscountsDtoList(discountsPage);
        responseMessage.setResponseData(discounts);
        return new ResponseEntity(responseMessage, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseMessage> updateDiscounts(List<DiscountsDto> discountObjects) {
        ResponseMessage responseMessage = new ResponseMessage();
        if(!discountObjects.isEmpty()) {
            for(DiscountsDto ddto : discountObjects) {
                Discount discount = discountsRepo.findByCode(ddto.getCode());
                updateDiscount(discount, ddto);
                discountsRepo.save(discount);
            }
        }
        responseMessage.addResponse(ResponseType.SUCCESS, "Discounts updated Successfully");
        return new ResponseEntity(responseMessage, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseMessage> removeDiscounts(List<Long> discountCodes) {
        ResponseMessage responseMessage = new ResponseMessage();
        if(!discountCodes.isEmpty()) {
            for(Long code : discountCodes) {
                try {
                    discountsRepo.deleteByCode(code);
                } catch (Exception ex) {
                    LOGGER.error("Failed to delete discount [" + ex.getMessage() + "]");
                }
            }
            responseMessage.addResponse(ResponseType.SUCCESS, "Discounts deleted Successfully");
            return new ResponseEntity(responseMessage, HttpStatus.OK);
        }
        responseMessage.addResponse(ResponseType.ERROR, "No Discounts to delete");
        return new ResponseEntity(responseMessage, HttpStatus.BAD_REQUEST);
    }

    private Page<Discount> fetchFilteredDiscounts(DiscountsFilter filter, int pageSize, int pageNum) {
        Page<Discount> discounts;
        if(filter != null) {
            QDiscount qDiscounts = QDiscount.discount;

            BooleanExpression discountCodes = Optional.ofNullable(filter.getDiscountCodes()).isPresent() ? qDiscounts.code.in(filter.getDiscountCodes()) : qDiscounts.code.isNotNull();
            BooleanExpression discountNames = Optional.ofNullable(filter.getDiscountNames()).isPresent() ? qDiscounts.name.in(filter.getDiscountNames()) : qDiscounts.name.isNotNull();
            BooleanExpression validFromRange = Optional.ofNullable(filter.getValidFromRange()).isPresent() ?
                                               qDiscounts.validFrom.between(DateConverter.convertToDate(filter.getValidFromRange().getLowerBound()),
                                                                            DateConverter.convertToDate(filter.getValidFromRange().getUpperBound())) :
                                               qDiscounts.validFrom.isNotNull();
            BooleanExpression validToRange = Optional.ofNullable(filter.getValidToRange()).isPresent() ?
                                             qDiscounts.validTo.between(DateConverter.convertToDate(filter.getValidToRange().getLowerBound()),
                                                                        DateConverter.convertToDate(filter.getValidToRange().getUpperBound())) :
                                             qDiscounts.validTo.isNotNull();
            BooleanExpression discountPercentageRange = Optional.ofNullable(filter.getDiscountPercentageRange()).isPresent() ?
                                                        qDiscounts.percentageValue.between(filter.getDiscountPercentageRange().getLowerBound(),
                                                                                           filter.getDiscountPercentageRange().getUpperBound()) :
                                                        qDiscounts.percentageValue.isNotNull();
            BooleanExpression discountStatus = Optional.ofNullable(filter.getDiscountStatus()).isPresent() ? qDiscounts.status.eq(filter.getDiscountStatus()) : qDiscounts.status.in(DiscountStatus.values());

            Predicate criteria = discountCodes.and(discountNames).and(validFromRange).and(validToRange).and(discountPercentageRange).and(discountStatus);
            PageRequest pageRequest = PageRequest.of(pageNum, pageSize);
            discounts = discountsRepo.findAll(criteria, pageRequest);
        } else
            discounts = discountsRepo.findAll(PageRequest.of(pageNum, pageSize));
        return discounts;
    }

    private List<Discount> convertToDiscountsList(List<DiscountsObjDto> discountObjects) {
        List<Discount> discounts = new ArrayList<>();
        for(DiscountsObjDto ddto : discountObjects) {
            Discount discount = new Discount();
            discount.setName(ddto.getName());
            discount.setValidFrom(DateConverter.convertToDate(ddto.getValidFrom()));
            discount.setValidTo(DateConverter.convertToDate(ddto.getValidTo()));
            discount.setStatus(ddto.getStatus() != null ? ddto.getStatus() : DiscountStatus.ACTIVE);
            discounts.add(discount);
        }
        return discounts;
    }

    private List<DiscountsDto> convertToDiscountsDtoList(Page<Discount> discountsPage) {
        List<DiscountsDto> discounts = new ArrayList<>();
        if(discountsPage.getNumberOfElements() > 0) {
            for(Discount d : discountsPage) {
                DiscountsDto ddto = new DiscountsDto();
                ddto.setCode(d.getCode());
                ddto.setName(d.getName());
                ddto.setPercentageValue(d.getPercentageValue());
                ddto.setValidFrom(DateConverter.convertToECcmDate(d.getValidFrom()));
                ddto.setValidTo(DateConverter.convertToECcmDate(d.getValidTo()));
                ddto.setStatus(d.getStatus());
                discounts.add(ddto);
            }
        }
        return discounts;
    }

    private void updateDiscount(Discount discount, DiscountsDto ddto) {
        discount.setName(ddto.getName());
        discount.setPercentageValue(ddto.getPercentageValue());
        discount.setValidFrom(DateConverter.convertToDate(ddto.getValidFrom()));
        discount.setValidTo(DateConverter.convertToDate(ddto.getValidTo()));
        discount.setStatus(ddto.getStatus());
    }
}
