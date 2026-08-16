package com.bstek.urule.console.security.entity;

import com.bstek.urule.console.type.PermissionType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(
   ignoreUnknown = true
)
public class Permission {
   private String a;
   private String b;
   private List c;
   private PermissionType d;
   private boolean e;
   private boolean f;

   public Permission() {
   }

   public Permission(String var1, String var2, List var3, PermissionType var4) {
      this.a = var1;
      this.b = var2;
      this.d = var4;
      this.c = var3;
   }

   public String getCode() {
      return this.a;
   }

   public void setCode(String var1) {
      this.a = var1;
   }

   public String getName() {
      return this.b;
   }

   public void setName(String var1) {
      this.b = var1;
   }

   public boolean isChecked() {
      return this.e;
   }

   public void setChecked(boolean var1) {
      this.e = var1;
   }

   public PermissionType getType() {
      return this.d;
   }

   public void setType(PermissionType var1) {
      this.d = var1;
   }

   public boolean isDisabled() {
      return this.f;
   }

   public void setDisabled(boolean var1) {
      this.f = var1;
   }

   public List getRoles() {
      return this.c;
   }

   public void setRoles(List var1) {
      this.c = var1;
   }
}
