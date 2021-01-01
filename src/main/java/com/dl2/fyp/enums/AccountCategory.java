package com.dl2.fyp.enums;


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

    public String getCategory() {
        return category;
    }
}
