package com.bstek.urule.parse.deserializer;

import com.bstek.urule.model.scorecard.ComplexScorecardDefinition;
import com.bstek.urule.parse.scorecard.ComplexScorecardParser;
import org.dom4j.Element;

public class ComplexScorecardDeserializer implements Deserializer<ComplexScorecardDefinition> {
   public static final String BEAN_ID = "urule.complexScorecardDeserializer";
   private ComplexScorecardParser complexScorecardParser;

   public ComplexScorecardDefinition deserialize(Element root) {
      return this.complexScorecardParser.parse(root);
   }

   public void setComplexScorecardParser(ComplexScorecardParser complexScorecardParser) {
      this.complexScorecardParser = complexScorecardParser;
   }

   @Override
   public boolean support(Element root) {
      return this.complexScorecardParser.support(root.getName());
   }
}
