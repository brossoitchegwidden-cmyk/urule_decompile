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

public class ExcelSupport {
   public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
   public static final String PREDEFINE_KEY = "Predefine";
   public static final String PREDEFINE_SHEET_NAME = "predefine";
   public static final String PROPERTY_SHEET_NAME = "property";
   public static final String PREDEFINE_SHEET_CNAME = "预定义变量";
   public static final String PROPERTY_SHEET_CNAME = "属性";
   private List a;
   private List b;
   private BuiltInActionLibraryBuilder c;
   private List d = new ArrayList();
   private Map e = new HashMap();
   private Map f = new HashMap();

   public static boolean isParameter(String var0) {
      return "参数".equals(var0) || "Parameter".equalsIgnoreCase(var0);
   }

   public ExcelSupport() {
      this.a = VariableLoader.ins.load(ContextHolder.getGroupId(), ContextHolder.getProjectId());
      this.b = ConstantLoader.ins.load(ContextHolder.getGroupId(), ContextHolder.getProjectId());
      ApplicationContext var1 = Utils.getApplicationContext();
      this.c = (BuiltInActionLibraryBuilder)var1.getBean("urule.builtInActionLibraryBuilder");

      for(FunctionDescriptor var4 : var1.getBeansOfType(FunctionDescriptor.class).values()) {
         if (!var4.isDisabled()) {
            this.d.add((FunctionDescriptor)ProxyUtils.getTargetObject(var4));
         }
      }

   }

   public List getVariableInfos() {
      return this.a;
   }

   public void setVariableInfos(List var1) {
      this.a = var1;
   }

   public List getConstantInfos() {
      return this.b;
   }

   public void setConstantInfos(List var1) {
      this.b = var1;
   }

   public void setBuiltInActionLibraryBuilder(BuiltInActionLibraryBuilder var1) {
      this.c = var1;
   }

   public List getFunctionDescriptors() {
      return this.d;
   }

   public void setFunctionDescriptors(List var1) {
      this.d = var1;
   }

   public Map getVarLibraries() {
      return this.e;
   }

   public Map getContLibraries() {
      return this.f;
   }

   public List getBuiltInActions() {
      return this.c.getBuiltInActions();
   }

   public FunctionDescriptor getFunction(String var1) {
      FunctionDescriptor var2 = null;

      for(FunctionDescriptor var4 : (Iterable<FunctionDescriptor>)(Iterable<?>)(this.d)) {
         if (var4.getLabel().equals(var1)) {
            var2 = var4;
            break;
         }
      }

      return var2;
   }

   public SpringBean getAction(String var1, String var2) {
      SpringBean var3 = null;
      Method var4 = null;

      for(SpringBean var6 : this.c.getBuiltInActions()) {
         if (var6.getName().equals(var1)) {
            var3 = var6;

            for(Method var8 : var6.getMethods()) {
               if (var8.getName().equals(var2)) {
                  var4 = var8;
                  return var4 != null ? var3 : null;
               }
            }
            break;
         }
      }

      return var4 != null ? var3 : null;
   }

   public Method getActionMethod(String var1, String var2) {
      Object var3 = null;
      Method var4 = null;

      for(SpringBean var6 : this.c.getBuiltInActions()) {
         if (var6.getName().equals(var1)) {
            for(Method var8 : var6.getMethods()) {
               if (var8.getName().equals(var2)) {
                  var4 = var8;
                  return var4;
               }
            }
            break;
         }
      }

      return var4;
   }

   public Variable findVariable(String[] var1, boolean var2) {
      String var3 = var1[0];
      String var4 = var1[1];

      for(VariableInfo var6 : (Iterable<VariableInfo>)(Iterable<?>)(this.a)) {
         for(VariableCategory var9 : (Iterable<VariableCategory>)(Iterable<?>)(var6.getVariableCategories())) {
            if (var9.getName().equals(var3)) {
               this.e.put(var6.getId(), var6);
               List var10 = var9.getVariables();
               if (var10 != null) {
                  for(Variable var12 : (Iterable<Variable>)(Iterable<?>)(var10)) {
                     if (var12.getLabel().equals(var4) || var12.getName().equals(var4)) {
                        return var12;
                     }
                  }
               }
            }
         }
      }

      if (var2) {
         return null;
      } else {
         throw new InfoException("变量[" + var3 + "." + var4 + "]在当前项目中未定义!");
      }
   }

   public VariableCategory findVariableCategory(String var1, String var2) {
      String[] var3 = new String[]{var1, var2};
      return this.findVariableCategory(var3);
   }

   public VariableCategory findVariableCategory(String[] var1) {
      VariableCategory var2 = null;
      String var3 = var1[0];
      String var4 = var1[1];

      for(VariableInfo var6 : (Iterable<VariableInfo>)(Iterable<?>)(this.a)) {
         for(VariableCategory var9 : (Iterable<VariableCategory>)(Iterable<?>)(var6.getVariableCategories())) {
            if (var9.getName().equals(var3)) {
               this.e.put(var6.getId(), var6);
               List var10 = var9.getVariables();
               if (var10 != null) {
                  for(Variable var12 : (Iterable<Variable>)(Iterable<?>)(var10)) {
                     if (var12.getLabel().equals(var4) || var12.getName().equals(var4)) {
                        var2 = var9;
                        break;
                     }
                  }

                  if (var2 != null) {
                     break;
                  }
               }
            }
         }

         if (var2 != null) {
            break;
         }
      }

      return var2;
   }

   public VariableCategory findVariableCategory(String var1) {
      VariableCategory var2 = null;

      for(VariableInfo var4 : (Iterable<VariableInfo>)(Iterable<?>)(this.a)) {
         for(VariableCategory var7 : (Iterable<VariableCategory>)(Iterable<?>)(var4.getVariableCategories())) {
            if (var7.getName().equals(var1)) {
               var2 = var7;
               break;
            }
         }

         if (var2 != null) {
            break;
         }
      }

      return var2;
   }

   public List findVariableCategorys(String var1) {
      ArrayList var2 = new ArrayList();

      for(VariableInfo var4 : (Iterable<VariableInfo>)(Iterable<?>)(this.a)) {
         for(VariableCategory var7 : (Iterable<VariableCategory>)(Iterable<?>)(var4.getVariableCategories())) {
            if (var7.getName().equals(var1)) {
               var2.add(var7);
               break;
            }
         }
      }

      return var2;
   }

   private List a() {
      return this.findVariableCategorys("参数");
   }

   public Variable findParameterByUuid(String var1) {
      if (StringUtils.isBlank(var1)) {
         return null;
      } else {
         Variable var2 = null;

         for(VariableCategory var5 : (Iterable<VariableCategory>)(Iterable<?>)(this.a())) {
            for(Variable var7 : var5.getVariables()) {
               if (var1.equals(var7.getUuid())) {
                  var2 = var7;
               }
            }
         }

         return var2;
      }
   }

   public Variable findSimpleParameterByLabel(String var1) {
      if (StringUtils.isBlank(var1)) {
         return null;
      } else {
         Variable var2 = null;
         VariableCategory var3 = null;

         for(VariableCategory var6 : (Iterable<VariableCategory>)(Iterable<?>)(this.a())) {
            if (var6.getVariableLabels().containsKey(var1)) {
               var2 = (Variable)var6.getVariableLabels().get(var1);
               var3 = var6;
            }
         }

         if (var2 != null && var3 != null) {
            for(VariableInfo var11 : (Iterable<VariableInfo>)(Iterable<?>)(this.a)) {
               for(VariableCategory var9 : (Iterable<VariableCategory>)(Iterable<?>)(var11.getVariableCategories())) {
                  if (var9.getUuid().equals(var3.getUuid()) && var9.getVariableLabels().containsKey(var1)) {
                     this.e.put(var11.getId(), var11);
                  }
               }
            }
         }

         return var2;
      }
   }

   public Variable findParameterByLabel(String var1, String var2) {
      Variable var3 = null;
      VariableCategory var4 = null;

      for(VariableCategory var7 : (Iterable<VariableCategory>)(Iterable<?>)(this.a())) {
         if (var7.getVariableLabels().containsKey(var1)) {
            Variable var8 = (Variable)var7.getVariableLabels().get(var1);
            if (StringUtils.isNotBlank(var8.getDataType())) {
               VariableCategory var9 = this.findVariableCategoryByUUID(var8.getDataType());
               if (var9 != null) {
                  Variable var10 = (Variable)var9.getVariableLabels().get(var2);
                  if (var10 != null) {
                     var3 = var8;
                     var4 = var7;
                     break;
                  }
               }
            }
         }
      }

      if (var3 != null && var4 != null) {
         for(VariableInfo var12 : (Iterable<VariableInfo>)(Iterable<?>)(this.a)) {
            for(VariableCategory var15 : (Iterable<VariableCategory>)(Iterable<?>)(var12.getVariableCategories())) {
               if (var15.getUuid().equals(var4.getUuid()) && var15.getVariableLabels().containsKey(var1)) {
                  this.e.put(var12.getId(), var12);
               }
            }
         }
      }

      return var3;
   }

   public VariableCategory findVariableCategoryByUUID(String var1) {
      VariableCategory var2 = null;

      for(VariableInfo var4 : (Iterable<VariableInfo>)(Iterable<?>)(this.a)) {
         for(VariableCategory var7 : (Iterable<VariableCategory>)(Iterable<?>)(var4.getVariableCategories())) {
            if (var7.getUuid().equals(var1)) {
               this.e.put(var4.getId(), var4);
               var2 = var7;
               break;
            }
         }

         if (var2 != null) {
            break;
         }
      }

      return var2;
   }

   public Variable findVariable(String var1, String var2, boolean var3) {
      String[] var4 = new String[]{var1, var2};
      return this.findVariable(var4, false);
   }

   public Variable findVariable(String var1, String var2) {
      return this.findVariable(var1, var2, false);
   }

   public Variable findVariable(String[] var1) {
      return this.findVariable(var1, false);
   }

   public Constant findConstant(String[] var1, boolean var2) {
      String var3 = var1[0];
      String var4 = var1[1];

      for(ConstantInfo var6 : (Iterable<ConstantInfo>)(Iterable<?>)(this.b)) {
         this.f.put(var6.getId(), var6);

         for(ConstantCategory var9 : (Iterable<ConstantCategory>)(Iterable<?>)(var6.getConstantCategories())) {
            if (var9.getLabel().equals(var3)) {
               List var10 = var9.getConstants();
               if (var10 != null) {
                  for(Constant var12 : (Iterable<Constant>)(Iterable<?>)(var10)) {
                     if (var12.getLabel().equals(var4) || var12.getName().equals(var4)) {
                        return var12;
                     }
                  }
               }
            }
         }
      }

      if (var2) {
         return null;
      } else {
         throw new InfoException("常量[" + var3 + "." + var4 + "]在当前项目中未定义!");
      }
   }

   public ConstantCategory findConstantCategory(String[] var1) {
      ConstantCategory var2 = null;
      String var3 = var1[0];
      String var4 = var1[1];

      for(ConstantInfo var6 : (Iterable<ConstantInfo>)(Iterable<?>)(this.b)) {
         List var7 = var6.getConstantCategories();
         this.f.put(var6.getId(), var6);

         for(ConstantCategory var9 : (Iterable<ConstantCategory>)(Iterable<?>)(var7)) {
            if (var9.getLabel().equals(var3)) {
               List var10 = var9.getConstants();
               if (var10 != null) {
                  for(Constant var12 : (Iterable<Constant>)(Iterable<?>)(var10)) {
                     if (var12.getLabel().equals(var4) || var12.getName().equals(var4)) {
                        var2 = var9;
                     }
                  }
               }
            }
         }
      }

      return var2;
   }
}
