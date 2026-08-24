package com.bstek.urule.console.security.entity;

import com.bstek.urule.console.type.ModuleType;
import com.bstek.urule.console.type.RoleCategory;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@JsonIgnoreProperties(
   ignoreUnknown = true
)
public class Module {
   private String code;
   private String name;
   private ModuleType category;
   private RoleCategory type;
   private List urls;
   private boolean checked;
   private boolean disabled;
   private List childModules;

   public Module() {
      this.childModules = new ArrayList();
   }

   public Module(String code, String name, RoleCategory roleType) {
      this.code = code;
      this.name = name;
      this.category = ModuleType.basic;
      this.type = roleType;
      this.childModules = new ArrayList();
   }

   public Module(String code, String name, String urls, RoleCategory roleType) {
      this.code = code;
      this.name = name;
      this.urls = new ArrayList(Arrays.asList(urls.split(",")));
      this.category = ModuleType.basic;
      this.type = roleType;
      this.childModules = new ArrayList();
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

   public List getItems() {
      return this.childModules;
   }

   public void setItems(List items) {
      this.childModules = items;
   }

   public RoleCategory getType() {
      return this.type;
   }

   public void setType(RoleCategory type) {
      this.type = type;
   }

   public boolean isChecked() {
      return this.checked;
   }

   public void setChecked(boolean checked) {
      this.checked = checked;
   }

   public boolean isDisabled() {
      return this.disabled;
   }

   public void setDisabled(boolean disabled) {
      this.disabled = disabled;
   }

   public ModuleType getCategory() {
      return this.category;
   }

   public void setCategory(ModuleType category) {
      this.category = category;
   }

   public List getUrls() {
      return this.urls;
   }

   public void setUrls(List urls) {
      this.urls = urls;
   }
}
