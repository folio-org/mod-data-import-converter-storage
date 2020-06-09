package org.folio.services.util;

public enum EntityTypes {

  INVENTORY_HOLDINGS("INVENTORY_HOLDINGS"),
  INVENTORY_INSTANCE("INVENTORY_INSTANCE"),
  INVENTORY_ITEM("INVENTORY_ITEM"),
  INVOICE("INVOICE"),
  MARC_AUTHORITY_RECORD("MARC_AUTHORITY_RECORD"),
  MARC_BIB_RECORD("MARC_BIB_RECORD"),
  MARC_HOLDINGS_RECORD("MARC_HOLDINGS_RECORD"),
  ORDER("ORDER"),
  ERROR("ERROR");

  private final String name;

  EntityTypes(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
