package org.bitchouse.ideastock.misc;

import java.awt.EventQueue;
import java.util.Formatter;

/**
 * Created by bitchouse
 */
public class FormatUtil {

  private static StringBuilder mBuilder = new StringBuilder();
  private static Formatter mFormatter = new Formatter(mBuilder);

  public static String formatFloat(String format, float input) {
    assert EventQueue.isDispatchThread();
    mBuilder.setLength(0);
    return mFormatter.format(format, input).toString();
  }
}
