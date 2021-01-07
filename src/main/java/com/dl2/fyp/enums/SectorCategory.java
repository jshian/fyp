package com.dl2.fyp.enums;


import com.fasterxml.jackson.annotation.JsonValue;

public enum SectorCategory {
  ENERGY("Energy"),
  MATERIALS("Materials"),
  INDUSTRIALS("Industrials"),
  UTILITIES("Utilities"),
  HEALTHCARE("Health Care"),
  FINANCIALS("Financials"),
  CONSUMER_DISCRETIONARY("Consumer Discretionary"),
  CONSUMER_STAPLES("Consumer Staples"),
  INFORMATION_TECHNOLOGY("Information Technology"),
  COMMUNICATION_SERVICES("Communication Services"),
  REAL_ESTATE("Real Estate");

  private final String sector;

  SectorCategory(String sector) {
    this.sector = sector;
  }

  @JsonValue
  public String getSector() {
    return sector;
  }
}
