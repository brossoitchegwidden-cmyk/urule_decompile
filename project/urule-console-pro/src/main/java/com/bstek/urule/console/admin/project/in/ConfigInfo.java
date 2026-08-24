package com.bstek.urule.console.admin.project.in;

public class ConfigInfo {
   private boolean replace;
   private boolean newPacketCode;
   private boolean forceLock;

   public boolean isReplace() {
      return this.replace;
   }

   public void setReplace(boolean replace) {
      this.replace = replace;
   }

   public boolean isNewPacketCode() {
      return this.newPacketCode;
   }

   public void setNewPacketCode(boolean newPacketCode) {
      this.newPacketCode = newPacketCode;
   }

   public boolean isForceLock() {
      return this.forceLock;
   }

   public void setForceLock(boolean forceLock) {
      this.forceLock = forceLock;
   }
}
