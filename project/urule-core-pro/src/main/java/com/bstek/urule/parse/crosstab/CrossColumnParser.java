package com.bstek.urule.parse.crosstab;

import com.bstek.urule.model.crosstab.CrossColumn;
import com.bstek.urule.model.crosstab.LeftColumn;
import com.bstek.urule.model.crosstab.TopColumn;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.parse.Parser;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

public class CrossColumnParser implements Parser<CrossColumn> {
   public CrossColumn parse(Element var1) {
      String var2 = var1.attributeValue("type");
      if (var2.equals("left")) {
         LeftColumn var6 = new LeftColumn();
         var6.setColumnNumber(Integer.valueOf(var1.attributeValue("number")));
         String var4 = var1.attributeValue("bundle-data-type");
         if (StringUtils.isNotBlank(var4)) {
            var6.setBundleDataType(var4);
            var6.setVariableCategory(var1.attributeValue("var-category"));
            var6.setVariableName(var1.attributeValue("var"));
            var6.setVariableLabel(var1.attributeValue("var-label"));
            String var5 = var1.attributeValue("datatype");
            if (var5 != null) {
               var6.setDatatype(Datatype.valueOf(var5));
            }

            var6.setKeyLabel(var1.attributeValue("key-label"));
            var6.setKeyName(var1.attributeValue("key-name"));
            var6.setKeyCategoryUuid(var1.attributeValue("key-category-uuid"));
            var6.setKeyUuid(var1.attributeValue("key-uuid"));
            var6.setCategoryUuid(var1.attributeValue("category-uuid"));
            var6.setUuid(var1.attributeValue("uuid"));
            var6.setPredefineUuid(var1.attributeValue("predefine-uuid"));
            var6.setPredefinePropertyUuid(var1.attributeValue("predefine-property-uuid"));
         }

         return var6;
      } else {
         TopColumn var3 = new TopColumn();
         var3.setColumnNumber(Integer.valueOf(var1.attributeValue("number")));
         return var3;
      }
   }

   @Override
   public boolean support(String var1) {
      return "column".equals(var1);
   }
}
