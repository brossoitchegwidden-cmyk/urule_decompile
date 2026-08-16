package com.bstek.urule.parse.deserializer;

import com.bstek.urule.model.library.constant.ConstantLibrary;
import com.bstek.urule.parse.ConstantLibraryParser;
import org.dom4j.Element;

public class ConstantLibraryDeserializer implements Deserializer<ConstantLibrary> {
   public static final String BEAN_ID = "urule.constantLibraryDeserializer";
   private ConstantLibraryParser a;

   public ConstantLibrary deserialize(Element var1) {
      return this.a.parse(var1);
   }

   @Override
   public boolean support(Element var1) {
      return this.a.support(var1.getName());
   }

   public void setConstantLibraryParser(ConstantLibraryParser var1) {
      this.a = var1;
   }
}
