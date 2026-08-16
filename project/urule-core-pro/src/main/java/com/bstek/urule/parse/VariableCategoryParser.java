package com.bstek.urule.parse;

import com.bstek.urule.model.library.variable.Act;
import com.bstek.urule.model.library.variable.CategoryType;
import com.bstek.urule.model.library.variable.VariableCategory;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

public class VariableCategoryParser implements Parser<VariableCategory> {
   private VariableParser a;

   public VariableCategory parse(Element var1) {
      VariableCategory var2 = new VariableCategory();
      var2.setUuid(var1.attributeValue("uuid"));
      var2.setName(var1.attributeValue("name"));
      var2.setClazz(var1.attributeValue("clazz"));
      String var3 = var1.attributeValue("act");
      if (StringUtils.isNotBlank(var3)) {
         var2.setAct(Act.valueOf(var3));
      }

      var2.setType(CategoryType.valueOf(var1.attributeValue("type")));

      for (Object var5 : var1.elements()) {
         if (var5 != null && var5 instanceof Element) {
            Element var6 = (Element)var5;
            String var7 = var6.getName();
            if (this.a.support(var7)) {
               var2.addVariable(this.a.parse(var6));
            }
         }
      }

      return var2;
   }

   @Override
   public boolean support(String var1) {
      return var1.equals("category");
   }

   public void setVariableParser(VariableParser var1) {
      this.a = var1;
   }
}
