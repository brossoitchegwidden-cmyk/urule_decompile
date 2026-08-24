package com.bstek.urule.parse;

import com.bstek.urule.action.Action;
import com.bstek.urule.action.ConsolePrintAction;
import org.dom4j.Element;

public class ConsolePrintActionParser extends ActionParser {
   public Action parse(Element element) {
      ConsolePrintAction consolePrintAction = new ConsolePrintAction();

      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            if (this.valueParser.support(element2.getName())) {
               consolePrintAction.setValue(this.valueParser.parse(element2));
               break;
            }
         }
      }

      return consolePrintAction;
   }

   @Override
   public boolean support(String name) {
      return name.equals("console-print");
   }
}
