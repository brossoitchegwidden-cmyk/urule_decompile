package com.bstek.urule.parse;

import com.bstek.urule.action.Action;
import com.bstek.urule.action.ExecuteCommonFunctionAction;
import com.bstek.urule.model.rule.lhs.CommonFunctionParameter;
import org.dom4j.Element;

public class CommonFunctionActionParser extends ActionParser {
   public Action parse(Element element) {
      ExecuteCommonFunctionAction executeCommonFunctionAction = new ExecuteCommonFunctionAction();
      executeCommonFunctionAction.setLabel(element.attributeValue("function-label"));
      executeCommonFunctionAction.setName(element.attributeValue("function-name"));

      for (Object objectValue : element.elements()) {
         if (objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            if (element2.getName().equals("function-parameter")) {
               CommonFunctionParameter commonFunctionParameter = new CommonFunctionParameter();
               commonFunctionParameter.setName(element2.attributeValue("name"));
               commonFunctionParameter.setProperty(element2.attributeValue("property-name"));
               commonFunctionParameter.setPropertyLabel(element2.attributeValue("property-label"));

               for (Object objectValue2 : element2.elements()) {
                  if (objectValue2 instanceof Element) {
                     Element element3 = (Element)objectValue2;
                     if (element3.getName().equals("value")) {
                        commonFunctionParameter.setObjectParameter(this.valueParser.parse(element3));
                     }
                  }
               }

               executeCommonFunctionAction.setParameter(commonFunctionParameter);
            }
         }
      }

      return executeCommonFunctionAction;
   }

   @Override
   public boolean support(String name) {
      return name.equals("execute-function");
   }
}
