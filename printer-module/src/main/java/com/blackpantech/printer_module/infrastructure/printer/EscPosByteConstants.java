package com.blackpantech.printer_module.infrastructure.printer;

import java.nio.charset.StandardCharsets;

public class EscPosByteConstants {
  // ESC/POS Commands
  public static final byte ESC = 0x1B;
  public static final byte GS = 0x1D;
  public static final byte LF = 0x0A;
  public static final byte[] INIT = {ESC, '@'};
  public static final byte[] LINE_FEED = {LF};
  public static final byte[] CUT_PAPER = {GS, 'V', 66, 0};

  // Text formatting commands
  public static final byte[] BOLD_ON = {ESC, 'E', 1};
  public static final byte[] BOLD_OFF = {ESC, 'E', 0};
  public static final byte[] ALIGN_LEFT = {ESC, 'a', 0};
  public static final byte[] ALIGN_CENTER = {ESC, 'a', 1};
  public static final byte[] ALIGN_RIGHT = {ESC, 'a', 2};
  public static final byte[] SEPARATOR =
      "------------------------------------------------".getBytes(StandardCharsets.UTF_8);
}
