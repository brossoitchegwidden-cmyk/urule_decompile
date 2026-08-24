package com.bstek.urule.parse.deserializer;

import com.bstek.urule.model.library.constant.ConstantLibrary;
import com.bstek.urule.parse.ConstantLibraryParser;
import org.dom4j.Element;

public class ConstantLibraryDeserializer implements Deserializer<ConstantLibrary> {
   public static final String BEAN_ID = "urule.constantLibraryDeserializer";
   private ConstantLibraryParser constantLibraryParser;

   public ConstantLibrary deserialize(Element root) {
      return this.constantLibraryParser.parse(root);
   }

   @Override
   public boolean support(Element root) {
      return this.constantLibraryParser.support(root.getName());
   }

   public void setConstantLibraryParser(ConstantLibraryParser constantLibraryParser) {
      this.constantLibraryParser = constantLibraryParser;
   }
}
