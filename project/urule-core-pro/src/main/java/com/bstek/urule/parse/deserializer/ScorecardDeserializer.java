package com.bstek.urule.parse.deserializer;

import com.bstek.urule.model.scorecard.ScorecardDefinition;
import com.bstek.urule.parse.scorecard.ScorecardParser;
import org.dom4j.Element;

public class ScorecardDeserializer implements Deserializer<ScorecardDefinition> {
   public static final String BEAN_ID = "urule.scorecardDeserializer";
   private ScorecardParser scorecardParser;

   public ScorecardDefinition deserialize(Element root) {
      return this.scorecardParser.parse(root);
   }

   public void setScorecardParser(ScorecardParser scorecardParser) {
      this.scorecardParser = scorecardParser;
   }

   @Override
   public boolean support(Element root) {
      return this.scorecardParser.support(root.getName());
   }
}
