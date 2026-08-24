package com.bstek.urule.parse.scorecard;

import com.bstek.urule.model.scorecard.AttributeRow;
import com.bstek.urule.model.scorecard.ConditionRow;
import com.bstek.urule.parse.Parser;
import java.util.ArrayList;
import org.dom4j.Element;

public class AttributeRowParser implements Parser<AttributeRow> {
   public AttributeRow parse(Element element) {
      AttributeRow attributeRow = new AttributeRow();
      attributeRow.setRowNumber(Integer.valueOf(element.attributeValue("row-number")));
      ArrayList items = new ArrayList();

      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            if (element2.getName().equals("condition-row")) {
               ConditionRow conditionRow = new ConditionRow();
               conditionRow.setRowNumber(Integer.valueOf(element2.attributeValue("row-number")));
               items.add(conditionRow);
            }
         }
      }

      attributeRow.setConditionRows(items);
      return attributeRow;
   }

   @Override
   public boolean support(String name) {
      return name.equals("attribute-row");
   }
}
