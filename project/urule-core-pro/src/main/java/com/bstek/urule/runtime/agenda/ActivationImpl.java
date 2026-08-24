package com.bstek.urule.runtime.agenda;

import com.bstek.urule.Utils;
import com.bstek.urule.action.Action;
import com.bstek.urule.action.ActionType;
import com.bstek.urule.exception.RuleAssertException;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.rule.Rhs;
import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.model.rule.lhs.Criteria;
import com.bstek.urule.model.rule.loop.LoopRule;
import com.bstek.urule.model.scorecard.runtime.ScoreRule;
import com.bstek.urule.runtime.rete.Context;
import com.bstek.urule.runtime.rete.ExecutionContextImpl;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ActivationImpl implements Activation {
   private Rule rule;
   private Set<Criteria> criterias;
   private Map<String, Object> factMap;

   public ActivationImpl(Rule rule) {
      this.rule = rule;
   }

   @Override
   public void execute(Context context) {
      try {
         if (this.rule.getDebug() != null && this.rule.getDebug()) {
            context.getLogger().logExecuteRule(this.rule);
         }

         context.cleanTipMsg();
         context.addTipMsg("执行规则[" + this.rule.getName() + "(" + this.rule.getFile() + ")]动作");
         ((ExecutionContextImpl)context).setCurrentRule(this.rule);
         Date date = new Date();
         Date effectiveDate = this.rule.getEffectiveDate();
         if (effectiveDate == null || effectiveDate.compareTo(date) <= 0) {
            Date expiresDate = this.rule.getExpiresDate();
            if (expiresDate == null || expiresDate.compareTo(date) >= 0) {
               ((ExecutionContextImpl)context).setCurrentRuleFactMap(this.factMap);
               ((ExecutionContextImpl)context).setCurrentRuleCriterias(this.criterias);
               if (this.rule instanceof LoopRule) {
                  LoopRule loopRule = (LoopRule)this.rule;
                  loopRule.execute(context, this.factMap);
               } else if (this.rule instanceof ScoreRule) {
                  ScoreRule scoreRule = (ScoreRule)this.rule;
                  scoreRule.execute(context, this.factMap);
               } else {
                  this.executeActions(context, this.factMap);
               }

               context.cleanTipMsg();
            }
         }
      } catch (Exception exception) {
         String tipMsg = context.getTipMsg();
         throw new RuleAssertException(tipMsg, exception);
      }
   }

   private void executeActions(Context context, Map<String, Object> valuesByKey) {
      Rhs rhs = this.rule.getRhs();
      if (rhs != null) {
         List actions = rhs.getActions();
         if (actions != null) {
            int number = 1;

            for (Action action : (Iterable<Action>)(Iterable<?>)(actions)) {
               if (this.rule.getDebug() != null) {
                  action.setDebug(this.rule.getDebug());
               }

               context.addTipMsg("动作" + number + "." + this.getActionDescription(action.getActionType()) + "");
               action.execute(context, valuesByKey);
               number++;
            }
         }
      }
   }

   private String getActionDescription(ActionType actionType) {
      String text = "未知";
      switch (actionType) {
         case ConsolePrint:
            text = "控制台输出";
            break;
         case ExecuteCommonFunction:
            text = "执行函数";
            break;
         case ExecuteMethod:
            text = "执行方法";
            break;
         case Scoring:
            text = "评分卡得分计算";
            break;
         case VariableAssign:
            text = "变量赋值";
            break;
         case TemplateAction:
            throw new RuleException("Unsupport action type:" + ActionType.TemplateAction);
      }

      return text;
   }

   @Override
   public Rule convertToElseRule() {
      this.rule = Utils.buildElseRule(this.rule);
      return this.rule;
   }

   public void setCriterias(Set<Criteria> criterias) {
      this.criterias = criterias;
   }

   public void setFactMap(Map<String, Object> factMap) {
      this.factMap = factMap;
   }

   @Override
   public Rule getRule() {
      return this.rule;
   }

   public void setRule(Rule rule) {
      this.rule = rule;
   }

   public int compareTo(Activation activation) {
      Integer salience = activation.getRule().getSalience();
      Integer salience2 = this.rule.getSalience();
      if (salience != null && salience2 != null) {
         return salience - salience2;
      } else if (salience != null) {
         return 1;
      } else {
         return salience2 != null ? -1 : 0;
      }
   }
}
