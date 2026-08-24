package com.bstek.urule.builder.rete;

import com.bstek.urule.model.rete.BaseReteNode;
import com.bstek.urule.model.rule.lhs.BaseCriterion;
import com.bstek.urule.model.rule.lhs.Criteria;
import com.bstek.urule.model.rule.lhs.Criterion;
import com.bstek.urule.model.rule.lhs.Junction;
import com.bstek.urule.model.rule.lhs.Met;
import java.util.List;

public abstract class JunctionBuilder extends CriterionBuilder {
   protected List<BaseReteNode> buildCriterion(Criterion criterion, BuildContext context, List<BaseReteNode> prevCriteriaNodes) {
      if (criterion instanceof Met) {
         return MetBuilder.ins.buildCriterion((BaseCriterion)criterion, prevCriteriaNodes, context);
      } else if (criterion instanceof Junction) {
         Junction junction = (Junction)criterion;
         return ReteBuilder.buildCriterion(context, junction);
      } else if (criterion instanceof Criteria) {
         Criteria criteria = (Criteria)criterion;
         return this.buildCriteria(criteria, prevCriteriaNodes, context);
      } else {
         return null;
      }
   }
}
