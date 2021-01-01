package com.dl2.fyp.enums;


public enum SectorCategory {
  ENERGY("Energy"),
  MATERIALS("Materials"),
  INDUSTRIALS("Industrials"),
  UTILITIES("Utilties"),
  HEALTHCARE("Healthcare"),
  FINANCIALS("Financials"),
  CONSUMER_DISCRETIONARY("ConsumerDiscretionary"),
  CONSUMER_STAPLES("ConsumerStaples"),
  INFORMATION_TECHNOLOGY("InformationTechnology"),
  COMMUNICATION_SERVICES("CommunicationServices"),
  REAL_ESTATE("RealEstate");

  private final String sector;

  SectorCategory(String sector) {
    this.sector = sector;
  }

  public String getSector() {
    return sector;
  }
}
