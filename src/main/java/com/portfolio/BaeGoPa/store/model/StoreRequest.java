package com.portfolio.BaeGoPa.store.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreRequest {
    private String storeName;
    private String address;
    private String phone;
    private String category;
    private String photoUrl;
}
