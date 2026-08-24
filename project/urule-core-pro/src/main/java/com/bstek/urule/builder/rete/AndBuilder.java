package com.bstek.urule.builder.rete;

import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.rete.AndNode;
import com.bstek.urule.model.rete.BaseReteNode;
import com.bstek.urule.model.rete.CriteriaNode;
import com.bstek.urule.model.rete.JunctionNode;
import com.bstek.urule.model.rete.MetNode;
import com.bstek.urule.model.rule.lhs.And;
import com.bstek.urule.model.rule.lhs.BaseCriterion;
import com.bstek.urule.model.rule.lhs.Criterion;
import java.util.ArrayList;
import java.util.List;

public class AndBuilder extends JunctionBuilder {
   @Override
   public List<BaseReteNode> buildCriterion(BaseCriterion baseCriterion, BuildContext context) {
      And and = (And)baseCriterion;
      AndNode andNode = null;
      List criterions = and.getCriterions();
      if (criterions != null && criterions.size() != 0) {
         BaseReteNode baseReteNode = null;

         for (Criterion criterion : (Iterable<Criterion>)(Iterable<?>)(criterions)) {
            ArrayList items = new ArrayList();
            if (baseReteNode != null) {
               items.add(baseReteNode);
            }

            List criterion2 = this.buildCriterion(criterion, context, items);
            if (criterion2 != null) {
               for (BaseReteNode baseReteNode2 : (Iterable<BaseReteNode>)(Iterable<?>)(criterion2)) {
                  if (baseReteNode2 instanceof CriteriaNode) {
                     if (baseReteNode != null) {
                        List childrenNodes = baseReteNode.getChildrenNodes();
                        if (!childrenNodes.contains(baseReteNode2)) {
                           if (andNode == null) {
                              andNode = new AndNode(context.nextId());
                           }

                           baseReteNode.addLine(andNode);
                        }
                     }

                     baseReteNode = baseReteNode2;
                  } else if (baseReteNode2 instanceof MetNode) {
                     if (baseReteNode != null) {
                        List childrenNodes2 = baseReteNode.getChildrenNodes();
                        if (!childrenNodes2.contains(baseReteNode2)) {
                           if (andNode == null) {
                              andNode = new AndNode(context.nextId());
                           }

                           baseReteNode.addLine(andNode);
                        }
                     }

                     baseReteNode = baseReteNode2;
                  } else if (baseReteNode2 instanceof JunctionNode) {
                     if (andNode == null) {
                        andNode = new AndNode(context.nextId());
                     }

                     ((JunctionNode)baseReteNode2).addLine(andNode);
                  }
               }
            }
         }

         ArrayList criterion3 = new ArrayList();
         if (criterions.size() == 1 && baseReteNode != null) {
            criterion3.add(baseReteNode);
            return criterion3;
         }

         if (andNode == null) {
            criterion3.add(baseReteNode);
            return criterion3;
         }

         if (andNode != null && baseReteNode != null) {
            baseReteNode.addLine(andNode);
         }

         criterion3.add(andNode);
         return criterion3;
      } else {
         throw new RuleException("Condition join node[and] need one child at least.");
      }
   }

   @Override
   public boolean support(Criterion criterion) {
      return criterion instanceof And;
   }
}
