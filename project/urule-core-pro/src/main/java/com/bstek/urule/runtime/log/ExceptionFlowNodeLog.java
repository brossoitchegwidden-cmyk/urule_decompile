package com.bstek.urule.runtime.log;

import com.bstek.urule.model.flow.ExceptionNode;

public class ExceptionFlowNodeLog extends DataLog {
   private String file;
   private String nodeName;
   private Exception exception;

   public ExceptionFlowNodeLog(ExceptionNode node, String file, Exception exception) {
      this.file = file;
      this.exception = exception;
      this.nodeName = node.getName();
      String text = this.isEnglishLanguage() ? ">>>>node 【%s】 throw exception 【%s】" : ">>>>节点【%s】捕获到异常【%s】";
      this.msg = String.format(text, node.getName(), exception.getClass().getName());
   }

   public String getNodeName() {
      return this.nodeName;
   }

   public Exception getException() {
      return this.exception;
   }

   public String getFile() {
      return this.file;
   }
}
