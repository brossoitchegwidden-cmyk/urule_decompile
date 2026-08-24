package com.bstek.urule.dsl.builder;

import com.bstek.urule.Utils;
import com.bstek.urule.dsl.RuleParserParser$ActionParametersContext;
import com.bstek.urule.dsl.RuleParserParser$BeanMethodContext;
import com.bstek.urule.dsl.RuleParserParser$CommonFunctionContext;
import com.bstek.urule.dsl.RuleParserParser$ComplexValueContext;
import com.bstek.urule.dsl.RuleParserParser$ConstantContext;
import com.bstek.urule.dsl.RuleParserParser$MethodInvokeContext;
import com.bstek.urule.dsl.RuleParserParser$ParameterContext;
import com.bstek.urule.dsl.RuleParserParser$PropertyContext;
import com.bstek.urule.dsl.RuleParserParser$ValueContext;
import com.bstek.urule.dsl.RuleParserParser$VariableCategoryContext;
import com.bstek.urule.dsl.RuleParserParser$VariableContext;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.function.FunctionDescriptor;
import com.bstek.urule.model.rule.AbstractValue;
import com.bstek.urule.model.rule.ArithmeticType;
import com.bstek.urule.model.rule.CommonFunctionValue;
import com.bstek.urule.model.rule.ComplexArithmetic;
import com.bstek.urule.model.rule.ConstantValue;
import com.bstek.urule.model.rule.MethodValue;
import com.bstek.urule.model.rule.Parameter;
import com.bstek.urule.model.rule.ParameterValue;
import com.bstek.urule.model.rule.ParenValue;
import com.bstek.urule.model.rule.SimpleValue;
import com.bstek.urule.model.rule.VariableCategoryValue;
import com.bstek.urule.model.rule.VariableValue;
import com.bstek.urule.model.rule.lhs.CommonFunctionParameter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

/** Builds rule values from the ANTLR DSL parse tree. */
public class BuildUtils {
   public static AbstractValue buildValue(RuleParserParser$ComplexValueContext context) {
      if (context == null) {
         return null;
      }

      AbstractValue abstractValue = null;
      if (context.leftParen() != null) {
         ParenValue parenValue = new ParenValue();
         List items = context.complexValue();
         AbstractValue abstractValue2 = buildValue((RuleParserParser$ComplexValueContext)items.get(0));
         parenValue.setValue(abstractValue2);
         abstractValue = parenValue;
      } else if (context.value() != null) {
         abstractValue = buildSimpleValue(context.value());
      } else if (context.variable() != null) {
         abstractValue = buildVariableValue(context.variable());
      } else if (context.constant() != null) {
         abstractValue = buildConstantValue(context.constant());
      } else if (context.variableCategory() != null) {
         RuleParserParser$VariableCategoryContext ruleParserParser$VariableCategoryContext = context.variableCategory();
         String text = ruleParserParser$VariableCategoryContext.Identifier().getText();
         abstractValue = new VariableCategoryValue(text);
      } else if (context.parameter() != null) {
         RuleParserParser$ParameterContext ruleParserParser$ParameterContext = context.parameter();
         ParameterValue parameterValue = new ParameterValue();
         parameterValue.setVariableLabel(ruleParserParser$ParameterContext.Identifier().getText());
         abstractValue = parameterValue;
      } else if (context.methodInvoke() != null) {
         RuleParserParser$MethodInvokeContext ruleParserParser$MethodInvokeContext = context.methodInvoke();
         MethodValue methodValue = new MethodValue();
         RuleParserParser$BeanMethodContext ruleParserParser$BeanMethodContext = ruleParserParser$MethodInvokeContext.beanMethod();
         String text2 = ruleParserParser$BeanMethodContext.Identifier(0).getText();
         String text3 = ruleParserParser$BeanMethodContext.Identifier(1).getText();
         methodValue.setBeanLabel(text2);
         methodValue.setMethodLabel(text3);
         RuleParserParser$ActionParametersContext ruleParserParser$ActionParametersContext = ruleParserParser$MethodInvokeContext.actionParameters();
         if (ruleParserParser$ActionParametersContext != null && ruleParserParser$ActionParametersContext.complexValue() != null) {
            List items2 = ruleParserParser$ActionParametersContext.complexValue();
            ArrayList items3 = new ArrayList();

            for (RuleParserParser$ComplexValueContext ruleParserParser$ComplexValueContext : (Iterable<RuleParserParser$ComplexValueContext>)(Iterable<?>)(items2)) {
               Parameter parameter = new Parameter();
               parameter.setValue(buildValue(ruleParserParser$ComplexValueContext));
               items3.add(parameter);
            }

            methodValue.setParameters(items3);
         }

         abstractValue = methodValue;
      } else if (context.commonFunction() != null) {
         RuleParserParser$CommonFunctionContext ruleParserParser$CommonFunctionContext = context.commonFunction();
         Collection functionDescriptors = Utils.getApplicationContext().getBeansOfType(FunctionDescriptor.class).values();
         CommonFunctionValue commonFunctionValue = new CommonFunctionValue();
         String text4 = ruleParserParser$CommonFunctionContext.Identifier().getText();

         for (FunctionDescriptor functionDescriptor : (Iterable<FunctionDescriptor>)(Iterable<?>)(functionDescriptors)) {
            if (text4.equals(functionDescriptor.getName())) {
               commonFunctionValue.setName(functionDescriptor.getName());
               commonFunctionValue.setLabel(functionDescriptor.getLabel());
               break;
            }

            if (text4.equals(functionDescriptor.getLabel())) {
               commonFunctionValue.setName(functionDescriptor.getName());
               commonFunctionValue.setLabel(functionDescriptor.getLabel());
               break;
            }
         }

         if (commonFunctionValue.getName() == null) {
            throw new RuleException("Function[" + text4 + "] not exist.");
         }

         RuleParserParser$ComplexValueContext ruleParserParser$ComplexValueContext2 = ruleParserParser$CommonFunctionContext.complexValue();
         CommonFunctionParameter commonFunctionParameter = new CommonFunctionParameter();
         commonFunctionParameter.setObjectParameter(buildValue(ruleParserParser$ComplexValueContext2));
         RuleParserParser$PropertyContext ruleParserParser$PropertyContext = ruleParserParser$CommonFunctionContext.property();
         if (ruleParserParser$PropertyContext != null) {
            commonFunctionParameter.setProperty(ruleParserParser$PropertyContext.getText());
         }

         commonFunctionValue.setParameter(commonFunctionParameter);
         abstractValue = commonFunctionValue;
      } else if (context.complexValue() != null) {
         List items4 = context.complexValue();
         abstractValue = buildValue((RuleParserParser$ComplexValueContext)items4.get(0));
      }

      List items5 = context.ARITH();
      if (!Objects.isNull(abstractValue) && items5 != null && items5.size() > 0) {
         TerminalNode terminalNode = (TerminalNode)items5.get(0);
         ComplexArithmetic complexArithmetic = new ComplexArithmetic();
         complexArithmetic.setType(ArithmeticType.parse(terminalNode.getText()));
         ParseTree child = context.getChild(2);
         complexArithmetic.setValue(buildValue((RuleParserParser$ComplexValueContext)child));
         abstractValue.setArithmetic(complexArithmetic);
      }

      return abstractValue;
   }

   private static ConstantValue buildConstantValue(RuleParserParser$ConstantContext context) {
      ConstantValue constantValue = new ConstantValue();
      constantValue.setConstantCategory(context.constantCategory().Identifier().getText());
      constantValue.setConstantLabel(context.property().getText());
      return constantValue;
   }

   private static VariableValue buildVariableValue(RuleParserParser$VariableContext context) {
      VariableValue variableValue = new VariableValue();
      variableValue.setVariableCategory(context.variableCategory().getText());
      variableValue.setVariableLabel(context.property().getText());
      return variableValue;
   }

   private static SimpleValue buildSimpleValue(RuleParserParser$ValueContext context) {
      SimpleValue simpleValue = new SimpleValue();
      if (context.STRING() != null) {
         simpleValue.setContent(getSTRINGContent(context.STRING()));
      } else if (context.Boolean() != null) {
         simpleValue.setContent(context.Boolean().getText());
      } else if (context.NUMBER() != null) {
         simpleValue.setContent(context.NUMBER().getText());
      }

      return simpleValue;
   }

   public static String getSTRINGContent(TerminalNode node) {
      String text = node.getText();
      return text.substring(1, text.length() - 1);
   }
}
