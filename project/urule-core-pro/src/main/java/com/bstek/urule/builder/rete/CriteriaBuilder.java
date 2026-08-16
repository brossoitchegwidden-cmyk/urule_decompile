package com.bstek.urule.builder.rete;

import com.bstek.urule.model.rete.BaseReteNode;
import com.bstek.urule.model.rule.lhs.BaseCriterion;
import com.bstek.urule.model.rule.lhs.Criteria;
import com.bstek.urule.model.rule.lhs.Criterion;
import java.util.List;

public class CriteriaBuilder extends CriterionBuilder {
   @Override
   public List<BaseReteNode> buildCriterion(BaseCriterion var1, BuildContext var2) {
      Criteria var3 = (Criteria)var1;
      return this.a(var3, null, var2);
   }

   @Override
   public boolean support(Criterion var1) {
      return var1 instanceof Criteria;
   }
}
