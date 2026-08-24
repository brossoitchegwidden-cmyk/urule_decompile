package com.bstek.urule.parse.flow;

import com.bstek.urule.model.flow.BindingFile;
import com.bstek.urule.model.flow.RuleNode;
import java.util.ArrayList;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

public class RuleNodeParser extends FlowNodeParser<RuleNode> {
   public RuleNode parse(Element element) {
      RuleNode ruleNode = new RuleNode(element.attributeValue("name"));
      ruleNode.setX(element.attributeValue("x"));
      ruleNode.setY(element.attributeValue("y"));
      ruleNode.setWidth(element.attributeValue("width"));
      ruleNode.setHeight(element.attributeValue("height"));
      ruleNode.setEventBean(element.attributeValue("event-bean"));
      ruleNode.setConnections(this.parseConnections(element));
      ArrayList items = new ArrayList();
      String text = element.attributeValue("file");
      if (StringUtils.isNotBlank(text)) {
         long longValue = Long.valueOf(text);
         BindingFile bindingFile = new BindingFile(longValue, "", null);
         items.add(bindingFile);
      }

      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            if (element2.getName().contentEquals("file")) {
               long longValue2 = Long.valueOf(element2.attributeValue("id"));
               String text2 = element2.attributeValue("path");
               String text3 = element2.attributeValue("version");
               BindingFile bindingFile2 = new BindingFile(longValue2, text2, text3);
               items.add(bindingFile2);
            }
         }
      }

      ruleNode.setFiles(items);
      return ruleNode;
   }

   @Override
   public boolean support(String name) {
      return name.equals("rule");
   }
}
