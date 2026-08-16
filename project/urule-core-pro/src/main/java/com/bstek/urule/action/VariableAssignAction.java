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

public class VariableAssignAction extends AbstractAction {
   private String b;
   private String c;
   private String d;
   private String e;
   private String f;
   private String g;
   private String h;
   private String i;
   private String j;
   private Datatype k;
   private Value l;
   private LeftType m;
   private ActionType n = ActionType.VariableAssign;

   @Override
   public ActionValue execute(Context var1, Map<String, Object> var2) {
      Object var3 = null;
      ValueCompute var4 = var1.getValueCompute();
      Object var5 = null;
      if (this.l == null) {
         return null;
      }

      var5 = var4.complexValueCompute(this.l, var1, var2);
      String var6 = var1.getVariableCategoryClass(this.h);
      if (var6.equals(HashMap.class.getName())) {
         var3 = var1.getWorkingMemory().getParameters();
      } else {
         var3 = var4.findObject(var6, var2, var1);
      }

      if (var3 == null) {
         throw new RuleException("The object【" + var6 + "】does not exist or is not initialized");
      }

      if (this.k.equals(Datatype.Enum) && var5 != null && StringUtils.isNotBlank(var5.toString())) {
         PropertyDescriptor var7 = BeanUtils.getPropertyDescriptor(var3.getClass(), this.f);
         if (var7 == null) {
            throw new RuleException("赋值操作无法获取当前枚举类型！");
         }

         Class var8 = var7.getPropertyType();
         var5 = Enum.valueOf(var8, var5.toString());
      } else if (var5 != null) {
         var5 = this.k.convert(var5);
      }

      String var11 = this.h;
      if (this.c == null && this.b == null) {
         var11 = var11 + "." + (this.g == null ? this.f : this.g);
      } else {
         var11 = var11 + "." + (this.c == null ? this.b : this.c) + "." + (this.g == null ? this.f : this.g);
      }

      if (this.a) {
         var1.getLogger().logValueAssign(var11, var5);
      }

      if (this.b != null) {
         var3 = Utils.getObjectProperty(var3, this.b);
         if (var3 == null) {
            throw new RuleException("要赋值的对象【" + this.c + "】在参数中不存在");
         }
      }

      if (var5 instanceof Map) {
         Map var14 = (Map)var5;
         this.a(var3, var14, var11);
         return null;
      }

      if (var5 instanceof String && this.k.equals(Datatype.Object)) {
         String var13 = (String)var5;
         this.a(var3, var13, var11);
      } else {
         this.a(var3, this.f, var5);
      }

      return null;
   }

   private void a(Object var1, String var2, String var3) {
      if (var2.startsWith("{") && var2.endsWith("}")) {
         ObjectMapper var4 = new ObjectMapper();

         try {
            Map var5 = (Map)var4.readValue(var2, HashMap.class);
            this.a(var1, var5, var3);
         } catch (Exception var6) {
            throw new RuleException("赋值操作值为Map类型:" + var2 + ",但无法将其转换为Map，请检查输入字符格式.");
         }
      } else {
         this.a(var1, this.f, (Object)var2);
      }
   }

   private void a(Object var1, Map<String, Object> var2, String var3) {
      String var4 = this.f;
      PropertyDescriptor var5 = BeanUtils.getPropertyDescriptor(var1.getClass(), this.f);
      if (var5 == null) {
         this.a(var1, var4, var2);
      } else {
         Class var6 = var5.getPropertyType();
         if (Map.class.isAssignableFrom(var6)) {
            this.a(var1, var4, var2);
         } else {
            Object var7 = Utils.getObjectProperty(var1, var4);
            if (var7 == null) {
               try {
                  Object var8 = var6.newInstance();

                  for (String var10 : var2.keySet()) {
                     this.a(var8, var10, var2.get(var10));
                  }

                  this.a(var1, var4, var8);
               } catch (InstantiationException | IllegalAccessException var12) {
                  throw new RuleException("赋值操作值为Map类型，赋值对象[" + var3 + "]类型为" + var6.getName() + "，无法对" + var6.getName() + "进行实例化");
               } catch (Exception var13) {
                  throw new RuleException("赋值操作值为Map类型，赋值对象[" + var3 + "]类型为" + var6.getName() + "，Map中key与[" + var6.getName() + "]类型对象属性存在不匹配情况");
               }
            } else {
               try {
                  for (String var15 : var2.keySet()) {
                     this.a(var7, var15, var2.get(var15));
                  }
               } catch (Exception var11) {
                  throw new RuleException("赋值操作值为Map类型，赋值对象[" + var3 + "]类型为" + var6.getName() + "，Map中key与[" + var6.getName() + "]类型对象属性存在不匹配情况");
               }
            }
         }
      }
   }

   private void a(Object var1, String var2, Object var3) {
      String[] var4 = var2.split("\\.");
      Object var5 = var1;

      for (int var6 = 0; var6 < var4.length; var6++) {
         String var7 = var4[var6];
         if (var6 == var4.length - 1) {
            Utils.setObjectProperty(var5, var7, var3);
            break;
         }

         Object var8 = Utils.getObjectProperty(var5, var7);
         if (var8 == null) {
            PropertyDescriptor var9 = BeanUtils.getPropertyDescriptor(var5.getClass(), var7);
            if (var9 == null) {
               var8 = new HashMap();
            } else {
               Class var10 = var9.getPropertyType();
               if (Map.class.isAssignableFrom(var10)) {
                  var8 = new HashMap();
               } else {
                  try {
                     var8 = var10.newInstance();
                  } catch (InstantiationException | IllegalAccessException var12) {
                     var12.printStackTrace();
                     throw new RuleException(
                        "尝试对[" + var1.getClass().getName() + "]对象实例的[" + var2 + "]里的子对象[" + var10.getName() + "]实例化失败，请确认子对象[" + var10.getName() + "]有空的构造函数"
                     );
                  }
               }
            }

            Utils.setObjectProperty(var5, var7, var8);
         }

         var5 = var8;
      }
   }

   public LeftType getType() {
      return this.m;
   }

   public void setType(LeftType var1) {
      this.m = var1;
   }

   public String getVariableName() {
      return this.f;
   }

   public void setVariableName(String var1) {
      this.f = var1;
   }

   public String getVariableLabel() {
      return this.g;
   }

   public void setVariableLabel(String var1) {
      this.g = var1;
   }

   public String getVariableCategory() {
      return this.h;
   }

   public void setVariableCategory(String var1) {
      this.h = var1;
   }

   public String getCategoryUuid() {
      return this.i;
   }

   public void setCategoryUuid(String var1) {
      this.i = var1;
   }

   public String getUuid() {
      return this.j;
   }

   public void setUuid(String var1) {
      this.j = var1;
   }

   public String getKeyName() {
      return this.b;
   }

   public void setKeyName(String var1) {
      this.b = var1;
   }

   public String getKeyLabel() {
      return this.c;
   }

   public void setKeyLabel(String var1) {
      this.c = var1;
   }

   public String getKeyCategoryUuid() {
      return this.d;
   }

   public void setKeyCategoryUuid(String var1) {
      this.d = var1;
   }

   public String getKeyUuid() {
      return this.e;
   }

   public void setKeyUuid(String var1) {
      this.e = var1;
   }

   public Value getValue() {
      return this.l;
   }

   public void setValue(Value var1) {
      this.l = var1;
   }

   public Datatype getDatatype() {
      return this.k;
   }

   public void setDatatype(Datatype var1) {
      this.k = var1;
   }

   @Override
   public ActionType getActionType() {
      return this.n;
   }
}
