package com.bstek.urule.console.admin.project.in;

public class ConfigInfo {
   private boolean a;
   private boolean b;
   private boolean c;

   public boolean isReplace() {
      return this.a;
   }

   public void setReplace(boolean var1) {
      this.a = var1;
   }

   public boolean isNewPacketCode() {
      return this.b;
   }

   public void setNewPacketCode(boolean var1) {
      this.b = var1;
   }

   public boolean isForceLock() {
      return this.c;
   }

   public void setForceLock(boolean var1) {
      this.c = var1;
   }
}
