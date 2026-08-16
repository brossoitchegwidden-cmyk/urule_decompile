package com.bstek.urule.runtime.log;

import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.model.rule.lhs.Criteria;
import java.util.Date;
import java.util.Set;

public class MatchedRuleLog extends DataLog {
   private static final String b = "√√规则【%s】匹配，(%s)";
   private static final String c = "√√Rule【%s】 is matched，(%s)";
   private static final String d = "，条件：";
   private static final String e = "，conditions：";
   private static final String f = "，条件：无";
   private static final String g = "，conditions：none";
   private Integer h;
   private String i;
   private String j;
   private Date k;
   private Date l;
   private Boolean m;
   private Boolean n;
   private String o;
   private String p;

   public MatchedRuleLog(Rule var1, Set<Criteria> var2) {
      this.i = var1.getName();
      this.j = var1.getFile();
      this.h = var1.getSalience();
      this.k = var1.getEffectiveDate();
      this.l = var1.getExpiresDate();
      this.m = var1.getEnabled();
      this.n = var1.getDebug();
      this.o = var1.getMutexGroup();
      this.p = var1.getPendedGroup();
      String var3 = this.a() ? "√√Rule【%s】 is matched，(%s)" : "√√规则【%s】匹配，(%s)";
      this.a = String.format(var3, var1.getName(), this.j);
      if (var2.size() > 0) {
         this.a = this.a + (this.a() ? "，conditions：" : "，条件：" + this.a(var2));
      } else {
         this.a = this.a + (this.a() ? "，conditions：none" : "，条件：无");
      }
   }

   private String a(Set<Criteria> var1) {
      StringBuilder var2 = new StringBuilder();

      for (Criteria var4 : var1) {
         if (var2.length() > 0) {
            var2.append("◆");
         }

         var2.append(var4.getId());
      }

      return var2.toString();
   }

   public String getRuleFile() {
      return this.j;
   }

   public String getRuleName() {
      return this.i;
   }

   public Integer getSalience() {
      return this.h;
   }

   public String getActivationGroup() {
      return this.o;
   }

   public String getAgendaGroup() {
      return this.p;
   }

   public Boolean getDebug() {
      return this.n;
   }

   public Date getEffectiveDate() {
      return this.k;
   }

   public Boolean getEnabled() {
      return this.m;
   }

   public Date getExpiresDate() {
      return this.l;
   }

   @Override
   public String toString() {
      return "MatchedRuleLog [ruleName=" + this.i + ", ruleFile=" + this.j + ", salience=" + this.h + "]";
   }
}
