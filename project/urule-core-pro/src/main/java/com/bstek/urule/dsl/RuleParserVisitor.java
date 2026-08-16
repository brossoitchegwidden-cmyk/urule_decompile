package com.bstek.urule.dsl;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

public interface RuleParserVisitor<T> extends ParseTreeVisitor<T> {
   T visitRuleSet(RuleParserParser$RuleSetContext var1);

   T visitRuleSetHeader(RuleParserParser$RuleSetHeaderContext var1);

   T visitRuleSetBody(RuleParserParser$RuleSetBodyContext var1);

   T visitRules(RuleParserParser$RulesContext var1);

   T visitFunctionImport(RuleParserParser$FunctionImportContext var1);

   T visitPackageDef(RuleParserParser$PackageDefContext var1);

   T visitResource(RuleParserParser$ResourceContext var1);

   T visitImportParameterLibrary(RuleParserParser$ImportParameterLibraryContext var1);

   T visitImportVariableLibrary(RuleParserParser$ImportVariableLibraryContext var1);

   T visitImportConstantLibrary(RuleParserParser$ImportConstantLibraryContext var1);

   T visitImportActionLibrary(RuleParserParser$ImportActionLibraryContext var1);

   T visitFunctionDef(RuleParserParser$FunctionDefContext var1);

   T visitFunctionParameters(RuleParserParser$FunctionParametersContext var1);

   T visitFunctionParameter(RuleParserParser$FunctionParameterContext var1);

   T visitRuleDef(RuleParserParser$RuleDefContext var1);

   T visitLoopRuleDef(RuleParserParser$LoopRuleDefContext var1);

   T visitLoopRuleUnit(RuleParserParser$LoopRuleUnitContext var1);

   T visitLoopTarget(RuleParserParser$LoopTargetContext var1);

   T visitLoopStart(RuleParserParser$LoopStartContext var1);

   T visitLoopEnd(RuleParserParser$LoopEndContext var1);

   T visitAttribute(RuleParserParser$AttributeContext var1);

   T visitLoopAttribute(RuleParserParser$LoopAttributeContext var1);

   T visitSalienceAttribute(RuleParserParser$SalienceAttributeContext var1);

   T visitEffectiveDateAttribute(RuleParserParser$EffectiveDateAttributeContext var1);

   T visitExpiresDateAttribute(RuleParserParser$ExpiresDateAttributeContext var1);

   T visitEnabledAttribute(RuleParserParser$EnabledAttributeContext var1);

   T visitDebugAttribute(RuleParserParser$DebugAttributeContext var1);

   T visitActivationGroupAttribute(RuleParserParser$ActivationGroupAttributeContext var1);

   T visitAgendaGroupAttribute(RuleParserParser$AgendaGroupAttributeContext var1);

   T visitAutoFocusAttribute(RuleParserParser$AutoFocusAttributeContext var1);

   T visitRuleflowGroupAttribute(RuleParserParser$RuleflowGroupAttributeContext var1);

   T visitLeft(RuleParserParser$LeftContext var1);

   T visitParenConditions(RuleParserParser$ParenConditionsContext var1);

   T visitMultiConditions(RuleParserParser$MultiConditionsContext var1);

   T visitSingleCondition(RuleParserParser$SingleConditionContext var1);

   T visitSingleNamedConditionSet(RuleParserParser$SingleNamedConditionSetContext var1);

   T visitNamedConditionSet(RuleParserParser$NamedConditionSetContext var1);

   T visitParenNamedConditions(RuleParserParser$ParenNamedConditionsContext var1);

   T visitMultiNamedConditions(RuleParserParser$MultiNamedConditionsContext var1);

   T visitSingleNamedConditions(RuleParserParser$SingleNamedConditionsContext var1);

   T visitSingleCellCondition(RuleParserParser$SingleCellConditionContext var1);

   T visitMultiCellConditions(RuleParserParser$MultiCellConditionsContext var1);

   T visitParenCellConditions(RuleParserParser$ParenCellConditionsContext var1);

   T visitRefName(RuleParserParser$RefNameContext var1);

   T visitRefObject(RuleParserParser$RefObjectContext var1);

   T visitNullValue(RuleParserParser$NullValueContext var1);

   T visitConditionLeft(RuleParserParser$ConditionLeftContext var1);

   T visitCommonFunction(RuleParserParser$CommonFunctionContext var1);

   T visitExprCondition(RuleParserParser$ExprConditionContext var1);

   T visitExpressionBody(RuleParserParser$ExpressionBodyContext var1);

   T visitPercent(RuleParserParser$PercentContext var1);

   T visitLeftParen(RuleParserParser$LeftParenContext var1);

   T visitRightParen(RuleParserParser$RightParenContext var1);

   T visitColon(RuleParserParser$ColonContext var1);

   T visitJoin(RuleParserParser$JoinContext var1);

   T visitRight(RuleParserParser$RightContext var1);

   T visitOther(RuleParserParser$OtherContext var1);

   T visitActions(RuleParserParser$ActionsContext var1);

   T visitAction(RuleParserParser$ActionContext var1);

   T visitAssignAction(RuleParserParser$AssignActionContext var1);

   T visitOutAction(RuleParserParser$OutActionContext var1);

   T visitMethodInvoke(RuleParserParser$MethodInvokeContext var1);

   T visitFunctionInvoke(RuleParserParser$FunctionInvokeContext var1);

   T visitActionParameters(RuleParserParser$ActionParametersContext var1);

   T visitBeanMethod(RuleParserParser$BeanMethodContext var1);

   T visitComplexValue(RuleParserParser$ComplexValueContext var1);

   T visitParameter(RuleParserParser$ParameterContext var1);

   T visitParameterName(RuleParserParser$ParameterNameContext var1);

   T visitConstant(RuleParserParser$ConstantContext var1);

   T visitVariable(RuleParserParser$VariableContext var1);

   T visitNamedVariable(RuleParserParser$NamedVariableContext var1);

   T visitProperty(RuleParserParser$PropertyContext var1);

   T visitVariableCategory(RuleParserParser$VariableCategoryContext var1);

   T visitNamedVariableCategory(RuleParserParser$NamedVariableCategoryContext var1);

   T visitConstantCategory(RuleParserParser$ConstantCategoryContext var1);

   T visitValue(RuleParserParser$ValueContext var1);

   T visitOp(RuleParserParser$OpContext var1);
}
