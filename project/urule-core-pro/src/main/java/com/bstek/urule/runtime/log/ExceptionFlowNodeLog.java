package com.bstek.urule.runtime.log;

import com.bstek.urule.model.flow.ExceptionNode;

public class ExceptionFlowNodeLog extends DataLog {
   private static final String b = ">>>>节点【%s】捕获到异常【%s】";
   private static final String c = ">>>>node 【%s】 throw exception 【%s】";
   private String d;
   private String e;
   private Exception f;

   public ExceptionFlowNodeLog(ExceptionNode var1, String var2, Exception var3) {
      this.d = var2;
      this.f = var3;
      this.e = var1.getName();
      String var4 = this.a() ? ">>>>node 【%s】 throw exception 【%s】" : ">>>>节点【%s】捕获到异常【%s】";
      this.a = String.format(var4, var1.getName(), var3.getClass().getName());
   }

   public String getNodeName() {
      return this.e;
   }

   public Exception getException() {
      return this.f;
   }

   public String getFile() {
      return this.d;
   }
}
