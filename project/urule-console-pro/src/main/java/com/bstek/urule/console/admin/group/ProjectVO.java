package com.bstek.urule.console.admin.group;

import com.bstek.urule.console.database.model.Project;

public class ProjectVO extends Project {
   private boolean a;
   private boolean b;
   private boolean c;

   public boolean isRemoveAble() {
      return this.b;
   }

   public void setRemoveAble(boolean var1) {
      this.b = var1;
   }

   public boolean isExportAble() {
      return this.c;
   }

   public void setExportAble(boolean var1) {
      this.c = var1;
   }

   public boolean isAccessable() {
      return this.a;
   }

   public void setAccessable(boolean var1) {
      this.a = var1;
   }
}
