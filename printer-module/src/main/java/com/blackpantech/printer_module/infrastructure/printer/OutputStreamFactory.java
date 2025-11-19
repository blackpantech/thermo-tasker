package com.blackpantech.printer_module.infrastructure.printer;

import java.io.IOException;
import java.io.OutputStream;

public interface OutputStreamFactory {
  OutputStream createOutputStream() throws IOException;
}
