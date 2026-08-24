package com.bstek.urule.parse.flow;

import com.bstek.urule.model.flow.ForkNode;
import org.dom4j.Element;

public class ForkNodeParser extends FlowNodeParser<ForkNode> {
   public ForkNode parse(Element element) {
      ForkNode forkNode = new ForkNode(element.attributeValue("name"));
      forkNode.setConnections(this.parseConnections(element));
      forkNode.setEventBean(element.attributeValue("event-bean"));
      forkNode.setX(element.attributeValue("x"));
      forkNode.setY(element.attributeValue("y"));
      forkNode.setWidth(element.attributeValue("width"));
      forkNode.setHeight(element.attributeValue("height"));
      return forkNode;
   }

   @Override
   public boolean support(String name) {
      return name.equals("fork");
   }
}
