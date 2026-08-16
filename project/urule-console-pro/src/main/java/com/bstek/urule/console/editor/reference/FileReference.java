package com.bstek.urule.console.editor.reference;

import com.bstek.urule.builder.resource.ResourceType;
import java.util.ArrayList;
import java.util.List;

public class FileReference {
   private long a;
   private long b;
   private String c;
   private String d;
   private String e;
   private boolean f = true;
   private ResourceType g;
   private List h;

   public long getId() {
      return this.a;
   }

   public void setId(long var1) {
      this.a = var1;
   }

   public long getProjectId() {
      return this.b;
   }

   public void setProjectId(long var1) {
      this.b = var1;
   }

   public String getName() {
      return this.c;
   }

   public void setName(String var1) {
      this.c = var1;
   }

   public String getVersion() {
      return this.d;
   }

   public void setVersion(String var1) {
      this.d = var1;
   }

   public String getPathInfo() {
      return this.e;
   }

   public void setPathInfo(String var1) {
      this.e = var1;
   }

   public boolean isExpand() {
      return this.f;
   }

   public void setExpand(boolean var1) {
      this.f = var1;
   }

   public ResourceType getType() {
      return this.g;
   }

   public void setType(ResourceType var1) {
      this.g = var1;
   }

   public List getChildren() {
      return (List)(this.h == null ? new ArrayList() : this.h);
   }

   public void setChildren(List var1) {
      this.h = var1;
   }
}
