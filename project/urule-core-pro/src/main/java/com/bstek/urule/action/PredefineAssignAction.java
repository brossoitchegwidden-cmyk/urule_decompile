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
   private String b;
   private String c;
   private Datatype d;
   private String e;
   private String f;
   private String g;
   private Datatype h;
   private String i;
   private String j;
   private Value k;

   @Override
   public ActionValue execute(Context var1, Map<String, Object> var2) {
      AbstractWorkingMemory var3 = (AbstractWorkingMemory)var1.getWorkingMemory();
      ValueCompute var4 = var1.getValueCompute();
      Object var5 = null;
      if (this.k == null) {
         return null;
      }

      var5 = var4.complexValueCompute(this.k, var1, var2);
      if (this.g == null) {
         if (this.d != null) {
            var5 = this.d.convert(var5);
         }

         var3.setPredefineValue(this.b, var5);
         if (this.a) {
            var1.getLogger().logValueAssign("[预定义值]" + this.c + "", var5);
         }
      } else {
         Object var6 = var3.getPredefineValue(this.b);
         if (var6 == null) {
            throw new RuleException("预定义对象【" + this.c + "】对应的对象【" + this.e + "】未初始化，不能为其属性【" + this.i + "】赋值");
         }

         var5 = this.h.convert(var5);
         Utils.setObjectProperty(var6, this.g, var5);
         if (this.a) {
            var1.getLogger().logValueAssign("[预定义值]" + this.c + "." + this.e + "." + this.g, var5);
         }
      }

      return null;
   }

   @Override
   public ActionType getActionType() {
      return ActionType.VariableAssign;
   }

   public String getUuid() {
      return this.b;
   }

   public void setUuid(String var1) {
      this.b = var1;
   }

   public LeftType getType() {
      return LeftType.predefine;
   }

   public String getName() {
      return this.c;
   }

   public void setName(String var1) {
      this.c = var1;
   }

   public Datatype getDatatype() {
      return this.d;
   }

   public void setDatatype(Datatype var1) {
      this.d = var1;
   }

   public String getVariableCategory() {
      return this.e;
   }

   public void setVariableCategory(String var1) {
      this.e = var1;
   }

   public String getVariableCategoryUuid() {
      return this.f;
   }

   public void setVariableCategoryUuid(String var1) {
      this.f = var1;
   }

   public String getPropertyName() {
      return this.g;
   }

   public void setPropertyName(String var1) {
      this.g = var1;
   }

   public Datatype getPropertyDatatype() {
      return this.h;
   }

   public void setPropertyDatatype(Datatype var1) {
      this.h = var1;
   }

   public String getPropertyLabel() {
      return this.i;
   }

   public void setPropertyLabel(String var1) {
      this.i = var1;
   }

   public String getPropertyUuid() {
      return this.j;
   }

   public void setPropertyUuid(String var1) {
      this.j = var1;
   }

   public Value getValue() {
      return this.k;
   }

   public void setValue(Value var1) {
      this.k = var1;
   }
}
