package com.portfolio.BaeGoPa.store.controller;

import com.portfolio.BaeGoPa.exception.model.ExceptionApi;
import com.portfolio.BaeGoPa.store.db.StoreEntity;
import com.portfolio.BaeGoPa.store.db.StoreReviewEntity;
import com.portfolio.BaeGoPa.store.model.StoreRequest;
import com.portfolio.BaeGoPa.store.model.StoreReviewRequest;
import com.portfolio.BaeGoPa.store.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stores")
public class StoreController {

    @Autowired
    private StoreService storeService;

    @PostMapping("/register")
    public ExceptionApi<StoreEntity> registerStore(
            @RequestBody StoreRequest storeRequest
    ){
        StoreEntity storeEntity = storeService.registerStore(
                storeRequest.getStoreName(),
                storeRequest.getAddress(),
                storeRequest.getCategory(),
                storeRequest.getPhone(),
                storeRequest.getPhotoUrl()
        );

        ExceptionApi<StoreEntity> registerStore = ExceptionApi.<StoreEntity>builder()
                .resultCode(String.valueOf(HttpStatus.OK.value()))
                .resultMessage(HttpStatus.OK.name())
                .data(storeEntity)
                .build();

        return registerStore;
    }

    @PostMapping("/review/register")
    public ExceptionApi<StoreReviewEntity> registerReview(
            @RequestBody StoreReviewRequest storeReviewRequest
            ){
        StoreReviewEntity storeReviewEntity = storeService.registerReview(
                storeReviewRequest.getStoreId(),
                storeReviewRequest.getScore(),
                storeReviewRequest.getReview()
        );

        ExceptionApi<StoreReviewEntity> registerReview = ExceptionApi.<StoreReviewEntity>builder()
                .resultCode(String.valueOf(HttpStatus.OK.value()))
                .resultMessage(HttpStatus.OK.name())
                .data(storeReviewEntity)
                .build();

        return registerReview;
    }
}
