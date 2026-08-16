package com.bstek.urule.builder.rete;

import com.bstek.urule.model.rete.BaseReteNode;
import com.bstek.urule.model.rete.MetNode;
import com.bstek.urule.model.rete.ObjectTypeNode;
import com.bstek.urule.model.rule.lhs.BaseCriterion;
import com.bstek.urule.model.rule.lhs.Criterion;
import com.bstek.urule.model.rule.lhs.Met;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class MetBuilder {
   public static final MetBuilder ins = new MetBuilder();

   private MetBuilder() {
   }

   public List<BaseReteNode> buildCriterion(BaseCriterion var1, List<BaseReteNode> var2, BuildContext var3) {
      MetNode var4 = this.a(var3, (Met)var1);
      ArrayList var5 = new ArrayList();
      if (var2 != null && var2.size() > 0) {
         for (BaseReteNode var12 : var2) {
            var12.addLine(var4);
            var5.add(var4);
         }
      } else {
         HashSet var6 = new HashSet();

         for (String var9 : var3.getObjectTypeByCriterions(var4.getCriterions())) {
            if (!var6.contains(var9)) {
               var6.add(var9);
               if (var9.equals("*")) {
                  var9 = HashMap.class.getName();
               }

               ObjectTypeNode var10 = var3.buildObjectTypeNode(var9);
               var10.addLine(var4);
            }
         }

         var5.add(var4);
      }

      return var5;
   }

   private MetNode a(BuildContext var1, Met var2) {
      MetNode var3 = new MetNode(var1.nextId(), var1.currentRuleIsDebug());
      var3.setMet(var2.getMet());
      var3.setOnly(var2.isOnly());
      var3.setCriterions(var2.getCriterions());
      return var3;
   }

   public boolean support(Criterion var1) {
      return var1 instanceof Met;
   }
}
