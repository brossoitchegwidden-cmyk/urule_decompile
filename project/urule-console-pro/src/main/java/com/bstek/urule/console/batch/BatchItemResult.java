package com.bstek.urule.console.batch;

import com.bstek.urule.console.database.model.batch.BatchUpdateMode;

public class BatchItemResult {
   private String a;
   private int b = 0;
   private int c = 0;
   private int d = 0;
   private String e;
   private BatchUpdateMode f;
   private String g;
   private String h;

   public int getReadCount() {
      return this.b;
   }

   public void setReadCount(int var1) {
      this.b = var1;
   }

   public int getWriteCount() {
      return this.c;
   }

   public void setWriteCount(int var1) {
      this.c = var1;
   }

   public String getTableName() {
      return this.e;
   }

   public void setTableName(String var1) {
      this.e = var1;
   }

   public BatchUpdateMode getUpdateMode() {
      return this.f;
   }

   public void setUpdateMode(BatchUpdateMode var1) {
      this.f = var1;
   }

   public String getName() {
      return this.a;
   }

   public void setName(String var1) {
      this.a = var1;
   }

   public int getFilterCount() {
      return this.d;
   }

   public void setFilterCount(int var1) {
      this.d = var1;
   }

   public String getExitCode() {
      return this.g;
   }

   public void setExitCode(String var1) {
      this.g = var1;
   }

   public String getExitMessage() {
      return this.h;
   }

   public void setExitMessage(String var1) {
      this.h = var1;
   }
}
