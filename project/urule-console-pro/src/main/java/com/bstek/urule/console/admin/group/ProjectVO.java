package com.bstek.urule.console.admin.group;

import com.bstek.urule.console.database.model.Project;

public class ProjectVO extends Project {
   private boolean accessable;
   private boolean removeAble;
   private boolean exportAble;

   public boolean isRemoveAble() {
      return this.removeAble;
   }

   public void setRemoveAble(boolean removeAble) {
      this.removeAble = removeAble;
   }

   public boolean isExportAble() {
      return this.exportAble;
   }

   public void setExportAble(boolean exportAble) {
      this.exportAble = exportAble;
   }

   public boolean isAccessable() {
      return this.accessable;
   }

   public void setAccessable(boolean accessable) {
      this.accessable = accessable;
   }
}
