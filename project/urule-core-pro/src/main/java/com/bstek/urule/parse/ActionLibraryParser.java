package com.bstek.urule.parse;

import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.library.action.ActionLibrary;
import com.bstek.urule.model.library.action.Method;
import com.bstek.urule.model.library.action.Parameter;
import com.bstek.urule.model.library.action.SpringBean;
import java.util.UUID;
import org.dom4j.Element;

public class ActionLibraryParser implements Parser<ActionLibrary> {
   public ActionLibrary parse(Element var1) {
      ActionLibrary var2 = new ActionLibrary();

      for (Object var4 : var1.elements()) {
         if (var4 != null && var4 instanceof Element) {
            Element var5 = (Element)var4;
            if (var5.getName().equals("spring-bean")) {
               var2.addSpringBean(this.a(var5));
            }
         }
      }

      return var2;
   }

   private SpringBean a(Element var1) {
      SpringBean var2 = new SpringBean();
      var2.setId(var1.attributeValue("id"));
      var2.setUuid(var1.attributeValue("uuid"));
      var2.setName(var1.attributeValue("name"));

      for (Object var4 : var1.elements()) {
         if (var4 != null && var4 instanceof Element) {
            Element var5 = (Element)var4;
            Method var6 = new Method();
            var6.setMethodName(var5.attributeValue("method-name"));
            var6.setUuid(var5.attributeValue("uuid"));
            var6.setName(var5.attributeValue("name"));

            for (Object var8 : var5.elements()) {
               if (var8 != null && var8 instanceof Element) {
                  Element var9 = (Element)var8;
                  if (var9.getName().equals("parameter")) {
                     Parameter var10 = new Parameter();
                     var10.setUuid(UUID.randomUUID().toString());
                     var10.setName(var9.attributeValue("name"));
                     var10.setType(Datatype.valueOf(var9.attributeValue("type")));
                     var6.addParameter(var10);
                  }
               }
            }

            var2.addMethod(var6);
         }
      }

      return var2;
   }

   @Override
   public boolean support(String var1) {
      return var1.equals("action-library");
   }
}
