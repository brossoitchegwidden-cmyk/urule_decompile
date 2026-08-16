package com.bstek.urule.parse;

import com.bstek.urule.action.Action;
import com.bstek.urule.action.ConsolePrintAction;
import org.dom4j.Element;

public class ConsolePrintActionParser extends ActionParser {
   public Action parse(Element var1) {
      ConsolePrintAction var2 = new ConsolePrintAction();

      for (Object var4 : var1.elements()) {
         if (var4 != null && var4 instanceof Element) {
            Element var5 = (Element)var4;
            if (this.a.support(var5.getName())) {
               var2.setValue(this.a.parse(var5));
               break;
            }
         }
      }

      return var2;
   }

   @Override
   public boolean support(String var1) {
      return var1.equals("console-print");
   }
}
