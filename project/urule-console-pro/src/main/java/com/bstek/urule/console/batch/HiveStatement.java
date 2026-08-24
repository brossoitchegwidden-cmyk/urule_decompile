package com.bstek.urule.console.batch;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HiveStatement {
   private List batchSqls = new ArrayList();

   public List getBatchSqls() {
      return this.batchSqls;
   }

   public void setBatchSqls(List batchSqls) {
      this.batchSqls = batchSqls;
   }

   public void addBatch(String batchSql) throws SQLException {
      this.batchSqls.add(batchSql);
   }
}
