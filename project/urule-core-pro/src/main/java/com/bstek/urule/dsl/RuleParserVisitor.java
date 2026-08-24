package com.bstek.urule.dsl;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**This interface defines a complete generic visitor for a parse tree produced by RuleParserParser.*/
public interface RuleParserVisitor<T> extends ParseTreeVisitor<T> {
   /**Visit a parse tree produced by RuleParserParser.ruleSet().*/
   T visitRuleSet(RuleParserParser$RuleSetContext ctx);

   /**Visit a parse tree produced by RuleParserParser.ruleSetHeader().*/
   T visitRuleSetHeader(RuleParserParser$RuleSetHeaderContext ctx);

   /**Visit a parse tree produced by RuleParserParser.ruleSetBody().*/
   T visitRuleSetBody(RuleParserParser$RuleSetBodyContext ctx);

   /**Visit a parse tree produced by RuleParserParser.rules().*/
   T visitRules(RuleParserParser$RulesContext ctx);

   /**Visit a parse tree produced by RuleParserParser.functionImport().*/
   T visitFunctionImport(RuleParserParser$FunctionImportContext ctx);

   /**Visit a parse tree produced by RuleParserParser.packageDef().*/
   T visitPackageDef(RuleParserParser$PackageDefContext ctx);

   /**Visit a parse tree produced by RuleParserParser.resource().*/
   T visitResource(RuleParserParser$ResourceContext ctx);

   /**Visit a parse tree produced by RuleParserParser.importParameterLibrary().*/
   T visitImportParameterLibrary(RuleParserParser$ImportParameterLibraryContext ctx);

   /**Visit a parse tree produced by RuleParserParser.importVariableLibrary().*/
   T visitImportVariableLibrary(RuleParserParser$ImportVariableLibraryContext ctx);

   /**Visit a parse tree produced by RuleParserParser.importConstantLibrary().*/
   T visitImportConstantLibrary(RuleParserParser$ImportConstantLibraryContext ctx);

   /**Visit a parse tree produced by RuleParserParser.importActionLibrary().*/
   T visitImportActionLibrary(RuleParserParser$ImportActionLibraryContext ctx);

   /**Visit a parse tree produced by RuleParserParser.functionDef().*/
   T visitFunctionDef(RuleParserParser$FunctionDefContext ctx);

   /**Visit a parse tree produced by RuleParserParser.functionParameters().*/
   T visitFunctionParameters(RuleParserParser$FunctionParametersContext ctx);

   /**Visit a parse tree produced by RuleParserParser.functionParameter().*/
   T visitFunctionParameter(RuleParserParser$FunctionParameterContext ctx);

   /**Visit a parse tree produced by RuleParserParser.ruleDef().*/
   T visitRuleDef(RuleParserParser$RuleDefContext ctx);

   /**Visit a parse tree produced by RuleParserParser.loopRuleDef().*/
   T visitLoopRuleDef(RuleParserParser$LoopRuleDefContext ctx);

   /**Visit a parse tree produced by RuleParserParser.loopRuleUnit().*/
   T visitLoopRuleUnit(RuleParserParser$LoopRuleUnitContext ctx);

   /**Visit a parse tree produced by RuleParserParser.loopTarget().*/
   T visitLoopTarget(RuleParserParser$LoopTargetContext ctx);

   /**Visit a parse tree produced by RuleParserParser.loopStart().*/
   T visitLoopStart(RuleParserParser$LoopStartContext ctx);

   /**Visit a parse tree produced by RuleParserParser.loopEnd().*/
   T visitLoopEnd(RuleParserParser$LoopEndContext ctx);

   /**Visit a parse tree produced by RuleParserParser.attribute().*/
   T visitAttribute(RuleParserParser$AttributeContext ctx);

   /**Visit a parse tree produced by RuleParserParser.loopAttribute().*/
   T visitLoopAttribute(RuleParserParser$LoopAttributeContext ctx);

   /**Visit a parse tree produced by RuleParserParser.salienceAttribute().*/
   T visitSalienceAttribute(RuleParserParser$SalienceAttributeContext ctx);

   /**Visit a parse tree produced by RuleParserParser.effectiveDateAttribute().*/
   T visitEffectiveDateAttribute(RuleParserParser$EffectiveDateAttributeContext ctx);

   /**Visit a parse tree produced by RuleParserParser.expiresDateAttribute().*/
   T visitExpiresDateAttribute(RuleParserParser$ExpiresDateAttributeContext ctx);

   /**Visit a parse tree produced by RuleParserParser.enabledAttribute().*/
   T visitEnabledAttribute(RuleParserParser$EnabledAttributeContext ctx);

   /**Visit a parse tree produced by RuleParserParser.debugAttribute().*/
   T visitDebugAttribute(RuleParserParser$DebugAttributeContext ctx);

   /**Visit a parse tree produced by RuleParserParser.activationGroupAttribute().*/
   T visitActivationGroupAttribute(RuleParserParser$ActivationGroupAttributeContext ctx);

   /**Visit a parse tree produced by RuleParserParser.agendaGroupAttribute().*/
   T visitAgendaGroupAttribute(RuleParserParser$AgendaGroupAttributeContext ctx);

   /**Visit a parse tree produced by RuleParserParser.autoFocusAttribute().*/
   T visitAutoFocusAttribute(RuleParserParser$AutoFocusAttributeContext ctx);

   /**Visit a parse tree produced by RuleParserParser.ruleflowGroupAttribute().*/
   T visitRuleflowGroupAttribute(RuleParserParser$RuleflowGroupAttributeContext ctx);

   /**Visit a parse tree produced by RuleParserParser.left().*/
   T visitLeft(RuleParserParser$LeftContext ctx);

   /**Visit a parse tree produced by the parenConditions labeled alternative in RuleParserParser.condition().*/
   T visitParenConditions(RuleParserParser$ParenConditionsContext ctx);

   /**Visit a parse tree produced by the multiConditions labeled alternative in RuleParserParser.condition().*/
   T visitMultiConditions(RuleParserParser$MultiConditionsContext ctx);

   /**Visit a parse tree produced by the singleCondition labeled alternative in RuleParserParser.condition().*/
   T visitSingleCondition(RuleParserParser$SingleConditionContext ctx);

   /**Visit a parse tree produced by the singleNamedConditionSet labeled alternative in RuleParserParser.condition().*/
   T visitSingleNamedConditionSet(RuleParserParser$SingleNamedConditionSetContext ctx);

   /**Visit a parse tree produced by RuleParserParser.namedConditionSet().*/
   T visitNamedConditionSet(RuleParserParser$NamedConditionSetContext ctx);

   /**Visit a parse tree produced by the parenNamedConditions labeled alternative in RuleParserParser.namedCondition().*/
   T visitParenNamedConditions(RuleParserParser$ParenNamedConditionsContext ctx);

   /**Visit a parse tree produced by the multiNamedConditions labeled alternative in RuleParserParser.namedCondition().*/
   T visitMultiNamedConditions(RuleParserParser$MultiNamedConditionsContext ctx);

   /**Visit a parse tree produced by the singleNamedConditions labeled alternative in RuleParserParser.namedCondition().*/
   T visitSingleNamedConditions(RuleParserParser$SingleNamedConditionsContext ctx);

   /**Visit a parse tree produced by the singleCellCondition labeled alternative in RuleParserParser.decisionTableCellCondition().*/
   T visitSingleCellCondition(RuleParserParser$SingleCellConditionContext ctx);

   /**Visit a parse tree produced by the multiCellConditions labeled alternative in RuleParserParser.decisionTableCellCondition().*/
   T visitMultiCellConditions(RuleParserParser$MultiCellConditionsContext ctx);

   /**Visit a parse tree produced by the parenCellConditions labeled alternative in RuleParserParser.decisionTableCellCondition().*/
   T visitParenCellConditions(RuleParserParser$ParenCellConditionsContext ctx);

   /**Visit a parse tree produced by RuleParserParser.refName().*/
   T visitRefName(RuleParserParser$RefNameContext ctx);

   /**Visit a parse tree produced by RuleParserParser.refObject().*/
   T visitRefObject(RuleParserParser$RefObjectContext ctx);

   /**Visit a parse tree produced by RuleParserParser.nullValue().*/
   T visitNullValue(RuleParserParser$NullValueContext ctx);

   /**Visit a parse tree produced by RuleParserParser.conditionLeft().*/
   T visitConditionLeft(RuleParserParser$ConditionLeftContext ctx);

   /**Visit a parse tree produced by RuleParserParser.commonFunction().*/
   T visitCommonFunction(RuleParserParser$CommonFunctionContext ctx);

   /**Visit a parse tree produced by RuleParserParser.exprCondition().*/
   T visitExprCondition(RuleParserParser$ExprConditionContext ctx);

   /**Visit a parse tree produced by RuleParserParser.expressionBody().*/
   T visitExpressionBody(RuleParserParser$ExpressionBodyContext ctx);

   /**Visit a parse tree produced by RuleParserParser.percent().*/
   T visitPercent(RuleParserParser$PercentContext ctx);

   /**Visit a parse tree produced by RuleParserParser.leftParen().*/
   T visitLeftParen(RuleParserParser$LeftParenContext ctx);

   /**Visit a parse tree produced by RuleParserParser.rightParen().*/
   T visitRightParen(RuleParserParser$RightParenContext ctx);

   /**Visit a parse tree produced by RuleParserParser.colon().*/
   T visitColon(RuleParserParser$ColonContext ctx);

   /**Visit a parse tree produced by RuleParserParser.join().*/
   T visitJoin(RuleParserParser$JoinContext ctx);

   /**Visit a parse tree produced by RuleParserParser.right().*/
   T visitRight(RuleParserParser$RightContext ctx);

   /**Visit a parse tree produced by RuleParserParser.other().*/
   T visitOther(RuleParserParser$OtherContext ctx);

   /**Visit a parse tree produced by RuleParserParser.actions().*/
   T visitActions(RuleParserParser$ActionsContext ctx);

   /**Visit a parse tree produced by RuleParserParser.action().*/
   T visitAction(RuleParserParser$ActionContext ctx);

   /**Visit a parse tree produced by RuleParserParser.assignAction().*/
   T visitAssignAction(RuleParserParser$AssignActionContext ctx);

   /**Visit a parse tree produced by RuleParserParser.outAction().*/
   T visitOutAction(RuleParserParser$OutActionContext ctx);

   /**Visit a parse tree produced by RuleParserParser.methodInvoke().*/
   T visitMethodInvoke(RuleParserParser$MethodInvokeContext ctx);

   /**Visit a parse tree produced by RuleParserParser.functionInvoke().*/
   T visitFunctionInvoke(RuleParserParser$FunctionInvokeContext ctx);

   /**Visit a parse tree produced by RuleParserParser.actionParameters().*/
   T visitActionParameters(RuleParserParser$ActionParametersContext ctx);

   /**Visit a parse tree produced by RuleParserParser.beanMethod().*/
   T visitBeanMethod(RuleParserParser$BeanMethodContext ctx);

   /**Visit a parse tree produced by RuleParserParser.complexValue().*/
   T visitComplexValue(RuleParserParser$ComplexValueContext ctx);

   /**Visit a parse tree produced by RuleParserParser.parameter().*/
   T visitParameter(RuleParserParser$ParameterContext ctx);

   /**Visit a parse tree produced by RuleParserParser.parameterName().*/
   T visitParameterName(RuleParserParser$ParameterNameContext ctx);

   /**Visit a parse tree produced by RuleParserParser.constant().*/
   T visitConstant(RuleParserParser$ConstantContext ctx);

   /**Visit a parse tree produced by RuleParserParser.variable().*/
   T visitVariable(RuleParserParser$VariableContext ctx);

   /**Visit a parse tree produced by RuleParserParser.namedVariable().*/
   T visitNamedVariable(RuleParserParser$NamedVariableContext ctx);

   /**Visit a parse tree produced by RuleParserParser.property().*/
   T visitProperty(RuleParserParser$PropertyContext ctx);

   /**Visit a parse tree produced by RuleParserParser.variableCategory().*/
   T visitVariableCategory(RuleParserParser$VariableCategoryContext ctx);

   /**Visit a parse tree produced by RuleParserParser.namedVariableCategory().*/
   T visitNamedVariableCategory(RuleParserParser$NamedVariableCategoryContext ctx);

   /**Visit a parse tree produced by RuleParserParser.constantCategory().*/
   T visitConstantCategory(RuleParserParser$ConstantCategoryContext ctx);

   /**Visit a parse tree produced by RuleParserParser.value().*/
   T visitValue(RuleParserParser$ValueContext ctx);

   /**Visit a parse tree produced by RuleParserParser.op().*/
   T visitOp(RuleParserParser$OpContext ctx);
}
