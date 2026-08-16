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
   private String b;
   private String c;
   private String d;
   private String e;
   private String f;
   private String g;
   private String h;
   private String i;
   private InvokeFile j;
   private InvokeKnowledgePackage k;
   private List<Parameter> l;
   private ActionType m = ActionType.ExecuteMethod;

   @Override
   public ActionValue execute(Context var1, Map<String, Object> var2) {
      String var3 = LocaleHolder.isEnglish() ? this.f : this.e;
      String var4 = LocaleHolder.isEnglish() ? this.h : this.g;
      String var5 = (var3 == null ? this.d : var3) + "." + (var4 == null ? this.i : var4);
      String var6 = LocaleHolder.isEnglish() ? "Execute Method" : "执行动作";
      var5 = "$$$" + var6 + "：" + var5;
      if (this.k != null) {
         if (this.a) {
            String var28 = this.k.getProject()
               + "："
               + this.k.getName()
               + "("
               + (StringUtils.isNotBlank(this.k.getCode()) ? this.k.getCode() + ">" : "")
               + this.k.getId()
               + ")";
            var1.getLogger().logExecuteBeanMethod(var5, var28);
         }

         this.a(var1);
         return null;
      } else if (this.j != null) {
         if (this.a) {
            String var26 = this.j.getPath() + "(" + this.j.getId() + ")";
            if (StringUtils.isNotBlank(this.j.getVersion())) {
               var26 = var26 + ":" + this.j.getVersion();
            }

            var1.getLogger().logExecuteBeanMethod(var5, var26);
         }

         KnowledgePackage var27 = this.j.getKnowledgePackageWrapper().getKnowledgePackage();
         this.a(var27, var1);
         return null;
      } else {
         try {
            boolean var7 = false;
            boolean var8 = false;
            if (this.d.contentEquals("urule.commonAction") && this.i.contentEquals("iferror")) {
               var7 = true;
            } else if (this.d.contentEquals("urule.objectAction") && this.i.contentEquals("newObjectInstance")) {
               var8 = true;
            }

            Object var9 = var1.getApplicationContext().getBean(this.d);
            Method var10 = null;
            if (this.l != null && this.l.size() > 0) {
               ParametersWrap var30 = this.a(var1, var2, var7, var8);
               Method[] var31 = var9.getClass().getMethods();
               Datatype[] var32 = var30.getDatatypes();
               boolean var14 = false;

               for (Method var18 : var31) {
                  var10 = var18;
                  String var19 = var18.getName();
                  if (var19.equals(this.i)) {
                     Class[] var20 = var18.getParameterTypes();
                     if (var20.length == this.l.size()) {
                        for (int var21 = 0; var21 < var20.length; var21++) {
                           Class var22 = var20[var21];
                           Datatype var23 = var32[var21];
                           var14 = this.a(var22, var23);
                           if (!var14) {
                              break;
                           }
                        }

                        if (var14) {
                           break;
                        }
                     }
                  }
               }

               if (!var14) {
                  throw new RuleException("Bean [" + this.d + "." + this.i + "] with " + this.l.size() + " parameters not exist");
               }

               String var33 = this.i;
               ActionId var34 = var10.getAnnotation(ActionId.class);
               if (var34 != null) {
                  var33 = var34.value();
               }

               if (this.a) {
                  var1.getLogger().logExecuteBeanMethod(var5, var30.valuesToString());
               }

               Object var35 = var10.invoke(var9, var30.getValues());
               if (var33.equals("_loop_rule_break_tag__")) {
                  var1.getWorkingMemory().getParameters().put(var33, var35);
                  return null;
               } else {
                  return new ActionValueImpl(var33, var35);
               }
            } else {
               var10 = var9.getClass().getMethod(this.i);
               String var11 = this.i;
               ActionId var12 = var10.getAnnotation(ActionId.class);
               if (var12 != null) {
                  var11 = var12.value();
               }

               if (this.a) {
                  var1.getLogger().logExecuteBeanMethod(var5, "");
               }

               Object var13 = var10.invoke(var9);
               if (var13 != null) {
                  if (var11.equals("_loop_rule_break_tag__")) {
                     var1.getWorkingMemory().getParameters().put(var11, var13);
                     return null;
                  } else {
                     return new ActionValueImpl(var11, var13);
                  }
               } else {
                  return null;
               }
            }
         } catch (Exception var24) {
            throw new RuleException(var24);
         }
      }
   }

   private void a(Context var1) {
      KnowledgeService var2 = (KnowledgeService)var1.getApplicationContext().getBean("urule.knowledgeService");

      try {
         KnowledgePackage var3 = null;
         if (StringUtils.isNotBlank(this.k.getCode())) {
            var3 = var2.getKnowledge(this.k.getCode());
         } else {
            var3 = var2.getKnowledge(String.valueOf(this.k.getId()));
         }

         this.a(var3, var1);
      } catch (Exception var4) {
         throw new RuleException(var4);
      }
   }

   private void a(KnowledgePackage var1, Context var2) {
      try {
         KnowledgeSession var3 = (KnowledgeSession)var2.getWorkingMemory();
         KnowledgeSession var4 = KnowledgeSessionFactory.newKnowledgeSession(var1, var3);
         if (var1.getFlowMap() != null && var1.getFlowMap().size() != 0) {
            String var5 = var1.getFlowMap().values().iterator().next().getId();
            var4.startProcess(var5, var3.getParameters());
         } else {
            var4.fireRules(var3.getParameters());
         }

         var2.addRuleData(var4.getLogManager().getRuleData());
         Map var10 = var4.getParameters();
         Map var6 = var3.getParameters();

         for (String var8 : (Iterable<String>)(Iterable<?>)(var10.keySet())) {
            var6.put(var8, var10.get(var8));
         }
      } catch (Exception var9) {
         throw new RuleException(var9);
      }
   }

   private boolean a(Class<?> var1, Datatype var2) {
      boolean var3 = false;
      switch (var2) {
         case String:
            if (var1.equals(String.class)) {
               var3 = true;
            } else {
               var3 = false;
            }
            break;
         case BigDecimal:
            if (var1.equals(BigDecimal.class)) {
               var3 = true;
            } else {
               var3 = false;
            }
            break;
         case Boolean:
            if (!var1.equals(Boolean.class) && !var1.equals(boolean.class)) {
               var3 = false;
            } else {
               var3 = true;
            }
            break;
         case Date:
            if (var1.equals(Date.class)) {
               var3 = true;
            } else {
               var3 = false;
            }
            break;
         case Double:
            if (!var1.equals(Double.class) && !var1.equals(double.class)) {
               var3 = false;
            } else {
               var3 = true;
            }
            break;
         case Enum:
            if (Enum.class.isAssignableFrom(var1)) {
               var3 = true;
            } else {
               var3 = false;
            }
            break;
         case Float:
            if (!var1.equals(Float.class) && !var1.equals(float.class)) {
               var3 = false;
            } else {
               var3 = true;
            }
            break;
         case Integer:
            if (!var1.equals(Integer.class) && !var1.equals(int.class)) {
               var3 = false;
            } else {
               var3 = true;
            }
            break;
         case Char:
            if (!var1.equals(Character.class) && !var1.equals(char.class)) {
               var3 = false;
            } else {
               var3 = true;
            }
            break;
         case List:
            if (List.class.isAssignableFrom(var1)) {
               var3 = true;
            } else {
               var3 = false;
            }
            break;
         case Long:
            if (!var1.equals(Long.class) && !var1.equals(long.class)) {
               var3 = false;
            } else {
               var3 = true;
            }
            break;
         case Map:
            if (Map.class.isAssignableFrom(var1)) {
               var3 = true;
            } else {
               var3 = false;
            }
            break;
         case Set:
            if (Set.class.isAssignableFrom(var1)) {
               var3 = true;
            } else {
               var3 = false;
            }
            break;
         case Object:
            var3 = true;
      }

      return var3;
   }

   private ParametersWrap a(Context var1, Map<String, Object> var2, boolean var3, boolean var4) {
      ArrayList var5 = new ArrayList();
      ArrayList var6 = new ArrayList();
      ValueCompute var7 = var1.getValueCompute();
      var1.resetParentIsObjectInstanceMethod(var4);

      for (int var8 = 0; var8 < this.l.size(); var8++) {
         Parameter var9 = this.l.get(var8);
         Datatype var10 = var9.getType();
         var5.add(var10);
         if (var8 == 0 && var3) {
            try {
               Object var16 = var7.complexValueCompute(var9.getValue(), var1, var2);
               var6.add(var10.convert(var16));
            } catch (Exception var12) {
               var1.getLogger().logIFErrorLog(var9, var12);
               var6.add(var12);
            }
         } else {
            Object var11 = var7.complexValueCompute(var9.getValue(), var1, var2);
            var6.add(var10.convert(var11));
         }
      }

      Datatype[] var13 = new Datatype[var5.size()];
      var5.toArray(var13);
      Object[] var14 = new Object[var6.size()];
      var6.toArray(var14);
      ParametersWrap var15 = new ParametersWrap();
      var15.setDatatypes(var13);
      var15.setValues(var14);
      return var15;
   }

   public String getMethodLabel() {
      return this.g;
   }

   public void setMethodLabel(String var1) {
      this.g = var1;
   }

   public String getBeanId() {
      return this.d;
   }

   public void setBeanId(String var1) {
      this.d = var1;
   }

   public String getMethodName() {
      return this.i;
   }

   public void setMethodName(String var1) {
      this.i = var1;
   }

   public String getBeanLabel() {
      return this.e;
   }

   public void setBeanLabel(String var1) {
      this.e = var1;
   }

   public String getBeanELabel() {
      return this.f;
   }

   public void setBeanELabel(String var1) {
      this.f = var1;
   }

   public String getMethodELabel() {
      return this.h;
   }

   public void setMethodELabel(String var1) {
      this.h = var1;
   }

   public List<Parameter> getParameters() {
      return this.l;
   }

   public void setParameters(List<Parameter> var1) {
      this.l = var1;
   }

   public String getCategoryUuid() {
      return this.b;
   }

   public void setCategoryUuid(String var1) {
      this.b = var1;
   }

   public String getUuid() {
      return this.c;
   }

   public void setUuid(String var1) {
      this.c = var1;
   }

   public InvokeFile getInvokeFile() {
      return this.j;
   }

   public void setInvokeFile(InvokeFile var1) {
      this.j = var1;
   }

   public InvokeKnowledgePackage getInvokeKnowledgePackage() {
      return this.k;
   }

   public void setInvokeKnowledgePackage(InvokeKnowledgePackage var1) {
      this.k = var1;
   }

   public void addParameter(Parameter var1) {
      if (this.l == null) {
         this.l = new ArrayList<>();
      }

      this.l.add(var1);
   }

   @Override
   public ActionType getActionType() {
      return this.m;
   }
}
