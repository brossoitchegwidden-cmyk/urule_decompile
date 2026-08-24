package com.bstek.urule.parse.math;

import com.bstek.urule.model.rule.math.MathSign;
import com.bstek.urule.model.rule.math.SigmaMath;
import com.bstek.urule.parse.ValueParser;
import org.dom4j.Element;

public class SigmaMathParser extends MathParser {
   public SigmaMathParser(ValueParser valueParser) {
      super(valueParser);
   }

   @Override
   public boolean support(String name) {
      return name.equals("sigma-sign");
   }

   public MathSign parse(Element element) {
      SigmaMath sigmaMath = new SigmaMath();

      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            if (element2.getName().equals("expr")) {
               sigmaMath.setExpr(this.parseValue(element2));
            } else if (element2.getName().equals("ivalue")) {
               sigmaMath.setIvalue(this.parseValue(element2));
            } else if (element2.getName().equals("superior")) {
               sigmaMath.setSuperior(this.parseValue(element2));
            }
         }
      }

      return sigmaMath;
   }
}
