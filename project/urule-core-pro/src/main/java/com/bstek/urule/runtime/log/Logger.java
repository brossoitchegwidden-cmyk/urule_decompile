package com.bstek.urule.runtime.log;

import com.bstek.urule.Utils;
import com.bstek.urule.builder.resource.ResourceType;
import com.bstek.urule.model.flow.DecisionNode;
import com.bstek.urule.model.flow.ExceptionNode;
import com.bstek.urule.model.flow.FlowNode;
import com.bstek.urule.model.rule.Parameter;
import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.model.rule.lhs.Criteria;
import com.bstek.urule.model.rule.lhs.EvaluateResponse;
import com.bstek.urule.runtime.KnowledgeSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Logger {
   private UnitLog logUnit;
   private List<Log> logs = new ArrayList<>();

   public Logger(KnowledgeSession parentSession) {
      if (parentSession != null) {
         Logger logger = parentSession.getLogManager().getLogger();
         this.logUnit = new UnitLog();
         logger.appendLog(this.logUnit);
      }
   }

   public void logMet(int met, int matchedCount, boolean only) {
      if (Utils.isDebug()) {
         this.appendLog(new MetLog(met, matchedCount, only));
      }
   }

   public void logCriteria(Criteria criteria, EvaluateResponse response) {
      if (Utils.isDebug()) {
         this.appendLog(new CriteriaLog(criteria, response));
      }
   }

   public void logMatchRule(Rule rule, Set<Criteria> criterias) {
      if (Utils.isDebug()) {
         if (!rule.isTargetResource(ResourceType.Flow)) {
            this.appendLog(new MatchedRuleLog(rule, criterias));
         }
      }
   }

   public void logExecuteRule(Rule rule) {
      if (Utils.isDebug()) {
         this.appendLog(new ExecuteRuleLog(rule));
      }
   }

   public void logIFErrorLog(Parameter param, Exception ex) {
      if (Utils.isDebug()) {
         this.appendLog(new IFErrorLog(param, ex));
      }
   }

   public void logDecisionNodeMatch(DecisionNode node, String file, String to) {
      if (Utils.isDebug()) {
         this.appendLog(new DecisionNodeMatchLog(node, file, to));
      }
   }

   public void logExceptionNode(ExceptionNode node, Exception ex, String file) {
      if (Utils.isDebug()) {
         this.appendLog(new ExceptionFlowNodeLog(node, file, ex));
      }
   }

   public void logFlowNode(FlowNode node, String file, boolean enter) {
      if (Utils.isDebug()) {
         this.appendLog(new FlowNodeLog(node, file, enter));
      }
   }

   public void logConsoleOutput(Object obj) {
      if (Utils.isDebug()) {
         this.appendLog(new ConsoleOutputLog(obj));
      }
   }

   public void logMessage(String msg) {
      if (Utils.isDebug()) {
         this.appendLog(new MessageLog(msg));
      }
   }

   public void logAddRuleToExecuteQueue(Rule rule, boolean add) {
      if (Utils.isDebug()) {
         this.appendLog(new AddRuleToExecuteQueueLog(rule, add));
      }
   }

   public void logExecuteFunction(String functionName, Object object) {
      if (Utils.isDebug()) {
         this.appendLog(new ExecuteFunctionLog(functionName, object));
      }
   }

   public void logExecuteBeanMethod(String methodInfo, String parameterInfo) {
      if (Utils.isDebug()) {
         this.appendLog(new ExecuteBeanMethodLog(methodInfo, parameterInfo));
      }
   }

   public void logValueAssign(String left, Object right) {
      if (Utils.isDebug()) {
         this.appendLog(new ValueAssignLog(left, right));
      }
   }

   public void logScoreCard(String name, String path) {
      if (Utils.isDebug()) {
         this.appendLog(new ScoreCardLog(name, path));
      }
   }

   public void logExecuteScoreCard(int rowNumber, Object value) {
      if (Utils.isDebug()) {
         this.appendLog(new ExcecuteScoreCardLog(rowNumber, value));
      }
   }

   public void logScoreCardSum(String cardName, Object value) {
      if (Utils.isDebug()) {
         this.appendLog(new ScoreCardSumLog(cardName, value));
      }
   }

   public void logScoreCardBean(String bean) {
      if (Utils.isDebug()) {
         this.appendLog(new ScoreCardBean(bean));
      }
   }

   private void appendLog(Log log) {
      this.logs.add(log);
      if (this.logUnit != null) {
         this.logUnit.addLog(log);
      }
   }

   public UnitLog getLogUnit() {
      return this.logUnit;
   }

   public List<Log> getLogs() {
      return this.logs;
   }
}
