package com.bstek.urule.builder;

import com.bstek.urule.builder.resource.Resource;
import com.bstek.urule.builder.resource.ResourceProvider;
import java.util.ArrayList;
import java.util.List;

public class ResourceBase {
   private ResourceProvider provider;
   private List<Resource> resources = new ArrayList<>();

   protected ResourceBase(ResourceProvider provider) {
      this.provider = provider;
   }

   public ResourceBase addResource(String fileInfo) {
      String[] parts = fileInfo.split(":");
      long longValue = Long.valueOf(parts[0]);
      String text = parts[1];
      if (text.contentEquals("false")) {
         text = null;
      }

      this.resources.add(this.provider.provide(longValue, text));
      return this;
   }

   public ResourceBase addResource(long fileId, String version) {
      this.resources.add(this.provider.provide(fileId, version));
      return this;
   }

   public List<Resource> getResources() {
      return this.resources;
   }
}
