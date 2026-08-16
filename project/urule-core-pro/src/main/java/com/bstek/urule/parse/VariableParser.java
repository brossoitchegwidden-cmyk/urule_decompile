package com.bstek.urule.parse;

import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.library.variable.Act;
import com.bstek.urule.model.library.variable.Variable;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

public class VariableParser implements Parser<Variable> {
   public Variable parse(Element var1) {
      Variable var2 = new Variable();
      var2.setUuid(var1.attributeValue("uuid"));
      var2.setName(var1.attributeValue("name"));
      var2.setLabel(var1.attributeValue("label"));
      String var3 = var1.attributeValue("default-value");
      if (StringUtils.isNotBlank(var3)) {
         var2.setDefaultValue(var3);
      }

      String var4 = var1.attributeValue("type");
      var2.setType(Datatype.parse(var4));
      var2.setDataType(var4);
      var2.setAct(Act.valueOf(var1.attributeValue("act")));
      var2.setChildType(var1.attributeValue("child-type"));
      var2.setChildTypeUuid(var1.attributeValue("child-type-uuid"));
      return var2;
   }

   @Override
   public boolean support(String var1) {
      return var1.equals("var");
   }
}
