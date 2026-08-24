package com.bstek.urule.console.batch.writer;

import com.bstek.urule.console.batch.exception.BatchException;
import com.bstek.urule.model.GeneralEntity;

public class WriterException extends BatchException {
   private static final long serialVersionUID = -2816573863669976595L;
   private Object data;

   public WriterException(String msg, Exception ex, GeneralEntity data) {
      super(msg, ex);
      this.data = data;
   }

   public Object getData() {
      return this.data;
   }

   public void setData(Object data) {
      this.data = data;
   }
}
