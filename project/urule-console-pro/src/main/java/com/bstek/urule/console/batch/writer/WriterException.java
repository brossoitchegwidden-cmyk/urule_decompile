package com.bstek.urule.console.batch.writer;

import com.bstek.urule.console.batch.exception.BatchException;
import com.bstek.urule.model.GeneralEntity;

public class WriterException extends BatchException {
   private static final long a = -2816573863669976595L;
   private Object b;

   public WriterException(String var1, Exception var2, GeneralEntity var3) {
      super(var1, var2);
      this.b = var3;
   }

   public Object getData() {
      return this.b;
   }

   public void setData(Object var1) {
      this.b = var1;
   }
}
