package com.ad.ecom.products.service;

import com.ad.ecom.common.ResponseMessage;
import com.ad.ecom.products.dto.ProductFullInfo;
import com.ad.ecom.products.dto.ProductsFilter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductsService {
    public ResponseEntity<ResponseMessage> showProductsForUser(ProductsFilter filter, int pageSize, int pageNum);
    public ResponseEntity<ResponseMessage> showProductsForOwner(ProductsFilter filter, int pageSize, int pageNum);
    public ResponseEntity<ResponseMessage> addProducts(List<ProductFullInfo> productFullInfoList);
    public ResponseEntity<ResponseMessage> updateProducts(List<ProductFullInfo> productFullInfoList);
    public ResponseEntity<ResponseMessage> removeProducts(List<String> productIDs);
}
