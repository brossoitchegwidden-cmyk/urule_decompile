package com.bstek.urule.parse;

import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.library.constant.Constant;
import com.bstek.urule.model.library.constant.ConstantCategory;
import com.bstek.urule.model.library.constant.ConstantLibrary;
import java.util.ArrayList;
import org.dom4j.Element;

public class ConstantLibraryParser implements Parser<ConstantLibrary> {
   public static final String BEAN_ID = "urule.constantLibraryParser";

   public ConstantLibrary parse(Element var1) {
      ArrayList var2 = new ArrayList();

      for (Object var4 : var1.elements()) {
         if (var4 != null && var4 instanceof Element) {
            Element var5 = (Element)var4;
            if (var5.getName().equals("category")) {
               var2.add(this.a(var5));
            }
         }
      }

      ConstantLibrary var6 = new ConstantLibrary();
      var6.setCategories(var2);
      return var6;
   }

   private ConstantCategory a(Element var1) {
      ConstantCategory var2 = new ConstantCategory();
      var2.setName(var1.attributeValue("name"));
      var2.setUuid(var1.attributeValue("uuid"));
      var2.setLabel(var1.attributeValue("label"));

      for (Object var4 : var1.elements()) {
         if (var4 != null && var4 instanceof Element) {
            Element var5 = (Element)var4;
            if (var5.getName().equals("constant")) {
               var2.addConstant(this.b(var5));
            }
         }
      }

      return var2;
   }

   private Constant b(Element var1) {
      Constant var2 = new Constant();
      var2.setUuid(var1.attributeValue("uuid"));
      var2.setName(var1.attributeValue("name"));
      var2.setLabel(var1.attributeValue("label"));
      var2.setType(Datatype.valueOf(var1.attributeValue("type")));
      return var2;
   }

   @Override
   public boolean support(String var1) {
      return var1.equals("constant-library");
   }
}
