package com.bstek.urule.parse;

import com.bstek.urule.model.rule.Predefine;
import com.bstek.urule.model.rule.PredefineGroupDefinition;
import com.bstek.urule.model.rule.PredefineValueType;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.lhs.Junction;
import java.util.ArrayList;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

public class PredefinesParser implements Parser<PredefineGroupDefinition> {
   protected ValueParser a;
   private JunctionParser b;

   @Override
   public boolean support(String var1) {
      return var1.contentEquals("predefine-group");
   }

   public PredefineGroupDefinition parse(Element var1) {
      PredefineGroupDefinition var2 = new PredefineGroupDefinition();
      ArrayList var3 = new ArrayList();
      var2.setPredefines(var3);
      String var4 = var1.attributeValue("priority");
      if (StringUtils.isNotBlank(var4)) {
         var2.setPriority(Integer.valueOf(var4));
      }

      for (Object var6 : var1.elements()) {
         if (var6 != null && var6 instanceof Element) {
            Element var7 = (Element)var6;
            String var8 = var7.getName();
            if (var8.contentEquals("predefine")) {
               Predefine var9 = new Predefine();
               var3.add(var9);
               var9.setUuid(var7.attributeValue("uuid"));
               var9.setName(var7.attributeValue("name"));
               var9.setType(var7.attributeValue("type"));
               var9.setValueType(PredefineValueType.valueOf(var7.attributeValue("value-type")));
               if (!var9.getValueType().equals(PredefineValueType.none)) {
                  Element var10 = var7.element("value");
                  if (var10 != null) {
                     Value var11 = this.a.parse(var10);
                     var9.setValue(var11);
                  }
               }

               for (Object var15 : var7.elements()) {
                  if (var15 != null && var15 instanceof Element) {
                     Element var12 = (Element)var15;
                     if (var12.getName().equals("and") || var12.getName().equals("or")) {
                        Junction var13 = (Junction)this.b.parse(var12);
                        var9.setJunction(var13);
                        break;
                     }
                  }
               }
            }
         }
      }

      return var2;
   }

   public void setValueParser(ValueParser var1) {
      this.a = var1;
   }

   public void setJunctionParser(JunctionParser var1) {
      this.b = var1;
   }
}
