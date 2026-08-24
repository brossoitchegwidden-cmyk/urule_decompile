package com.bstek.urule.parse.math;

import com.bstek.urule.model.rule.math.MathSign;
import com.bstek.urule.model.rule.math.TriangleFunctionMath;
import com.bstek.urule.parse.ValueParser;
import org.dom4j.Element;

public class TriangleFunctionMathParser extends MathParser {
   public TriangleFunctionMathParser(ValueParser valueParser) {
      super(valueParser);
   }

   @Override
   public boolean support(String name) {
      return name.equals("triangle");
   }

   public MathSign parse(Element element) {
      TriangleFunctionMath triangleFunctionMath = new TriangleFunctionMath();
      triangleFunctionMath.setName(element.attributeValue("name"));
      triangleFunctionMath.setValue(this.parseValue(element));
      return triangleFunctionMath;
   }
}
