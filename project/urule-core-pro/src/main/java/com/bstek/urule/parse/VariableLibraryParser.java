package com.bstek.urule.parse;

import com.bstek.urule.model.library.variable.VariableCategory;
import java.util.ArrayList;
import java.util.List;
import org.dom4j.Element;

public class VariableLibraryParser implements Parser<List<VariableCategory>> {
   private VariableCategoryParser a;

   public List<VariableCategory> parse(Element var1) {
      ArrayList var2 = new ArrayList();

      for (Object var4 : var1.elements()) {
         if (var4 != null && var4 instanceof Element) {
            Element var5 = (Element)var4;
            String var6 = var5.getName();
            if (this.a.support(var6)) {
               var2.add(this.a.parse(var5));
            }
         }
      }

      return var2;
   }

   @Override
   public boolean support(String var1) {
      return var1.equals("variable-library");
   }

   public void setVariableCategoryParser(VariableCategoryParser var1) {
      this.a = var1;
   }
}
