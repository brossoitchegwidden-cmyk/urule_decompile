package com.bstek.urule.console.database.model;

import java.util.List;

public class Page {
   private int a = 1;
   private int b = 10;
   private long c;
   private long d;
   private int e;
   private long f;
   private List g;

   public Page(int var1, int var2) {
      this.a = var1;
      this.b = var2;
      this.c = var1 <= 1 ? 0L : (long)((var1 - 1) * var2);
      this.d = (long)(var2 - 1);
   }

   public int getPageNumber() {
      return this.a;
   }

   public int getPageSize() {
      return this.b;
   }

   private void a() {
      if (this.b > 0) {
         this.e = Double.valueOf(Math.ceil(Double.valueOf((double)this.f) / (double)this.b)).intValue();
         this.c = this.a <= 1 ? 0L : (long)((this.a - 1) * this.b);
         this.d = this.c + (long)this.b - 1L;
      } else {
         this.e = 0;
         this.c = 0L;
         this.d = (long)(this.b - 1);
      }

      if (this.a > this.e) {
         this.a = this.e;
      }

   }

   public long getTotalRows() {
      return this.f;
   }

   public void setTotalRows(long var1) {
      this.f = var1;
      this.a();
   }

   public List getData() {
      return this.g;
   }

   public void setData(List var1) {
      this.g = var1;
   }

   public long getStartRow() {
      return this.c;
   }

   public long getEndRow() {
      return this.d;
   }

   public void setEndRow(long var1) {
      this.d = var1;
   }

   public int getTotalPage() {
      return this.e;
   }

   public void setTotalPage(int var1) {
      this.e = var1;
   }

   public void setStartRow(long var1) {
      this.c = var1;
   }
}
