package com.blackpantech.printer_module.infrastructure.printer;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blackpantech.printer_module.domain.Task;
import com.blackpantech.printer_module.domain.ports.TaskPrinter;

public class EpsonTaskPrinter implements TaskPrinter {

  private final OutputStreamFactory outputStreamFactory;
  private final Logger logger = LoggerFactory.getLogger(EpsonTaskPrinter.class);
  private final Object writeLock = new Object();

  public EpsonTaskPrinter(final OutputStreamFactory outputStreamFactory) {
    this.outputStreamFactory = outputStreamFactory;
  }

  @Override
  public boolean printTask(final Task task) {
    logger.debug(
        "Sending task {} with topic \"{}\", description \"{}\" and due date \"{}\" to be printed.",
        task.id(), task.topic(), task.description(), task.dueDate());
    try (OutputStream out = outputStreamFactory.createOutputStream()) {
      synchronized (writeLock) {
        // Initialize printer
        out.write(EscPosByteConstants.INIT);
        // Print header (centered and bold)
        out.write(EscPosByteConstants.ALIGN_CENTER);
        out.write(EscPosByteConstants.BOLD_ON);
        out.write(task.topic().getBytes(StandardCharsets.UTF_8));
        out.write(EscPosByteConstants.BOLD_OFF);
        out.write(EscPosByteConstants.LINE_FEED);
        out.write(EscPosByteConstants.SEPARATOR);
        // Print content (left aligned)
        out.write(EscPosByteConstants.ALIGN_LEFT);
        out.write(EscPosByteConstants.LINE_FEED);
        out.write(task.description().getBytes(StandardCharsets.UTF_8));
        out.write(EscPosByteConstants.LINE_FEED);
        out.write(EscPosByteConstants.SEPARATOR);
        out.write(EscPosByteConstants.LINE_FEED);
        out.write(EscPosByteConstants.LINE_FEED);
        out.write(EscPosByteConstants.LINE_FEED);
        out.write(EscPosByteConstants.LINE_FEED);
        // Cut paper
        out.write(EscPosByteConstants.CUT_PAPER);
        out.flush();
      }
      return true;
    } catch (final Exception e) {
      logger.error(
          "Error while printing task {} with topic \"{}\", description \"{}\" and due date \"{}\".",
          task.id(), task.topic(), task.description(), task.dueDate(), e);
      return false;
    }
  }
}
