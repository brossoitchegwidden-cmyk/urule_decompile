package com.bstek.urule.console.editor.constant;

import java.util.List;

public class ConstantInfo {
   private long id;
   private String path;
   private String type;
   private List constantCategories;

   public ConstantInfo(long id, String path, String type, List constantCategories) {
      this.id = id;
      this.path = path;
      this.type = type;
      this.constantCategories = constantCategories;
   }

   public long getId() {
      return this.id;
   }

   public String getPath() {
      return this.path;
   }

   public String getType() {
      return this.type;
   }

   public List getConstantCategories() {
      return this.constantCategories;
   }
}
