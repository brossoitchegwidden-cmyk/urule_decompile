package com.bstek.urule.builder.rete;

import com.bstek.urule.Utils;
import com.bstek.urule.model.Node;
import com.bstek.urule.model.rete.BaseReteNode;
import com.bstek.urule.model.rete.ConditionNode;
import com.bstek.urule.model.rete.CriteriaNode;
import com.bstek.urule.model.rete.MetNode;
import com.bstek.urule.model.rete.ObjectTypeNode;
import com.bstek.urule.model.rete.ReteNode;
import com.bstek.urule.model.rule.lhs.BaseCriteria;
import com.bstek.urule.model.rule.lhs.BaseCriterion;
import com.bstek.urule.model.rule.lhs.Criteria;
import com.bstek.urule.model.rule.lhs.Criterion;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class CriterionBuilder {
   public abstract List<BaseReteNode> buildCriterion(BaseCriterion baseCriterion, BuildContext context);

   public abstract boolean support(Criterion criterion);

   protected List<BaseReteNode> buildCriteria(Criteria criteria, List<BaseReteNode> prevNodes, BuildContext context) {
      ArrayList criteria2 = new ArrayList();
      if (Utils.isDebug() && context.currentRule() != null) {
         criteria.setFile(context.currentRule().getFile());
      }

      List objectType = context.getObjectType(criteria);
      if (prevNodes != null && prevNodes.size() > 0) {
         for (BaseReteNode baseReteNode : prevNodes) {
            boolean flag = true;
            List items = null;
            if (baseReteNode instanceof MetNode) {
               new ArrayList();
               MetNode metNode = (MetNode)baseReteNode;
               items = context.getObjectTypeByCriterions(metNode.getCriterions());
            } else {
               items = context.getObjectType(((ConditionNode)baseReteNode).getCriteria());
            }

            if (items.size() == objectType.size()) {
               for (String text : (Iterable<String>)(Iterable<?>)(items)) {
                  if (!objectType.contains(text)) {
                     flag = false;
                     break;
                  }
               }
            } else {
               flag = false;
            }

            ReteNode reteNode = null;
            if (flag) {
               List childrenNodes = baseReteNode.getChildrenNodes();
               reteNode = this.resolveCriteriaNode(criteria, childrenNodes);
               if (reteNode == null) {
                  reteNode = new CriteriaNode(criteria, context.nextId(), context.currentRuleIsDebug());
                  baseReteNode.addLine(reteNode);
               }

               criteria2.add(reteNode);
            } else {
               CriteriaNode criteriaNode = this.resolveCriteriaNode(criteria, context, objectType);
               criteria2.add(criteriaNode);
            }
         }
      } else {
         CriteriaNode criteriaNode2 = this.resolveCriteriaNode(criteria, context, objectType);
         criteria2.add(criteriaNode2);
      }

      return criteria2;
   }

   private CriteriaNode resolveCriteriaNode(BaseCriteria baseCriteria, BuildContext buildContext, List<String> strings) {
      CriteriaNode criteriaNode = null;
      ObjectTypeNode objectTypeNode = null;

      for (String text : strings) {
         if (text.equals("*")) {
            text = HashMap.class.getName();
         }

         objectTypeNode = buildContext.buildObjectTypeNode(text);
         if (criteriaNode == null) {
            List childrenNodes = objectTypeNode.getChildrenNodes();
            criteriaNode = this.resolveCriteriaNode(baseCriteria, childrenNodes);
         } else {
            List childrenNodes2 = objectTypeNode.getChildrenNodes();
            if (!childrenNodes2.contains(criteriaNode)) {
               objectTypeNode.addLine(criteriaNode);
            }
         }
      }

      if (criteriaNode == null) {
         for (String name : strings) {
            if (name.equals("*")) {
               name = HashMap.class.getName();
            }

            objectTypeNode = buildContext.buildObjectTypeNode(name);
            if (criteriaNode == null) {
               criteriaNode = new CriteriaNode((Criteria)baseCriteria, buildContext.nextId(), buildContext.currentRuleIsDebug());
               objectTypeNode.addLine(criteriaNode);
            } else {
               objectTypeNode.addLine(criteriaNode);
            }
         }
      }

      return criteriaNode;
   }

   private CriteriaNode resolveCriteriaNode(BaseCriteria baseCriteria, List<ReteNode> reteNodes) {
      String id = baseCriteria.getId();
      CriteriaNode criteriaNode = null;

      for (Node node : reteNodes) {
         if (node instanceof ConditionNode && (!(baseCriteria instanceof Criteria) || node instanceof CriteriaNode)) {
            ConditionNode conditionNode = (ConditionNode)node;
            String criteriaInfo = conditionNode.getCriteriaInfo();
            if (criteriaInfo.equals(id)) {
               criteriaNode = (CriteriaNode)conditionNode;
               break;
            }
         }
      }

      return criteriaNode;
   }
}
