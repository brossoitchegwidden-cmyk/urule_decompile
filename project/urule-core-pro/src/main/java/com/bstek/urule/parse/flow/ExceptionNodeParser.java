package com.bstek.urule.parse.flow;

import com.bstek.urule.model.flow.ExceptionNode;
import org.dom4j.Element;

public class ExceptionNodeParser extends FlowNodeParser<ExceptionNode> {
   public ExceptionNode parse(Element element) {
      ExceptionNode exceptionNode = new ExceptionNode(element.attributeValue("name"), element.attributeValue("exception"));
      exceptionNode.setExceptionBean(element.attributeValue("exception-bean"));
      exceptionNode.setEventBean(element.attributeValue("event-bean"));
      exceptionNode.setX(element.attributeValue("x"));
      exceptionNode.setY(element.attributeValue("y"));
      exceptionNode.setWidth(element.attributeValue("width"));
      exceptionNode.setHeight(element.attributeValue("height"));
      exceptionNode.setConnections(this.parseConnections(element));
      return exceptionNode;
   }

   @Override
   public boolean support(String name) {
      return name.equals("exception");
   }
}
