package com.bstek.urule.console.config;

import java.sql.Timestamp;
import java.util.Date;

public class Configuration {
   private Long id;
   private String key;
   private String value;
   private String label;
   private String type;
   private Timestamp createDate;
   private Timestamp updateDate;

   public Long getId() {
      return this.id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getKey() {
      return this.key;
   }

   public void setKey(String key) {
      this.key = key;
   }

   public String getValue() {
      return this.value;
   }

   public void setValue(String value) {
      this.value = value;
   }

   public String getType() {
      return this.type;
   }

   public void setType(String type) {
      this.type = type;
   }

   public String getLabel() {
      return this.label;
   }

   public void setLabel(String label) {
      this.label = label;
   }

   public Date getCreateDate() {
      return this.createDate;
   }

   public void setCreateDate(Timestamp createDate) {
      this.createDate = createDate;
   }

   public Date getUpdateDate() {
      return this.updateDate;
   }

   public void setUpdateDate(Timestamp updateDate) {
      this.updateDate = updateDate;
   }
}
