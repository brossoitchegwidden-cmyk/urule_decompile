package com.bstek.urule.runtime.log;

import com.bstek.urule.model.rule.Rule;
import java.util.Date;

public class AddRuleToExecuteQueueLog extends DataLog {
   private static final String b = "规则 【%s】(%s)，已被添加到执行队列";
   private static final String c = "The rule 【%s】(%s) has been added to the execution queue";
   private static final String d = "规则 【%s】(%s)，未被添加到执行队列,因当前匹配操作在工作区更新情况下发生，且当前规则在执行队列中已存在！";
   private static final String e = "Rule 【%s】(%s)，which was not added to the execution queue because the current match operation occurred with a workspace update, and the current rule already exists in the execution queue!";
   private Integer f;
   private String g;
   private String h;
   private Date i;
   private Date j;
   private Boolean k;
   private Boolean l;
   private String m;
   private String n;
   private boolean o;

   public AddRuleToExecuteQueueLog(Rule var1, boolean var2) {
      this.g = var1.getName();
      this.h = var1.getFile();
      this.f = var1.getSalience();
      this.i = var1.getEffectiveDate();
      this.j = var1.getExpiresDate();
      this.k = var1.getEnabled();
      this.l = var1.getDebug();
      this.m = var1.getMutexGroup();
      this.n = var1.getPendedGroup();
      this.o = var2;
      String var3 = "";
      if (var2) {
         var3 = this.a() ? "The rule 【%s】(%s) has been added to the execution queue" : "规则 【%s】(%s)，已被添加到执行队列";
      } else {
         var3 = this.a()
            ? "Rule 【%s】(%s)，which was not added to the execution queue because the current match operation occurred with a workspace update, and the current rule already exists in the execution queue!"
            : "规则 【%s】(%s)，未被添加到执行队列,因当前匹配操作在工作区更新情况下发生，且当前规则在执行队列中已存在！";
      }

      this.a = "》》》" + String.format(var3, var1.getName(), this.h);
   }

   public String getRuleFile() {
      return this.h;
   }

   public String getRuleName() {
      return this.g;
   }

   public Integer getSalience() {
      return this.f;
   }

   public String getActivationGroup() {
      return this.m;
   }

   public String getAgendaGroup() {
      return this.n;
   }

   public Boolean getDebug() {
      return this.l;
   }

   public Date getEffectiveDate() {
      return this.i;
   }

   public Boolean getEnabled() {
      return this.k;
   }

   public Date getExpiresDate() {
      return this.j;
   }

   public boolean isAdd() {
      return this.o;
   }
}
