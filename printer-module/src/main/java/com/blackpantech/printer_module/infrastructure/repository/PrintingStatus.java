package com.blackpantech.printer_module.infrastructure.repository;

public enum PrintingStatus {
  PENDING(0),
  SUCCESS(1),
  FAILED(2);

  @SuppressWarnings("unused")
  private final int value;

  PrintingStatus(final int i) {
    value = i;
  }
}
