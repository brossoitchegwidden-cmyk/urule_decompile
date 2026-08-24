package com.bstek.urule.parse.crosstab;

import com.bstek.urule.model.crosstab.CrossCell;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

public abstract class CrossCellParser {
   protected void parseCrossCell(CrossCell cell, Element element) {
      int number = Integer.valueOf(element.attributeValue("row"));
      int number2 = Integer.valueOf(element.attributeValue("col"));
      String text = element.attributeValue("rowspan");
      if (StringUtils.isNotBlank(text)) {
         int number3 = Integer.valueOf(text);
         cell.setRowspan(number3);
      }

      String text2 = element.attributeValue("colspan");
      if (StringUtils.isNotBlank(text2)) {
         int number4 = Integer.valueOf(text2);
         cell.setColspan(number4);
      }

      cell.setRow(number);
      cell.setCol(number2);
   }
}
