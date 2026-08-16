package com.bstek.urule.parse.flow;

import com.bstek.urule.model.flow.ScriptNode;
import com.bstek.urule.parse.ActionParser;
import java.util.ArrayList;
import java.util.Collection;
import org.dom4j.Element;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

public class ScriptNodeParser extends FlowNodeParser<ScriptNode> {
   private Collection<ActionParser> b;

   public ScriptNode parse(Element var1) {
      ScriptNode var2 = new ScriptNode();
      var2.setName(var1.attributeValue("name"));
      var2.setEventBean(var1.attributeValue("event-bean"));
      var2.setX(var1.attributeValue("x"));
      var2.setY(var1.attributeValue("y"));
      var2.setWidth(var1.attributeValue("width"));
      var2.setHeight(var1.attributeValue("height"));
      var2.setConnections(this.a(var1));
      ArrayList var3 = new ArrayList();
      var2.setActionsData(var3);
      StringBuilder var4 = new StringBuilder();

      for (Object var6 : var1.elements()) {
         if (var6 != null && var6 instanceof Element) {
            Element var7 = (Element)var6;
            String var8 = var7.getName();

            for (ActionParser var10 : this.b) {
               if (var10.support(var8)) {
                  var4.append(var7.asXML());
                  var3.add(var10.parse(var7));
                  break;
               }
            }

            var2.setActionXml(var4.toString());
         }
      }

      return var2;
   }

   @Override
   public boolean support(String var1) {
      return var1.equals("script");
   }

   @Override
   public void setApplicationContext(ApplicationContext var1) throws BeansException {
      super.setApplicationContext(var1);
      this.b = var1.getBeansOfType(ActionParser.class).values();
   }
}
