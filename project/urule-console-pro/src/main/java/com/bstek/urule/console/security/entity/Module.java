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
   private String a;
   private String b;
   private ModuleType c;
   private RoleCategory d;
   private List e;
   private boolean f;
   private boolean g;
   private List h;

   public Module() {
      this.h = new ArrayList();
   }

   public Module(String var1, String var2, RoleCategory var3) {
      this.a = var1;
      this.b = var2;
      this.c = ModuleType.basic;
      this.d = var3;
      this.h = new ArrayList();
   }

   public Module(String var1, String var2, String var3, RoleCategory var4) {
      this.a = var1;
      this.b = var2;
      this.e = new ArrayList(Arrays.asList(var3.split(",")));
      this.c = ModuleType.basic;
      this.d = var4;
      this.h = new ArrayList();
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

   public List getItems() {
      return this.h;
   }

   public void setItems(List var1) {
      this.h = var1;
   }

   public RoleCategory getType() {
      return this.d;
   }

   public void setType(RoleCategory var1) {
      this.d = var1;
   }

   public boolean isChecked() {
      return this.f;
   }

   public void setChecked(boolean var1) {
      this.f = var1;
   }

   public boolean isDisabled() {
      return this.g;
   }

   public void setDisabled(boolean var1) {
      this.g = var1;
   }

   public ModuleType getCategory() {
      return this.c;
   }

   public void setCategory(ModuleType var1) {
      this.c = var1;
   }

   public List getUrls() {
      return this.e;
   }

   public void setUrls(List var1) {
      this.e = var1;
   }
}
