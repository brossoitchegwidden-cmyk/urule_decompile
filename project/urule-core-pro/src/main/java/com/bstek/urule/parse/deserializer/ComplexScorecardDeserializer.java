package com.bstek.urule.parse.deserializer;

import com.bstek.urule.model.scorecard.ComplexScorecardDefinition;
import com.bstek.urule.parse.scorecard.ComplexScorecardParser;
import org.dom4j.Element;

public class ComplexScorecardDeserializer implements Deserializer<ComplexScorecardDefinition> {
   public static final String BEAN_ID = "urule.complexScorecardDeserializer";
   private ComplexScorecardParser a;

   public ComplexScorecardDefinition deserialize(Element var1) {
      return this.a.parse(var1);
   }

   public void setComplexScorecardParser(ComplexScorecardParser var1) {
      this.a = var1;
   }

   @Override
   public boolean support(Element var1) {
      return this.a.support(var1.getName());
   }
}
