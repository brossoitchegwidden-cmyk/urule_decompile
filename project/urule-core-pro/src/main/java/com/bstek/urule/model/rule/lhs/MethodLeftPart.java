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

   public void setBeanId(String beanId) {
      this.beanId = beanId;
   }

   public String getBeanLabel() {
      return this.beanLabel;
   }

   public void setBeanLabel(String beanLabel) {
      this.beanLabel = beanLabel;
   }

   public String getMethodName() {
      return this.methodName;
   }

   public void setMethodName(String methodName) {
      this.methodName = methodName;
   }

   public String getMethodLabel() {
      return this.methodLabel;
   }

   public void setMethodLabel(String methodLabel) {
      this.methodLabel = methodLabel;
   }

   public String getUuid() {
      return this.uuid;
   }

   public void setUuid(String uuid) {
      this.uuid = uuid;
   }

   public String getCategoryUuid() {
      return this.categoryUuid;
   }

   public void setCategoryUuid(String categoryUuid) {
      this.categoryUuid = categoryUuid;
   }

   public void setId(String id) {
      this.id = id;
   }

   public List<Parameter> getParameters() {
      return this.parameters;
   }

   public void setParameters(List<Parameter> parameters) {
      this.parameters = parameters;
   }

   @Override
   public String getId() {
      if (this.id == null) {
         String text = LocaleHolder.isEnglish() ? "Method" : "方法";
         if (this.parameters != null) {
            String text2 = "";
            int number = 0;

            for (Parameter parameter : this.parameters) {
               if (number > 0) {
                  text2 = text2 + ",";
               }

               text2 = text2 + parameter.getId();
               number++;
            }

            this.id = "[" + text + "]" + this.beanLabel + "." + this.methodLabel + "(" + text2 + ")";
         } else {
            this.id = "[" + text + "]" + this.beanLabel + "." + this.methodLabel;
         }
      }

      return this.id;
   }
}
