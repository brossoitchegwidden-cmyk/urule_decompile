package com.bstek.urule.console.xml;

import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class XXESAXReader extends SAXReader {
   protected void configureReader(XMLReader reader, DefaultHandler handler) throws DocumentException {
      super.configureReader(reader, handler);
      String text = "http://javax.xml.XMLConstants/feature/secure-processing";
      this.evaluateCondition(reader, text, true);
      text = "http://apache.org/xml/features/disallow-doctype-decl";
      this.evaluateCondition(reader, text, true);
      text = "http://xml.org/sax/features/external-parameter-entities";
      this.evaluateCondition(reader, text, false);
      text = "http://xml.org/sax/features/external-general-entities";
      this.evaluateCondition(reader, text, false);
      text = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
      this.evaluateCondition(reader, text, false);
   }

   private boolean evaluateCondition(XMLReader xMLReader, String text, boolean flag) {
      try {
         xMLReader.setFeature(text, flag);
         return true;
      } catch (SAXNotSupportedException sAXNotSupportedException) {
      } catch (SAXNotRecognizedException sAXNotRecognizedException) {
      }

      return false;
   }
}
