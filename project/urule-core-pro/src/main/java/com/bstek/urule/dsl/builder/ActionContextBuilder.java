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
   private Collection<FunctionDescriptor> a;

   public Action build(ParserRuleContext var1) {
      RuleParserParser$ActionContext var2 = (RuleParserParser$ActionContext)var1;
      if (var2.outAction() != null) {
         return this.a(var2.outAction());
      } else if (var2.assignAction() != null) {
         return this.a(var2.assignAction());
      } else if (var2.methodInvoke() != null) {
         return this.a(var2.methodInvoke());
      } else {
         return var2.commonFunction() != null ? this.a(var2.commonFunction()) : null;
      }
   }

   private ExecuteCommonFunctionAction a(RuleParserParser$CommonFunctionContext var1) {
      ExecuteCommonFunctionAction var2 = new ExecuteCommonFunctionAction();
      String var3 = var1.Identifier().getText();

      for (FunctionDescriptor var5 : this.a) {
         if (var3.equals(var5.getName())) {
            var2.setName(var5.getName());
            var2.setLabel(var5.getLabel());
            break;
         }

         if (var3.equals(var5.getLabel())) {
            var2.setName(var5.getName());
            var2.setLabel(var5.getLabel());
            break;
         }
      }

      if (var2.getName() == null) {
         throw new RuleException("Function[" + var3 + "] not exist.");
      }

      RuleParserParser$ComplexValueContext var7 = var1.complexValue();
      CommonFunctionParameter var8 = new CommonFunctionParameter();
      var8.setObjectParameter(BuildUtils.buildValue(var7));
      RuleParserParser$PropertyContext var6 = var1.property();
      if (var6 != null) {
         var8.setProperty(var6.getText());
      }

      var2.setParameter(var8);
      return var2;
   }

   private ExecuteMethodAction a(RuleParserParser$MethodInvokeContext var1) {
      ExecuteMethodAction var2 = new ExecuteMethodAction();
      RuleParserParser$BeanMethodContext var3 = var1.beanMethod();
      var2.setBeanLabel(var3.getChild(0).getText());
      var2.setMethodLabel(var3.getChild(2).getText());
      RuleParserParser$ActionParametersContext var4 = var1.actionParameters();
      if (var4 != null) {
         for (RuleParserParser$ComplexValueContext var6 : var4.complexValue()) {
            Parameter var7 = new Parameter();
            var7.setValue(BuildUtils.buildValue(var6));
            var2.addParameter(var7);
         }
      }

      return var2;
   }

   private VariableAssignAction a(RuleParserParser$AssignActionContext var1) {
      VariableAssignAction var2 = new VariableAssignAction();
      RuleParserParser$ParameterContext var3 = var1.parameter();
      if (var3 == null) {
         var2.setVariableCategory(var1.variable().variableCategory().getText());
         var2.setVariableLabel(var1.variable().property().getText());
      } else {
         var2.setVariableCategory("参数");
         var2.setVariableLabel(var3.Identifier().getText());
      }

      var2.setValue(BuildUtils.buildValue(var1.complexValue()));
      return var2;
   }

   private ConsolePrintAction a(RuleParserParser$OutActionContext var1) {
      ConsolePrintAction var2 = new ConsolePrintAction();
      AbstractValue var3 = BuildUtils.buildValue(var1.complexValue());
      var2.setValue(var3);
      return var2;
   }

   public void setApplicationContext(ApplicationContext var1) throws BeansException {
      this.a = var1.getBeansOfType(FunctionDescriptor.class).values();
   }

   @Override
   public boolean support(ParserRuleContext var1) {
      return var1 instanceof RuleParserParser$ActionContext;
   }
}
