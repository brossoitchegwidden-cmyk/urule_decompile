package com.bstek.urule.parse.flow;

import com.bstek.urule.model.flow.ExceptionNode;
import org.dom4j.Element;

public class ExceptionNodeParser extends FlowNodeParser<ExceptionNode> {
   public ExceptionNode parse(Element var1) {
      ExceptionNode var2 = new ExceptionNode(var1.attributeValue("name"), var1.attributeValue("exception"));
      var2.setExceptionBean(var1.attributeValue("exception-bean"));
      var2.setEventBean(var1.attributeValue("event-bean"));
      var2.setX(var1.attributeValue("x"));
      var2.setY(var1.attributeValue("y"));
      var2.setWidth(var1.attributeValue("width"));
      var2.setHeight(var1.attributeValue("height"));
      var2.setConnections(this.a(var1));
      return var2;
   }

   @Override
   public boolean support(String var1) {
      return var1.equals("exception");
   }
}
