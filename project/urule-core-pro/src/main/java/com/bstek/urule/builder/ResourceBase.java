package com.bstek.urule.builder;

import com.bstek.urule.builder.resource.Resource;
import com.bstek.urule.builder.resource.ResourceProvider;
import java.util.ArrayList;
import java.util.List;

public class ResourceBase {
   private ResourceProvider a;
   private List<Resource> b = new ArrayList<>();

   protected ResourceBase(ResourceProvider var1) {
      this.a = var1;
   }

   public ResourceBase addResource(String var1) {
      String[] var2 = var1.split(":");
      long var3 = Long.valueOf(var2[0]);
      String var5 = var2[1];
      if (var5.contentEquals("false")) {
         var5 = null;
      }

      this.b.add(this.a.provide(var3, var5));
      return this;
   }

   public ResourceBase addResource(long var1, String var3) {
      this.b.add(this.a.provide(var1, var3));
      return this;
   }

   public List<Resource> getResources() {
      return this.b;
   }
}
