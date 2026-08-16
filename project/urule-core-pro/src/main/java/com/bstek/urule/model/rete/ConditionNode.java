package com.bstek.urule.model.rete;

import com.bstek.urule.model.rule.lhs.BaseCriteria;
import java.util.List;

public interface ConditionNode {
   String getCriteriaInfo();

   BaseCriteria getCriteria();

   List<ReteNode> getChildrenNodes();

   Line addLine(ReteNode var1);
}
