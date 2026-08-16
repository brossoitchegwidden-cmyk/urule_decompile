package com.bstek.urule.console.batch;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HiveStatement {
   private List a = new ArrayList();

   public List getBatchSqls() {
      return this.a;
   }

   public void setBatchSqls(List var1) {
      this.a = var1;
   }

   public void addBatch(String var1) throws SQLException {
      this.a.add(var1);
   }
}
