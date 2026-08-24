package com.bstek.urule.console.cache.packet;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.apache.commons.io.IOUtils;

public class ExceptionUtils {
   public static String buildExceptionStack(Throwable throwable) {
      StringBuilder stringBuilder = new StringBuilder();
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      PrintStream printStream = new PrintStream(byteArrayOutputStream);
      throwable.printStackTrace(printStream);
      String string = new String(byteArrayOutputStream.toByteArray());
      IOUtils.closeQuietly(printStream);
      IOUtils.closeQuietly(byteArrayOutputStream);
      string = string.replaceAll("\n", "<br>");
      if (stringBuilder.length() > 0) {
         stringBuilder.append("<br>");
      }

      stringBuilder.append(string);
      return stringBuilder.toString();
   }
}
