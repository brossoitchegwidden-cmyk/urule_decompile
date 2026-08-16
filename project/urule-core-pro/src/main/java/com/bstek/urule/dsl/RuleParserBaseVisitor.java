package com.bstek.urule.dsl;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

public class RuleParserBaseVisitor<T> extends AbstractParseTreeVisitor<T> implements RuleParserVisitor<T> {
   @Override
   public T visitRuleSet(RuleParserParser$RuleSetContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitRuleSetHeader(RuleParserParser$RuleSetHeaderContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitRuleSetBody(RuleParserParser$RuleSetBodyContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitRules(RuleParserParser$RulesContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitFunctionImport(RuleParserParser$FunctionImportContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitPackageDef(RuleParserParser$PackageDefContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitResource(RuleParserParser$ResourceContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitImportParameterLibrary(RuleParserParser$ImportParameterLibraryContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitImportVariableLibrary(RuleParserParser$ImportVariableLibraryContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitImportConstantLibrary(RuleParserParser$ImportConstantLibraryContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitImportActionLibrary(RuleParserParser$ImportActionLibraryContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitFunctionDef(RuleParserParser$FunctionDefContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitFunctionParameters(RuleParserParser$FunctionParametersContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitFunctionParameter(RuleParserParser$FunctionParameterContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitRuleDef(RuleParserParser$RuleDefContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitLoopRuleDef(RuleParserParser$LoopRuleDefContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitLoopRuleUnit(RuleParserParser$LoopRuleUnitContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitLoopTarget(RuleParserParser$LoopTargetContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitLoopStart(RuleParserParser$LoopStartContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitLoopEnd(RuleParserParser$LoopEndContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitAttribute(RuleParserParser$AttributeContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitLoopAttribute(RuleParserParser$LoopAttributeContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitSalienceAttribute(RuleParserParser$SalienceAttributeContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitEffectiveDateAttribute(RuleParserParser$EffectiveDateAttributeContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitExpiresDateAttribute(RuleParserParser$ExpiresDateAttributeContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitEnabledAttribute(RuleParserParser$EnabledAttributeContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitDebugAttribute(RuleParserParser$DebugAttributeContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitActivationGroupAttribute(RuleParserParser$ActivationGroupAttributeContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitAgendaGroupAttribute(RuleParserParser$AgendaGroupAttributeContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitAutoFocusAttribute(RuleParserParser$AutoFocusAttributeContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitRuleflowGroupAttribute(RuleParserParser$RuleflowGroupAttributeContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitLeft(RuleParserParser$LeftContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitParenConditions(RuleParserParser$ParenConditionsContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitMultiConditions(RuleParserParser$MultiConditionsContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitSingleCondition(RuleParserParser$SingleConditionContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitSingleNamedConditionSet(RuleParserParser$SingleNamedConditionSetContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitNamedConditionSet(RuleParserParser$NamedConditionSetContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitParenNamedConditions(RuleParserParser$ParenNamedConditionsContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitMultiNamedConditions(RuleParserParser$MultiNamedConditionsContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitSingleNamedConditions(RuleParserParser$SingleNamedConditionsContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitSingleCellCondition(RuleParserParser$SingleCellConditionContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitMultiCellConditions(RuleParserParser$MultiCellConditionsContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitParenCellConditions(RuleParserParser$ParenCellConditionsContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitRefName(RuleParserParser$RefNameContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitRefObject(RuleParserParser$RefObjectContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitNullValue(RuleParserParser$NullValueContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitConditionLeft(RuleParserParser$ConditionLeftContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitCommonFunction(RuleParserParser$CommonFunctionContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitExprCondition(RuleParserParser$ExprConditionContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitExpressionBody(RuleParserParser$ExpressionBodyContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitPercent(RuleParserParser$PercentContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitLeftParen(RuleParserParser$LeftParenContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitRightParen(RuleParserParser$RightParenContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitColon(RuleParserParser$ColonContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitJoin(RuleParserParser$JoinContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitRight(RuleParserParser$RightContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitOther(RuleParserParser$OtherContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitActions(RuleParserParser$ActionsContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitAction(RuleParserParser$ActionContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitAssignAction(RuleParserParser$AssignActionContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitOutAction(RuleParserParser$OutActionContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitMethodInvoke(RuleParserParser$MethodInvokeContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitFunctionInvoke(RuleParserParser$FunctionInvokeContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitActionParameters(RuleParserParser$ActionParametersContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitBeanMethod(RuleParserParser$BeanMethodContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitComplexValue(RuleParserParser$ComplexValueContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitParameter(RuleParserParser$ParameterContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitParameterName(RuleParserParser$ParameterNameContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitConstant(RuleParserParser$ConstantContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitVariable(RuleParserParser$VariableContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitNamedVariable(RuleParserParser$NamedVariableContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitProperty(RuleParserParser$PropertyContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitVariableCategory(RuleParserParser$VariableCategoryContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitNamedVariableCategory(RuleParserParser$NamedVariableCategoryContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitConstantCategory(RuleParserParser$ConstantCategoryContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitValue(RuleParserParser$ValueContext var1) {
      return (T)this.visitChildren(var1);
   }

   @Override
   public T visitOp(RuleParserParser$OpContext var1) {
      return (T)this.visitChildren(var1);
   }
}
