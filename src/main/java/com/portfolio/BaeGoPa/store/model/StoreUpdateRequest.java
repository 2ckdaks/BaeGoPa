package com.portfolio.BaeGoPa.store.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreUpdateRequest {
    private String storeName;
    private String address;
    private String category;
    private String phone;
    private String photoUrl;
}
