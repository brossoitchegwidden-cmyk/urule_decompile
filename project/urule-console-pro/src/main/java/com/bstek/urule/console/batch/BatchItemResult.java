package com.bstek.urule.console.batch;

import com.bstek.urule.console.database.model.batch.BatchUpdateMode;

public class BatchItemResult {
   private String name;
   private int readCount = 0;
   private int writeCount = 0;
   private int filterCount = 0;
   private String tableName;
   private BatchUpdateMode updateMode;
   private String exitCode;
   private String exitMessage;

   public int getReadCount() {
      return this.readCount;
   }

   public void setReadCount(int recordCount) {
      this.readCount = recordCount;
   }

   public int getWriteCount() {
      return this.writeCount;
   }

   public void setWriteCount(int successCount) {
      this.writeCount = successCount;
   }

   public String getTableName() {
      return this.tableName;
   }

   public void setTableName(String tableName) {
      this.tableName = tableName;
   }

   public BatchUpdateMode getUpdateMode() {
      return this.updateMode;
   }

   public void setUpdateMode(BatchUpdateMode updateMode) {
      this.updateMode = updateMode;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public int getFilterCount() {
      return this.filterCount;
   }

   public void setFilterCount(int filterCount) {
      this.filterCount = filterCount;
   }

   public String getExitCode() {
      return this.exitCode;
   }

   public void setExitCode(String exitCode) {
      this.exitCode = exitCode;
   }

   public String getExitMessage() {
      return this.exitMessage;
   }

   public void setExitMessage(String exitMessage) {
      this.exitMessage = exitMessage;
   }
}
