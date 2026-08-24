package com.bstek.urule.parse.deserializer;

import com.bstek.urule.model.decisiontree.DecisionTree;
import com.bstek.urule.parse.decisiontree.DecisionTreeParser;
import org.dom4j.Element;

public class DecisionTreeDeserializer implements Deserializer<DecisionTree> {
   public static final String BEAN_ID = "urule.decisionTreeDeserializer";
   private DecisionTreeParser decisionTreeParser;

   public DecisionTree deserialize(Element root) {
      return this.decisionTreeParser.parse(root);
   }

   public void setDecisionTreeParser(DecisionTreeParser decisionTreeParser) {
      this.decisionTreeParser = decisionTreeParser;
   }

   @Override
   public boolean support(Element root) {
      return this.decisionTreeParser.support(root.getName());
   }
}
