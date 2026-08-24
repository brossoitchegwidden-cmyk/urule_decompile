package com.bstek.urule.dsl;

import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

/**This class provides an empty implementation of RuleParserVisitor, which can be extended to create a visitor which only needs to handle a subset of the available methods.*/
public class RuleParserBaseVisitor<T> extends AbstractParseTreeVisitor<T> implements RuleParserVisitor<T> {
   /**Visit a parse tree produced by RuleParserParser.ruleSet(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitRuleSet(RuleParserParser$RuleSetContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.ruleSetHeader(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitRuleSetHeader(RuleParserParser$RuleSetHeaderContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.ruleSetBody(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitRuleSetBody(RuleParserParser$RuleSetBodyContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.rules(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitRules(RuleParserParser$RulesContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.functionImport(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitFunctionImport(RuleParserParser$FunctionImportContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.packageDef(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitPackageDef(RuleParserParser$PackageDefContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.resource(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitResource(RuleParserParser$ResourceContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.importParameterLibrary(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitImportParameterLibrary(RuleParserParser$ImportParameterLibraryContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.importVariableLibrary(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitImportVariableLibrary(RuleParserParser$ImportVariableLibraryContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.importConstantLibrary(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitImportConstantLibrary(RuleParserParser$ImportConstantLibraryContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.importActionLibrary(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitImportActionLibrary(RuleParserParser$ImportActionLibraryContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.functionDef(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitFunctionDef(RuleParserParser$FunctionDefContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.functionParameters(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitFunctionParameters(RuleParserParser$FunctionParametersContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.functionParameter(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitFunctionParameter(RuleParserParser$FunctionParameterContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.ruleDef(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitRuleDef(RuleParserParser$RuleDefContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.loopRuleDef(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitLoopRuleDef(RuleParserParser$LoopRuleDefContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.loopRuleUnit(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitLoopRuleUnit(RuleParserParser$LoopRuleUnitContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.loopTarget(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitLoopTarget(RuleParserParser$LoopTargetContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.loopStart(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitLoopStart(RuleParserParser$LoopStartContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.loopEnd(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitLoopEnd(RuleParserParser$LoopEndContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.attribute(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitAttribute(RuleParserParser$AttributeContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.loopAttribute(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitLoopAttribute(RuleParserParser$LoopAttributeContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.salienceAttribute(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitSalienceAttribute(RuleParserParser$SalienceAttributeContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.effectiveDateAttribute(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitEffectiveDateAttribute(RuleParserParser$EffectiveDateAttributeContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.expiresDateAttribute(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitExpiresDateAttribute(RuleParserParser$ExpiresDateAttributeContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.enabledAttribute(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitEnabledAttribute(RuleParserParser$EnabledAttributeContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.debugAttribute(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitDebugAttribute(RuleParserParser$DebugAttributeContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.activationGroupAttribute(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitActivationGroupAttribute(RuleParserParser$ActivationGroupAttributeContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.agendaGroupAttribute(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitAgendaGroupAttribute(RuleParserParser$AgendaGroupAttributeContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.autoFocusAttribute(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitAutoFocusAttribute(RuleParserParser$AutoFocusAttributeContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.ruleflowGroupAttribute(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitRuleflowGroupAttribute(RuleParserParser$RuleflowGroupAttributeContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.left(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitLeft(RuleParserParser$LeftContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by the parenConditions labeled alternative in RuleParserParser.condition(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitParenConditions(RuleParserParser$ParenConditionsContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by the multiConditions labeled alternative in RuleParserParser.condition(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitMultiConditions(RuleParserParser$MultiConditionsContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by the singleCondition labeled alternative in RuleParserParser.condition(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitSingleCondition(RuleParserParser$SingleConditionContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by the singleNamedConditionSet labeled alternative in RuleParserParser.condition(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitSingleNamedConditionSet(RuleParserParser$SingleNamedConditionSetContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.namedConditionSet(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitNamedConditionSet(RuleParserParser$NamedConditionSetContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by the parenNamedConditions labeled alternative in RuleParserParser.namedCondition(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitParenNamedConditions(RuleParserParser$ParenNamedConditionsContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by the multiNamedConditions labeled alternative in RuleParserParser.namedCondition(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitMultiNamedConditions(RuleParserParser$MultiNamedConditionsContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by the singleNamedConditions labeled alternative in RuleParserParser.namedCondition(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitSingleNamedConditions(RuleParserParser$SingleNamedConditionsContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by the singleCellCondition labeled alternative in RuleParserParser.decisionTableCellCondition(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitSingleCellCondition(RuleParserParser$SingleCellConditionContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by the multiCellConditions labeled alternative in RuleParserParser.decisionTableCellCondition(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitMultiCellConditions(RuleParserParser$MultiCellConditionsContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by the parenCellConditions labeled alternative in RuleParserParser.decisionTableCellCondition(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitParenCellConditions(RuleParserParser$ParenCellConditionsContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.refName(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitRefName(RuleParserParser$RefNameContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.refObject(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitRefObject(RuleParserParser$RefObjectContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.nullValue(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitNullValue(RuleParserParser$NullValueContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.conditionLeft(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitConditionLeft(RuleParserParser$ConditionLeftContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.commonFunction(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitCommonFunction(RuleParserParser$CommonFunctionContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.exprCondition(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitExprCondition(RuleParserParser$ExprConditionContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.expressionBody(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitExpressionBody(RuleParserParser$ExpressionBodyContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.percent(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitPercent(RuleParserParser$PercentContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.leftParen(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitLeftParen(RuleParserParser$LeftParenContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.rightParen(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitRightParen(RuleParserParser$RightParenContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.colon(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitColon(RuleParserParser$ColonContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.join(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitJoin(RuleParserParser$JoinContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.right(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitRight(RuleParserParser$RightContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.other(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitOther(RuleParserParser$OtherContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.actions(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitActions(RuleParserParser$ActionsContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.action(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitAction(RuleParserParser$ActionContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.assignAction(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitAssignAction(RuleParserParser$AssignActionContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.outAction(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitOutAction(RuleParserParser$OutActionContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.methodInvoke(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitMethodInvoke(RuleParserParser$MethodInvokeContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.functionInvoke(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitFunctionInvoke(RuleParserParser$FunctionInvokeContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.actionParameters(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitActionParameters(RuleParserParser$ActionParametersContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.beanMethod(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitBeanMethod(RuleParserParser$BeanMethodContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.complexValue(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitComplexValue(RuleParserParser$ComplexValueContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.parameter(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitParameter(RuleParserParser$ParameterContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.parameterName(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitParameterName(RuleParserParser$ParameterNameContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.constant(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitConstant(RuleParserParser$ConstantContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.variable(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitVariable(RuleParserParser$VariableContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.namedVariable(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitNamedVariable(RuleParserParser$NamedVariableContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.property(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitProperty(RuleParserParser$PropertyContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.variableCategory(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitVariableCategory(RuleParserParser$VariableCategoryContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.namedVariableCategory(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitNamedVariableCategory(RuleParserParser$NamedVariableCategoryContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.constantCategory(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitConstantCategory(RuleParserParser$ConstantCategoryContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.value(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitValue(RuleParserParser$ValueContext ctx) {
      return (T)this.visitChildren(ctx);
   }

   /**Visit a parse tree produced by RuleParserParser.op(). The default implementation returns the result of calling AbstractParseTreeVisitor.visitChildren(org.antlr.v4.runtime.tree.RuleNode) on ctx.*/
   @Override
   public T visitOp(RuleParserParser$OpContext ctx) {
      return (T)this.visitChildren(ctx);
   }
}
