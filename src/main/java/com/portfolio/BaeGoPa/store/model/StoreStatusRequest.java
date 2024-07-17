package com.portfolio.BaeGoPa.store.model;

import com.portfolio.BaeGoPa.store.db.StoreStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreStatusRequest {
    private StoreStatus status;
}
