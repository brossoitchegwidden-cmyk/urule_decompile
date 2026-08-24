package com.bstek.urule.parse.crosstab;

import com.bstek.urule.model.crosstab.CrossColumn;
import com.bstek.urule.model.crosstab.LeftColumn;
import com.bstek.urule.model.crosstab.TopColumn;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.parse.Parser;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

public class CrossColumnParser implements Parser<CrossColumn> {
   public CrossColumn parse(Element element) {
      String text = element.attributeValue("type");
      if (text.equals("left")) {
         LeftColumn leftColumn = new LeftColumn();
         leftColumn.setColumnNumber(Integer.valueOf(element.attributeValue("number")));
         String text2 = element.attributeValue("bundle-data-type");
         if (StringUtils.isNotBlank(text2)) {
            leftColumn.setBundleDataType(text2);
            leftColumn.setVariableCategory(element.attributeValue("var-category"));
            leftColumn.setVariableName(element.attributeValue("var"));
            leftColumn.setVariableLabel(element.attributeValue("var-label"));
            String text3 = element.attributeValue("datatype");
            if (text3 != null) {
               leftColumn.setDatatype(Datatype.valueOf(text3));
            }

            leftColumn.setKeyLabel(element.attributeValue("key-label"));
            leftColumn.setKeyName(element.attributeValue("key-name"));
            leftColumn.setKeyCategoryUuid(element.attributeValue("key-category-uuid"));
            leftColumn.setKeyUuid(element.attributeValue("key-uuid"));
            leftColumn.setCategoryUuid(element.attributeValue("category-uuid"));
            leftColumn.setUuid(element.attributeValue("uuid"));
            leftColumn.setPredefineUuid(element.attributeValue("predefine-uuid"));
            leftColumn.setPredefinePropertyUuid(element.attributeValue("predefine-property-uuid"));
         }

         return leftColumn;
      } else {
         TopColumn topColumn = new TopColumn();
         topColumn.setColumnNumber(Integer.valueOf(element.attributeValue("number")));
         return topColumn;
      }
   }

   @Override
   public boolean support(String name) {
      return "column".equals(name);
   }
}
