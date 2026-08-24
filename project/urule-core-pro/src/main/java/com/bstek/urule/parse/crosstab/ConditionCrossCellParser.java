package com.bstek.urule.parse.crosstab;

import com.bstek.urule.model.crosstab.ConditionCrossCell;
import com.bstek.urule.parse.Parser;
import com.bstek.urule.parse.table.JointParser;
import org.dom4j.Element;

public class ConditionCrossCellParser extends CrossCellParser implements Parser<ConditionCrossCell> {
   private JointParser jointParser;

   public ConditionCrossCell parse(Element element) {
      ConditionCrossCell conditionCrossCell = new ConditionCrossCell();
      this.parseCrossCell(conditionCrossCell, element);

      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            if (this.jointParser.support(element2.getName())) {
               conditionCrossCell.setJoint(this.jointParser.parse(element2));
            }
         }
      }

      return conditionCrossCell;
   }

   @Override
   public boolean support(String name) {
      return "condition-cell".equals(name);
   }

   public void setJointParser(JointParser jointParser) {
      this.jointParser = jointParser;
   }
}
