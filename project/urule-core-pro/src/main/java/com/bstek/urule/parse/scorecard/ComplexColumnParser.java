package com.bstek.urule.parse.scorecard;

import com.bstek.urule.model.scorecard.ComplexColumn;
import com.bstek.urule.model.scorecard.ComplexColumnType;
import com.bstek.urule.parse.Parser;
import org.dom4j.Element;

public class ComplexColumnParser implements Parser<ComplexColumn> {
   public ComplexColumn parse(Element element) {
      ComplexColumn complexColumn = new ComplexColumn();
      complexColumn.setNum(Integer.valueOf(element.attributeValue("num")));
      complexColumn.setType(ComplexColumnType.valueOf(element.attributeValue("type")));
      complexColumn.setVariableCategory(element.attributeValue("var-category"));
      complexColumn.setUuid(element.attributeValue("uuid"));
      complexColumn.setWidth(Double.valueOf(element.attributeValue("width")).intValue());
      complexColumn.setCustomLabel(element.attributeValue("custom-label"));
      return complexColumn;
   }

   @Override
   public boolean support(String name) {
      return name.equals("col");
   }
}
