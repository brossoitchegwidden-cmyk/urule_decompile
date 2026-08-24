package com.bstek.urule.parse.math;

import com.bstek.urule.model.rule.math.ExtremumMath;
import com.bstek.urule.model.rule.math.MathSign;
import com.bstek.urule.parse.ValueParser;
import org.dom4j.Element;

public class ExtremumFunctionMathParser extends MathParser {
   public ExtremumFunctionMathParser(ValueParser valueParser) {
      super(valueParser);
   }

   @Override
   public boolean support(String name) {
      return name.equals("extremum");
   }

   public MathSign parse(Element element) {
      ExtremumMath extremumMath = new ExtremumMath();
      extremumMath.setName(element.attributeValue("name"));
      extremumMath.setValue1(this.parseValue(element.element("value1")));
      extremumMath.setValue2(this.parseValue(element.element("value2")));
      return extremumMath;
   }
}
