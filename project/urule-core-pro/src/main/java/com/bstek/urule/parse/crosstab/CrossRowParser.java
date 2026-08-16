package com.bstek.urule.parse.crosstab;

import com.bstek.urule.model.crosstab.CrossRow;
import com.bstek.urule.model.crosstab.LeftRow;
import com.bstek.urule.model.crosstab.TopRow;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.parse.Parser;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

public class CrossRowParser implements Parser<CrossRow> {
   public CrossRow parse(Element var1) {
      String var2 = var1.attributeValue("type");
      if (var2.equals("left")) {
         LeftRow var6 = new LeftRow();
         var6.setRowNumber(Integer.valueOf(var1.attributeValue("number")));
         return var6;
      }

      TopRow var3 = new TopRow();
      var3.setRowNumber(Integer.valueOf(var1.attributeValue("number")));
      String var4 = var1.attributeValue("bundle-data-type");
      if (StringUtils.isNotBlank(var4)) {
         var3.setBundleDataType(var4);
         var3.setVariableCategory(var1.attributeValue("var-category"));
         var3.setVariableName(var1.attributeValue("var"));
         var3.setVariableLabel(var1.attributeValue("var-label"));
         String var5 = var1.attributeValue("datatype");
         if (var5 != null) {
            var3.setDatatype(Datatype.valueOf(var5));
         }

         var3.setKeyLabel(var1.attributeValue("key-label"));
         var3.setKeyName(var1.attributeValue("key-name"));
         var3.setKeyCategoryUuid(var1.attributeValue("key-category-uuid"));
         var3.setKeyUuid(var1.attributeValue("key-uuid"));
         var3.setCategoryUuid(var1.attributeValue("category-uuid"));
         var3.setUuid(var1.attributeValue("uuid"));
         var3.setPredefineUuid(var1.attributeValue("predefine-uuid"));
         var3.setPredefinePropertyUuid(var1.attributeValue("predefine-property-uuid"));
      }

      return var3;
   }

   @Override
   public boolean support(String var1) {
      return "row".equals(var1);
   }
}
