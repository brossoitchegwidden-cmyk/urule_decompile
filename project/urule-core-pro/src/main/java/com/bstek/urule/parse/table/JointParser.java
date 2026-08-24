package com.bstek.urule.parse.table;

import com.bstek.urule.model.rule.Op;
import com.bstek.urule.model.table.Condition;
import com.bstek.urule.model.table.Joint;
import com.bstek.urule.model.table.JointType;
import com.bstek.urule.parse.Parser;
import com.bstek.urule.parse.ValueParser;
import org.dom4j.Element;

public class JointParser implements Parser<Joint> {
   private ValueParser valueParser;

   public Joint parse(Element element) {
      Joint joint = new Joint();
      joint.setType(JointType.valueOf(element.attributeValue("type")));

      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            if (element2.getName().equals("condition")) {
               joint.addCondition(this.parseCondition(element2));
            } else if (this.support(element2.getName())) {
               joint.addJoint(this.parse(element2));
            }
         }
      }

      return joint;
   }

   public Condition parseCondition(Element element) {
      Condition condition = new Condition();
      condition.setOp(Op.valueOf(element.attributeValue("op")));

      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            if (this.valueParser.support(element2.getName())) {
               condition.setValue(this.valueParser.parse(element2));
               break;
            }
         }
      }

      return condition;
   }

   @Override
   public boolean support(String name) {
      return name.equals("joint");
   }

   public void setValueParser(ValueParser valueParser) {
      this.valueParser = valueParser;
   }
}
