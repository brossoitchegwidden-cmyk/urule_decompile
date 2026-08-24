package com.bstek.urule.exception;

public class RuleException extends RuntimeException {
   private static final long serialVersionUID = -8624533394127244753L;
   private String tipMsg;

   public RuleException() {
   }

   public RuleException(String msg) {
      super(msg);
   }

   public RuleException(Exception ex) {
      super(ex);
      java.util.logging.Logger.getLogger(RuleException.class.getName()).log(java.util.logging.Level.SEVERE, ex.getMessage(), ex);
   }

   public RuleException(String msg, Exception ex) {
      super(ex);
      if (msg != null) {
         msg = "错误发生位置：" + msg;
         System.err.println(msg);
      }

      java.util.logging.Logger.getLogger(RuleException.class.getName()).log(java.util.logging.Level.SEVERE, ex.getMessage(), ex);
      this.tipMsg = msg;
   }

   public String getTipMsg() {
      return this.tipMsg;
   }
}
