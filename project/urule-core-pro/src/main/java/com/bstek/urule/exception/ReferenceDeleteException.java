package com.bstek.urule.exception;

public class ReferenceDeleteException extends RuleException {
   private static final long a = -6528443821980042158L;

   public ReferenceDeleteException(String var1) {
      super("该资源分析文件引用失败，异常信息:\r\n" + var1);
   }

   public ReferenceDeleteException(int var1) {
      super("该资源已被" + var1 + "个文件引用,无法删除,请先取消引用或勾选强制删除!<br>The Resource has been referenced by " + var1 + " files, cannot be deleted！");
   }
}
