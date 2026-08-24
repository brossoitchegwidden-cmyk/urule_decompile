package com.bstek.urule.parse;

import com.bstek.urule.action.Action;
import com.bstek.urule.action.PredefineAssignAction;
import com.bstek.urule.action.VariableAssignAction;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.lhs.LeftType;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

public class VariableAssignActionParser extends ActionParser {
   public Action parse(Element element) {
      String text = element.attributeValue("type");
      LeftType leftType = LeftType.variable;
      if (StringUtils.isNotEmpty(text)) {
         leftType = LeftType.valueOf(text);
      }

      if (leftType.equals(LeftType.predefine)) {
         PredefineAssignAction predefineAssignAction = new PredefineAssignAction();
         predefineAssignAction.setUuid(element.attributeValue("uuid"));
         predefineAssignAction.setPropertyUuid(element.attributeValue("property-uuid"));
         predefineAssignAction.setValue(this.resolveValue(element));
         return predefineAssignAction;
      }

      VariableAssignAction variableAssignAction = new VariableAssignAction();
      variableAssignAction.setType(leftType);
      String text2 = element.attributeValue("var");
      if (StringUtils.isEmpty(text2)) {
         text2 = element.attributeValue("property-name");
      }

      variableAssignAction.setCategoryUuid(element.attributeValue("category-uuid"));
      variableAssignAction.setUuid(element.attributeValue("uuid"));
      variableAssignAction.setVariableName(text2);
      String text3 = element.attributeValue("var-label");
      if (StringUtils.isEmpty(text3)) {
         text3 = element.attributeValue("property-label");
      }

      variableAssignAction.setVariableLabel(text3);
      String text4 = element.attributeValue("var-category");
      variableAssignAction.setVariableCategory(text4);
      String text5 = element.attributeValue("datatype");
      if (StringUtils.isNotEmpty(text5)) {
         variableAssignAction.setDatatype(Datatype.valueOf(text5));
      }

      variableAssignAction.setKeyLabel(element.attributeValue("key-label"));
      variableAssignAction.setKeyName(element.attributeValue("key-name"));
      variableAssignAction.setKeyUuid(element.attributeValue("key-uuid"));
      variableAssignAction.setKeyCategoryUuid(element.attributeValue("key-category-uuid"));
      variableAssignAction.setValue(this.resolveValue(element));
      return variableAssignAction;
   }

   private Value resolveValue(Element element) {
      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            if (this.valueParser.support(element2.getName())) {
               return this.valueParser.parse(element2);
            }
         }
      }

      return null;
   }

   @Override
   public boolean support(String name) {
      return name.equals("var-assign");
   }
}
