package com.bstek.urule.console.xml;

import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class XXESAXReader extends SAXReader {
   protected void configureReader(XMLReader var1, DefaultHandler var2) throws DocumentException {
      super.configureReader(var1, var2);
      String var3 = "http://javax.xml.XMLConstants/feature/secure-processing";
      this.a(var1, var3, true);
      var3 = "http://apache.org/xml/features/disallow-doctype-decl";
      this.a(var1, var3, true);
      var3 = "http://xml.org/sax/features/external-parameter-entities";
      this.a(var1, var3, false);
      var3 = "http://xml.org/sax/features/external-general-entities";
      this.a(var1, var3, false);
      var3 = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
      this.a(var1, var3, false);
   }

   private boolean a(XMLReader var1, String var2, boolean var3) {
      try {
         var1.setFeature(var2, var3);
         return true;
      } catch (SAXNotSupportedException var5) {
      } catch (SAXNotRecognizedException var6) {
      }

      return false;
   }
}
