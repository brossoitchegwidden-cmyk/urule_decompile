package com.bstek.urule.console.xml;

import java.io.StringReader;
import java.util.StringTokenizer;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

public class DocumentHelper {
   public static Document parseText(String text) throws DocumentException {
      Document document = null;
      XXESAXReader xXESAXReader = new XXESAXReader();
      String text2 = detectXmlEncoding(text);
      InputSource inputSource = new InputSource(new StringReader(text));
      inputSource.setEncoding(text2);
      document = ((SAXReader)xXESAXReader).read(inputSource);
      if (document.getXMLEncoding() == null) {
         document.setXMLEncoding(text2);
      }

      return document;
   }

   private static String detectXmlEncoding(String text) {
      String text2 = null;
      String trimmedText = text.trim();
      if (trimmedText.startsWith("<?xml")) {
         int number = trimmedText.indexOf("?>");
         String substring = trimmedText.substring(0, number);
         StringTokenizer stringTokenizer = new StringTokenizer(substring, " =\"'");

         while(stringTokenizer.hasMoreTokens()) {
            String text3 = stringTokenizer.nextToken();
            if ("encoding".equals(text3)) {
               if (stringTokenizer.hasMoreTokens()) {
                  text2 = stringTokenizer.nextToken();
               }
               break;
            }
         }
      }

      return text2;
   }
}
