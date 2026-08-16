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
   public abstract List<BaseReteNode> buildCriterion(BaseCriterion var1, BuildContext var2);

   public abstract boolean support(Criterion var1);

   protected List<BaseReteNode> a(Criteria var1, List<BaseReteNode> var2, BuildContext var3) {
      ArrayList var4 = new ArrayList();
      if (Utils.isDebug() && var3.currentRule() != null) {
         var1.setFile(var3.currentRule().getFile());
      }

      List var5 = var3.getObjectType(var1);
      if (var2 != null && var2.size() > 0) {
         for (BaseReteNode var7 : var2) {
            boolean var8 = true;
            List var9 = null;
            if (var7 instanceof MetNode) {
               new ArrayList();
               MetNode var10 = (MetNode)var7;
               var9 = var3.getObjectTypeByCriterions(var10.getCriterions());
            } else {
               var9 = var3.getObjectType(((ConditionNode)var7).getCriteria());
            }

            if (var9.size() == var5.size()) {
               for (String var11 : (Iterable<String>)(Iterable<?>)(var9)) {
                  if (!var5.contains(var11)) {
                     var8 = false;
                     break;
                  }
               }
            } else {
               var8 = false;
            }

            ReteNode var15 = null;
            if (var8) {
               List var18 = var7.getChildrenNodes();
               var15 = this.a(var1, var18);
               if (var15 == null) {
                  var15 = new CriteriaNode(var1, var3.nextId(), var3.currentRuleIsDebug());
                  var7.addLine(var15);
               }

               var4.add(var15);
            } else {
               CriteriaNode var17 = this.a(var1, var3, var5);
               var4.add(var17);
            }
         }
      } else {
         CriteriaNode var6 = this.a(var1, var3, var5);
         var4.add(var6);
      }

      return var4;
   }

   private CriteriaNode a(BaseCriteria var1, BuildContext var2, List<String> var3) {
      CriteriaNode var4 = null;
      ObjectTypeNode var5 = null;

      for (String var7 : var3) {
         if (var7.equals("*")) {
            var7 = HashMap.class.getName();
         }

         var5 = var2.buildObjectTypeNode(var7);
         if (var4 == null) {
            List var8 = var5.getChildrenNodes();
            var4 = this.a(var1, var8);
         } else {
            List var13 = var5.getChildrenNodes();
            if (!var13.contains(var4)) {
               var5.addLine(var4);
            }
         }
      }

      if (var4 == null) {
         for (String var12 : var3) {
            if (var12.equals("*")) {
               var12 = HashMap.class.getName();
            }

            var5 = var2.buildObjectTypeNode(var12);
            if (var4 == null) {
               var4 = new CriteriaNode((Criteria)var1, var2.nextId(), var2.currentRuleIsDebug());
               var5.addLine(var4);
            } else {
               var5.addLine(var4);
            }
         }
      }

      return var4;
   }

   private CriteriaNode a(BaseCriteria var1, List<ReteNode> var2) {
      String var3 = var1.getId();
      CriteriaNode var4 = null;

      for (Node var6 : var2) {
         if (var6 instanceof ConditionNode && (!(var1 instanceof Criteria) || var6 instanceof CriteriaNode)) {
            ConditionNode var7 = (ConditionNode)var6;
            String var8 = var7.getCriteriaInfo();
            if (var8.equals(var3)) {
               var4 = (CriteriaNode)var7;
               break;
            }
         }
      }

      return var4;
   }
}
