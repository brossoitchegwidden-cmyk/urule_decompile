package com.bstek.urule.console.batch.reader;

import com.bstek.urule.console.batch.exception.BatchException;

public class ReaderException extends BatchException {
   private static final long a = -3084486347160291976L;
   private int b = -1;

   public ReaderException(String var1, Exception var2) {
      super(var1, var2);
   }

   public int getPageIndex() {
      return this.b;
   }

   public void setPageIndex(int var1) {
      this.b = var1;
   }
}
