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
   public boolean doEval(EvaluationContext context, boolean debug) {
      Map factMap = context.getWorkingMemory().getFactManager().getFactMap();
      EvaluateResponse evaluateResponse = this.evaluate(context, factMap);
      if (debug) {
         context.getLogger().logCriteria(this, evaluateResponse);
      }

      return evaluateResponse.getResult();
   }

   @Override
   public EvaluateResponse evaluate(EvaluationContext context, Map<String, Object> factMap) {
      Datatype datatype = null;
      Object objectValue = null;
      context.cleanTipMsg();
      if (this.file != null) {
         context.addTipMsg("计算条件(" + this.file + ")：" + this.getId());
      } else {
         context.addTipMsg("计算条件：" + this.getId());
      }

      ValueCompute valueCompute = context.getValueCompute();
      LeftPart leftPart = this.left.getLeftPart();
      if (leftPart instanceof AccumulateLeftPart) {
         AccumulateLeftPart accumulateLeftPart = (AccumulateLeftPart)leftPart;
         return accumulateLeftPart.evaluate(context, factMap);
      }

      String id = this.left.getId();
      context.addTipMsg("左值：" + id);
      Object objectValue2 = null;
      if (leftPart instanceof VariableLeftPart) {
         VariableLeftPart variableLeftPart = (VariableLeftPart)leftPart;
         String variableCategoryClass = context.getVariableCategoryClass(variableLeftPart.getVariableCategory());
         if ("参数".equals(variableLeftPart.getVariableCategory())) {
            variableCategoryClass = HashMap.class.getName();
         }

         Object objectValue3 = context.getValueCompute().findObject(variableCategoryClass, factMap, context);
         datatype = variableLeftPart.getDatatype();
         if (StringUtils.isNotBlank(variableLeftPart.getVariableName())) {
            if (objectValue3 != null) {
               String keyName = variableLeftPart.getKeyName();
               if (StringUtils.isNotBlank(keyName)) {
                  Object objectProperty = Utils.getObjectProperty(objectValue3, keyName);
                  if (objectProperty == null) {
                     throw new RuleException("[参数]中的[" + variableLeftPart.getKeyLabel() + "]不存在！");
                  }

                  objectValue2 = Utils.getObjectProperty(objectProperty, variableLeftPart.getVariableName());
               } else {
                  objectValue2 = Utils.getObjectProperty(objectValue3, variableLeftPart.getVariableName());
               }
            } else {
               log.warning("Object [" + variableCategoryClass + "] not exist.");
               objectValue2 = objectValue3;
            }
         } else {
            objectValue2 = objectValue3;
         }
      } else if (leftPart instanceof PredefineLeftPart) {
         AbstractWorkingMemory workingMemory = (AbstractWorkingMemory)context.getWorkingMemory();
         PredefineLeftPart predefineLeftPart = (PredefineLeftPart)leftPart;
         Object predefineValue = workingMemory.getPredefineValue(predefineLeftPart.getUuid());
         if (predefineLeftPart.getPropertyName() != null) {
            predefineValue = Utils.getObjectProperty(predefineValue, predefineLeftPart.getPropertyName());
         }

         objectValue2 = predefineValue;
      } else if (leftPart instanceof MethodLeftPart) {
         MethodLeftPart methodLeftPart = (MethodLeftPart)leftPart;
         ExecuteMethodAction executeMethodAction = new ExecuteMethodAction();
         executeMethodAction.setBeanId(methodLeftPart.getBeanId());
         executeMethodAction.setBeanLabel(methodLeftPart.getBeanLabel());
         executeMethodAction.setMethodLabel(methodLeftPart.getMethodLabel());
         executeMethodAction.setMethodName(methodLeftPart.getMethodName());
         executeMethodAction.setParameters(methodLeftPart.getParameters());
         SpringBean builtinAction = ActionUtils.getBuiltinAction(methodLeftPart.getBeanId());
         if (builtinAction != null) {
            executeMethodAction.setBeanELabel(builtinAction.getEname());
         }

         ActionValue actionValue = executeMethodAction.execute(context, factMap);
         if (actionValue == null) {
            objectValue2 = null;
         } else {
            objectValue2 = actionValue.getValue();
         }
      } else if (leftPart instanceof CommonFunctionLeftPart) {
         CommonFunctionLeftPart commonFunctionLeftPart = (CommonFunctionLeftPart)leftPart;
         objectValue2 = commonFunctionLeftPart.evaluate(context, factMap);
      }

      objectValue = objectValue2;
      ComplexArithmetic arithmetic = this.left.getArithmetic();
      if (arithmetic != null) {
         objectValue = valueCompute.complexArithmeticCompute(context, factMap, arithmetic, objectValue2, id);
      }

      EvaluateResponse evaluateResponse = new EvaluateResponse();
      evaluateResponse.setLeftResult(objectValue);
      Object objectValue4 = null;
      if (this.value != null) {
         String id2 = this.value.getId();
         context.addTipMsg("右值：" + id2);
         objectValue4 = valueCompute.complexValueCompute(this.value, context, factMap);
         evaluateResponse.setRightResult(objectValue4);
      }

      if (datatype == null) {
         datatype = Utils.getDatatype(objectValue);
      }

      context.addTipMsg("执行比较：" + this.op.toString());
      boolean flag = context.getAssertorEvaluator().evaluate(objectValue, objectValue4, datatype, this.op);
      evaluateResponse.setResult(flag);
      context.cleanTipMsg();
      return evaluateResponse;
   }

   public boolean necessaryClassEval(Set<String> classSet) {
      for (String text : this.necessaryClassList) {
         if (!text.equals("*") && !classSet.contains(text)) {
            return false;
         }
      }

      return true;
   }

   public void addNecessaryClass(String clazz) {
      this.necessaryClassList.add(clazz);
   }

   public void addNecessaryClasses(List<String> classes) {
      this.necessaryClassList.addAll(classes);
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

   public void setId(String id) {
      this.id = id;
   }

   public String getFile() {
      return this.file;
   }

   public void setFile(String file) {
      this.file = file;
   }

   public Op getOp() {
      return this.op;
   }

   public void setOp(Op op) {
      this.op = op;
   }

   public Left getLeft() {
      return this.left;
   }

   public void setLeft(Left left) {
      this.left = left;
   }

   public Value getValue() {
      return this.value;
   }

   public void setValue(Value value) {
      this.value = value;
   }
}
