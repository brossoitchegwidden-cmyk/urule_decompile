package com.bstek.urule.parse.flow;

import com.bstek.urule.model.flow.ActionNode;
import org.dom4j.Element;

public class ActionNodeParser extends FlowNodeParser<ActionNode> {
   public ActionNode parse(Element element) {
      ActionNode actionNode = new ActionNode(element.attributeValue("name"));
      actionNode.setActionBean(element.attributeValue("action-bean"));
      actionNode.setEventBean(element.attributeValue("event-bean"));
      actionNode.setX(element.attributeValue("x"));
      actionNode.setY(element.attributeValue("y"));
      actionNode.setWidth(element.attributeValue("width"));
      actionNode.setHeight(element.attributeValue("height"));
      actionNode.setConnections(this.parseConnections(element));
      return actionNode;
   }

   @Override
   public boolean support(String name) {
      return name.equals("action");
   }
}
