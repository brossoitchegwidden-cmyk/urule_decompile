package com.bstek.urule.model.rule.lhs;

import com.bstek.urule.LocaleHolder;
import com.bstek.urule.model.rule.Parameter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

public class MethodLeftPart implements LeftPart {
   @JsonIgnore
   private String id;
   private String beanId;
   private String beanLabel;
   private String methodName;
   private String methodLabel;
   private String categoryUuid;
   private String uuid;
   private List<Parameter> parameters;

   public String getBeanId() {
      return this.beanId;
   }

   public void setBeanId(String var1) {
      this.beanId = var1;
   }

   public String getBeanLabel() {
      return this.beanLabel;
   }

   public void setBeanLabel(String var1) {
      this.beanLabel = var1;
   }

   public String getMethodName() {
      return this.methodName;
   }

   public void setMethodName(String var1) {
      this.methodName = var1;
   }

   public String getMethodLabel() {
      return this.methodLabel;
   }

   public void setMethodLabel(String var1) {
      this.methodLabel = var1;
   }

   public String getUuid() {
      return this.uuid;
   }

   public void setUuid(String var1) {
      this.uuid = var1;
   }

   public String getCategoryUuid() {
      return this.categoryUuid;
   }

   public void setCategoryUuid(String var1) {
      this.categoryUuid = var1;
   }

   public void setId(String var1) {
      this.id = var1;
   }

   public List<Parameter> getParameters() {
      return this.parameters;
   }

   public void setParameters(List<Parameter> var1) {
      this.parameters = var1;
   }

   @Override
   public String getId() {
      if (this.id == null) {
         String var1 = LocaleHolder.isEnglish() ? "Method" : "方法";
         if (this.parameters != null) {
            String var2 = "";
            int var3 = 0;

            for (Parameter var5 : this.parameters) {
               if (var3 > 0) {
                  var2 = var2 + ",";
               }

               var2 = var2 + var5.getId();
               var3++;
            }

            this.id = "[" + var1 + "]" + this.beanLabel + "." + this.methodLabel + "(" + var2 + ")";
         } else {
            this.id = "[" + var1 + "]" + this.beanLabel + "." + this.methodLabel;
         }
      }

      return this.id;
   }
}
