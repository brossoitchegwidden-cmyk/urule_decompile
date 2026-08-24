package com.bstek.urule.dsl.builder;

import com.bstek.urule.action.Action;
import com.bstek.urule.action.ConsolePrintAction;
import com.bstek.urule.action.ExecuteCommonFunctionAction;
import com.bstek.urule.action.ExecuteMethodAction;
import com.bstek.urule.action.VariableAssignAction;
import com.bstek.urule.dsl.RuleParserParser$ActionContext;
import com.bstek.urule.dsl.RuleParserParser$ActionParametersContext;
import com.bstek.urule.dsl.RuleParserParser$AssignActionContext;
import com.bstek.urule.dsl.RuleParserParser$BeanMethodContext;
import com.bstek.urule.dsl.RuleParserParser$CommonFunctionContext;
import com.bstek.urule.dsl.RuleParserParser$ComplexValueContext;
import com.bstek.urule.dsl.RuleParserParser$MethodInvokeContext;
import com.bstek.urule.dsl.RuleParserParser$OutActionContext;
import com.bstek.urule.dsl.RuleParserParser$ParameterContext;
import com.bstek.urule.dsl.RuleParserParser$PropertyContext;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.function.FunctionDescriptor;
import com.bstek.urule.model.rule.AbstractValue;
import com.bstek.urule.model.rule.Parameter;
import com.bstek.urule.model.rule.lhs.CommonFunctionParameter;
import java.util.Collection;
import org.antlr.v4.runtime.ParserRuleContext;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ActionContextBuilder extends AbstractContextBuilder implements ApplicationContextAware {
   private Collection<FunctionDescriptor> functionDescriptors;

   public Action build(ParserRuleContext context) {
      RuleParserParser$ActionContext ruleParserParser$ActionContext = (RuleParserParser$ActionContext)context;
      if (ruleParserParser$ActionContext.outAction() != null) {
         return this.buildConsolePrintAction(ruleParserParser$ActionContext.outAction());
      } else if (ruleParserParser$ActionContext.assignAction() != null) {
         return this.buildVariableAssignAction(ruleParserParser$ActionContext.assignAction());
      } else if (ruleParserParser$ActionContext.methodInvoke() != null) {
         return this.buildExecuteMethodAction(ruleParserParser$ActionContext.methodInvoke());
      } else {
         return ruleParserParser$ActionContext.commonFunction() != null ? this.buildExecuteCommonFunctionAction(ruleParserParser$ActionContext.commonFunction()) : null;
      }
   }

   private ExecuteCommonFunctionAction buildExecuteCommonFunctionAction(RuleParserParser$CommonFunctionContext ruleParserParser$CommonFunctionContext) {
      ExecuteCommonFunctionAction executeCommonFunctionAction = new ExecuteCommonFunctionAction();
      String text = ruleParserParser$CommonFunctionContext.Identifier().getText();

      for (FunctionDescriptor functionDescriptor : this.functionDescriptors) {
         if (text.equals(functionDescriptor.getName())) {
            executeCommonFunctionAction.setName(functionDescriptor.getName());
            executeCommonFunctionAction.setLabel(functionDescriptor.getLabel());
            break;
         }

         if (text.equals(functionDescriptor.getLabel())) {
            executeCommonFunctionAction.setName(functionDescriptor.getName());
            executeCommonFunctionAction.setLabel(functionDescriptor.getLabel());
            break;
         }
      }

      if (executeCommonFunctionAction.getName() == null) {
         throw new RuleException("Function[" + text + "] not exist.");
      }

      RuleParserParser$ComplexValueContext ruleParserParser$ComplexValueContext = ruleParserParser$CommonFunctionContext.complexValue();
      CommonFunctionParameter commonFunctionParameter = new CommonFunctionParameter();
      commonFunctionParameter.setObjectParameter(BuildUtils.buildValue(ruleParserParser$ComplexValueContext));
      RuleParserParser$PropertyContext ruleParserParser$PropertyContext = ruleParserParser$CommonFunctionContext.property();
      if (ruleParserParser$PropertyContext != null) {
         commonFunctionParameter.setProperty(ruleParserParser$PropertyContext.getText());
      }

      executeCommonFunctionAction.setParameter(commonFunctionParameter);
      return executeCommonFunctionAction;
   }

   private ExecuteMethodAction buildExecuteMethodAction(RuleParserParser$MethodInvokeContext ruleParserParser$MethodInvokeContext) {
      ExecuteMethodAction executeMethodAction = new ExecuteMethodAction();
      RuleParserParser$BeanMethodContext ruleParserParser$BeanMethodContext = ruleParserParser$MethodInvokeContext.beanMethod();
      executeMethodAction.setBeanLabel(ruleParserParser$BeanMethodContext.getChild(0).getText());
      executeMethodAction.setMethodLabel(ruleParserParser$BeanMethodContext.getChild(2).getText());
      RuleParserParser$ActionParametersContext ruleParserParser$ActionParametersContext = ruleParserParser$MethodInvokeContext.actionParameters();
      if (ruleParserParser$ActionParametersContext != null) {
         for (RuleParserParser$ComplexValueContext ruleParserParser$ComplexValueContext : ruleParserParser$ActionParametersContext.complexValue()) {
            Parameter parameter = new Parameter();
            parameter.setValue(BuildUtils.buildValue(ruleParserParser$ComplexValueContext));
            executeMethodAction.addParameter(parameter);
         }
      }

      return executeMethodAction;
   }

   private VariableAssignAction buildVariableAssignAction(RuleParserParser$AssignActionContext ruleParserParser$AssignActionContext) {
      VariableAssignAction variableAssignAction = new VariableAssignAction();
      RuleParserParser$ParameterContext ruleParserParser$ParameterContext = ruleParserParser$AssignActionContext.parameter();
      if (ruleParserParser$ParameterContext == null) {
         variableAssignAction.setVariableCategory(ruleParserParser$AssignActionContext.variable().variableCategory().getText());
         variableAssignAction.setVariableLabel(ruleParserParser$AssignActionContext.variable().property().getText());
      } else {
         variableAssignAction.setVariableCategory("参数");
         variableAssignAction.setVariableLabel(ruleParserParser$ParameterContext.Identifier().getText());
      }

      variableAssignAction.setValue(BuildUtils.buildValue(ruleParserParser$AssignActionContext.complexValue()));
      return variableAssignAction;
   }

   private ConsolePrintAction buildConsolePrintAction(RuleParserParser$OutActionContext ruleParserParser$OutActionContext) {
      ConsolePrintAction consolePrintAction = new ConsolePrintAction();
      AbstractValue abstractValue = BuildUtils.buildValue(ruleParserParser$OutActionContext.complexValue());
      consolePrintAction.setValue(abstractValue);
      return consolePrintAction;
   }

   public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
      this.functionDescriptors = applicationContext.getBeansOfType(FunctionDescriptor.class).values();
   }

   @Override
   public boolean support(ParserRuleContext context) {
      return context instanceof RuleParserParser$ActionContext;
   }
}
