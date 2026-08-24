package com.bstek.urule.action;

import com.bstek.urule.Utils;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.lhs.LeftType;
import com.bstek.urule.runtime.rete.Context;
import com.bstek.urule.runtime.rete.ValueCompute;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

/** Assigns a computed rule value to a fact, parameter map, or nested property. */
public class VariableAssignAction extends AbstractAction {
   private String keyName;
   private String keyLabel;
   private String keyCategoryUuid;
   private String keyUuid;
   private String variableName;
   private String variableLabel;
   private String variableCategory;
   private String categoryUuid;
   private String uuid;
   private Datatype datatype;
   private Value value;
   private LeftType type;
   private ActionType actionType = ActionType.VariableAssign;

   @Override
   public ActionValue execute(Context context, Map<String, Object> factMap) {
      Object targetObject = null;
      ValueCompute valueCompute = context.getValueCompute();
      Object assignedValue = null;
      if (this.value == null) {
         return null;
      }

      assignedValue = valueCompute.complexValueCompute(this.value, context, factMap);
      String variableCategoryClass = context.getVariableCategoryClass(this.variableCategory);
      if (variableCategoryClass.equals(HashMap.class.getName())) {
         targetObject = context.getWorkingMemory().getParameters();
      } else {
         targetObject = valueCompute.findObject(variableCategoryClass, factMap, context);
      }

      if (targetObject == null) {
         throw new RuleException("The object【" + variableCategoryClass + "】does not exist or is not initialized");
      }

      if (this.datatype.equals(Datatype.Enum) && assignedValue != null && StringUtils.isNotBlank(assignedValue.toString())) {
         PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(targetObject.getClass(), this.variableName);
         if (propertyDescriptor == null) {
            throw new RuleException("赋值操作无法获取当前枚举类型！");
         }

         Class propertyType = propertyDescriptor.getPropertyType();
         assignedValue = Enum.valueOf(propertyType, assignedValue.toString());
      } else if (assignedValue != null) {
         assignedValue = this.datatype.convert(assignedValue);
      }

      String assignmentPath = this.variableCategory;
      if (this.keyLabel == null && this.keyName == null) {
         assignmentPath = assignmentPath + "." + (this.variableLabel == null ? this.variableName : this.variableLabel);
      } else {
         assignmentPath = assignmentPath + "." + (this.keyLabel == null ? this.keyName : this.keyLabel) + "." + (this.variableLabel == null ? this.variableName : this.variableLabel);
      }

      if (this.debug) {
         context.getLogger().logValueAssign(assignmentPath, assignedValue);
      }

      if (this.keyName != null) {
         targetObject = Utils.getObjectProperty(targetObject, this.keyName);
         if (targetObject == null) {
            throw new RuleException("要赋值的对象【" + this.keyLabel + "】在参数中不存在");
         }
      }

      if (assignedValue instanceof Map) {
         Map<String, Object> valuesByKey = (Map<String, Object>)assignedValue;
         this.assignMap(targetObject, valuesByKey, assignmentPath);
         return null;
      }

      if (assignedValue instanceof String && this.datatype.equals(Datatype.Object)) {
         String jsonValue = (String)assignedValue;
         this.assignObjectJson(targetObject, jsonValue, assignmentPath);
      } else {
         this.setNestedProperty(targetObject, this.variableName, assignedValue);
      }

      return null;
   }

   private void assignObjectJson(Object targetObject, String jsonValue, String assignmentPath) {
      if (jsonValue.startsWith("{") && jsonValue.endsWith("}")) {
         ObjectMapper objectMapper = new ObjectMapper();

         try {
            Map<String, Object> valuesByKey = objectMapper.readValue(jsonValue, HashMap.class);
            this.assignMap(targetObject, valuesByKey, assignmentPath);
         } catch (Exception exception) {
            throw new RuleException("赋值操作值为Map类型:" + jsonValue + ",但无法将其转换为Map，请检查输入字符格式.");
         }
      } else {
         this.setNestedProperty(targetObject, this.variableName, jsonValue);
      }
   }

   private void assignMap(Object targetObject, Map<String, Object> valuesByKey, String assignmentPath) {
      String propertyName = this.variableName;
      PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(targetObject.getClass(), this.variableName);
      if (propertyDescriptor == null) {
         this.setNestedProperty(targetObject, propertyName, valuesByKey);
      } else {
         Class propertyType = propertyDescriptor.getPropertyType();
         if (Map.class.isAssignableFrom(propertyType)) {
            this.setNestedProperty(targetObject, propertyName, valuesByKey);
         } else {
            Object objectProperty = Utils.getObjectProperty(targetObject, propertyName);
            if (objectProperty == null) {
               try {
                  Object propertyObject = propertyType.newInstance();

                  for (String key : valuesByKey.keySet()) {
                     this.setNestedProperty(propertyObject, key, valuesByKey.get(key));
                  }

                  this.setNestedProperty(targetObject, propertyName, propertyObject);
               } catch (InstantiationException | IllegalAccessException exception) {
                  throw new RuleException("赋值操作值为Map类型，赋值对象[" + assignmentPath + "]类型为" + propertyType.getName() + "，无法对" + propertyType.getName() + "进行实例化");
               } catch (Exception exception) {
                  throw new RuleException("赋值操作值为Map类型，赋值对象[" + assignmentPath + "]类型为" + propertyType.getName() + "，Map中key与[" + propertyType.getName() + "]类型对象属性存在不匹配情况");
               }
            } else {
               try {
                  for (String key : valuesByKey.keySet()) {
                     this.setNestedProperty(objectProperty, key, valuesByKey.get(key));
                  }
               } catch (Exception exception) {
                  throw new RuleException("赋值操作值为Map类型，赋值对象[" + assignmentPath + "]类型为" + propertyType.getName() + "，Map中key与[" + propertyType.getName() + "]类型对象属性存在不匹配情况");
               }
            }
         }
      }
   }

   private void setNestedProperty(Object targetObject, String propertyPath, Object value) {
      String[] parts = propertyPath.split("\\.");
      Object currentObject = targetObject;

      for (int index = 0; index < parts.length; index++) {
         String propertyName = parts[index];
         if (index == parts.length - 1) {
            Utils.setObjectProperty(currentObject, propertyName, value);
            break;
         }

         Object objectProperty = Utils.getObjectProperty(currentObject, propertyName);
         if (objectProperty == null) {
            PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(currentObject.getClass(), propertyName);
            if (propertyDescriptor == null) {
               objectProperty = new HashMap();
            } else {
               Class propertyType = propertyDescriptor.getPropertyType();
               if (Map.class.isAssignableFrom(propertyType)) {
                  objectProperty = new HashMap();
               } else {
                  try {
                     objectProperty = propertyType.newInstance();
                  } catch (InstantiationException | IllegalAccessException exception) {
                     throw new RuleException(
                        "尝试对[" + targetObject.getClass().getName() + "]对象实例的[" + propertyPath + "]里的子对象[" + propertyType.getName() + "]实例化失败，请确认子对象[" + propertyType.getName() + "]有空的构造函数"
                     );
                  }
               }
            }

            Utils.setObjectProperty(currentObject, propertyName, objectProperty);
         }

         currentObject = objectProperty;
      }
   }

   public LeftType getType() {
      return this.type;
   }

   public void setType(LeftType type) {
      this.type = type;
   }

   public String getVariableName() {
      return this.variableName;
   }

   public void setVariableName(String variableName) {
      this.variableName = variableName;
   }

   public String getVariableLabel() {
      return this.variableLabel;
   }

   public void setVariableLabel(String variableLabel) {
      this.variableLabel = variableLabel;
   }

   public String getVariableCategory() {
      return this.variableCategory;
   }

   public void setVariableCategory(String variableCategory) {
      this.variableCategory = variableCategory;
   }

   public String getCategoryUuid() {
      return this.categoryUuid;
   }

   public void setCategoryUuid(String categoryUuid) {
      this.categoryUuid = categoryUuid;
   }

   public String getUuid() {
      return this.uuid;
   }

   public void setUuid(String uuid) {
      this.uuid = uuid;
   }

   public String getKeyName() {
      return this.keyName;
   }

   public void setKeyName(String keyName) {
      this.keyName = keyName;
   }

   public String getKeyLabel() {
      return this.keyLabel;
   }

   public void setKeyLabel(String keyLabel) {
      this.keyLabel = keyLabel;
   }

   public String getKeyCategoryUuid() {
      return this.keyCategoryUuid;
   }

   public void setKeyCategoryUuid(String keyCategoryUuid) {
      this.keyCategoryUuid = keyCategoryUuid;
   }

   public String getKeyUuid() {
      return this.keyUuid;
   }

   public void setKeyUuid(String keyUuid) {
      this.keyUuid = keyUuid;
   }

   public Value getValue() {
      return this.value;
   }

   public void setValue(Value value) {
      this.value = value;
   }

   public Datatype getDatatype() {
      return this.datatype;
   }

   public void setDatatype(Datatype datatype) {
      this.datatype = datatype;
   }

   @Override
   public ActionType getActionType() {
      return this.actionType;
   }
}
