package com.bstek.urule.parse;

import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.library.action.Method;
import com.bstek.urule.model.library.action.Parameter;
import com.bstek.urule.model.library.action.SpringBean;
import org.dom4j.Element;

public class SpringBeanParser implements Parser<SpringBean> {
   public SpringBean parse(Element var1) {
      SpringBean var2 = new SpringBean();
      var2.setId(var1.attributeValue("id"));
      var2.setUuid(var1.attributeValue("uuid"));

      for (Object var4 : var1.elements()) {
         if (var4 != null && var4 instanceof Element) {
            Element var5 = (Element)var4;
            if (var5.getName().equals("method")) {
               Method var6 = this.a(var5);
               var2.addMethod(var6);
            }
         }
      }

      return var2;
   }

   private Method a(Element var1) {
      Method var2 = new Method();
      var2.setName(var1.attributeValue("name"));
      var2.setUuid(var1.attributeValue("uuid"));

      for (Object var4 : var1.elements()) {
         if (var4 != null && var4 instanceof Element) {
            Element var5 = (Element)var4;
            if (var5.getName().equals("parameter")) {
               Parameter var6 = new Parameter();
               var6.setType(Datatype.valueOf(var5.attributeValue("type")));
               var2.addParameter(var6);
            }
         }
      }

      return var2;
   }

   @Override
   public boolean support(String var1) {
      return var1.equals("spring-bean");
   }
}
