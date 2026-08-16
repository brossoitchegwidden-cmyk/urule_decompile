package com.bstek.urule.parse.flow;

import com.bstek.urule.model.flow.BindingFile;
import com.bstek.urule.model.flow.RuleNode;
import java.util.ArrayList;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

public class RuleNodeParser extends FlowNodeParser<RuleNode> {
   public RuleNode parse(Element var1) {
      RuleNode var2 = new RuleNode(var1.attributeValue("name"));
      var2.setX(var1.attributeValue("x"));
      var2.setY(var1.attributeValue("y"));
      var2.setWidth(var1.attributeValue("width"));
      var2.setHeight(var1.attributeValue("height"));
      var2.setEventBean(var1.attributeValue("event-bean"));
      var2.setConnections(this.a(var1));
      ArrayList var3 = new ArrayList();
      String var4 = var1.attributeValue("file");
      if (StringUtils.isNotBlank(var4)) {
         long var5 = Long.valueOf(var4);
         BindingFile var7 = new BindingFile(var5, "", null);
         var3.add(var7);
      }

      for (Object var6 : var1.elements()) {
         if (var6 != null && var6 instanceof Element) {
            Element var14 = (Element)var6;
            if (var14.getName().contentEquals("file")) {
               long var8 = Long.valueOf(var14.attributeValue("id"));
               String var10 = var14.attributeValue("path");
               String var11 = var14.attributeValue("version");
               BindingFile var12 = new BindingFile(var8, var10, var11);
               var3.add(var12);
            }
         }
      }

      var2.setFiles(var3);
      return var2;
   }

   @Override
   public boolean support(String var1) {
      return var1.equals("rule");
   }
}
