package com.bstek.urule.builder.rete;

import com.bstek.urule.model.rete.BaseReteNode;
import com.bstek.urule.model.rete.MetNode;
import com.bstek.urule.model.rete.ObjectTypeNode;
import com.bstek.urule.model.rule.lhs.BaseCriterion;
import com.bstek.urule.model.rule.lhs.Criterion;
import com.bstek.urule.model.rule.lhs.Met;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class MetBuilder {
   public static final MetBuilder ins = new MetBuilder();

   private MetBuilder() {
   }

   public List<BaseReteNode> buildCriterion(BaseCriterion baseCriterion, List<BaseReteNode> prevNodes, BuildContext context) {
      MetNode metNode = this.resolveMetNode(context, (Met)baseCriterion);
      ArrayList criterion = new ArrayList();
      if (prevNodes != null && prevNodes.size() > 0) {
         for (BaseReteNode baseReteNode : prevNodes) {
            baseReteNode.addLine(metNode);
            criterion.add(metNode);
         }
      } else {
         HashSet uniqueItems = new HashSet();

         for (String text : context.getObjectTypeByCriterions(metNode.getCriterions())) {
            if (!uniqueItems.contains(text)) {
               uniqueItems.add(text);
               if (text.equals("*")) {
                  text = HashMap.class.getName();
               }

               ObjectTypeNode objectTypeNode = context.buildObjectTypeNode(text);
               objectTypeNode.addLine(metNode);
            }
         }

         criterion.add(metNode);
      }

      return criterion;
   }

   private MetNode resolveMetNode(BuildContext buildContext, Met met) {
      MetNode metNode = new MetNode(buildContext.nextId(), buildContext.currentRuleIsDebug());
      metNode.setMet(met.getMet());
      metNode.setOnly(met.isOnly());
      metNode.setCriterions(met.getCriterions());
      return metNode;
   }

   public boolean support(Criterion criterion) {
      return criterion instanceof Met;
   }
}
