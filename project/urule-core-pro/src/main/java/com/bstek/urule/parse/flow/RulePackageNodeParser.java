package com.bstek.urule.parse.flow;

import com.bstek.urule.model.flow.RulePackageNode;
import org.dom4j.Element;

public class RulePackageNodeParser extends FlowNodeParser<RulePackageNode> {
   public RulePackageNode parse(Element element) {
      RulePackageNode rulePackageNode = new RulePackageNode(element.attributeValue("name"));
      rulePackageNode.setConnections(this.parseConnections(element));
      rulePackageNode.setProject(element.attributeValue("project"));
      rulePackageNode.setPackageId(element.attributeValue("package-id"));
      String packageId = element.attributeValue("code");
      if (packageId == null) {
         packageId = rulePackageNode.getPackageId();
      }

      rulePackageNode.setCode(packageId);
      rulePackageNode.setEventBean(element.attributeValue("event-bean"));
      rulePackageNode.setX(element.attributeValue("x"));
      rulePackageNode.setY(element.attributeValue("y"));
      rulePackageNode.setWidth(element.attributeValue("width"));
      rulePackageNode.setHeight(element.attributeValue("height"));
      return rulePackageNode;
   }

   @Override
   public boolean support(String name) {
      return name.equals("rule-package");
   }
}
