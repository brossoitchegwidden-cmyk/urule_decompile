package com.bstek.urule.action;

import com.bstek.urule.Utils;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.lhs.LeftType;
import com.bstek.urule.runtime.AbstractWorkingMemory;
import com.bstek.urule.runtime.rete.Context;
import com.bstek.urule.runtime.rete.ValueCompute;
import java.util.Map;

public class PredefineAssignAction extends AbstractAction {
   private String uuid;
   private String name;
   private Datatype datatype;
   private String variableCategory;
   private String variableCategoryUuid;
   private String propertyName;
   private Datatype propertyDatatype;
   private String propertyLabel;
   private String propertyUuid;
   private Value value;

   @Override
   public ActionValue execute(Context context, Map<String, Object> factMap) {
      AbstractWorkingMemory workingMemory = (AbstractWorkingMemory)context.getWorkingMemory();
      ValueCompute valueCompute = context.getValueCompute();
      Object objectValue = null;
      if (this.value == null) {
         return null;
      }

      objectValue = valueCompute.complexValueCompute(this.value, context, factMap);
      if (this.propertyName == null) {
         if (this.datatype != null) {
            objectValue = this.datatype.convert(objectValue);
         }

         workingMemory.setPredefineValue(this.uuid, objectValue);
         if (this.debug) {
            context.getLogger().logValueAssign("[预定义值]" + this.name + "", objectValue);
         }
      } else {
         Object predefineValue = workingMemory.getPredefineValue(this.uuid);
         if (predefineValue == null) {
            throw new RuleException("预定义对象【" + this.name + "】对应的对象【" + this.variableCategory + "】未初始化，不能为其属性【" + this.propertyLabel + "】赋值");
         }

         objectValue = this.propertyDatatype.convert(objectValue);
         Utils.setObjectProperty(predefineValue, this.propertyName, objectValue);
         if (this.debug) {
            context.getLogger().logValueAssign("[预定义值]" + this.name + "." + this.variableCategory + "." + this.propertyName, objectValue);
         }
      }

      return null;
   }

   @Override
   public ActionType getActionType() {
      return ActionType.VariableAssign;
   }

   public String getUuid() {
      return this.uuid;
   }

   public void setUuid(String uuid) {
      this.uuid = uuid;
   }

   public LeftType getType() {
      return LeftType.predefine;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public Datatype getDatatype() {
      return this.datatype;
   }

   public void setDatatype(Datatype datatype) {
      this.datatype = datatype;
   }

   public String getVariableCategory() {
      return this.variableCategory;
   }

   public void setVariableCategory(String variableCategory) {
      this.variableCategory = variableCategory;
   }

   public String getVariableCategoryUuid() {
      return this.variableCategoryUuid;
   }

   public void setVariableCategoryUuid(String variableCategoryUuid) {
      this.variableCategoryUuid = variableCategoryUuid;
   }

   public String getPropertyName() {
      return this.propertyName;
   }

   public void setPropertyName(String propertyName) {
      this.propertyName = propertyName;
   }

   public Datatype getPropertyDatatype() {
      return this.propertyDatatype;
   }

   public void setPropertyDatatype(Datatype propertyDatatype) {
      this.propertyDatatype = propertyDatatype;
   }

   public String getPropertyLabel() {
      return this.propertyLabel;
   }

   public void setPropertyLabel(String propertyLabel) {
      this.propertyLabel = propertyLabel;
   }

   public String getPropertyUuid() {
      return this.propertyUuid;
   }

   public void setPropertyUuid(String propertyUuid) {
      this.propertyUuid = propertyUuid;
   }

   public Value getValue() {
      return this.value;
   }

   public void setValue(Value value) {
      this.value = value;
   }
}
