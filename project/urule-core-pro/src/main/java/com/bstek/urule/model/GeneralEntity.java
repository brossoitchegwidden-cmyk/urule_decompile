package com.bstek.urule.model;

import com.bstek.urule.exception.RuleException;
import java.util.HashMap;
import java.util.UUID;
import org.apache.commons.lang.StringUtils;

public class GeneralEntity extends HashMap<String, Object> {
   private static final long serialVersionUID = 2778576006420277518L;
   private final String id = UUID.randomUUID().toString();
   private String targetClass;

   public GeneralEntity() {
   }

   public GeneralEntity(String targetClass) {
      if (StringUtils.isBlank(targetClass)) {
         throw new RuleException("Target class cannot be null.");
      }

      this.targetClass = targetClass;
   }

   public String getTargetClass() {
      return this.targetClass;
   }

   public void setTargetClass(String targetClass) {
      this.targetClass = targetClass;
   }

   public String getId() {
      return this.id;
   }

   @Override
   public boolean equals(Object objectValue) {
      if (objectValue instanceof GeneralEntity) {
         GeneralEntity generalEntity = (GeneralEntity)objectValue;
         return generalEntity.getId().contentEquals(this.id);
      } else {
         return false;
      }
   }
}
