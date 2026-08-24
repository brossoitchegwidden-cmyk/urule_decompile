package com.bstek.urule.console.config.exception;

public class ConfigLoadException extends RuntimeException {
   private static final long serialVersionUID = -6344469287551676573L;

   public ConfigLoadException(String msg) {
      super(msg);
   }

   public ConfigLoadException(Exception exception) {
      super(exception);
   }
}
