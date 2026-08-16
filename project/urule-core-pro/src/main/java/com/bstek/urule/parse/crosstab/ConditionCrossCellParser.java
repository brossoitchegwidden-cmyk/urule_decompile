package com.bstek.urule.parse.crosstab;

import com.bstek.urule.model.crosstab.ConditionCrossCell;
import com.bstek.urule.parse.Parser;
import com.bstek.urule.parse.table.JointParser;
import org.dom4j.Element;

public class ConditionCrossCellParser extends CrossCellParser implements Parser<ConditionCrossCell> {
   private JointParser a;

   public ConditionCrossCell parse(Element var1) {
      ConditionCrossCell var2 = new ConditionCrossCell();
      this.a(var2, var1);

      for (Object var4 : var1.elements()) {
         if (var4 != null && var4 instanceof Element) {
            Element var5 = (Element)var4;
            if (this.a.support(var5.getName())) {
               var2.setJoint(this.a.parse(var5));
            }
         }
      }

      return var2;
   }

   @Override
   public boolean support(String var1) {
      return "condition-cell".equals(var1);
   }

   public void setJointParser(JointParser var1) {
      this.a = var1;
   }
}
