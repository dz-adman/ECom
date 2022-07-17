package com.ad.ecom.products.service.impl;

import com.ad.ecom.common.ResponseMessage;
import com.ad.ecom.common.stub.ResponseType;
import com.ad.ecom.core.context.EComUserLoginContext;
import com.ad.ecom.discounts.repository.DiscountRepository;
import com.ad.ecom.discounts.repository.DiscountSubscriptionRepository;
import com.ad.ecom.ecomuser.persistence.EcomUser;
import com.ad.ecom.products.dto.ProductDto;
import com.ad.ecom.products.dto.ProductFilter;
import com.ad.ecom.products.dto.ProductFullInfo;
import com.ad.ecom.products.persistence.Product;
import com.ad.ecom.products.persistence.QProduct;
import com.ad.ecom.products.repository.ProductRepository;
import com.ad.ecom.products.service.ProductsService;
import com.ad.ecom.products.stubs.ProductStatus;
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
public class ProductsServiceImpl implements ProductsService {

    private final Logger LOGGER = LogManager.getLogger(ProductsServiceImpl.class);

    @Autowired
    private ProductRepository productsRepo;
    @Autowired
    private EComUserLoginContext loginContext;
    @Autowired
    private DiscountSubscriptionRepository discountSubsRepo;
    @Autowired
    private DiscountRepository discountsRepo;


    @Override
    public ResponseEntity<ResponseMessage> showProductsForUser(ProductFilter filter, int pageSize, int pageNum) {
        List<ProductDto> productsForUser = new ArrayList<>();
        List<Product> products = this.fetchFilteredProducts(filter, pageSize, pageNum).toList();
        if(products != null)
            productsForUser = this.convertToProductListForUser(products);
        ResponseMessage respMsg = new ResponseMessage();
        respMsg.setResponseData(productsForUser);
        return new ResponseEntity(respMsg, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseMessage> showProductsForOwner(ProductFilter filter, int pageSize, int pageNum) {
        List<ProductFullInfo> productsForOwner = new ArrayList<>();
        List<Product> products = this.fetchFilteredProducts(filter, pageSize, pageNum).toList();
        if(products != null)
            productsForOwner = this.convertToProductListForOwner(products, loginContext.getUserInfo());
        ResponseMessage respMsg = new ResponseMessage();
        respMsg.setResponseData(productsForOwner);
        return new ResponseEntity(respMsg, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseMessage> addProducts(List<ProductFullInfo> productFullInfoList) {
        ResponseMessage responseMessage = new ResponseMessage();
        List<Product> products;
        if(productFullInfoList != null) {
            try {
                products = this.convertToProductList(productFullInfoList);
                if (!products.isEmpty()) {
                    for (Product product : products) {
                        productsRepo.save(product);
                    }
                } else {
                    responseMessage.addResponse(ResponseType.ERROR, "No Products to Add");
                    return new ResponseEntity(responseMessage, HttpStatus.BAD_REQUEST);
                }
            } catch (Exception ex) {
                LOGGER.error("FAILED to ADD PRODUCTS [" + ex.getMessage() + "]");
                ex.printStackTrace();
                responseMessage.addResponse(ResponseType.ERROR, "Products Addition FAILED [Some Error Occurred]");
                return new ResponseEntity(responseMessage, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            responseMessage.addResponse(ResponseType.SUCCESS, "Products Addition SUCCESSFUL");
            return new ResponseEntity(responseMessage, HttpStatus.CREATED);
        }
        responseMessage.addResponse(ResponseType.ERROR, "No Product to Add");
        return new ResponseEntity(responseMessage, HttpStatus.BAD_REQUEST);
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseMessage> updateProducts(List<ProductFullInfo> productFullInfoList) {
        ResponseMessage responseMessage = new ResponseMessage();
        if(productFullInfoList != null) {
            try {
                List<Product> productsToEdit = loadProductsToUpdate(productFullInfoList);
                List<Product> products = this.convertToProductList(productFullInfoList);
                if (!products.isEmpty()) {
                    for (Product product : products) productsRepo.save(product);
                }
                responseMessage.addResponse(ResponseType.SUCCESS, "Products Update SUCCESSFUL");
                return  new ResponseEntity(responseMessage, HttpStatus.OK);
            } catch (Exception ex) {
                LOGGER.error("FAILED to EDIT PRODUCTS [" + ex.getMessage() + "]");
                ex.printStackTrace();
                responseMessage.addResponse(ResponseType.ERROR, "Products Update FAILED [Some Error Occurred]");
                return new ResponseEntity(responseMessage , HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        responseMessage.addResponse(ResponseType.ERROR, "No Product to Edit");
        return new ResponseEntity(responseMessage, HttpStatus.BAD_REQUEST);
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseMessage> removeProducts(List<String> productIDs) {
        ResponseMessage responseMessage = new ResponseMessage();
        if(productIDs != null) {
            if(productIDs.stream().anyMatch(p -> productsRepo.findByProductId(p).get().getProductOwnerId() != loginContext.getUserInfo().getId()))
                return new ResponseEntity("You're not owner of one or more products that you're trying to delete.", HttpStatus.BAD_REQUEST);
            try {
                for (String pId : productIDs) productsRepo.deleteByProductId(pId);
                responseMessage.addResponse(ResponseType.SUCCESS, "Products Deleted SUCCESSFULLY");
                return new ResponseEntity(responseMessage, HttpStatus.OK);
            } catch (Exception ex) {
                LOGGER.error("FAILED to DELETE PRODUCTS [" + ex.getMessage() + "]");
                ex.printStackTrace();
                responseMessage.addResponse(ResponseType.ERROR, "Products Deletion FAILED [Some Error Occurred]");
                return new ResponseEntity(responseMessage, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        responseMessage.addResponse(ResponseType.ERROR,"No Product to Delete");
        return new ResponseEntity(responseMessage, HttpStatus.BAD_REQUEST);
    }

    private Page<Product> fetchFilteredProducts(ProductFilter filter, int pageSize, int pageNum) {
        Page<Product> products;
        if(filter != null) {
            QProduct qProducts = QProduct.product;
            BooleanExpression brands = Optional.ofNullable(filter.getBrands()).isPresent() ? qProducts.brand.in(filter.getBrands()) : qProducts.brand.isNotNull();
            BooleanExpression categories = Optional.ofNullable(filter.getProductCategories()).isPresent() ?
                                           qProducts.category.in(filter.getProductCategories()) : qProducts.category.isNotNull();
            BooleanExpression subCategories = Optional.ofNullable(filter.getProductSubCategories()).isPresent() ?
                                              qProducts.subCategory.in(filter.getProductSubCategories()) : qProducts.subCategory.isNotNull();
            BooleanExpression priceRange = Optional.ofNullable(filter.getPriceRange()).isPresent() &&
                                                   Optional.ofNullable(filter.getPriceRange().getLowerBound()).isPresent() &&
                                                   Optional.ofNullable(filter.getPriceRange().getUpperBound()).isPresent() ?
                                           qProducts.price.between(filter.getPriceRange().getLowerBound(), filter.getPriceRange().getUpperBound()) : qProducts.price.isNotNull();
            BooleanExpression name = Optional.ofNullable(filter.getProductName()).isPresent() ? qProducts.name.eq(filter.getProductName()) : qProducts.name.isNotNull();

            Predicate criteria = name.and(brands).and(categories).and(subCategories).and(priceRange);
            PageRequest pageRequest = PageRequest.of(pageNum, pageSize);
            products = productsRepo.findAll(criteria, pageRequest);
        } else
            products = productsRepo.findAll(PageRequest.of(pageNum, pageSize));
        return products;
    }

    private List<ProductDto> convertToProductListForUser(List<Product> products) {
        List<ProductDto> productDtoList = new ArrayList<>();
        for (Product prdct : products) {
            if (prdct.getStatus().equals(ProductStatus.ACTIVE)) {
                double effectivePrice = prdct.getDiscountOnProduct(discountSubsRepo);
                ProductDto productDto = new ProductDto();
                productDto.setProductId(prdct.getProductId());
                productDto.setName(prdct.getName());
                productDto.setCategory(prdct.getCategory());
                productDto.setSubCategory(prdct.getSubCategory());
                productDto.setBrand(prdct.getBrand());
                productDto.setPrice(prdct.getPrice());
                productDto.setEffectivePrice(effectivePrice);
                productDto.setInStock(prdct.getStock() > 0);
                productDto.setStockUnit(prdct.getStockUnit());
                productDtoList.add(productDto);
            }
        }
        return productDtoList;
    }

    private List<ProductFullInfo> convertToProductListForOwner(List<Product> products, EcomUser user) {
        List<ProductFullInfo> productFullInfoList = new ArrayList<>();
        for (Product prdct : products) {
            ProductFullInfo product = new ProductFullInfo();
            if (prdct.getProductOwnerId() == user.getId()) {
                product.setProductId(prdct.getProductId());
                product.setName(prdct.getName());
                product.setCategory(prdct.getCategory());
                product.setSubCategory(prdct.getSubCategory());
                product.setBrand(prdct.getBrand());
                product.setPrice(prdct.getPrice());
                product.setStock(prdct.getStock());
                product.setStockUnit(prdct.getStockUnit());
                product.setStatus(prdct.getStatus());
                productFullInfoList.add(product);
            }
        }
        return productFullInfoList;
    }

    private List<Product> convertToProductList(List<ProductFullInfo> productFullInfoList) {
        List<Product> products = new ArrayList<>();
        if (productFullInfoList != null) {
            for (ProductFullInfo prdct : productFullInfoList) {
                Product product = new Product();
                product.setName(prdct.getName());
                product.setCategory(prdct.getCategory());
                product.setSubCategory(prdct.getSubCategory());
                product.setBrand(prdct.getBrand());
                product.setPrice(prdct.getPrice());
                product.setStock(prdct.getStock());
                product.setStockUnit(prdct.getStockUnit());
                products.add(product);
            }
        }
        return products;
    }

    private List<Product> loadProductsToUpdate(List<ProductFullInfo> productFullInfoList) {
        List<Product> products = new ArrayList<>();
        for(ProductFullInfo prdct : productFullInfoList) {
            Optional<Product> product = productsRepo.findByProductId(prdct.getProductId());
            if(product.isPresent()) products.add(product.get());
        }
        return products;
    }

}
