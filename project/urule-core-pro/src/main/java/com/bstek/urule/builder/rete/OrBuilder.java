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
   public List<BaseReteNode> buildCriterion(BaseCriterion baseCriterion, BuildContext context) {
      Or or = (Or)baseCriterion;
      List criterions = or.getCriterions();
      if (criterions != null && criterions.size() != 0) {
         ArrayList criterion2 = new ArrayList();

         for (Criterion criterion : (Iterable<Criterion>)(Iterable<?>)(criterions)) {
            List criterion3 = this.buildCriterion((Criterion)criterion, context, (List<BaseReteNode>)null);
            if (criterion3 != null) {
               criterion2.addAll(criterion3);
            }
         }

         if (criterion2.size() == 0) {
            return null;
         }

         if (criterion2.size() == 1) {
            return criterion2;
         }

         OrNode orNode = new OrNode(context.nextId());

         for (BaseReteNode baseReteNode : (Iterable<BaseReteNode>)(Iterable<?>)(criterion2)) {
            baseReteNode.addLine(orNode);
         }

         ArrayList criterion4 = new ArrayList();
         criterion4.add(orNode);
         return criterion4;
      } else {
         throw new RuleException("Condition join node[or] need one child at least.");
      }
   }

   @Override
   public boolean support(Criterion criterion) {
      return criterion instanceof Or;
   }
}
