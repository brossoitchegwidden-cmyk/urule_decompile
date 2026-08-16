package com.bstek.urule.runtime.rete;

import java.util.UUID;

public class Path {
   private Activity a;
   private String b = UUID.randomUUID().toString();

   public Path(Activity var1) {
      this.a = var1;
      if (var1 instanceof JoinActivity) {
         JoinActivity var2 = (JoinActivity)var1;
         var2.addFromPath(this);
      }
   }

   public Activity getTo() {
      return this.a;
   }

   public String getId() {
      return this.b;
   }
}
