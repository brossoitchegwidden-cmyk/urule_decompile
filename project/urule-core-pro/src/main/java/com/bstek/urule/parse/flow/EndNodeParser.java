package com.bstek.urule.parse.flow;

import com.bstek.urule.model.flow.EndNode;
import org.dom4j.Element;

public class EndNodeParser extends FlowNodeParser<EndNode> {
   public EndNode parse(Element element) {
      EndNode endNode = new EndNode(element.attributeValue("name"));
      endNode.setConnections(this.parseConnections(element));
      endNode.setEventBean(element.attributeValue("event-bean"));
      return endNode;
   }

   @Override
   public boolean support(String name) {
      return name.equals("end");
   }
}
