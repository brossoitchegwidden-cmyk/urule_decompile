package com.bstek.urule.action;

import org.apache.commons.lang.StringUtils;

public class InvokeKnowledgePackage {
   private String project;
   private String name;
   private String code;
   private long id;

   public InvokeKnowledgePackage(String project, String name, long id, String code) {
      this.project = project;
      this.name = name;
      this.id = id;
      if (StringUtils.isNotBlank(code)) {
         this.code = code;
      } else {
         this.code = String.valueOf(id);
      }
   }

   public String getProject() {
      return this.project;
   }

   public String getName() {
      return this.name;
   }

   public long getId() {
      return this.id;
   }

   public String getCode() {
      return this.code;
   }

   public void setCode(String code) {
      this.code = code;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setProject(String project) {
      this.project = project;
   }
}
