package com.bstek.urule.runtime.log;

import com.bstek.urule.LocaleHolder;
import com.bstek.urule.PropertyConfigurer;
import com.bstek.urule.Utils;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.rete.RuleData;
import com.bstek.urule.runtime.KnowledgeSession;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;

public class LogManager {
   private Logger logger;
   private Collection<LogWriter> logWriters;
   private List<MatchedRuleLog> matchedRuleLogs;
   private List<RuleData> ruleData = new ArrayList<>();

   public LogManager(KnowledgeSession parentSession) {
      this.logger = new Logger(parentSession);
      ApplicationContext applicationContext = Utils.getApplicationContext();
      this.logWriters = applicationContext.getBeansOfType(LogWriter.class).values();
      String property = PropertyConfigurer.getProperty("urule.runtime.log.language");
      if (StringUtils.isNotBlank(property) && property.equals("en")) {
         LocaleHolder.setLocale(Locale.ENGLISH);
      }
   }

   public List<MatchedRuleLog> buildMatchedRuleLog() {
      if (this.matchedRuleLogs != null) {
         return this.matchedRuleLogs;
      }

      this.matchedRuleLogs = new ArrayList<>();
      this.collectMatchedRuleLogs(this.matchedRuleLogs, this.logger.getLogs());
      return this.matchedRuleLogs;
   }

   public void clean() {
      this.logger.getLogs().clear();
      if (this.matchedRuleLogs != null) {
         this.matchedRuleLogs.clear();
         this.matchedRuleLogs = null;
      }
   }

   private void collectMatchedRuleLogs(List<MatchedRuleLog> matchedRuleLogs, List<Log> logs) {
      for (Log log : logs) {
         if (log instanceof MatchedRuleLog) {
            matchedRuleLogs.add((MatchedRuleLog)log);
         } else if (log instanceof UnitLog) {
            UnitLog unitLog = (UnitLog)log;
            this.collectMatchedRuleLogs(matchedRuleLogs, unitLog.getLogs());
         }
      }
   }

   public List<RuleData> buildNotMatchRuleData() {
      ArrayList notMatchRuleData = new ArrayList();
      List matchedRuleLog = this.buildMatchedRuleLog();
      ArrayList items = new ArrayList();
      items.addAll(this.ruleData);

      for (RuleData ruleData : (Iterable<RuleData>)(Iterable<?>)(items)) {
         boolean flag = false;

         for (MatchedRuleLog matchedRuleLog2 : (Iterable<MatchedRuleLog>)(Iterable<?>)(matchedRuleLog)) {
            if (ruleData.getFile().equals(matchedRuleLog2.getRuleFile()) && ruleData.getName().equals(matchedRuleLog2.getRuleName())) {
               flag = true;
               break;
            }
         }

         if (!flag) {
            notMatchRuleData.add(ruleData);
         }
      }

      return notMatchRuleData;
   }

   public List<FlowNodeLog> buildFlowNodeData() {
      ArrayList flowNodeData = new ArrayList();
      this.collectFlowNodeLogs(flowNodeData, this.logger.getLogs());
      return flowNodeData;
   }

   private void collectFlowNodeLogs(List<FlowNodeLog> flowNodeLogs, List<Log> logs) {
      for (Log log : logs) {
         if (log instanceof FlowNodeLog) {
            FlowNodeLog flowNodeLog = (FlowNodeLog)log;
            if (flowNodeLog.isEnter()) {
               flowNodeLogs.add(flowNodeLog);
            }
         } else if (log instanceof UnitLog) {
            UnitLog unitLog = (UnitLog)log;
            this.collectFlowNodeLogs(flowNodeLogs, unitLog.getLogs());
         }
      }
   }

   public void writeLog() {
      try {
         for (LogWriter logWriter : this.logWriters) {
            logWriter.write(this.logger.getLogs());
         }
      } catch (Exception exception) {
         throw new RuleException(exception);
      }
   }

   public void addRuleData(List<RuleData> list) {
      if (list != null) {
         this.ruleData.addAll(list);
      }
   }

   public Logger getLogger() {
      return this.logger;
   }

   public List<RuleData> getRuleData() {
      return this.ruleData;
   }
}
