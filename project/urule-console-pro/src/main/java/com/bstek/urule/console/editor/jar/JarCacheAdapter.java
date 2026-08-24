package com.bstek.urule.console.editor.jar;

import com.bstek.urule.console.database.model.UrlType;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import org.apache.commons.io.IOUtils;

public abstract class JarCacheAdapter {
   public static String BEAN_ID = "urule.jarCacheAdapter";

   /**重新加载jar，热部署jar时触发*/
   public abstract List loadDynamicJars(String groupId, UrlType urlType) throws Exception;

   protected String buildExceptionStack(Throwable throwable) {
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
