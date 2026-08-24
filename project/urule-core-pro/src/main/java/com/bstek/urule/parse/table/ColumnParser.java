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
   public Column parse(Element element) {
      Column column = new Column();
      String text = element.attributeValue("predefine");
      if (StringUtils.isNotBlank(text)) {
         column.setPredefine(Boolean.valueOf(text));
         if (column.isPredefine()) {
            column.setPropertyUuid(element.attributeValue("property-uuid"));
         }
      }

      column.setCategoryUuid(element.attributeValue("category-uuid"));
      column.setUuid(element.attributeValue("uuid"));
      column.setKeyName(element.attributeValue("key-name"));
      column.setKeyLabel(element.attributeValue("key-label"));
      column.setKeyUuid(element.attributeValue("key-uuid"));
      column.setKeyCategoryUuid(element.attributeValue("key-category-uuid"));
      column.setNum(Integer.valueOf(element.attributeValue("num")));
      column.setType(ColumnType.valueOf(element.attributeValue("type")));
      column.setVariableCategory(element.attributeValue("var-category"));
      column.setVariableLabel(element.attributeValue("var-label"));
      column.setVariableName(element.attributeValue("var"));
      String text2 = element.attributeValue("width");
      BigDecimal decimalValue = Utils.toBigDecimal(text2);
      column.setWidth(decimalValue.intValue());
      String text3 = element.attributeValue("datatype");
      if (StringUtils.isNotEmpty(text3)) {
         column.setDatatype(Datatype.valueOf(text3));
      }

      return column;
   }

   @Override
   public boolean support(String name) {
      return name.equals("col");
   }
}
