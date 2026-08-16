package com.bstek.urule.parse;

import com.bstek.urule.action.Action;
import com.bstek.urule.action.ExecuteCommonFunctionAction;
import com.bstek.urule.model.rule.lhs.CommonFunctionParameter;
import org.dom4j.Element;

public class CommonFunctionActionParser extends ActionParser {
   public Action parse(Element var1) {
      ExecuteCommonFunctionAction var2 = new ExecuteCommonFunctionAction();
      var2.setLabel(var1.attributeValue("function-label"));
      var2.setName(var1.attributeValue("function-name"));

      for (Object var4 : var1.elements()) {
         if (var4 instanceof Element) {
            Element var5 = (Element)var4;
            if (var5.getName().equals("function-parameter")) {
               CommonFunctionParameter var6 = new CommonFunctionParameter();
               var6.setName(var5.attributeValue("name"));
               var6.setProperty(var5.attributeValue("property-name"));
               var6.setPropertyLabel(var5.attributeValue("property-label"));

               for (Object var8 : var5.elements()) {
                  if (var8 instanceof Element) {
                     Element var9 = (Element)var8;
                     if (var9.getName().equals("value")) {
                        var6.setObjectParameter(this.a.parse(var9));
                     }
                  }
               }

               var2.setParameter(var6);
            }
         }
      }

      return var2;
   }

   @Override
   public boolean support(String var1) {
      return var1.equals("execute-function");
   }
}
