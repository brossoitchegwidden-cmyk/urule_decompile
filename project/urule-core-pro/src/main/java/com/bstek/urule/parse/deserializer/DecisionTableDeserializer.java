package com.bstek.urule.parse.deserializer;

import com.bstek.urule.model.table.DecisionTable;
import com.bstek.urule.parse.table.DecisionTableParser;
import org.dom4j.Element;

public class DecisionTableDeserializer implements Deserializer<DecisionTable> {
   public static final String BEAN_ID = "urule.decisionTableDeserializer";
   private DecisionTableParser decisionTableParser;

   public DecisionTable deserialize(Element root) {
      return this.decisionTableParser.parse(root);
   }

   @Override
   public boolean support(Element root) {
      return this.decisionTableParser.support(root.getName());
   }

   public void setDecisionTableParser(DecisionTableParser decisionTableParser) {
      this.decisionTableParser = decisionTableParser;
   }
}
