package com.bstek.urule.parse.scorecard;

import com.bstek.urule.model.scorecard.AttributeRow;
import com.bstek.urule.model.scorecard.ConditionRow;
import com.bstek.urule.parse.Parser;
import java.util.ArrayList;
import org.dom4j.Element;

public class AttributeRowParser implements Parser<AttributeRow> {
   public AttributeRow parse(Element var1) {
      AttributeRow var2 = new AttributeRow();
      var2.setRowNumber(Integer.valueOf(var1.attributeValue("row-number")));
      ArrayList var3 = new ArrayList();

      for (Object var5 : var1.elements()) {
         if (var5 != null && var5 instanceof Element) {
            Element var6 = (Element)var5;
            if (var6.getName().equals("condition-row")) {
               ConditionRow var7 = new ConditionRow();
               var7.setRowNumber(Integer.valueOf(var6.attributeValue("row-number")));
               var3.add(var7);
            }
         }
      }

      var2.setConditionRows(var3);
      return var2;
   }

   @Override
   public boolean support(String var1) {
      return var1.equals("attribute-row");
   }
}
