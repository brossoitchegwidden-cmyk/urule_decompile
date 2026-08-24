package com.bstek.urule.parse.crosstab;

import com.bstek.urule.model.crosstab.HeaderCell;
import com.bstek.urule.parse.Parser;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

public class HeaderCellParser implements Parser<HeaderCell> {
   public HeaderCell parse(Element element) {
      HeaderCell headerCell = new HeaderCell();
      String text2 = element.attributeValue("rowspan");
      if (StringUtils.isNotBlank(text2)) {
         int number = Integer.valueOf(text2);
         headerCell.setRowspan(number);
      }

      String text3 = element.attributeValue("colspan");
      if (StringUtils.isNotBlank(text3)) {
         int number2 = Integer.valueOf(text3);
         headerCell.setColspan(number2);
      }

      String text = element.getText();
      headerCell.setText(text);
      return headerCell;
   }

   @Override
   public boolean support(String name) {
      return "header".equals(name);
   }
}
