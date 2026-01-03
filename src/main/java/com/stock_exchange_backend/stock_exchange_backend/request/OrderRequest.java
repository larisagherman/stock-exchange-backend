package com.stock_exchange_backend.stock_exchange_backend.request;

import com.stock_exchange_backend.stock_exchange_backend.entity.OrderType;
import lombok.Data;

@Data
public class OrderRequest {
    private String userId;
    private String ticker;
    private int quantity;
    private double pricePerShare;
    private OrderType type;

    @Override
    public String toString() {
        return "OrderRequest{" +
                "userId='" + userId + '\'' +
                ", ticker='" + ticker + '\'' +
                ", noOfShares=" + quantity +
                ", pricePerShare=" + pricePerShare +
                ", type=" + type +
                '}';
    }
}
