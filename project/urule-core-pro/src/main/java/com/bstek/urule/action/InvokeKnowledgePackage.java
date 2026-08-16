package com.bstek.urule.action;

import org.apache.commons.lang.StringUtils;

public class InvokeKnowledgePackage {
   private String a;
   private String b;
   private String c;
   private long d;

   public InvokeKnowledgePackage(String var1, String var2, long var3, String var5) {
      this.a = var1;
      this.b = var2;
      this.d = var3;
      if (StringUtils.isNotBlank(var5)) {
         this.c = var5;
      } else {
         this.c = String.valueOf(var3);
      }
   }

   public String getProject() {
      return this.a;
   }

   public String getName() {
      return this.b;
   }

   public long getId() {
      return this.d;
   }

   public String getCode() {
      return this.c;
   }

   public void setCode(String var1) {
      this.c = var1;
   }

   public void setName(String var1) {
      this.b = var1;
   }

   public void setProject(String var1) {
      this.a = var1;
   }
}
