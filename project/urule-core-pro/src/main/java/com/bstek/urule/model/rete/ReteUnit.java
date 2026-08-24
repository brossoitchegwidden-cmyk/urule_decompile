package com.bstek.urule.model.rete;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import java.util.Date;

@JsonTypeInfo(use = Id.NAME, include = As.PROPERTY, property = "type")
@JsonSubTypes({@Type(value = ReteUnit.class, name = "nomal"), @Type(value = MutexReteUnit.class, name = "mutex")})
@JsonTypeName("nomal")
public class ReteUnit {
   private String ruleName;
   private Date effectiveDate;
   private Date expiresDate;
   private Rete rete;

   public ReteUnit() {
   }

   public ReteUnit(Rete rete, String ruleName) {
      this.rete = rete;
      this.ruleName = ruleName;
   }

   public String getRuleName() {
      return this.ruleName;
   }

   public void setRuleName(String ruleName) {
      this.ruleName = ruleName;
   }

   public Date getEffectiveDate() {
      return this.effectiveDate;
   }

   public void setEffectiveDate(Date effectiveDate) {
      this.effectiveDate = effectiveDate;
   }

   public Date getExpiresDate() {
      return this.expiresDate;
   }

   public void setExpiresDate(Date expiresDate) {
      this.expiresDate = expiresDate;
   }

   public Rete getRete() {
      return this.rete;
   }

   public void setRete(Rete rete) {
      this.rete = rete;
   }
}
