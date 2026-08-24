package com.bstek.urule.console.editor;

import com.bstek.urule.Utils;
import com.bstek.urule.console.ContextHolder;
import com.bstek.urule.console.InfoException;
import com.bstek.urule.console.editor.constant.ConstantInfo;
import com.bstek.urule.console.editor.constant.ConstantLoader;
import com.bstek.urule.console.editor.lib.VariableInfo;
import com.bstek.urule.console.editor.lib.VariableLoader;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.model.function.FunctionDescriptor;
import com.bstek.urule.model.library.action.Method;
import com.bstek.urule.model.library.action.SpringBean;
import com.bstek.urule.model.library.constant.Constant;
import com.bstek.urule.model.library.constant.ConstantCategory;
import com.bstek.urule.model.library.variable.Variable;
import com.bstek.urule.model.library.variable.VariableCategory;
import com.bstek.urule.runtime.BuiltInActionLibraryBuilder;
import com.bstek.urule.runtime.ProxyUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.context.ApplicationContext;

/**Excel导入导出辅助工具类*/
public class ExcelSupport {
   public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
   public static final String PREDEFINE_KEY = "Predefine";
   public static final String PREDEFINE_SHEET_NAME = "predefine";
   public static final String PROPERTY_SHEET_NAME = "property";
   public static final String PREDEFINE_SHEET_CNAME = "预定义变量";
   public static final String PROPERTY_SHEET_CNAME = "属性";
   private List variableInfos;
   private List constantInfos;
   private BuiltInActionLibraryBuilder builtInActionLibraryBuilder;
   private List functionDescriptors = new ArrayList();
   private Map varLibraries = new HashMap();
   private Map contLibraries = new HashMap();

   public static boolean isParameter(String value) {
      return "参数".equals(value) || "Parameter".equalsIgnoreCase(value);
   }

   public ExcelSupport() {
      this.variableInfos = VariableLoader.ins.load(ContextHolder.getGroupId(), ContextHolder.getProjectId());
      this.constantInfos = ConstantLoader.ins.load(ContextHolder.getGroupId(), ContextHolder.getProjectId());
      ApplicationContext applicationContext = Utils.getApplicationContext();
      this.builtInActionLibraryBuilder = (BuiltInActionLibraryBuilder)applicationContext.getBean("urule.builtInActionLibraryBuilder");

      for(FunctionDescriptor functionDescriptor : applicationContext.getBeansOfType(FunctionDescriptor.class).values()) {
         if (!functionDescriptor.isDisabled()) {
            this.functionDescriptors.add((FunctionDescriptor)ProxyUtils.getTargetObject(functionDescriptor));
         }
      }

   }

   public List getVariableInfos() {
      return this.variableInfos;
   }

   public void setVariableInfos(List variableInfos) {
      this.variableInfos = variableInfos;
   }

   public List getConstantInfos() {
      return this.constantInfos;
   }

   public void setConstantInfos(List constantInfos) {
      this.constantInfos = constantInfos;
   }

   public void setBuiltInActionLibraryBuilder(BuiltInActionLibraryBuilder builtInActionLibraryBuilder) {
      this.builtInActionLibraryBuilder = builtInActionLibraryBuilder;
   }

   public List getFunctionDescriptors() {
      return this.functionDescriptors;
   }

   public void setFunctionDescriptors(List functionDescriptors) {
      this.functionDescriptors = functionDescriptors;
   }

   public Map getVarLibraries() {
      return this.varLibraries;
   }

   public Map getContLibraries() {
      return this.contLibraries;
   }

   public List getBuiltInActions() {
      return this.builtInActionLibraryBuilder.getBuiltInActions();
   }

   public FunctionDescriptor getFunction(String funName) {
      FunctionDescriptor functionDescriptor = null;

      for(FunctionDescriptor functionDescriptor2 : (Iterable<FunctionDescriptor>)(Iterable<?>)(this.functionDescriptors)) {
         if (functionDescriptor2.getLabel().equals(funName)) {
            functionDescriptor = functionDescriptor2;
            break;
         }
      }

      return functionDescriptor;
   }

   public SpringBean getAction(String beanName, String methodName) {
      SpringBean springBean = null;
      Method method = null;

      for(SpringBean springBean2 : this.builtInActionLibraryBuilder.getBuiltInActions()) {
         if (springBean2.getName().equals(beanName)) {
            springBean = springBean2;

            for(Method method2 : springBean2.getMethods()) {
               if (method2.getName().equals(methodName)) {
                  method = method2;
                  return method != null ? springBean : null;
               }
            }
            break;
         }
      }

      return method != null ? springBean : null;
   }

   public Method getActionMethod(String beanName, String methodName) {
      Object objectValue = null;
      Method method = null;

      for(SpringBean springBean : this.builtInActionLibraryBuilder.getBuiltInActions()) {
         if (springBean.getName().equals(beanName)) {
            for(Method method2 : springBean.getMethods()) {
               if (method2.getName().equals(methodName)) {
                  method = method2;
                  return method;
               }
            }
            break;
         }
      }

      return method;
   }

   public Variable findVariable(String[] names, boolean allowNull) {
      String text = names[0];
      String text2 = names[1];

      for(VariableInfo variableInfo : (Iterable<VariableInfo>)(Iterable<?>)(this.variableInfos)) {
         for(VariableCategory variableCategory : (Iterable<VariableCategory>)(Iterable<?>)(variableInfo.getVariableCategories())) {
            if (variableCategory.getName().equals(text)) {
               this.varLibraries.put(variableInfo.getId(), variableInfo);
               List variables = variableCategory.getVariables();
               if (variables != null) {
                  for(Variable variable : (Iterable<Variable>)(Iterable<?>)(variables)) {
                     if (variable.getLabel().equals(text2) || variable.getName().equals(text2)) {
                        return variable;
                     }
                  }
               }
            }
         }
      }

      if (allowNull) {
         return null;
      } else {
         throw new InfoException("变量[" + text + "." + text2 + "]在当前项目中未定义!");
      }
   }

   public VariableCategory findVariableCategory(String categoryLabel, String propertyLabel) {
      String[] values = new String[]{categoryLabel, propertyLabel};
      return this.findVariableCategory(values);
   }

   public VariableCategory findVariableCategory(String[] names) {
      VariableCategory variableCategory = null;
      String text = names[0];
      String text2 = names[1];

      for(VariableInfo variableInfo : (Iterable<VariableInfo>)(Iterable<?>)(this.variableInfos)) {
         for(VariableCategory variableCategory2 : (Iterable<VariableCategory>)(Iterable<?>)(variableInfo.getVariableCategories())) {
            if (variableCategory2.getName().equals(text)) {
               this.varLibraries.put(variableInfo.getId(), variableInfo);
               List variables = variableCategory2.getVariables();
               if (variables != null) {
                  for(Variable variable : (Iterable<Variable>)(Iterable<?>)(variables)) {
                     if (variable.getLabel().equals(text2) || variable.getName().equals(text2)) {
                        variableCategory = variableCategory2;
                        break;
                     }
                  }

                  if (variableCategory != null) {
                     break;
                  }
               }
            }
         }

         if (variableCategory != null) {
            break;
         }
      }

      return variableCategory;
   }

   public VariableCategory findVariableCategory(String category) {
      VariableCategory variableCategory = null;

      for(VariableInfo variableInfo : (Iterable<VariableInfo>)(Iterable<?>)(this.variableInfos)) {
         for(VariableCategory variableCategory2 : (Iterable<VariableCategory>)(Iterable<?>)(variableInfo.getVariableCategories())) {
            if (variableCategory2.getName().equals(category)) {
               variableCategory = variableCategory2;
               break;
            }
         }

         if (variableCategory != null) {
            break;
         }
      }

      return variableCategory;
   }

   public List findVariableCategorys(String category) {
      ArrayList variableCategorys = new ArrayList();

      for(VariableInfo variableInfo : (Iterable<VariableInfo>)(Iterable<?>)(this.variableInfos)) {
         for(VariableCategory variableCategory : (Iterable<VariableCategory>)(Iterable<?>)(variableInfo.getVariableCategories())) {
            if (variableCategory.getName().equals(category)) {
               variableCategorys.add(variableCategory);
               break;
            }
         }
      }

      return variableCategorys;
   }

   private List findParameterCategories() {
      return this.findVariableCategorys("参数");
   }

   public Variable findParameterByUuid(String uuid) {
      if (StringUtils.isBlank(uuid)) {
         return null;
      } else {
         Variable variable = null;

         for(VariableCategory variableCategory : (Iterable<VariableCategory>)(Iterable<?>)(this.findParameterCategories())) {
            for(Variable variable2 : variableCategory.getVariables()) {
               if (uuid.equals(variable2.getUuid())) {
                  variable = variable2;
               }
            }
         }

         return variable;
      }
   }

   public Variable findSimpleParameterByLabel(String label) {
      if (StringUtils.isBlank(label)) {
         return null;
      } else {
         Variable variable = null;
         VariableCategory variableCategory = null;

         for(VariableCategory variableCategory2 : (Iterable<VariableCategory>)(Iterable<?>)(this.findParameterCategories())) {
            if (variableCategory2.getVariableLabels().containsKey(label)) {
               variable = (Variable)variableCategory2.getVariableLabels().get(label);
               variableCategory = variableCategory2;
            }
         }

         if (variable != null && variableCategory != null) {
            for(VariableInfo variableInfo : (Iterable<VariableInfo>)(Iterable<?>)(this.variableInfos)) {
               for(VariableCategory variableCategory3 : (Iterable<VariableCategory>)(Iterable<?>)(variableInfo.getVariableCategories())) {
                  if (variableCategory3.getUuid().equals(variableCategory.getUuid()) && variableCategory3.getVariableLabels().containsKey(label)) {
                     this.varLibraries.put(variableInfo.getId(), variableInfo);
                  }
               }
            }
         }

         return variable;
      }
   }

   public Variable findParameterByLabel(String label, String propertyLabel) {
      Variable variable = null;
      VariableCategory variableCategory = null;

      for(VariableCategory variableCategory2 : (Iterable<VariableCategory>)(Iterable<?>)(this.findParameterCategories())) {
         if (variableCategory2.getVariableLabels().containsKey(label)) {
            Variable variable2 = (Variable)variableCategory2.getVariableLabels().get(label);
            if (StringUtils.isNotBlank(variable2.getDataType())) {
               VariableCategory variableCategoryByUUID = this.findVariableCategoryByUUID(variable2.getDataType());
               if (variableCategoryByUUID != null) {
                  Variable variable3 = (Variable)variableCategoryByUUID.getVariableLabels().get(propertyLabel);
                  if (variable3 != null) {
                     variable = variable2;
                     variableCategory = variableCategory2;
                     break;
                  }
               }
            }
         }
      }

      if (variable != null && variableCategory != null) {
         for(VariableInfo variableInfo : (Iterable<VariableInfo>)(Iterable<?>)(this.variableInfos)) {
            for(VariableCategory variableCategory3 : (Iterable<VariableCategory>)(Iterable<?>)(variableInfo.getVariableCategories())) {
               if (variableCategory3.getUuid().equals(variableCategory.getUuid()) && variableCategory3.getVariableLabels().containsKey(label)) {
                  this.varLibraries.put(variableInfo.getId(), variableInfo);
               }
            }
         }
      }

      return variable;
   }

   public VariableCategory findVariableCategoryByUUID(String uuid) {
      VariableCategory variableCategory = null;

      for(VariableInfo variableInfo : (Iterable<VariableInfo>)(Iterable<?>)(this.variableInfos)) {
         for(VariableCategory variableCategory2 : (Iterable<VariableCategory>)(Iterable<?>)(variableInfo.getVariableCategories())) {
            if (variableCategory2.getUuid().equals(uuid)) {
               this.varLibraries.put(variableInfo.getId(), variableInfo);
               variableCategory = variableCategory2;
               break;
            }
         }

         if (variableCategory != null) {
            break;
         }
      }

      return variableCategory;
   }

   public Variable findVariable(String categoryLabel, String variableLabel, boolean allowNull) {
      String[] values = new String[]{categoryLabel, variableLabel};
      return this.findVariable(values, false);
   }

   public Variable findVariable(String categoryLabel, String variableLabel) {
      return this.findVariable(categoryLabel, variableLabel, false);
   }

   public Variable findVariable(String[] names) {
      return this.findVariable(names, false);
   }

   public Constant findConstant(String[] names, boolean allowNull) {
      String text = names[0];
      String text2 = names[1];

      for(ConstantInfo constantInfo : (Iterable<ConstantInfo>)(Iterable<?>)(this.constantInfos)) {
         this.contLibraries.put(constantInfo.getId(), constantInfo);

         for(ConstantCategory constantCategory : (Iterable<ConstantCategory>)(Iterable<?>)(constantInfo.getConstantCategories())) {
            if (constantCategory.getLabel().equals(text)) {
               List constants = constantCategory.getConstants();
               if (constants != null) {
                  for(Constant constant : (Iterable<Constant>)(Iterable<?>)(constants)) {
                     if (constant.getLabel().equals(text2) || constant.getName().equals(text2)) {
                        return constant;
                     }
                  }
               }
            }
         }
      }

      if (allowNull) {
         return null;
      } else {
         throw new InfoException("常量[" + text + "." + text2 + "]在当前项目中未定义!");
      }
   }

   public ConstantCategory findConstantCategory(String[] names) {
      ConstantCategory constantCategory = null;
      String text = names[0];
      String text2 = names[1];

      for(ConstantInfo constantInfo : (Iterable<ConstantInfo>)(Iterable<?>)(this.constantInfos)) {
         List constantCategories = constantInfo.getConstantCategories();
         this.contLibraries.put(constantInfo.getId(), constantInfo);

         for(ConstantCategory constantCategory2 : (Iterable<ConstantCategory>)(Iterable<?>)(constantCategories)) {
            if (constantCategory2.getLabel().equals(text)) {
               List constants = constantCategory2.getConstants();
               if (constants != null) {
                  for(Constant constant : (Iterable<Constant>)(Iterable<?>)(constants)) {
                     if (constant.getLabel().equals(text2) || constant.getName().equals(text2)) {
                        constantCategory = constantCategory2;
                     }
                  }
               }
            }
         }
      }

      return constantCategory;
   }
}
