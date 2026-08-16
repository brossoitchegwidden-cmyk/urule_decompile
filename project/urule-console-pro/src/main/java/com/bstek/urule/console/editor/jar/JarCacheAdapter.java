package com.bstek.urule.console.editor.jar;

import com.bstek.urule.console.database.model.UrlType;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import org.apache.commons.io.IOUtils;

public abstract class JarCacheAdapter {
   public static String BEAN_ID = "urule.jarCacheAdapter";

   public abstract List loadDynamicJars(String var1, UrlType var2) throws Exception;

   protected String a(Throwable var1) {
      StringBuilder var2 = new StringBuilder();
      ByteArrayOutputStream var3 = new ByteArrayOutputStream();
      PrintStream var4 = new PrintStream(var3);
      var1.printStackTrace(var4);
      String var5 = new String(var3.toByteArray());
      IOUtils.closeQuietly(var4);
      IOUtils.closeQuietly(var3);
      var5 = var5.replaceAll("\n", "<br>");
      if (var2.length() > 0) {
         var2.append("<br>");
      }

      var2.append(var5);
      return var2.toString();
   }
}
