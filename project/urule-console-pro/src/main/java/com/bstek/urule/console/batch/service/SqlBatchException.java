package com.bstek.urule.console.batch.service;

import com.bstek.urule.console.batch.exception.BatchException;

public class SqlBatchException extends BatchException {
   private static final long serialVersionUID = 4591262746110874161L;

   public SqlBatchException(String msg, Exception ex) {
      super(msg, ex);
   }
}
