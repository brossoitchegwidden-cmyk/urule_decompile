package com.bstek.urule.console.editor.todo;

import com.bstek.urule.console.database.model.Packet;

public interface PacketPublishListener {
   String BEAN_ID = "urule.packetPublishListener";

   /**知识包发布before事件*/
   void beforePublish(Packet packet, String comment);

   /**知识包发布after事件*/
   void afterPublish(Packet packet, String comment);

   /**知识包版本切换before事件*/
   void beforeActive(Packet packet, String version);

   /**知识包版本切换after事件*/
   void afterActive(Packet packet, String version);

   /**知识包启用before事件*/
   void beforeEnable(Packet packet, String comment);

   /**知识包启用after事件*/
   void afterEnable(Packet packet, String comment);

   /**知识包禁用before事件*/
   void beforeDisable(Packet packet, String comment);

   /**知识包禁用after事件*/
   void afterDisable(Packet packet, String comment);
}
