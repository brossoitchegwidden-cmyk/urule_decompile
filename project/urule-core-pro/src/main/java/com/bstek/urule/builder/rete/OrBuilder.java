package com.bstek.urule.builder.rete;

import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.rete.BaseReteNode;
import com.bstek.urule.model.rete.OrNode;
import com.bstek.urule.model.rule.lhs.BaseCriterion;
import com.bstek.urule.model.rule.lhs.Criterion;
import com.bstek.urule.model.rule.lhs.Or;
import java.util.ArrayList;
import java.util.List;

public class OrBuilder extends JunctionBuilder {
   @Override
   public List<BaseReteNode> buildCriterion(BaseCriterion var1, BuildContext var2) {
      Or var3 = (Or)var1;
      List var4 = var3.getCriterions();
      if (var4 != null && var4.size() != 0) {
         ArrayList var5 = new ArrayList();

         for (Criterion var7 : (Iterable<Criterion>)(Iterable<?>)(var4)) {
            List var8 = this.a((Criterion)var7, var2, (List<BaseReteNode>)null);
            if (var8 != null) {
               var5.addAll(var8);
            }
         }

         if (var5.size() == 0) {
            return null;
         }

         if (var5.size() == 1) {
            return var5;
         }

         OrNode var9 = new OrNode(var2.nextId());

         for (BaseReteNode var12 : (Iterable<BaseReteNode>)(Iterable<?>)(var5)) {
            var12.addLine(var9);
         }

         ArrayList var11 = new ArrayList();
         var11.add(var9);
         return var11;
      } else {
         throw new RuleException("Condition join node[or] need one child at least.");
      }
   }

   @Override
   public boolean support(Criterion var1) {
      return var1 instanceof Or;
   }
}
