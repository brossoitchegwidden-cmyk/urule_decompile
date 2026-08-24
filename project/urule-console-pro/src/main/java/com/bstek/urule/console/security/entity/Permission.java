package com.bstek.urule.console.security.entity;

import com.bstek.urule.console.type.PermissionType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(
   ignoreUnknown = true
)
public class Permission {
   private String code;
   private String name;
   private List roles;
   private PermissionType type;
   private boolean checked;
   private boolean disabled;

   public Permission() {
   }

   public Permission(String code, String name, List roles, PermissionType type) {
      this.code = code;
      this.name = name;
      this.type = type;
      this.roles = roles;
   }

   public String getCode() {
      return this.code;
   }

   public void setCode(String code) {
      this.code = code;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public boolean isChecked() {
      return this.checked;
   }

   public void setChecked(boolean checked) {
      this.checked = checked;
   }

   public PermissionType getType() {
      return this.type;
   }

   public void setType(PermissionType type) {
      this.type = type;
   }

   public boolean isDisabled() {
      return this.disabled;
   }

   public void setDisabled(boolean disabled) {
      this.disabled = disabled;
   }

   public List getRoles() {
      return this.roles;
   }

   public void setRoles(List roles) {
      this.roles = roles;
   }
}
