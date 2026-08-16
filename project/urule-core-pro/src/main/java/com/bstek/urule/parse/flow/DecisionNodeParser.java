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
   private LhsParser b;

   public DecisionNode parse(Element var1) {
      DecisionNode var2 = new DecisionNode(var1.attributeValue("name"));
      var2.setEventBean(var1.attributeValue("event-bean"));
      String var3 = var1.attributeValue("decision-type");
      if (StringUtils.isNotBlank(var3)) {
         var2.setDecisionType(DecisionType.valueOf(var3));
      }

      String var4 = var1.attributeValue("percent-scope");
      if (StringUtils.isNotBlank(var4)) {
         var2.setPercentScope(PercentScope.valueOf(var4));
      }

      if (var2.getPercentScope() == null) {
         var2.setPercentScope(PercentScope.batch);
      }

      var2.setX(var1.attributeValue("x"));
      var2.setY(var1.attributeValue("y"));
      var2.setWidth(var1.attributeValue("width"));
      var2.setHeight(var1.attributeValue("height"));
      var2.setConnections(this.a(var1));
      ArrayList var5 = new ArrayList();

      for (Object var7 : var1.elements()) {
         if (var7 != null && var7 instanceof Element) {
            Element var8 = (Element)var7;
            if (var8.getName().equals("item")) {
               DecisionItem var9 = this.b(var8);
               var5.add(var9);
            }
         }
      }

      var2.setItems(var5);
      return var2;
   }

   private DecisionItem b(Element var1) {
      DecisionItem var2 = new DecisionItem();
      var2.setTo(var1.attributeValue("connection"));
      String var3 = var1.attributeValue("percent");
      if (StringUtils.isNotEmpty(var3)) {
         var2.setPercent(Integer.valueOf(var3));
      }

      String var4 = var1.attributeValue("condition-type");
      if (var4 == null) {
         var4 = "script";
      }

      var2.setConditionType(var4);
      if (var4.equals("script")) {
         String var5 = var1.getStringValue();
         var2.setScript(var5);
      } else {
         for (Object var6 : var1.elements()) {
            if (var6 != null && var6 instanceof Element) {
               Element var7 = (Element)var6;
               if (this.b.support(var7.getName())) {
                  var2.setLhs(this.b.parse(var7));
                  var2.setLhsXml(var7.asXML());
               }
            }
         }
      }

      return var2;
   }

   @Override
   public boolean support(String var1) {
      return var1.equals("decision");
   }

   public void setLhsParser(LhsParser var1) {
      this.b = var1;
   }
}
