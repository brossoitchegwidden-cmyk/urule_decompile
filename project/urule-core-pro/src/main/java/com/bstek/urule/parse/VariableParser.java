package com.bstek.urule.parse;

import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.library.variable.Act;
import com.bstek.urule.model.library.variable.Variable;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

public class VariableParser implements Parser<Variable> {
   public Variable parse(Element element) {
      Variable variable = new Variable();
      variable.setUuid(element.attributeValue("uuid"));
      variable.setName(element.attributeValue("name"));
      variable.setLabel(element.attributeValue("label"));
      String text = element.attributeValue("default-value");
      if (StringUtils.isNotBlank(text)) {
         variable.setDefaultValue(text);
      }

      String text2 = element.attributeValue("type");
      variable.setType(Datatype.parse(text2));
      variable.setDataType(text2);
      variable.setAct(Act.valueOf(element.attributeValue("act")));
      variable.setChildType(element.attributeValue("child-type"));
      variable.setChildTypeUuid(element.attributeValue("child-type-uuid"));
      return variable;
   }

   @Override
   public boolean support(String name) {
      return name.equals("var");
   }
}
