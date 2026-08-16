package com.bstek.urule.parse;

import com.bstek.urule.model.library.variable.Variable;
import java.util.ArrayList;
import java.util.List;
import org.dom4j.Element;

public class ParameterLibraryParser implements Parser<List<Variable>> {
   private VariableParser a;

   public List<Variable> parse(Element var1) {
      ArrayList var2 = new ArrayList();

      for (Object var4 : var1.elements()) {
         if (var4 != null && var4 instanceof Element) {
            Element var5 = (Element)var4;
            String var6 = var5.getName();
            if (var6.equals("parameter")) {
               var2.add(this.a.parse(var5));
            }
         }
      }

      return var2;
   }

   @Override
   public boolean support(String var1) {
      return var1.equals("parameter-library");
   }

   public void setVariableParser(VariableParser var1) {
      this.a = var1;
   }
}
