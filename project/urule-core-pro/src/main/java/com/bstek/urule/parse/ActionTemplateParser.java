package com.bstek.urule.parse;

import com.bstek.urule.action.Action;
import com.bstek.urule.builder.RulesRebuilder;
import com.bstek.urule.model.library.ResourceLibrary;
import com.bstek.urule.model.rule.Library;
import com.bstek.urule.model.template.ActionTemplate;
import com.bstek.urule.model.template.ActionTemplateUnit;
import java.util.ArrayList;
import java.util.List;
import org.dom4j.Element;

public class ActionTemplateParser extends LibrariesParser<ActionTemplate> {
   private RhsParser a;
   private RulesRebuilder b;

   public ActionTemplate parse(Element var1) {
      ActionTemplate var2 = new ActionTemplate();
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

      List var11 = var2.getLibraries();
      if (var11 != null) {
         ResourceLibrary var12 = this.b.getResourceLibraryBuilder().buildResourceLibrary(var11, null);

         for (ActionTemplateUnit var14 : var2.getTemplates()) {
            List var15 = var14.getActions();
            if (var15 != null) {
               for (Action var10 : (Iterable<Action>)(Iterable<?>)(var15)) {
                  this.b.rebuildAction(var10, var12, false);
               }
            }
         }
      }

      return var2;
   }

   private ActionTemplateUnit b(Element var1) {
      ActionTemplateUnit var2 = new ActionTemplateUnit();
      var2.setId(var1.attributeValue("id"));
      var2.setName(var1.attributeValue("name"));
      var2.setActions(this.a.parseActions(var1));
      return var2;
   }

   @Override
   public boolean support(String var1) {
      return var1.equals("action-templates");
   }

   public void setRhsParser(RhsParser var1) {
      this.a = var1;
   }

   public void setRulesRebuilder(RulesRebuilder var1) {
      this.b = var1;
   }
}
