package com.bstek.urule.parse.crosstab;

import com.bstek.urule.model.crosstab.ValueCrossCell;
import com.bstek.urule.parse.Parser;
import com.bstek.urule.parse.ValueParser;
import org.dom4j.Element;

public class ValueCrossCellParser extends CrossCellParser implements Parser<ValueCrossCell> {
   private ValueParser valueParser;

   public ValueCrossCell parse(Element element) {
      ValueCrossCell valueCrossCell = new ValueCrossCell();
      this.parseCrossCell(valueCrossCell, element);

      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            if (this.valueParser.support(element2.getName())) {
               valueCrossCell.setValue(this.valueParser.parse(element2));
               break;
            }
         }
      }

      return valueCrossCell;
   }

   @Override
   public boolean support(String name) {
      return "value-cell".equals(name);
   }

   public void setValueParser(ValueParser valueParser) {
      this.valueParser = valueParser;
   }
}
