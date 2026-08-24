package com.bstek.urule.parse;

import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rule.Parameter;
import com.bstek.urule.model.rule.Value;
import java.util.ArrayList;
import java.util.List;
import org.dom4j.Element;

public abstract class AbstractParser<T> implements Parser<T> {
   protected List<Parameter> parseParameters(Element element, ValueParser valueParser) {
      ArrayList parameters = new ArrayList();

      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            if (element2.getName().equals("parameter")) {
               Parameter parameter = new Parameter();
               parameter.setName(element2.attributeValue("name"));
               parameter.setType(Datatype.valueOf(element2.attributeValue("type")));

               for (Object objectValue2 : element2.elements()) {
                  if (objectValue2 != null && objectValue2 instanceof Element) {
                     Element element3 = (Element)objectValue2;
                     if (valueParser.support(element3.getName())) {
                        Value localValue = valueParser.parse(element3);
                        parameter.setValue(localValue);
                        break;
                     }
                  }
               }

               parameters.add(parameter);
            }
         }
      }

      return parameters;
   }
}
