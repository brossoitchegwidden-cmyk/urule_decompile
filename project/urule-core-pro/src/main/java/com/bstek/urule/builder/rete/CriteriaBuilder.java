package com.bstek.urule.builder.rete;

import com.bstek.urule.model.rete.BaseReteNode;
import com.bstek.urule.model.rule.lhs.BaseCriterion;
import com.bstek.urule.model.rule.lhs.Criteria;
import com.bstek.urule.model.rule.lhs.Criterion;
import java.util.List;

public class CriteriaBuilder extends CriterionBuilder {
   @Override
   public List<BaseReteNode> buildCriterion(BaseCriterion baseCriterion, BuildContext context) {
      Criteria criteria = (Criteria)baseCriterion;
      return this.buildCriteria(criteria, null, context);
   }

   @Override
   public boolean support(Criterion criterion) {
      return criterion instanceof Criteria;
   }
}
