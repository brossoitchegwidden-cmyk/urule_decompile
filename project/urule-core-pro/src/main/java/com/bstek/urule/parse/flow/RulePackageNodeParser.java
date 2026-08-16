package com.bstek.urule.parse.flow;

import com.bstek.urule.model.flow.RulePackageNode;
import org.dom4j.Element;

public class RulePackageNodeParser extends FlowNodeParser<RulePackageNode> {
   public RulePackageNode parse(Element var1) {
      RulePackageNode var2 = new RulePackageNode(var1.attributeValue("name"));
      var2.setConnections(this.a(var1));
      var2.setProject(var1.attributeValue("project"));
      var2.setPackageId(var1.attributeValue("package-id"));
      String var3 = var1.attributeValue("code");
      if (var3 == null) {
         var3 = var2.getPackageId();
      }

      var2.setCode(var3);
      var2.setEventBean(var1.attributeValue("event-bean"));
      var2.setX(var1.attributeValue("x"));
      var2.setY(var1.attributeValue("y"));
      var2.setWidth(var1.attributeValue("width"));
      var2.setHeight(var1.attributeValue("height"));
      return var2;
   }

   @Override
   public boolean support(String var1) {
      return var1.equals("rule-package");
   }
}
