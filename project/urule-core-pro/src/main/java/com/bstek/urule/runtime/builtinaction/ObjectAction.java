package com.bstek.urule.runtime.builtinaction;

import com.bstek.urule.ClassUtils;
import com.bstek.urule.Utils;
import com.bstek.urule.action.WorkingMemoryHolder;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.GeneralEntity;
import com.bstek.urule.model.library.action.annotation.ActionBean;
import com.bstek.urule.model.library.action.annotation.ActionMethod;
import com.bstek.urule.model.library.action.annotation.ActionMethodParameter;
import com.bstek.urule.runtime.KnowledgeSession;
import com.bstek.urule.runtime.WorkingMemory;

@ActionBean(name = "对象", ename = "Object")
public class ObjectAction {
   public static final String BEAN_ID = "urule.objectAction";
   public static final String NEW_OBJECT_INSTANCE = "newObjectInstance";

   @ActionMethod(name = "空字符串")
   @ActionMethodParameter(names = {})
   public String toEmptyString() {
      return "";
   }

   @ActionMethod(name = "null")
   @ActionMethodParameter(names = {})
   public Object toNull() {
      return null;
   }

   @ActionMethod(name = "对象实例化")
   @ActionMethodParameter(names = "完整类路径", enames = "targetClass")
   public Object newObjectInstance(String targetClass) {
      try {
         Class targetClassDefaultNull = ClassUtils.getTargetClassDefaultNull(targetClass);
         if (targetClassDefaultNull == null) {
            GeneralEntity generalEntity = new GeneralEntity(targetClass);
            WorkingMemory currentWorkingMemory = WorkingMemoryHolder.getCurrentWorkingMemory();
            Utils.assignVariableObjectDefaultValue(generalEntity, currentWorkingMemory);
            return generalEntity;
         } else {
            return targetClassDefaultNull.newInstance();
         }
      } catch (Exception exception) {
         GeneralEntity newObjectInstanceResult = new GeneralEntity(targetClass);
         WorkingMemory currentWorkingMemory2 = WorkingMemoryHolder.getCurrentWorkingMemory();
         KnowledgeSession knowledgeSession = (KnowledgeSession)currentWorkingMemory2;
         Utils.assignVariableObjectDefaultValue(newObjectInstanceResult, knowledgeSession);
         return newObjectInstanceResult;
      }
   }

   @ActionMethod(name = "根据对象创建新实例")
   @ActionMethodParameter(names = "目标对象", enames = "Object")
   public Object newObjectInstanceByObject(Object obj) {
      try {
         if (obj instanceof GeneralEntity) {
            GeneralEntity generalEntity = (GeneralEntity)obj;
            String targetClass = generalEntity.getTargetClass();
            generalEntity = new GeneralEntity(targetClass);
            WorkingMemory currentWorkingMemory = WorkingMemoryHolder.getCurrentWorkingMemory();
            Utils.assignVariableObjectDefaultValue(generalEntity, currentWorkingMemory);
            return generalEntity;
         } else {
            return obj.getClass().newInstance();
         }
      } catch (Exception exception) {
         throw new RuleException(exception);
      }
   }

   @ActionMethod(name = "对象取值")
   @ActionMethodParameter(names = {"目标对象", "属性名"}, enames = {"Object", "propertyName"})
   public Object getObjectProperty(Object obj, String property) {
      try {
         return Utils.getObjectProperty(obj, property);
      } catch (Exception exception) {
         throw new RuleException(exception);
      }
   }
}
