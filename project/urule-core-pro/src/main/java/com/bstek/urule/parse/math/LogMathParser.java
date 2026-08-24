package com.bstek.urule.parse.math;

import com.bstek.urule.model.rule.math.LogMath;
import com.bstek.urule.model.rule.math.MathSign;
import com.bstek.urule.parse.ValueParser;
import org.dom4j.Element;

public class LogMathParser extends MathParser {
   public LogMathParser(ValueParser valueParser) {
      super(valueParser);
   }

   @Override
   public boolean support(String name) {
      return name.equals("log-sign");
   }

   public MathSign parse(Element element) {
      LogMath logMath = new LogMath();

      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            if (element2.getName().equals("base")) {
               logMath.setBaseValue(this.parseValue(element2));
            } else if (element2.getName().equals("real")) {
               logMath.setValue(this.parseValue(element2));
            }
         }
      }

      return logMath;
   }
}
