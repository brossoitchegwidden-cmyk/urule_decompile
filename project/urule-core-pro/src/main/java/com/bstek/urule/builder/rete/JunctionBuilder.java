package com.bstek.urule.builder.rete;

import com.bstek.urule.model.rete.BaseReteNode;
import com.bstek.urule.model.rule.lhs.BaseCriterion;
import com.bstek.urule.model.rule.lhs.Criteria;
import com.bstek.urule.model.rule.lhs.Criterion;
import com.bstek.urule.model.rule.lhs.Junction;
import com.bstek.urule.model.rule.lhs.Met;
import java.util.List;

public abstract class JunctionBuilder extends CriterionBuilder {
   protected List<BaseReteNode> a(Criterion var1, BuildContext var2, List<BaseReteNode> var3) {
      if (var1 instanceof Met) {
         return MetBuilder.ins.buildCriterion((BaseCriterion)var1, var3, var2);
      } else if (var1 instanceof Junction) {
         Junction var5 = (Junction)var1;
         return ReteBuilder.buildCriterion(var2, var5);
      } else if (var1 instanceof Criteria) {
         Criteria var4 = (Criteria)var1;
         return this.a(var4, var3, var2);
      } else {
         return null;
      }
   }
}
