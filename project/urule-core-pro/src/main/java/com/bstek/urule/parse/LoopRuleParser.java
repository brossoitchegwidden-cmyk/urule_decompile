package com.bstek.urule.parse;

import com.bstek.urule.model.rule.loop.LoopEnd;
import com.bstek.urule.model.rule.loop.LoopRule;
import com.bstek.urule.model.rule.loop.LoopRuleUnit;
import com.bstek.urule.model.rule.loop.LoopStart;
import com.bstek.urule.model.rule.loop.LoopTarget;
import com.bstek.urule.model.rule.loop.LoopTargetType;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

public class LoopRuleParser extends AbstractRuleParser<LoopRule> {
   private ValueParser d;

   public LoopRule parse(Element var1) {
      LoopRule var2 = new LoopRule();
      this.parseRule(var2, var1);
      LoopStart var3 = new LoopStart();
      var2.setLoopStart(var3);
      LoopEnd var4 = new LoopEnd();
      var2.setLoopEnd(var4);
      String var5 = var1.attributeValue("loop-target-type");
      if (StringUtils.isNotBlank(var5)) {
         var2.setLoopTargetType(LoopTargetType.valueOf(var5));
      }

      for (Object var7 : var1.elements()) {
         if (var7 != null && var7 instanceof Element) {
            Element var8 = (Element)var7;
            String var9 = var8.getName();
            if (var9.equals("loop-start")) {
               var3.setActions(this.b.parseActions(var8));
            } else if (var9.equals("loop-end")) {
               var4.setActions(this.b.parseActions(var8));
            } else if (var9.equals("loop-target")) {
               LoopTarget var10 = new LoopTarget();
               var2.setLoopTarget(var10);

               for (Object var12 : var8.elements()) {
                  if (var12 != null && var12 instanceof Element) {
                     Element var13 = (Element)var12;
                     if (this.d.support(var13.getName())) {
                        var10.setValue(this.d.parse(var13));
                        break;
                     }
                  }
               }
            } else if (var9.equals("units")) {
               var2.setUnits(this.a(var8));
            }
         }
      }

      this.a(var2);
      return var2;
   }

   private void a(LoopRule var1) {
      if (var1.getUnits() == null || var1.getUnits().size() <= 0) {
         LoopRuleUnit var2 = new LoopRuleUnit();
         var2.setLhs(var1.getLhs());
         var2.setOther(var1.getOther());
         var2.setRhs(var1.getRhs());
         var1.setLhs(null);
         var1.setRhs(null);
         var1.setOther(null);
         ArrayList var3 = new ArrayList();
         var3.add(var2);
         var1.setUnits(var3);
      }
   }

   private List<LoopRuleUnit> a(Element var1) {
      ArrayList var2 = new ArrayList();

      for (Object var4 : var1.elements()) {
         if (var4 != null && var4 instanceof Element) {
            Element var5 = (Element)var4;
            if (var5.getName().equals("unit")) {
               LoopRuleUnit var6 = this.b(var5);
               var2.add(var6);
            }
         }
      }

      return var2;
   }

   private LoopRuleUnit b(Element var1) {
      LoopRuleUnit var2 = new LoopRuleUnit();
      var2.setName(var1.attributeValue("name"));

      for (Object var4 : var1.elements()) {
         if (var4 != null && var4 instanceof Element) {
            Element var5 = (Element)var4;
            if (this.a.support(var5.getName())) {
               var2.setLhs(this.a.parse(var5));
            } else if (this.b.support(var5.getName())) {
               var2.setRhs(this.b.parse(var5));
            } else if (this.c.support(var5.getName())) {
               var2.setOther(this.c.parse(var5));
            }
         }
      }

      return var2;
   }

   public void setValueParser(ValueParser var1) {
      this.d = var1;
   }

   @Override
   public boolean support(String var1) {
      return var1.equals("loop-rule");
   }
}
