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
   public List<BaseReteNode> buildCriterion(BaseCriterion var1, BuildContext var2) {
      And var3 = (And)var1;
      AndNode var4 = null;
      List var5 = var3.getCriterions();
      if (var5 != null && var5.size() != 0) {
         BaseReteNode var6 = null;

         for (Criterion var8 : (Iterable<Criterion>)(Iterable<?>)(var5)) {
            ArrayList var9 = new ArrayList();
            if (var6 != null) {
               var9.add(var6);
            }

            List var10 = this.a(var8, var2, var9);
            if (var10 != null) {
               for (BaseReteNode var12 : (Iterable<BaseReteNode>)(Iterable<?>)(var10)) {
                  if (var12 instanceof CriteriaNode) {
                     if (var6 != null) {
                        List var13 = var6.getChildrenNodes();
                        if (!var13.contains(var12)) {
                           if (var4 == null) {
                              var4 = new AndNode(var2.nextId());
                           }

                           var6.addLine(var4);
                        }
                     }

                     var6 = var12;
                  } else if (var12 instanceof MetNode) {
                     if (var6 != null) {
                        List var15 = var6.getChildrenNodes();
                        if (!var15.contains(var12)) {
                           if (var4 == null) {
                              var4 = new AndNode(var2.nextId());
                           }

                           var6.addLine(var4);
                        }
                     }

                     var6 = var12;
                  } else if (var12 instanceof JunctionNode) {
                     if (var4 == null) {
                        var4 = new AndNode(var2.nextId());
                     }

                     ((JunctionNode)var12).addLine(var4);
                  }
               }
            }
         }

         ArrayList var14 = new ArrayList();
         if (var5.size() == 1 && var6 != null) {
            var14.add(var6);
            return var14;
         }

         if (var4 == null) {
            var14.add(var6);
            return var14;
         }

         if (var4 != null && var6 != null) {
            var6.addLine(var4);
         }

         var14.add(var4);
         return var14;
      } else {
         throw new RuleException("Condition join node[and] need one child at least.");
      }
   }

   @Override
   public boolean support(Criterion var1) {
      return var1 instanceof And;
   }
}
