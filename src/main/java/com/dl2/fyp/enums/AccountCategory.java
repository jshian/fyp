package com.dl2.fyp.enums;


import com.fasterxml.jackson.annotation.JsonValue;

public enum AccountCategory {
    CASH("Cash"),
    STOCK("Stock"),
    ETF("ETF"),
    BOND("Bond"),
    INVESTMENT_FUND("Invesment_fund");
    private final String category;
    AccountCategory(String category) {
        this.category = category;
    }

    @JsonValue
    public String getCategory() {
        return category;
    }
}
