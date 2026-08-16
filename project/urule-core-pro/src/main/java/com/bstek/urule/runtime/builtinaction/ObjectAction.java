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
   public Object newObjectInstance(String var1) {
      try {
         Class var2 = ClassUtils.getTargetClassDefaultNull(var1);
         if (var2 == null) {
            GeneralEntity var7 = new GeneralEntity(var1);
            WorkingMemory var8 = WorkingMemoryHolder.getCurrentWorkingMemory();
            Utils.assignVariableObjectDefaultValue(var7, var8);
            return var7;
         } else {
            return var2.newInstance();
         }
      } catch (Exception var6) {
         GeneralEntity var3 = new GeneralEntity(var1);
         WorkingMemory var4 = WorkingMemoryHolder.getCurrentWorkingMemory();
         KnowledgeSession var5 = (KnowledgeSession)var4;
         Utils.assignVariableObjectDefaultValue(var3, var5);
         return var3;
      }
   }

   @ActionMethod(name = "根据对象创建新实例")
   @ActionMethodParameter(names = "目标对象", enames = "Object")
   public Object newObjectInstanceByObject(Object var1) {
      try {
         if (var1 instanceof GeneralEntity) {
            GeneralEntity var2 = (GeneralEntity)var1;
            String var3 = var2.getTargetClass();
            var2 = new GeneralEntity(var3);
            WorkingMemory var4 = WorkingMemoryHolder.getCurrentWorkingMemory();
            Utils.assignVariableObjectDefaultValue(var2, var4);
            return var2;
         } else {
            return var1.getClass().newInstance();
         }
      } catch (Exception var5) {
         throw new RuleException(var5);
      }
   }

   @ActionMethod(name = "对象取值")
   @ActionMethodParameter(names = {"目标对象", "属性名"}, enames = {"Object", "propertyName"})
   public Object getObjectProperty(Object var1, String var2) {
      try {
         return Utils.getObjectProperty(var1, var2);
      } catch (Exception var4) {
         throw new RuleException(var4);
      }
   }
}
