package com.bstek.urule.parse.flow;

import com.bstek.urule.model.flow.JoinNode;
import org.dom4j.Element;

public class JoinNodeParser extends FlowNodeParser<JoinNode> {
   public JoinNode parse(Element element) {
      JoinNode joinNode = new JoinNode(element.attributeValue("name"));
      joinNode.setConnections(this.parseConnections(element));
      joinNode.setEventBean(element.attributeValue("event-bean"));
      joinNode.setX(element.attributeValue("x"));
      joinNode.setY(element.attributeValue("y"));
      joinNode.setWidth(element.attributeValue("width"));
      joinNode.setHeight(element.attributeValue("height"));
      return joinNode;
   }

   @Override
   public boolean support(String name) {
      return name.equals("join");
   }
}
