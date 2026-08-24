package com.bstek.urule.parse.flow;

import com.bstek.urule.model.flow.DecisionItem;
import com.bstek.urule.model.flow.DecisionNode;
import com.bstek.urule.model.flow.DecisionType;
import com.bstek.urule.model.flow.PercentScope;
import com.bstek.urule.parse.LhsParser;
import java.util.ArrayList;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

public class DecisionNodeParser extends FlowNodeParser<DecisionNode> {
   private LhsParser lhsParser;

   public DecisionNode parse(Element element) {
      DecisionNode decisionNode = new DecisionNode(element.attributeValue("name"));
      decisionNode.setEventBean(element.attributeValue("event-bean"));
      String text = element.attributeValue("decision-type");
      if (StringUtils.isNotBlank(text)) {
         decisionNode.setDecisionType(DecisionType.valueOf(text));
      }

      String text2 = element.attributeValue("percent-scope");
      if (StringUtils.isNotBlank(text2)) {
         decisionNode.setPercentScope(PercentScope.valueOf(text2));
      }

      if (decisionNode.getPercentScope() == null) {
         decisionNode.setPercentScope(PercentScope.batch);
      }

      decisionNode.setX(element.attributeValue("x"));
      decisionNode.setY(element.attributeValue("y"));
      decisionNode.setWidth(element.attributeValue("width"));
      decisionNode.setHeight(element.attributeValue("height"));
      decisionNode.setConnections(this.parseConnections(element));
      ArrayList items = new ArrayList();

      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            if (element2.getName().equals("item")) {
               DecisionItem decisionItem = this.resolveDecisionItem(element2);
               items.add(decisionItem);
            }
         }
      }

      decisionNode.setItems(items);
      return decisionNode;
   }

   private DecisionItem resolveDecisionItem(Element element) {
      DecisionItem decisionItem = new DecisionItem();
      decisionItem.setTo(element.attributeValue("connection"));
      String text = element.attributeValue("percent");
      if (StringUtils.isNotEmpty(text)) {
         decisionItem.setPercent(Integer.valueOf(text));
      }

      String text2 = element.attributeValue("condition-type");
      if (text2 == null) {
         text2 = "script";
      }

      decisionItem.setConditionType(text2);
      if (text2.equals("script")) {
         String stringValue = element.getStringValue();
         decisionItem.setScript(stringValue);
      } else {
         for (Object objectValue : element.elements()) {
            if (objectValue != null && objectValue instanceof Element) {
               Element element2 = (Element)objectValue;
               if (this.lhsParser.support(element2.getName())) {
                  decisionItem.setLhs(this.lhsParser.parse(element2));
                  decisionItem.setLhsXml(element2.asXML());
               }
            }
         }
      }

      return decisionItem;
   }

   @Override
   public boolean support(String name) {
      return name.equals("decision");
   }

   public void setLhsParser(LhsParser lhsParser) {
      this.lhsParser = lhsParser;
   }
}
