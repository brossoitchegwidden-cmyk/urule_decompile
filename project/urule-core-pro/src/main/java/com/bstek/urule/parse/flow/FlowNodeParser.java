package com.bstek.urule.parse.flow;

import com.bstek.urule.model.flow.Connection;
import com.bstek.urule.parse.CriterionParser;
import com.bstek.urule.parse.Parser;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public abstract class FlowNodeParser<T> implements Parser<T>, ApplicationContextAware {
   protected Collection<CriterionParser> criterionParsers;

   protected List<Connection> parseConnections(Element element) {
      ArrayList connections = new ArrayList();

      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            if (element2.getName().equals("connection")) {
               connections.add(this.resolveConnection(element2));
            }
         }
      }

      return connections;
   }

   private Connection resolveConnection(Element element) {
      Connection connection = new Connection();
      connection.setName(element.attributeValue("name"));
      connection.setToName(element.attributeValue("to"));
      connection.setG(element.attributeValue("g"));
      String stringValue = element.getStringValue();
      if (StringUtils.isNotEmpty(stringValue)) {
         connection.setScript(stringValue);
      }

      return connection;
   }

   public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
      this.criterionParsers = applicationContext.getBeansOfType(CriterionParser.class).values();
   }
}
