package com.bstek.urule.dsl.builder;

import com.bstek.urule.dsl.DSLUtils;
import com.bstek.urule.dsl.RuleParserParser$ActionParametersContext;
import com.bstek.urule.dsl.RuleParserParser$BeanMethodContext;
import com.bstek.urule.dsl.RuleParserParser$CommonFunctionContext;
import com.bstek.urule.dsl.RuleParserParser$ComplexValueContext;
import com.bstek.urule.dsl.RuleParserParser$ConditionLeftContext;
import com.bstek.urule.dsl.RuleParserParser$FunctionInvokeContext;
import com.bstek.urule.dsl.RuleParserParser$MethodInvokeContext;
import com.bstek.urule.dsl.RuleParserParser$NullValueContext;
import com.bstek.urule.dsl.RuleParserParser$ParameterContext;
import com.bstek.urule.dsl.RuleParserParser$PropertyContext;
import com.bstek.urule.dsl.RuleParserParser$SingleConditionContext;
import com.bstek.urule.dsl.RuleParserParser$VariableContext;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.function.FunctionDescriptor;
import com.bstek.urule.model.rule.Op;
import com.bstek.urule.model.rule.Parameter;
import com.bstek.urule.model.rule.lhs.CommonFunctionLeftPart;
import com.bstek.urule.model.rule.lhs.CommonFunctionParameter;
import com.bstek.urule.model.rule.lhs.Criteria;
import com.bstek.urule.model.rule.lhs.FunctionLeftPart;
import com.bstek.urule.model.rule.lhs.Left;
import com.bstek.urule.model.rule.lhs.LeftPart;
import com.bstek.urule.model.rule.lhs.LeftType;
import com.bstek.urule.model.rule.lhs.MethodLeftPart;
import com.bstek.urule.model.rule.lhs.VariableLeftPart;
import java.util.ArrayList;
import java.util.Collection;
import org.antlr.v4.runtime.ParserRuleContext;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class CriteriaContextBuilder extends AbstractContextBuilder implements ApplicationContextAware {
   private Collection<FunctionDescriptor> functionDescriptors;

   public Criteria build(ParserRuleContext context) {
      RuleParserParser$SingleConditionContext ruleParserParser$SingleConditionContext = (RuleParserParser$SingleConditionContext)context;
      RuleParserParser$ConditionLeftContext ruleParserParser$ConditionLeftContext = ruleParserParser$SingleConditionContext.conditionLeft();
      RuleParserParser$VariableContext ruleParserParser$VariableContext = null;
      RuleParserParser$ParameterContext ruleParserParser$ParameterContext = null;
      RuleParserParser$FunctionInvokeContext ruleParserParser$FunctionInvokeContext = null;
      RuleParserParser$CommonFunctionContext ruleParserParser$CommonFunctionContext = null;
      RuleParserParser$MethodInvokeContext ruleParserParser$MethodInvokeContext = null;
      if (ruleParserParser$ConditionLeftContext != null) {
         ruleParserParser$VariableContext = ruleParserParser$ConditionLeftContext.variable();
         ruleParserParser$ParameterContext = ruleParserParser$ConditionLeftContext.parameter();
         ruleParserParser$FunctionInvokeContext = ruleParserParser$ConditionLeftContext.functionInvoke();
         ruleParserParser$CommonFunctionContext = ruleParserParser$ConditionLeftContext.commonFunction();
         ruleParserParser$MethodInvokeContext = ruleParserParser$ConditionLeftContext.methodInvoke();
      }

      Criteria criteria = new Criteria();
      Left left = new Left();
      LeftPart leftPart = null;
      String text2 = null;
      String text3 = null;
      if (ruleParserParser$VariableContext != null) {
         text2 = ruleParserParser$VariableContext.variableCategory().Identifier().getText();
         text3 = ruleParserParser$VariableContext.property().getText();
         VariableLeftPart variableLeftPart = new VariableLeftPart();
         variableLeftPart.setVariableCategory(text2);
         variableLeftPart.setVariableLabel(text3);
         left.setType(LeftType.variable);
         leftPart = variableLeftPart;
      } else if (ruleParserParser$ParameterContext != null) {
         text2 = "参数";
         text3 = ruleParserParser$ParameterContext.Identifier().getText();
         VariableLeftPart variableLeftPart2 = new VariableLeftPart();
         variableLeftPart2.setVariableCategory(text2);
         variableLeftPart2.setVariableLabel(text3);
         left.setType(LeftType.variable);
         leftPart = variableLeftPart2;
      } else if (ruleParserParser$FunctionInvokeContext != null) {
         FunctionLeftPart functionLeftPart = new FunctionLeftPart();
         String text = ruleParserParser$FunctionInvokeContext.Identifier().getText();
         RuleParserParser$ActionParametersContext ruleParserParser$ActionParametersContext = ruleParserParser$FunctionInvokeContext.actionParameters();
         if (ruleParserParser$ActionParametersContext != null) {
            ArrayList items = new ArrayList();

            for (RuleParserParser$ComplexValueContext ruleParserParser$ComplexValueContext : ruleParserParser$ActionParametersContext.complexValue()) {
               Parameter parameter = new Parameter();
               parameter.setValue(BuildUtils.buildValue(ruleParserParser$ComplexValueContext));
               items.add(parameter);
            }

            functionLeftPart.setParameters(items);
         }

         functionLeftPart.setName(text);
         left.setType(LeftType.function);
         leftPart = functionLeftPart;
      } else if (ruleParserParser$CommonFunctionContext != null) {
         CommonFunctionLeftPart commonFunctionLeftPart = new CommonFunctionLeftPart();
         String text4 = ruleParserParser$CommonFunctionContext.Identifier().getText();

         for (FunctionDescriptor functionDescriptor : this.functionDescriptors) {
            if (text4.equals(functionDescriptor.getName())) {
               commonFunctionLeftPart.setName(functionDescriptor.getName());
               commonFunctionLeftPart.setLabel(functionDescriptor.getLabel());
               break;
            }

            if (text4.equals(functionDescriptor.getLabel())) {
               commonFunctionLeftPart.setName(functionDescriptor.getName());
               commonFunctionLeftPart.setLabel(functionDescriptor.getLabel());
               break;
            }
         }

         if (commonFunctionLeftPart.getName() == null) {
            throw new RuleException("Function[" + text4 + "] not exist.");
         }

         RuleParserParser$ComplexValueContext ruleParserParser$ComplexValueContext2 = ruleParserParser$CommonFunctionContext.complexValue();
         CommonFunctionParameter commonFunctionParameter = new CommonFunctionParameter();
         commonFunctionParameter.setObjectParameter(BuildUtils.buildValue(ruleParserParser$ComplexValueContext2));
         RuleParserParser$PropertyContext ruleParserParser$PropertyContext = ruleParserParser$CommonFunctionContext.property();
         if (ruleParserParser$PropertyContext != null) {
            commonFunctionParameter.setProperty(ruleParserParser$PropertyContext.getText());
         }

         commonFunctionLeftPart.setParameter(commonFunctionParameter);
         left.setType(LeftType.commonfunction);
         leftPart = commonFunctionLeftPart;
      } else if (ruleParserParser$MethodInvokeContext != null) {
         MethodLeftPart methodLeftPart = new MethodLeftPart();
         RuleParserParser$BeanMethodContext ruleParserParser$BeanMethodContext = ruleParserParser$MethodInvokeContext.beanMethod();
         String text5 = ruleParserParser$BeanMethodContext.Identifier(0).getText();
         String text6 = ruleParserParser$BeanMethodContext.Identifier(1).getText();
         methodLeftPart.setBeanLabel(text5);
         methodLeftPart.setMethodLabel(text6);
         RuleParserParser$ActionParametersContext ruleParserParser$ActionParametersContext2 = ruleParserParser$MethodInvokeContext.actionParameters();
         if (ruleParserParser$ActionParametersContext2 != null) {
            ArrayList items2 = new ArrayList();

            for (RuleParserParser$ComplexValueContext ruleParserParser$ComplexValueContext3 : ruleParserParser$ActionParametersContext2.complexValue()) {
               Parameter parameter2 = new Parameter();
               parameter2.setValue(BuildUtils.buildValue(ruleParserParser$ComplexValueContext3));
               items2.add(parameter2);
            }

            methodLeftPart.setParameters(items2);
         }

         left.setType(LeftType.method);
         leftPart = methodLeftPart;
      }

      left.setLeftPart(leftPart);
      criteria.setLeft(left);
      Op op = DSLUtils.parseOp(ruleParserParser$SingleConditionContext.op());
      criteria.setOp(op);
      RuleParserParser$NullValueContext ruleParserParser$NullValueContext = ruleParserParser$SingleConditionContext.nullValue();
      if (ruleParserParser$NullValueContext != null) {
         if (op.equals(Op.Equals)) {
            criteria.setOp(Op.Null);
         } else {
            if (!op.equals(Op.NotEquals)) {
               throw new RuleException("'null' value only support '==' or '!=' operator.");
            }

            criteria.setOp(Op.NotNull);
         }
      } else {
         criteria.setValue(BuildUtils.buildValue(ruleParserParser$SingleConditionContext.complexValue()));
      }

      return criteria;
   }

   @Override
   public boolean support(ParserRuleContext context) {
      return context instanceof RuleParserParser$SingleConditionContext;
   }

   public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
      this.functionDescriptors = applicationContext.getBeansOfType(FunctionDescriptor.class).values();
   }
}
