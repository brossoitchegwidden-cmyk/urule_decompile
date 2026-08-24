package com.bstek.urule.parse.table;

import com.bstek.urule.model.table.Row;
import com.bstek.urule.parse.Parser;
import org.dom4j.Element;

public class RowParser implements Parser<Row> {
   public Row parse(Element element) {
      Row row = new Row();
      row.setHeight(Integer.valueOf(element.attributeValue("height")));
      row.setNum(Integer.valueOf(element.attributeValue("num")));
      return row;
   }

   @Override
   public boolean support(String name) {
      return name.equals("row");
   }
}
