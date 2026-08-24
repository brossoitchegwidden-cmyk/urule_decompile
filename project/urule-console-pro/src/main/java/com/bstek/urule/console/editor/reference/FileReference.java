package com.bstek.urule.console.editor.reference;

import com.bstek.urule.builder.resource.ResourceType;
import java.util.ArrayList;
import java.util.List;

public class FileReference {
   private long id;
   private long projectId;
   private String name;
   private String version;
   private String pathInfo;
   private boolean expand = true;
   private ResourceType type;
   private List children;

   public long getId() {
      return this.id;
   }

   public void setId(long id) {
      this.id = id;
   }

   public long getProjectId() {
      return this.projectId;
   }

   public void setProjectId(long projectId) {
      this.projectId = projectId;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getVersion() {
      return this.version;
   }

   public void setVersion(String version) {
      this.version = version;
   }

   public String getPathInfo() {
      return this.pathInfo;
   }

   public void setPathInfo(String pathInfo) {
      this.pathInfo = pathInfo;
   }

   public boolean isExpand() {
      return this.expand;
   }

   public void setExpand(boolean expand) {
      this.expand = expand;
   }

   public ResourceType getType() {
      return this.type;
   }

   public void setType(ResourceType type) {
      this.type = type;
   }

   public List getChildren() {
      return (List)(this.children == null ? new ArrayList() : this.children);
   }

   public void setChildren(List children) {
      this.children = children;
   }
}
