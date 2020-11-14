package com.dl2.fyp.enums;

public enum SectorCategory {
  ENERGY("Energy"),
  MATERIALS("Materials"),
  INDUSTRIALS("Industrials"),
  UTILITIES("Industrials"),
  HEALTHCARE("Industrials"),
  FINANCIALS("Industrials"),
  CONSUMER_DISCRETIONARY("Industrials"),
  CONSUMER_STAPLES("Industrials"),
  INFORMATION_TECHNOLOGY("Industrials"),
  COMMUNICATION_SERVICES("Industrials"),
  REAL_ESTATE("Industrials");

  public final String sector;

  private SectorCategory(String sector) {
    this.sector = sector;
  }
}
