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
   public Action parse(Element var1) {
      String var2 = var1.attributeValue("type");
      LeftType var3 = LeftType.variable;
      if (StringUtils.isNotEmpty(var2)) {
         var3 = LeftType.valueOf(var2);
      }

      if (var3.equals(LeftType.predefine)) {
         PredefineAssignAction var9 = new PredefineAssignAction();
         var9.setUuid(var1.attributeValue("uuid"));
         var9.setPropertyUuid(var1.attributeValue("property-uuid"));
         var9.setValue(this.a(var1));
         return var9;
      }

      VariableAssignAction var4 = new VariableAssignAction();
      var4.setType(var3);
      String var5 = var1.attributeValue("var");
      if (StringUtils.isEmpty(var5)) {
         var5 = var1.attributeValue("property-name");
      }

      var4.setCategoryUuid(var1.attributeValue("category-uuid"));
      var4.setUuid(var1.attributeValue("uuid"));
      var4.setVariableName(var5);
      String var6 = var1.attributeValue("var-label");
      if (StringUtils.isEmpty(var6)) {
         var6 = var1.attributeValue("property-label");
      }

      var4.setVariableLabel(var6);
      String var7 = var1.attributeValue("var-category");
      var4.setVariableCategory(var7);
      String var8 = var1.attributeValue("datatype");
      if (StringUtils.isNotEmpty(var8)) {
         var4.setDatatype(Datatype.valueOf(var8));
      }

      var4.setKeyLabel(var1.attributeValue("key-label"));
      var4.setKeyName(var1.attributeValue("key-name"));
      var4.setKeyUuid(var1.attributeValue("key-uuid"));
      var4.setKeyCategoryUuid(var1.attributeValue("key-category-uuid"));
      var4.setValue(this.a(var1));
      return var4;
   }

   private Value a(Element var1) {
      for (Object var3 : var1.elements()) {
         if (var3 != null && var3 instanceof Element) {
            Element var4 = (Element)var3;
            if (this.a.support(var4.getName())) {
               return this.a.parse(var4);
            }
         }
      }

      return null;
   }

   @Override
   public boolean support(String var1) {
      return var1.equals("var-assign");
   }
}
