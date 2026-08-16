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
   private Logger a;
   private Collection<LogWriter> b;
   private List<MatchedRuleLog> c;
   private List<RuleData> d = new ArrayList<>();

   public LogManager(KnowledgeSession var1) {
      this.a = new Logger(var1);
      ApplicationContext var2 = Utils.getApplicationContext();
      this.b = var2.getBeansOfType(LogWriter.class).values();
      String var3 = PropertyConfigurer.getProperty("urule.runtime.log.language");
      if (StringUtils.isNotBlank(var3) && var3.equals("en")) {
         LocaleHolder.setLocale(Locale.ENGLISH);
      }
   }

   public List<MatchedRuleLog> buildMatchedRuleLog() {
      if (this.c != null) {
         return this.c;
      }

      this.c = new ArrayList<>();
      this.a(this.c, this.a.getLogs());
      return this.c;
   }

   public void clean() {
      this.a.getLogs().clear();
      if (this.c != null) {
         this.c.clear();
         this.c = null;
      }
   }

   private void a(List<MatchedRuleLog> var1, List<Log> var2) {
      for (Log var4 : var2) {
         if (var4 instanceof MatchedRuleLog) {
            var1.add((MatchedRuleLog)var4);
         } else if (var4 instanceof UnitLog) {
            UnitLog var5 = (UnitLog)var4;
            this.a(var1, var5.getLogs());
         }
      }
   }

   public List<RuleData> buildNotMatchRuleData() {
      ArrayList var1 = new ArrayList();
      List var2 = this.buildMatchedRuleLog();
      ArrayList var3 = new ArrayList();
      var3.addAll(this.d);

      for (RuleData var5 : (Iterable<RuleData>)(Iterable<?>)(var3)) {
         boolean var6 = false;

         for (MatchedRuleLog var8 : (Iterable<MatchedRuleLog>)(Iterable<?>)(var2)) {
            if (var5.getFile().equals(var8.getRuleFile()) && var5.getName().equals(var8.getRuleName())) {
               var6 = true;
               break;
            }
         }

         if (!var6) {
            var1.add(var5);
         }
      }

      return var1;
   }

   public List<FlowNodeLog> buildFlowNodeData() {
      ArrayList var1 = new ArrayList();
      this.b(var1, this.a.getLogs());
      return var1;
   }

   private void b(List<FlowNodeLog> var1, List<Log> var2) {
      for (Log var4 : var2) {
         if (var4 instanceof FlowNodeLog) {
            FlowNodeLog var5 = (FlowNodeLog)var4;
            if (var5.isEnter()) {
               var1.add(var5);
            }
         } else if (var4 instanceof UnitLog) {
            UnitLog var6 = (UnitLog)var4;
            this.b(var1, var6.getLogs());
         }
      }
   }

   public void writeLog() {
      try {
         for (LogWriter var2 : this.b) {
            var2.write(this.a.getLogs());
         }
      } catch (Exception var3) {
         throw new RuleException(var3);
      }
   }

   public void addRuleData(List<RuleData> var1) {
      if (var1 != null) {
         this.d.addAll(var1);
      }
   }

   public Logger getLogger() {
      return this.a;
   }

   public List<RuleData> getRuleData() {
      return this.d;
   }
}
