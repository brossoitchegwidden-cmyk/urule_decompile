package com.bstek.urule.parse.flow;

import com.bstek.urule.model.flow.StartNode;
import org.dom4j.Element;

public class StartNodeParser extends FlowNodeParser<StartNode> {
   public StartNode parse(Element element) {
      StartNode startNode = new StartNode(element.attributeValue("name"));
      startNode.setConnections(this.parseConnections(element));
      startNode.setEventBean(element.attributeValue("event-bean"));
      startNode.setX(element.attributeValue("x"));
      startNode.setY(element.attributeValue("y"));
      startNode.setWidth(element.attributeValue("width"));
      startNode.setHeight(element.attributeValue("height"));
      return startNode;
   }

   @Override
   public boolean support(String name) {
      return name.equals("start");
   }
}
