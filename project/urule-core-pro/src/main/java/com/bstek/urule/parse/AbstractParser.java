package com.bstek.urule.parse;

import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rule.Parameter;
import com.bstek.urule.model.rule.Value;
import java.util.ArrayList;
import java.util.List;
import org.dom4j.Element;

public abstract class AbstractParser<T> implements Parser<T> {
   protected List<Parameter> a(Element var1, ValueParser var2) {
      ArrayList var3 = new ArrayList();

      for (Object var5 : var1.elements()) {
         if (var5 != null && var5 instanceof Element) {
            Element var6 = (Element)var5;
            if (var6.getName().equals("parameter")) {
               Parameter var7 = new Parameter();
               var7.setName(var6.attributeValue("name"));
               var7.setType(Datatype.valueOf(var6.attributeValue("type")));

               for (Object var9 : var6.elements()) {
                  if (var9 != null && var9 instanceof Element) {
                     Element var10 = (Element)var9;
                     if (var2.support(var10.getName())) {
                        Value var11 = var2.parse(var10);
                        var7.setValue(var11);
                        break;
                     }
                  }
               }

               var3.add(var7);
            }
         }
      }

      return var3;
   }
}
