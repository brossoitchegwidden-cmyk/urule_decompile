package com.bstek.urule.action;

import com.bstek.urule.LocaleHolder;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rule.Parameter;
import com.bstek.urule.runtime.KnowledgePackage;
import com.bstek.urule.runtime.KnowledgeSession;
import com.bstek.urule.runtime.KnowledgeSessionFactory;
import com.bstek.urule.runtime.rete.Context;
import com.bstek.urule.runtime.rete.ValueCompute;
import com.bstek.urule.runtime.service.KnowledgeService;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;

public class ExecuteMethodAction extends AbstractAction {
   private String categoryUuid;
   private String uuid;
   private String beanId;
   private String beanLabel;
   private String beanELabel;
   private String methodLabel;
   private String methodELabel;
   private String methodName;
   private InvokeFile invokeFile;
   private InvokeKnowledgePackage invokeKnowledgePackage;
   private List<Parameter> parameters;
   private ActionType actionType = ActionType.ExecuteMethod;

   @Override
   public ActionValue execute(Context context, Map<String, Object> factMap) {
      String text = LocaleHolder.isEnglish() ? this.beanELabel : this.beanLabel;
      String text2 = LocaleHolder.isEnglish() ? this.methodELabel : this.methodLabel;
      String text3 = (text == null ? this.beanId : text) + "." + (text2 == null ? this.methodName : text2);
      String text4 = LocaleHolder.isEnglish() ? "Execute Method" : "执行动作";
      text3 = "$$$" + text4 + "：" + text3;
      if (this.invokeKnowledgePackage != null) {
         if (this.debug) {
            String text5 = this.invokeKnowledgePackage.getProject()
               + "："
               + this.invokeKnowledgePackage.getName()
               + "("
               + (StringUtils.isNotBlank(this.invokeKnowledgePackage.getCode()) ? this.invokeKnowledgePackage.getCode() + ">" : "")
               + this.invokeKnowledgePackage.getId()
               + ")";
            context.getLogger().logExecuteBeanMethod(text3, text5);
         }

         this.executeKnowledgePackageReference(context);
         return null;
      } else if (this.invokeFile != null) {
         if (this.debug) {
            String text6 = this.invokeFile.getPath() + "(" + this.invokeFile.getId() + ")";
            if (StringUtils.isNotBlank(this.invokeFile.getVersion())) {
               text6 = text6 + ":" + this.invokeFile.getVersion();
            }

            context.getLogger().logExecuteBeanMethod(text3, text6);
         }

         KnowledgePackage knowledgePackage = this.invokeFile.getKnowledgePackageWrapper().getKnowledgePackage();
         this.executeKnowledgePackage(knowledgePackage, context);
         return null;
      } else {
         try {
            boolean flag = false;
            boolean flag2 = false;
            if (this.beanId.contentEquals("urule.commonAction") && this.methodName.contentEquals("iferror")) {
               flag = true;
            } else if (this.beanId.contentEquals("urule.objectAction") && this.methodName.contentEquals("newObjectInstance")) {
               flag2 = true;
            }

            Object objectValue = context.getApplicationContext().getBean(this.beanId);
            Method method = null;
            if (this.parameters != null && this.parameters.size() > 0) {
               ParametersWrap parametersWrap = this.buildParameters(context, factMap, flag, flag2);
               Method[] methods = objectValue.getClass().getMethods();
               Datatype[] datatypes = parametersWrap.getDatatypes();
               boolean flag3 = false;

               for (Method method2 : methods) {
                  method = method2;
                  String name = method2.getName();
                  if (name.equals(this.methodName)) {
                     Class[] parameterTypes = method2.getParameterTypes();
                     if (parameterTypes.length == this.parameters.size()) {
                        for (int index = 0; index < parameterTypes.length; index++) {
                           Class valueType = parameterTypes[index];
                           Datatype datatype = datatypes[index];
                           flag3 = this.classMatches(valueType, datatype);
                           if (!flag3) {
                              break;
                           }
                        }

                        if (flag3) {
                           break;
                        }
                     }
                  }
               }

               if (!flag3) {
                  throw new RuleException("Bean [" + this.beanId + "." + this.methodName + "] with " + this.parameters.size() + " parameters not exist");
               }

               String text7 = this.methodName;
               ActionId annotation = method.getAnnotation(ActionId.class);
               if (annotation != null) {
                  text7 = annotation.value();
               }

               if (this.debug) {
                  context.getLogger().logExecuteBeanMethod(text3, parametersWrap.valuesToString());
               }

               Object objectValue2 = method.invoke(objectValue, parametersWrap.getValues());
               if (text7.equals("_loop_rule_break_tag__")) {
                  context.getWorkingMemory().getParameters().put(text7, objectValue2);
                  return null;
               } else {
                  return new ActionValueImpl(text7, objectValue2);
               }
            } else {
               method = objectValue.getClass().getMethod(this.methodName);
               String text8 = this.methodName;
               ActionId annotation2 = method.getAnnotation(ActionId.class);
               if (annotation2 != null) {
                  text8 = annotation2.value();
               }

               if (this.debug) {
                  context.getLogger().logExecuteBeanMethod(text3, "");
               }

               Object objectValue3 = method.invoke(objectValue);
               if (objectValue3 != null) {
                  if (text8.equals("_loop_rule_break_tag__")) {
                     context.getWorkingMemory().getParameters().put(text8, objectValue3);
                     return null;
                  } else {
                     return new ActionValueImpl(text8, objectValue3);
                  }
               } else {
                  return null;
               }
            }
         } catch (Exception exception) {
            throw new RuleException(exception);
         }
      }
   }

   private void executeKnowledgePackageReference(Context context) {
      KnowledgeService knowledgeService = (KnowledgeService)context.getApplicationContext().getBean("urule.knowledgeService");

      try {
         KnowledgePackage knowledgePackage = null;
         if (StringUtils.isNotBlank(this.invokeKnowledgePackage.getCode())) {
            knowledgePackage = knowledgeService.getKnowledge(this.invokeKnowledgePackage.getCode());
         } else {
            knowledgePackage = knowledgeService.getKnowledge(String.valueOf(this.invokeKnowledgePackage.getId()));
         }

         this.executeKnowledgePackage(knowledgePackage, context);
      } catch (Exception exception) {
         throw new RuleException(exception);
      }
   }

   private void executeKnowledgePackage(KnowledgePackage knowledgePackage, Context context) {
      try {
         KnowledgeSession workingMemory = (KnowledgeSession)context.getWorkingMemory();
         KnowledgeSession knowledgeSession = KnowledgeSessionFactory.newKnowledgeSession(knowledgePackage, workingMemory);
         if (knowledgePackage.getFlowMap() != null && knowledgePackage.getFlowMap().size() != 0) {
            String id = knowledgePackage.getFlowMap().values().iterator().next().getId();
            knowledgeSession.startProcess(id, workingMemory.getParameters());
         } else {
            knowledgeSession.fireRules(workingMemory.getParameters());
         }

         context.addRuleData(knowledgeSession.getLogManager().getRuleData());
         Map parameters = knowledgeSession.getParameters();
         Map parameters2 = workingMemory.getParameters();

         for (String text : (Iterable<String>)(Iterable<?>)(parameters.keySet())) {
            parameters2.put(text, parameters.get(text));
         }
      } catch (Exception exception) {
         throw new RuleException(exception);
      }
   }

   private boolean classMatches(Class<?> valueType, Datatype datatype) {
      boolean flag = false;
      switch (datatype) {
         case String:
            if (valueType.equals(String.class)) {
               flag = true;
            } else {
               flag = false;
            }
            break;
         case BigDecimal:
            if (valueType.equals(BigDecimal.class)) {
               flag = true;
            } else {
               flag = false;
            }
            break;
         case Boolean:
            if (!valueType.equals(Boolean.class) && !valueType.equals(boolean.class)) {
               flag = false;
            } else {
               flag = true;
            }
            break;
         case Date:
            if (valueType.equals(Date.class)) {
               flag = true;
            } else {
               flag = false;
            }
            break;
         case Double:
            if (!valueType.equals(Double.class) && !valueType.equals(double.class)) {
               flag = false;
            } else {
               flag = true;
            }
            break;
         case Enum:
            if (Enum.class.isAssignableFrom(valueType)) {
               flag = true;
            } else {
               flag = false;
            }
            break;
         case Float:
            if (!valueType.equals(Float.class) && !valueType.equals(float.class)) {
               flag = false;
            } else {
               flag = true;
            }
            break;
         case Integer:
            if (!valueType.equals(Integer.class) && !valueType.equals(int.class)) {
               flag = false;
            } else {
               flag = true;
            }
            break;
         case Char:
            if (!valueType.equals(Character.class) && !valueType.equals(char.class)) {
               flag = false;
            } else {
               flag = true;
            }
            break;
         case List:
            if (List.class.isAssignableFrom(valueType)) {
               flag = true;
            } else {
               flag = false;
            }
            break;
         case Long:
            if (!valueType.equals(Long.class) && !valueType.equals(long.class)) {
               flag = false;
            } else {
               flag = true;
            }
            break;
         case Map:
            if (Map.class.isAssignableFrom(valueType)) {
               flag = true;
            } else {
               flag = false;
            }
            break;
         case Set:
            if (Set.class.isAssignableFrom(valueType)) {
               flag = true;
            } else {
               flag = false;
            }
            break;
         case Object:
            flag = true;
      }

      return flag;
   }

   private ParametersWrap buildParameters(Context context, Map<String, Object> valuesByKey, boolean captureIfError, boolean objectInstanceMethod) {
      ArrayList items = new ArrayList();
      ArrayList items2 = new ArrayList();
      ValueCompute valueCompute = context.getValueCompute();
      context.resetParentIsObjectInstanceMethod(objectInstanceMethod);

      for (int index = 0; index < this.parameters.size(); index++) {
         Parameter parameter = this.parameters.get(index);
         Datatype type = parameter.getType();
         items.add(type);
         if (index == 0 && captureIfError) {
            try {
               Object objectValue = valueCompute.complexValueCompute(parameter.getValue(), context, valuesByKey);
               items2.add(type.convert(objectValue));
            } catch (Exception exception) {
               context.getLogger().logIFErrorLog(parameter, exception);
               items2.add(exception);
            }
         } else {
            Object objectValue2 = valueCompute.complexValueCompute(parameter.getValue(), context, valuesByKey);
            items2.add(type.convert(objectValue2));
         }
      }

      Datatype[] datatype = new Datatype[items.size()];
      items.toArray(datatype);
      Object[] values = new Object[items2.size()];
      items2.toArray(values);
      ParametersWrap parametersWrap = new ParametersWrap();
      parametersWrap.setDatatypes(datatype);
      parametersWrap.setValues(values);
      return parametersWrap;
   }

   public String getMethodLabel() {
      return this.methodLabel;
   }

   public void setMethodLabel(String methodLabel) {
      this.methodLabel = methodLabel;
   }

   public String getBeanId() {
      return this.beanId;
   }

   public void setBeanId(String beanId) {
      this.beanId = beanId;
   }

   public String getMethodName() {
      return this.methodName;
   }

   public void setMethodName(String methodName) {
      this.methodName = methodName;
   }

   public String getBeanLabel() {
      return this.beanLabel;
   }

   public void setBeanLabel(String beanLabel) {
      this.beanLabel = beanLabel;
   }

   public String getBeanELabel() {
      return this.beanELabel;
   }

   public void setBeanELabel(String beanELabel) {
      this.beanELabel = beanELabel;
   }

   public String getMethodELabel() {
      return this.methodELabel;
   }

   public void setMethodELabel(String methodELabel) {
      this.methodELabel = methodELabel;
   }

   public List<Parameter> getParameters() {
      return this.parameters;
   }

   public void setParameters(List<Parameter> parameters) {
      this.parameters = parameters;
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

   public InvokeFile getInvokeFile() {
      return this.invokeFile;
   }

   public void setInvokeFile(InvokeFile invokeFile) {
      this.invokeFile = invokeFile;
   }

   public InvokeKnowledgePackage getInvokeKnowledgePackage() {
      return this.invokeKnowledgePackage;
   }

   public void setInvokeKnowledgePackage(InvokeKnowledgePackage invokeKnowledgePackage) {
      this.invokeKnowledgePackage = invokeKnowledgePackage;
   }

   public void addParameter(Parameter parameter) {
      if (this.parameters == null) {
         this.parameters = new ArrayList<>();
      }

      this.parameters.add(parameter);
   }

   @Override
   public ActionType getActionType() {
      return this.actionType;
   }
}
