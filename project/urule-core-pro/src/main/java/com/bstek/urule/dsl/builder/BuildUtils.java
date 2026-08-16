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

public class BuildUtils {
   public static AbstractValue buildValue(RuleParserParser$ComplexValueContext var0) {
      if (var0 == null) {
         return null;
      }

      AbstractValue var1 = null;
      if (var0.leftParen() != null) {
         ParenValue var2 = new ParenValue();
         List var3 = var0.complexValue();
         AbstractValue var4 = buildValue((RuleParserParser$ComplexValueContext)var3.get(0));
         var2.setValue(var4);
         var1 = var2;
      } else if (var0.value() != null) {
         var1 = a(var0.value());
      } else if (var0.variable() != null) {
         var1 = a(var0.variable());
      } else if (var0.constant() != null) {
         var1 = a(var0.constant());
      } else if (var0.variableCategory() != null) {
         RuleParserParser$VariableCategoryContext var13 = var0.variableCategory();
         String var19 = var13.Identifier().getText();
         var1 = new VariableCategoryValue(var19);
      } else if (var0.parameter() != null) {
         RuleParserParser$ParameterContext var14 = var0.parameter();
         ParameterValue var20 = new ParameterValue();
         var20.setVariableLabel(var14.Identifier().getText());
         var1 = var20;
      } else if (var0.methodInvoke() != null) {
         RuleParserParser$MethodInvokeContext var15 = var0.methodInvoke();
         MethodValue var21 = new MethodValue();
         RuleParserParser$BeanMethodContext var24 = var15.beanMethod();
         String var5 = var24.Identifier(0).getText();
         String var6 = var24.Identifier(1).getText();
         var21.setBeanLabel(var5);
         var21.setMethodLabel(var6);
         RuleParserParser$ActionParametersContext var7 = var15.actionParameters();
         if (var7 != null && var7.complexValue() != null) {
            List var8 = var7.complexValue();
            ArrayList var9 = new ArrayList();

            for (RuleParserParser$ComplexValueContext var11 : (Iterable<RuleParserParser$ComplexValueContext>)(Iterable<?>)(var8)) {
               Parameter var12 = new Parameter();
               var12.setValue(buildValue(var11));
               var9.add(var12);
            }

            var21.setParameters(var9);
         }

         var1 = var21;
      } else if (var0.commonFunction() != null) {
         RuleParserParser$CommonFunctionContext var16 = var0.commonFunction();
         Collection var22 = Utils.getApplicationContext().getBeansOfType(FunctionDescriptor.class).values();
         CommonFunctionValue var25 = new CommonFunctionValue();
         String var27 = var16.Identifier().getText();

         for (FunctionDescriptor var31 : (Iterable<FunctionDescriptor>)(Iterable<?>)(var22)) {
            if (var27.equals(var31.getName())) {
               var25.setName(var31.getName());
               var25.setLabel(var31.getLabel());
               break;
            }

            if (var27.equals(var31.getLabel())) {
               var25.setName(var31.getName());
               var25.setLabel(var31.getLabel());
               break;
            }
         }

         if (var25.getName() == null) {
            throw new RuleException("Function[" + var27 + "] not exist.");
         }

         RuleParserParser$ComplexValueContext var30 = var16.complexValue();
         CommonFunctionParameter var32 = new CommonFunctionParameter();
         var32.setObjectParameter(buildValue(var30));
         RuleParserParser$PropertyContext var33 = var16.property();
         if (var33 != null) {
            var32.setProperty(var33.getText());
         }

         var25.setParameter(var32);
         var1 = var25;
      } else if (var0.complexValue() != null) {
         List var17 = var0.complexValue();
         var1 = buildValue((RuleParserParser$ComplexValueContext)var17.get(0));
      }

      List var18 = var0.ARITH();
      if (!Objects.isNull(var1) && var18 != null && var18.size() > 0) {
         TerminalNode var23 = (TerminalNode)var18.get(0);
         ComplexArithmetic var26 = new ComplexArithmetic();
         var26.setType(ArithmeticType.parse(var23.getText()));
         ParseTree var28 = var0.getChild(2);
         var26.setValue(buildValue((RuleParserParser$ComplexValueContext)var28));
         var1.setArithmetic(var26);
      }

      return var1;
   }

   private static ConstantValue a(RuleParserParser$ConstantContext var0) {
      ConstantValue var1 = new ConstantValue();
      var1.setConstantCategory(var0.constantCategory().Identifier().getText());
      var1.setConstantLabel(var0.property().getText());
      return var1;
   }

   private static VariableValue a(RuleParserParser$VariableContext var0) {
      VariableValue var1 = new VariableValue();
      var1.setVariableCategory(var0.variableCategory().getText());
      var1.setVariableLabel(var0.property().getText());
      return var1;
   }

   private static SimpleValue a(RuleParserParser$ValueContext var0) {
      SimpleValue var1 = new SimpleValue();
      if (var0.STRING() != null) {
         var1.setContent(getSTRINGContent(var0.STRING()));
      } else if (var0.Boolean() != null) {
         var1.setContent(var0.Boolean().getText());
      } else if (var0.NUMBER() != null) {
         var1.setContent(var0.NUMBER().getText());
      }

      return var1;
   }

   public static String getSTRINGContent(TerminalNode var0) {
      String var1 = var0.getText();
      return var1.substring(1, var1.length() - 1);
   }
}
