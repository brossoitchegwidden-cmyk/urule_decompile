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
   private Collection<FunctionDescriptor> a;

   public Criteria build(ParserRuleContext var1) {
      RuleParserParser$SingleConditionContext var2 = (RuleParserParser$SingleConditionContext)var1;
      RuleParserParser$ConditionLeftContext var3 = var2.conditionLeft();
      RuleParserParser$VariableContext var4 = null;
      RuleParserParser$ParameterContext var5 = null;
      RuleParserParser$FunctionInvokeContext var6 = null;
      RuleParserParser$CommonFunctionContext var7 = null;
      RuleParserParser$MethodInvokeContext var8 = null;
      if (var3 != null) {
         var4 = var3.variable();
         var5 = var3.parameter();
         var6 = var3.functionInvoke();
         var7 = var3.commonFunction();
         var8 = var3.methodInvoke();
      }

      Criteria var9 = new Criteria();
      Left var10 = new Left();
      LeftPart var11 = null;
      String var12 = null;
      String var13 = null;
      if (var4 != null) {
         var12 = var4.variableCategory().Identifier().getText();
         var13 = var4.property().getText();
         VariableLeftPart var14 = new VariableLeftPart();
         var14.setVariableCategory(var12);
         var14.setVariableLabel(var13);
         var10.setType(LeftType.variable);
         var11 = var14;
      } else if (var5 != null) {
         var12 = "参数";
         var13 = var5.Identifier().getText();
         VariableLeftPart var27 = new VariableLeftPart();
         var27.setVariableCategory(var12);
         var27.setVariableLabel(var13);
         var10.setType(LeftType.variable);
         var11 = var27;
      } else if (var6 != null) {
         FunctionLeftPart var28 = new FunctionLeftPart();
         String var15 = var6.Identifier().getText();
         RuleParserParser$ActionParametersContext var16 = var6.actionParameters();
         if (var16 != null) {
            ArrayList var17 = new ArrayList();

            for (RuleParserParser$ComplexValueContext var19 : var16.complexValue()) {
               Parameter var20 = new Parameter();
               var20.setValue(BuildUtils.buildValue(var19));
               var17.add(var20);
            }

            var28.setParameters(var17);
         }

         var28.setName(var15);
         var10.setType(LeftType.function);
         var11 = var28;
      } else if (var7 != null) {
         CommonFunctionLeftPart var29 = new CommonFunctionLeftPart();
         String var32 = var7.Identifier().getText();

         for (FunctionDescriptor var38 : this.a) {
            if (var32.equals(var38.getName())) {
               var29.setName(var38.getName());
               var29.setLabel(var38.getLabel());
               break;
            }

            if (var32.equals(var38.getLabel())) {
               var29.setName(var38.getName());
               var29.setLabel(var38.getLabel());
               break;
            }
         }

         if (var29.getName() == null) {
            throw new RuleException("Function[" + var32 + "] not exist.");
         }

         RuleParserParser$ComplexValueContext var36 = var7.complexValue();
         CommonFunctionParameter var39 = new CommonFunctionParameter();
         var39.setObjectParameter(BuildUtils.buildValue(var36));
         RuleParserParser$PropertyContext var41 = var7.property();
         if (var41 != null) {
            var39.setProperty(var41.getText());
         }

         var29.setParameter(var39);
         var10.setType(LeftType.commonfunction);
         var11 = var29;
      } else if (var8 != null) {
         MethodLeftPart var30 = new MethodLeftPart();
         RuleParserParser$BeanMethodContext var33 = var8.beanMethod();
         String var37 = var33.Identifier(0).getText();
         String var40 = var33.Identifier(1).getText();
         var30.setBeanLabel(var37);
         var30.setMethodLabel(var40);
         RuleParserParser$ActionParametersContext var42 = var8.actionParameters();
         if (var42 != null) {
            ArrayList var43 = new ArrayList();

            for (RuleParserParser$ComplexValueContext var21 : var42.complexValue()) {
               Parameter var22 = new Parameter();
               var22.setValue(BuildUtils.buildValue(var21));
               var43.add(var22);
            }

            var30.setParameters(var43);
         }

         var10.setType(LeftType.method);
         var11 = var30;
      }

      var10.setLeftPart(var11);
      var9.setLeft(var10);
      Op var31 = DSLUtils.parseOp(var2.op());
      var9.setOp(var31);
      RuleParserParser$NullValueContext var34 = var2.nullValue();
      if (var34 != null) {
         if (var31.equals(Op.Equals)) {
            var9.setOp(Op.Null);
         } else {
            if (!var31.equals(Op.NotEquals)) {
               throw new RuleException("'null' value only support '==' or '!=' operator.");
            }

            var9.setOp(Op.NotNull);
         }
      } else {
         var9.setValue(BuildUtils.buildValue(var2.complexValue()));
      }

      return var9;
   }

   @Override
   public boolean support(ParserRuleContext var1) {
      return var1 instanceof RuleParserParser$SingleConditionContext;
   }

   public void setApplicationContext(ApplicationContext var1) throws BeansException {
      this.a = var1.getBeansOfType(FunctionDescriptor.class).values();
   }
}
