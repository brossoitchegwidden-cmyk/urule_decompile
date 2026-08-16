package com.bstek.urule.parse.table;

import com.bstek.urule.Utils;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.table.Column;
import com.bstek.urule.model.table.ColumnType;
import com.bstek.urule.parse.Parser;
import java.math.BigDecimal;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

public class ColumnParser implements Parser<Column> {
   public Column parse(Element var1) {
      Column var2 = new Column();
      String var3 = var1.attributeValue("predefine");
      if (StringUtils.isNotBlank(var3)) {
         var2.setPredefine(Boolean.valueOf(var3));
         if (var2.isPredefine()) {
            var2.setPropertyUuid(var1.attributeValue("property-uuid"));
         }
      }

      var2.setCategoryUuid(var1.attributeValue("category-uuid"));
      var2.setUuid(var1.attributeValue("uuid"));
      var2.setKeyName(var1.attributeValue("key-name"));
      var2.setKeyLabel(var1.attributeValue("key-label"));
      var2.setKeyUuid(var1.attributeValue("key-uuid"));
      var2.setKeyCategoryUuid(var1.attributeValue("key-category-uuid"));
      var2.setNum(Integer.valueOf(var1.attributeValue("num")));
      var2.setType(ColumnType.valueOf(var1.attributeValue("type")));
      var2.setVariableCategory(var1.attributeValue("var-category"));
      var2.setVariableLabel(var1.attributeValue("var-label"));
      var2.setVariableName(var1.attributeValue("var"));
      String var4 = var1.attributeValue("width");
      BigDecimal var5 = Utils.toBigDecimal(var4);
      var2.setWidth(var5.intValue());
      String var6 = var1.attributeValue("datatype");
      if (StringUtils.isNotEmpty(var6)) {
         var2.setDatatype(Datatype.valueOf(var6));
      }

      return var2;
   }

   @Override
   public boolean support(String var1) {
      return var1.equals("col");
   }
}
