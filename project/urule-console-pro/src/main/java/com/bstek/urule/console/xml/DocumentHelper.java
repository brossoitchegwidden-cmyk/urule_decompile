package com.bstek.urule.console.xml;

import java.io.StringReader;
import java.util.StringTokenizer;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

public class DocumentHelper {
   public static Document parseText(String var0) throws DocumentException {
      Document var1 = null;
      XXESAXReader var2 = new XXESAXReader();
      String var3 = a(var0);
      InputSource var4 = new InputSource(new StringReader(var0));
      var4.setEncoding(var3);
      var1 = ((SAXReader)var2).read(var4);
      if (var1.getXMLEncoding() == null) {
         var1.setXMLEncoding(var3);
      }

      return var1;
   }

   private static String a(String var0) {
      String var1 = null;
      String var2 = var0.trim();
      if (var2.startsWith("<?xml")) {
         int var3 = var2.indexOf("?>");
         String var4 = var2.substring(0, var3);
         StringTokenizer var5 = new StringTokenizer(var4, " =\"'");

         while(var5.hasMoreTokens()) {
            String var6 = var5.nextToken();
            if ("encoding".equals(var6)) {
               if (var5.hasMoreTokens()) {
                  var1 = var5.nextToken();
               }
               break;
            }
         }
      }

      return var1;
   }
}
