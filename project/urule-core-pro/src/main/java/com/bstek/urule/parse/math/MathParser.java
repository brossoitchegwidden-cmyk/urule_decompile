package com.bstek.urule.parse.math;

import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.math.MathSign;
import com.bstek.urule.parse.Parser;
import com.bstek.urule.parse.ValueParser;
import org.dom4j.Element;

public abstract class MathParser implements Parser<MathSign> {
   protected ValueParser valueParser;

   public MathParser(ValueParser valueParser) {
      this.valueParser = valueParser;
   }

   protected Value parseValue(Element element) {
      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            if (this.valueParser.support(element2.getName())) {
               return this.valueParser.parse(element2);
            }
         }
      }

      throw new RuleException("Unknow value element[" + element.asXML() + "]");
   }
}
