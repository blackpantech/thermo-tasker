package com.blackpantech.printer_module.infrastructure.printer;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.blackpantech.printer_module.domain.Task;

@DisplayName("Printer Module Task Printer")
public class EpsonTaskPrinterTest {
  EpsonTaskPrinter epsonTaskPrinter;

  String mockPrinterPath = "./src/test/resources/mockUsb/lp3";
  String wrongPrinterPath = "./src/test/resources/mockUsb/lp2";

  @AfterEach
  void clearMockPrinterPath() throws IOException {
    new FileOutputStream(mockPrinterPath).close();
  }

  @Test
  @DisplayName("Should print task successfully")
  void shouldPrintTaskSuccessfully() throws IOException {
    // GIVEN
    Path path = Paths.get(mockPrinterPath);
    long initialSize = Files.size(path);

    epsonTaskPrinter = new EpsonTaskPrinter(mockPrinterPath);
    var task = new Task(UUID.randomUUID(), "Groceries", "Get milk", Instant.now());

    // WHEN
    var result = epsonTaskPrinter.printTask(task);

    // THEN
    assertTrue(result);
    long endSize = Files.size(path);
    assertTrue(endSize > initialSize);
  }

  @Test
  @DisplayName("Should fail to print task")
  void shouldFailToPrintTask() {
    // GIVEN
    epsonTaskPrinter = new EpsonTaskPrinter(wrongPrinterPath);
    var task = new Task(UUID.randomUUID(), "Groceries", "Get milk", Instant.now());

    // WHEN
    var result = epsonTaskPrinter.printTask(task);

    // THEN
    assertFalse(result);
  }
}
