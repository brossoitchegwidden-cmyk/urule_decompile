package com.bstek.urule.parse.scorecard;

import com.bstek.urule.model.scorecard.ComplexColumn;
import com.bstek.urule.model.scorecard.ComplexColumnType;
import com.bstek.urule.parse.Parser;
import org.dom4j.Element;

public class ComplexColumnParser implements Parser<ComplexColumn> {
   public ComplexColumn parse(Element var1) {
      ComplexColumn var2 = new ComplexColumn();
      var2.setNum(Integer.valueOf(var1.attributeValue("num")));
      var2.setType(ComplexColumnType.valueOf(var1.attributeValue("type")));
      var2.setVariableCategory(var1.attributeValue("var-category"));
      var2.setUuid(var1.attributeValue("uuid"));
      var2.setWidth(Double.valueOf(var1.attributeValue("width")).intValue());
      var2.setCustomLabel(var1.attributeValue("custom-label"));
      return var2;
   }

   @Override
   public boolean support(String var1) {
      return var1.equals("col");
   }
}
