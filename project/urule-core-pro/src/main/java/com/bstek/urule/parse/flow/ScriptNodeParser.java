package com.bstek.urule.parse.flow;

import com.bstek.urule.model.flow.ScriptNode;
import com.bstek.urule.parse.ActionParser;
import java.util.ArrayList;
import java.util.Collection;
import org.dom4j.Element;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

public class ScriptNodeParser extends FlowNodeParser<ScriptNode> {
   private Collection<ActionParser> actionParsers;

   public ScriptNode parse(Element element) {
      ScriptNode scriptNode = new ScriptNode();
      scriptNode.setName(element.attributeValue("name"));
      scriptNode.setEventBean(element.attributeValue("event-bean"));
      scriptNode.setX(element.attributeValue("x"));
      scriptNode.setY(element.attributeValue("y"));
      scriptNode.setWidth(element.attributeValue("width"));
      scriptNode.setHeight(element.attributeValue("height"));
      scriptNode.setConnections(this.parseConnections(element));
      ArrayList items = new ArrayList();
      scriptNode.setActionsData(items);
      StringBuilder stringBuilder = new StringBuilder();

      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            String name = element2.getName();

            for (ActionParser actionParser : this.actionParsers) {
               if (actionParser.support(name)) {
                  stringBuilder.append(element2.asXML());
                  items.add(actionParser.parse(element2));
                  break;
               }
            }

            scriptNode.setActionXml(stringBuilder.toString());
         }
      }

      return scriptNode;
   }

   @Override
   public boolean support(String name) {
      return name.equals("script");
   }

   @Override
   public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
      super.setApplicationContext(applicationContext);
      this.actionParsers = applicationContext.getBeansOfType(ActionParser.class).values();
   }
}
