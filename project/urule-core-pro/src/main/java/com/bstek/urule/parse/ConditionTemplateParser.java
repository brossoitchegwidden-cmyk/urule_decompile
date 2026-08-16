package com.bstek.urule.parse;

import com.bstek.urule.builder.RulesRebuilder;
import com.bstek.urule.model.library.ResourceLibrary;
import com.bstek.urule.model.rule.Library;
import com.bstek.urule.model.template.ConditionTemplate;
import com.bstek.urule.model.template.ConditionTemplateUnit;
import java.util.ArrayList;
import java.util.List;
import org.dom4j.Element;

public class ConditionTemplateParser extends LibrariesParser<ConditionTemplate> {
   private RulesRebuilder a;
   private LhsParser b;

   public ConditionTemplate parse(Element var1) {
      ConditionTemplate var2 = new ConditionTemplate();
      ArrayList var3 = new ArrayList();
      var2.setTemplates(var3);

      for (Object var5 : var1.elements()) {
         if (var5 != null && var5 instanceof Element) {
            Element var6 = (Element)var5;
            String var7 = var6.getName();
            Library var8 = this.a(var6);
            if (var8 != null) {
               var2.addLibrary(var8);
            } else if (var7.equals("template")) {
               var3.add(this.b(var6));
            }
         }
      }

      List var9 = var2.getLibraries();
      if (var9 != null) {
         ResourceLibrary var10 = this.a.getResourceLibraryBuilder().buildResourceLibrary(var9, null);

         for (ConditionTemplateUnit var12 : var2.getTemplates()) {
            this.a.rebuildCriterion(var12.getCriterion(), var10, false);
         }
      }

      return var2;
   }

   private ConditionTemplateUnit b(Element var1) {
      ConditionTemplateUnit var2 = new ConditionTemplateUnit();
      var2.setId(var1.attributeValue("id"));
      var2.setName(var1.attributeValue("name"));
      var2.setCriterion(this.b.parseCriterion(var1));
      return var2;
   }

   @Override
   public boolean support(String var1) {
      return var1.equals("templates");
   }

   public void setLhsParser(LhsParser var1) {
      this.b = var1;
   }

   public void setRulesRebuilder(RulesRebuilder var1) {
      this.a = var1;
   }
}
