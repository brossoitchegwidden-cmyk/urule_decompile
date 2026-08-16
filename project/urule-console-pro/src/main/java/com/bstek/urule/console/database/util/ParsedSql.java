package com.bstek.urule.console.database.util;

import java.util.ArrayList;
import java.util.List;

public class ParsedSql {
   private String a;
   private List b = new ArrayList();
   private List c = new ArrayList();
   private int d;
   private int e;
   private int f;

   ParsedSql(String var1) {
      this.a = var1;
   }

   public String getOriginalSql() {
      return this.a;
   }

   void a(String var1, int var2, int var3) {
      this.b.add(var1);
      this.c.add(new int[]{var2, var3});
   }

   public List getParameterNames() {
      return this.b;
   }

   public int[] getParameterIndexes(int var1) {
      return (int[])this.c.get(var1);
   }

   void a(int var1) {
      this.d = var1;
   }

   public int getNamedParameterCount() {
      return this.d;
   }

   void b(int var1) {
      this.e = var1;
   }

   public int getUnnamedParameterCount() {
      return this.e;
   }

   void c(int var1) {
      this.f = var1;
   }

   public int getTotalParameterCount() {
      return this.f;
   }

   public String toString() {
      return this.a;
   }
}
