package com.bstek.urule.console.batch.reader;

import com.bstek.urule.console.batch.exception.BatchException;

public class ReaderException extends BatchException {
   private static final long serialVersionUID = -3084486347160291976L;
   private int pageIndex = -1;

   public ReaderException(String msg, Exception ex) {
      super(msg, ex);
   }

   public int getPageIndex() {
      return this.pageIndex;
   }

   public void setPageIndex(int pageIndex) {
      this.pageIndex = pageIndex;
   }
}
