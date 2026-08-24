package com.bstek.urule.parse.crosstab;

import com.bstek.urule.model.crosstab.CrossRow;
import com.bstek.urule.model.crosstab.LeftRow;
import com.bstek.urule.model.crosstab.TopRow;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.parse.Parser;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

public class CrossRowParser implements Parser<CrossRow> {
   public CrossRow parse(Element element) {
      String text = element.attributeValue("type");
      if (text.equals("left")) {
         LeftRow leftRow = new LeftRow();
         leftRow.setRowNumber(Integer.valueOf(element.attributeValue("number")));
         return leftRow;
      }

      TopRow topRow = new TopRow();
      topRow.setRowNumber(Integer.valueOf(element.attributeValue("number")));
      String text2 = element.attributeValue("bundle-data-type");
      if (StringUtils.isNotBlank(text2)) {
         topRow.setBundleDataType(text2);
         topRow.setVariableCategory(element.attributeValue("var-category"));
         topRow.setVariableName(element.attributeValue("var"));
         topRow.setVariableLabel(element.attributeValue("var-label"));
         String text3 = element.attributeValue("datatype");
         if (text3 != null) {
            topRow.setDatatype(Datatype.valueOf(text3));
         }

         topRow.setKeyLabel(element.attributeValue("key-label"));
         topRow.setKeyName(element.attributeValue("key-name"));
         topRow.setKeyCategoryUuid(element.attributeValue("key-category-uuid"));
         topRow.setKeyUuid(element.attributeValue("key-uuid"));
         topRow.setCategoryUuid(element.attributeValue("category-uuid"));
         topRow.setUuid(element.attributeValue("uuid"));
         topRow.setPredefineUuid(element.attributeValue("predefine-uuid"));
         topRow.setPredefinePropertyUuid(element.attributeValue("predefine-property-uuid"));
      }

      return topRow;
   }

   @Override
   public boolean support(String name) {
      return "row".equals(name);
   }
}
