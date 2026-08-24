package com.bstek.urule.debug;

public class MessageItem {
   private String msg;
   private String htmlMsg;

   public MessageItem(String msg, String htmlMsg) {
      this.msg = msg;
      this.htmlMsg = htmlMsg;
   }

   public String toHtml() {
      return this.htmlMsg;
   }

   public String getMsg() {
      return this.msg;
   }
}
