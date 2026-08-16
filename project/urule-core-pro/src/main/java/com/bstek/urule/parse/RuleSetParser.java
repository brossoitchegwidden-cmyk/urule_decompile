package com.bstek.urule.parse;

import com.bstek.urule.builder.RulesRebuilder;
import com.bstek.urule.model.rule.Library;
import com.bstek.urule.model.rule.ParentFile;
import com.bstek.urule.model.rule.PredefineGroupDefinition;
import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.model.rule.RuleSet;
import java.util.ArrayList;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

public class RuleSetParser extends LibrariesParser<RuleSet> {
   private RuleParser a;
   private PredefinesParser b;
   private LoopRuleParser c;
   private RulesRebuilder d;

   public RuleSet parse(Element var1) {
      RuleSet var2 = new RuleSet();
      String var3 = var1.attributeValue("alone");
      if (StringUtils.isNotBlank(var3)) {
         var2.setAlone(Boolean.valueOf(var3));
      }

      String var4 = var1.attributeValue("debug");
      if (StringUtils.isNotBlank(var4)) {
         var2.setDebug(Boolean.valueOf(var4));
      }

      ArrayList var5 = new ArrayList();
      var2.setParents(var5);
      String var6 = var1.attributeValue("parent");
      if (StringUtils.isNotBlank(var6)) {
         long var7 = Long.valueOf(var6);
         var5.add(new ParentFile(var7, "", null));
      }

      ArrayList var17 = new ArrayList();

      for (Object var9 : var1.elements()) {
         if (var9 != null && var9 instanceof Element) {
            Element var10 = (Element)var9;
            String var11 = var10.getName();
            Library var12 = this.a(var10);
            if (var12 != null) {
               var2.addLibrary(var12);
            } else if (this.a.support(var11)) {
               var17.add(this.a.parse(var10));
            } else if (var11.equals("quick-test-data")) {
               String var13 = var10.getTextTrim();
               var2.setQuickTestData(var13);
            } else if (this.c.support(var11)) {
               var17.add(this.c.parse(var10));
            } else if (this.b.support(var11)) {
               PredefineGroupDefinition var21 = this.b.parse(var10);
               var2.setPredefineGroup(var21);
            }

            if (var11.equals("remark")) {
               var2.setRemark(var10.getText());
            } else if (var11.contentEquals("extends-file") || var11.contentEquals("parent")) {
               String var22 = var10.attributeValue("path");
               long var14 = Long.valueOf(var10.attributeValue("id"));
               String var16 = var10.attributeValue("version");
               var5.add(new ParentFile(var14, var22, var16));
            }
         }
      }

      if (var2.isDebug()) {
         for (Rule var20 : (Iterable<Rule>)(Iterable<?>)(var17)) {
            if (var20.getDebug() == null) {
               var20.setDebug(true);
               var20.setDebugFromGlobal(true);
            }
         }
      }

      var2.setRules(var17);
      PredefineGroupDefinition var19 = var2.getPredefineGroup();
      if (var19 != null) {
         this.d.rebuildRules(var2.getLibraries(), var17, var19.getPredefines());
      } else {
         this.d.rebuildRules(var2.getLibraries(), var17, null);
      }

      return var2;
   }

   public void setRulesRebuilder(RulesRebuilder var1) {
      this.d = var1;
   }

   @Override
   public boolean support(String var1) {
      return var1.equals("rule-set");
   }

   public void setPredefinesParser(PredefinesParser var1) {
      this.b = var1;
   }

   public void setRuleParser(RuleParser var1) {
      this.a = var1;
   }

   public void setLoopRuleParser(LoopRuleParser var1) {
      this.c = var1;
   }
}
