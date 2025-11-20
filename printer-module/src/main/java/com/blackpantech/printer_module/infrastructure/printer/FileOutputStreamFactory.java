package com.blackpantech.printer_module.infrastructure.printer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileOutputStreamFactory implements OutputStreamFactory {

  private final String printerPath;
  private final Logger logger = LoggerFactory.getLogger(FileOutputStreamFactory.class);

  public FileOutputStreamFactory(String printerPath) {
    this.printerPath = printerPath;
    validatePrinterPath();
  }

  @Override
  public OutputStream createOutputStream() throws IOException {
    File printerFile = new File(printerPath);

    if (!printerFile.exists()) {
      throw new IOException("Printer device not found at: " + printerPath);
    }

    if (!printerFile.canWrite()) {
      throw new IOException(
          "Printer device can not be written to. Check permissions for: " + printerPath);
    }

    return new FileOutputStream(printerFile);
  }

  private void validatePrinterPath() {
    try {
      Path p = Path.of(printerPath);
      if (!Files.exists(p)) {
        logger.warn("Configured printer path does not exist: {}", printerPath);
      } else if (!Files.isWritable(p)) {
        logger.warn("Configured printer path is not writable: {}", printerPath);
      } else if (Files.isRegularFile(p)) {
        logger.info("Configured printer path is a regular file (OK for tests): {}", printerPath);
      }
    } catch (Exception ex) {
      logger.debug("Failed to validate printer path {}: {}", printerPath, ex.toString());
    }
  }
}
