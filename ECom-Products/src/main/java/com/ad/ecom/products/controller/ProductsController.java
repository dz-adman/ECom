package com.ad.ecom.products.controller;

import com.ad.ecom.common.ResponseMessage;
import com.ad.ecom.products.dto.ProductFullInfo;
import com.ad.ecom.products.dto.ProductsFilter;
import com.ad.ecom.products.service.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductsService productsService;

    @RolesAllowed("USER")
    @PostMapping(value = "/user/showAll/{pageSize}/{pageNum}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseMessage> fetchProductsForUser(@RequestBody ProductsFilter filter,
                                                                 @PathVariable @Min(value = 1, message = "PageSize too short") @Max(value = 500, message = "Max PageSize limit is 500") int pageSize,
                                                                 @PathVariable @Min(value = 0, message = "Invalid PageNUmber") int pageNum) {
        return productsService.showProductsForUser(filter, pageSize, pageNum);
    }

    @RolesAllowed({"ADMIN", "SELLER"})
    @PostMapping(value = "/owner/showAll/{pageSize}/{pageNum}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseMessage> fetchProductsForOwners(@RequestBody ProductsFilter filter,
                                                                 @PathVariable @Min(value = 1, message = "PageSize too short") @Max(value = 500, message = "Max PageSize limit is 500") int pageSize,
                                                                 @PathVariable @Min(value = 0, message = "Invalid PageNUmber") int pageNum) throws ClassNotFoundException {
        return productsService.showProductsForOwner(filter, pageSize, pageNum);
    }

    @RolesAllowed({"ADMIN", "SELLER"})
    @PostMapping(value = "/owner/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseMessage> addProducts(@Valid @RequestBody List<ProductFullInfo> productFullInfoList) {
        return productsService.addProducts(productFullInfoList);
    }

    @RolesAllowed({"ADMIN", "SELLER"})
    @PatchMapping(value = "/owner/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseMessage> editProducts(@RequestBody List<ProductFullInfo> productList) {
        return productsService.updateProducts(productList);
    }

    @RolesAllowed({"ADMIN", "SELLER"})
    @DeleteMapping(value = "/owner/remove", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseMessage> removeProducts(@RequestBody List<String> productIDs) {
        return productsService.removeProducts(productIDs);
    }

}
