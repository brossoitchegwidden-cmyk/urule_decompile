package com.bstek.urule.model.rule.lhs;

import com.bstek.urule.Utils;
import com.bstek.urule.action.ActionValue;
import com.bstek.urule.action.ExecuteMethodAction;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.library.action.SpringBean;
import com.bstek.urule.model.rule.ComplexArithmetic;
import com.bstek.urule.model.rule.Op;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.runtime.AbstractWorkingMemory;
import com.bstek.urule.runtime.ActionUtils;
import com.bstek.urule.runtime.rete.EvaluationContext;
import com.bstek.urule.runtime.rete.ValueCompute;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;

public class Criteria extends BaseCriterion implements BaseCriteria {
   private static Logger log = Logger.getGlobal();
   @JsonIgnore
   private String id;
   private Op op;
   private Left left;
   private Value value;
   private String file;
   private Set<String> necessaryClassList = new HashSet<>();

   @Override
   public boolean doEval(EvaluationContext var1, boolean var2) {
      Map var3 = var1.getWorkingMemory().getFactManager().getFactMap();
      EvaluateResponse var4 = this.evaluate(var1, var3);
      if (var2) {
         var1.getLogger().logCriteria(this, var4);
      }

      return var4.getResult();
   }

   @Override
   public EvaluateResponse evaluate(EvaluationContext var1, Map<String, Object> var2) {
      Datatype var3 = null;
      Object var4 = null;
      var1.cleanTipMsg();
      if (this.file != null) {
         var1.addTipMsg("计算条件(" + this.file + ")：" + this.getId());
      } else {
         var1.addTipMsg("计算条件：" + this.getId());
      }

      ValueCompute var5 = var1.getValueCompute();
      LeftPart var6 = this.left.getLeftPart();
      if (var6 instanceof AccumulateLeftPart) {
         AccumulateLeftPart var15 = (AccumulateLeftPart)var6;
         return var15.evaluate(var1, var2);
      }

      String var7 = this.left.getId();
      var1.addTipMsg("左值：" + var7);
      Object var8 = null;
      if (var6 instanceof VariableLeftPart) {
         VariableLeftPart var9 = (VariableLeftPart)var6;
         String var10 = var1.getVariableCategoryClass(var9.getVariableCategory());
         if ("参数".equals(var9.getVariableCategory())) {
            var10 = HashMap.class.getName();
         }

         Object var11 = var1.getValueCompute().findObject(var10, var2, var1);
         var3 = var9.getDatatype();
         if (StringUtils.isNotBlank(var9.getVariableName())) {
            if (var11 != null) {
               String var12 = var9.getKeyName();
               if (StringUtils.isNotBlank(var12)) {
                  Object var13 = Utils.getObjectProperty(var11, var12);
                  if (var13 == null) {
                     throw new RuleException("[参数]中的[" + var9.getKeyLabel() + "]不存在！");
                  }

                  var8 = Utils.getObjectProperty(var13, var9.getVariableName());
               } else {
                  var8 = Utils.getObjectProperty(var11, var9.getVariableName());
               }
            } else {
               log.warning("Object [" + var10 + "] not exist.");
               var8 = var11;
            }
         } else {
            var8 = var11;
         }
      } else if (var6 instanceof PredefineLeftPart) {
         AbstractWorkingMemory var16 = (AbstractWorkingMemory)var1.getWorkingMemory();
         PredefineLeftPart var20 = (PredefineLeftPart)var6;
         Object var23 = var16.getPredefineValue(var20.getUuid());
         if (var20.getPropertyName() != null) {
            var23 = Utils.getObjectProperty(var23, var20.getPropertyName());
         }

         var8 = var23;
      } else if (var6 instanceof MethodLeftPart) {
         MethodLeftPart var17 = (MethodLeftPart)var6;
         ExecuteMethodAction var21 = new ExecuteMethodAction();
         var21.setBeanId(var17.getBeanId());
         var21.setBeanLabel(var17.getBeanLabel());
         var21.setMethodLabel(var17.getMethodLabel());
         var21.setMethodName(var17.getMethodName());
         var21.setParameters(var17.getParameters());
         SpringBean var24 = ActionUtils.getBuiltinAction(var17.getBeanId());
         if (var24 != null) {
            var21.setBeanELabel(var24.getEname());
         }

         ActionValue var26 = var21.execute(var1, var2);
         if (var26 == null) {
            var8 = null;
         } else {
            var8 = var26.getValue();
         }
      } else if (var6 instanceof CommonFunctionLeftPart) {
         CommonFunctionLeftPart var18 = (CommonFunctionLeftPart)var6;
         var8 = var18.evaluate(var1, var2);
      }

      var4 = var8;
      ComplexArithmetic var19 = this.left.getArithmetic();
      if (var19 != null) {
         var4 = var5.complexArithmeticCompute(var1, var2, var19, var8, var7);
      }

      EvaluateResponse var22 = new EvaluateResponse();
      var22.setLeftResult(var4);
      Object var25 = null;
      if (this.value != null) {
         String var27 = this.value.getId();
         var1.addTipMsg("右值：" + var27);
         var25 = var5.complexValueCompute(this.value, var1, var2);
         var22.setRightResult(var25);
      }

      if (var3 == null) {
         var3 = Utils.getDatatype(var4);
      }

      var1.addTipMsg("执行比较：" + this.op.toString());
      boolean var28 = var1.getAssertorEvaluator().evaluate(var4, var25, var3, this.op);
      var22.setResult(var28);
      var1.cleanTipMsg();
      return var22;
   }

   public boolean necessaryClassEval(Set<String> var1) {
      for (String var3 : this.necessaryClassList) {
         if (!var3.equals("*") && !var1.contains(var3)) {
            return false;
         }
      }

      return true;
   }

   public void addNecessaryClass(String var1) {
      this.necessaryClassList.add(var1);
   }

   public void addNecessaryClasses(List<String> var1) {
      this.necessaryClassList.addAll(var1);
   }

   public Set<String> getNecessaryClassList() {
      return this.necessaryClassList;
   }

   @Override
   public String getId() {
      if (this.id == null) {
         this.id = this.left.getId();
         if (this.op != null) {
            this.id = this.id + "【" + this.op.toString() + "】";
         }

         if (this.value != null) {
            this.id = this.id + this.value.getId();
         }
      }

      return this.id;
   }

   public void setId(String var1) {
      this.id = var1;
   }

   public String getFile() {
      return this.file;
   }

   public void setFile(String var1) {
      this.file = var1;
   }

   public Op getOp() {
      return this.op;
   }

   public void setOp(Op var1) {
      this.op = var1;
   }

   public Left getLeft() {
      return this.left;
   }

   public void setLeft(Left var1) {
      this.left = var1;
   }

   public Value getValue() {
      return this.value;
   }

   public void setValue(Value var1) {
      this.value = var1;
   }
}
