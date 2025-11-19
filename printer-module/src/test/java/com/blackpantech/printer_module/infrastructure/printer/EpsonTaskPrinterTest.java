package com.blackpantech.printer_module.infrastructure.printer;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.blackpantech.printer_module.domain.Task;

@DisplayName("EpsonTaskPrinter Tests")
class EpsonTaskPrinterTest {

  private EpsonTaskPrinter printer;
  private OutputStreamFactory mockFactory;
  private ByteArrayOutputStream byteArrayOut;

  @BeforeEach
  void setUp() {
    mockFactory = mock(OutputStreamFactory.class);
    byteArrayOut = new ByteArrayOutputStream();
    printer = new EpsonTaskPrinter(mockFactory);
  }

  @AfterEach
  void tearDown() throws IOException {
    byteArrayOut.close();
  }

  @Test
  @DisplayName("Should print task successfully and return true")
  void testPrintTaskSuccess() throws IOException {
    // Given
    when(mockFactory.createOutputStream()).thenReturn(byteArrayOut);
    Task task = new Task(UUID.randomUUID(), "Test Topic", "Test Description", Instant.now());

    // When
    boolean result = printer.printTask(task);

    // Then
    assertTrue(result);
    verify(mockFactory).createOutputStream();
    // Verify bytes were written (should contain init + topic + description + separators)
    assertTrue(byteArrayOut.size() > 0);
    assertTrue(byteArrayOut.toString("UTF-8").contains("Test Topic"));
    assertTrue(byteArrayOut.toString("UTF-8").contains("Test Description"));
  }

  @Test
  @DisplayName("Should return false when IOException occurs during print")
  void testPrintTaskIOException() throws IOException {
    // Given
    when(mockFactory.createOutputStream()).thenThrow(new IOException("Device not found"));
    Task task = new Task(UUID.randomUUID(), "Test Topic", "Test Description", Instant.now());

    // When
    boolean result = printer.printTask(task);

    // Then
    assertFalse(result);
    verify(mockFactory).createOutputStream();
  }

  @Test
  @DisplayName("Should return false when unexpected exception occurs")
  void testPrintTaskUnexpectedException() throws IOException {
    // Given
    when(mockFactory.createOutputStream()).thenThrow(new RuntimeException("Unexpected error"));
    Task task = new Task(UUID.randomUUID(), "Test Topic", "Test Description", Instant.now());

    // When
    boolean result = printer.printTask(task);

    // Then
    assertFalse(result);
  }

  @Test
  @DisplayName("Should serialize concurrent print requests")
  void testConcurrentPrintSerialization() throws IOException, InterruptedException {
    // Given
    when(mockFactory.createOutputStream()).thenReturn(byteArrayOut);
    Task task1 = new Task(UUID.randomUUID(), "Test Topic 1", "Test Description 1", Instant.now());
    Task task2 = new Task(UUID.randomUUID(), "Test Topic 2", "Test Description 2", Instant.now());

    // When
    Thread t1 = new Thread(() -> printer.printTask(task1));
    Thread t2 = new Thread(() -> printer.printTask(task2));

    t1.start();
    t2.start();
    t1.join();
    t2.join();

    // Then — both prints succeed and output is written
    assertTrue(byteArrayOut.size() > 0);
    verify(mockFactory, times(2)).createOutputStream();
    assertTrue(byteArrayOut.toString("UTF-8").contains("Test Topic 1"));
    assertTrue(byteArrayOut.toString("UTF-8").contains("Test Description 1"));
    assertTrue(byteArrayOut.toString("UTF-8").contains("Test Topic 2"));
    assertTrue(byteArrayOut.toString("UTF-8").contains("Test Description 2"));
  }
}
