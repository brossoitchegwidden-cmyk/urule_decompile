package com.bstek.urule.console.cache.packet;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.apache.commons.io.IOUtils;

public class ExceptionUtils {
   public static String buildExceptionStack(Throwable var0) {
      StringBuilder var1 = new StringBuilder();
      ByteArrayOutputStream var2 = new ByteArrayOutputStream();
      PrintStream var3 = new PrintStream(var2);
      var0.printStackTrace(var3);
      String var4 = new String(var2.toByteArray());
      IOUtils.closeQuietly(var3);
      IOUtils.closeQuietly(var2);
      var4 = var4.replaceAll("\n", "<br>");
      if (var1.length() > 0) {
         var1.append("<br>");
      }

      var1.append(var4);
      return var1.toString();
   }
}
