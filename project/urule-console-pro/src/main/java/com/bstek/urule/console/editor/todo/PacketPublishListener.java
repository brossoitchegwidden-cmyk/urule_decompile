package com.bstek.urule.console.editor.todo;

import com.bstek.urule.console.database.model.Packet;

public interface PacketPublishListener {
   String BEAN_ID = "urule.packetPublishListener";

   void beforePublish(Packet var1, String var2);

   void afterPublish(Packet var1, String var2);

   void beforeActive(Packet var1, String var2);

   void afterActive(Packet var1, String var2);

   void beforeEnable(Packet var1, String var2);

   void afterEnable(Packet var1, String var2);

   void beforeDisable(Packet var1, String var2);

   void afterDisable(Packet var1, String var2);
}
