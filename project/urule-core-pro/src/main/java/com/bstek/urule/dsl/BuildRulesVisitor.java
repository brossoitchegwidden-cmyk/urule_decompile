package com.bstek.urule.dsl;

import com.bstek.urule.Configure;
import com.bstek.urule.action.Action;
import com.bstek.urule.dsl.builder.BuildUtils;
import com.bstek.urule.dsl.builder.ContextBuilder;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.rule.Library;
import com.bstek.urule.model.rule.Other;
import com.bstek.urule.model.rule.Rhs;
import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.model.rule.RuleSet;
import com.bstek.urule.model.rule.lhs.And;
import com.bstek.urule.model.rule.lhs.Criteria;
import com.bstek.urule.model.rule.lhs.Criterion;
import com.bstek.urule.model.rule.lhs.Junction;
import com.bstek.urule.model.rule.lhs.Lhs;
import com.bstek.urule.model.rule.lhs.Or;
import com.bstek.urule.model.rule.loop.LoopEnd;
import com.bstek.urule.model.rule.loop.LoopRule;
import com.bstek.urule.model.rule.loop.LoopRuleUnit;
import com.bstek.urule.model.rule.loop.LoopStart;
import com.bstek.urule.model.rule.loop.LoopTarget;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.ParseTree;

public class BuildRulesVisitor extends RuleParserBaseVisitor<Object> {
   private Map<ParseTree, Junction> junctionsByParseTree = new HashMap<>();
   private Collection<ContextBuilder> builders;
   private CommonTokenStream tokenStream;

   public BuildRulesVisitor(Collection<ContextBuilder> builders, CommonTokenStream tokenStream) {
      this.builders = builders;
      this.tokenStream = tokenStream;
   }

   public RuleSet buildRuleSet(RuleParserParser$RuleSetContext ctx, String path) {
      RuleSet ruleSet = this.visitRuleSet(ctx);

      for (Rule rule : ruleSet.getRules()) {
         rule.setFile(path);
      }

      return ruleSet;
   }
   public RuleSet visitRuleSet(RuleParserParser$RuleSetContext ctx) {
      RuleSet ruleSet = new RuleSet();
      RuleParserParser$RuleSetHeaderContext ruleParserParser$RuleSetHeaderContext = ctx.ruleSetHeader();
      List items = ruleParserParser$RuleSetHeaderContext.resource();
      if (items != null) {
         for (RuleParserParser$ResourceContext ruleParserParser$ResourceContext : (Iterable<RuleParserParser$ResourceContext>)(Iterable<?>)(items)) {
            ruleSet.addLibrary(this.visitResource(ruleParserParser$ResourceContext));
         }
      }

      StringBuffer stringBuffer = null;
      List items2 = ruleParserParser$RuleSetHeaderContext.functionImport();
      if (items2 != null) {
         stringBuffer = new StringBuffer();

         for (RuleParserParser$FunctionImportContext ruleParserParser$FunctionImportContext : (Iterable<RuleParserParser$FunctionImportContext>)(Iterable<?>)(items2)) {
            stringBuffer.append("import ");
            stringBuffer.append(ruleParserParser$FunctionImportContext.packageDef().getText());
            stringBuffer.append(";");
         }
      }

      RuleParserParser$RuleSetBodyContext ruleParserParser$RuleSetBodyContext = ctx.ruleSetBody();
      List items3 = ruleParserParser$RuleSetBodyContext.rules();
      if (items3 != null) {
         ArrayList items4 = new ArrayList();
         ruleSet.setRules(items4);

         for (RuleParserParser$RulesContext ruleParserParser$RulesContext : (Iterable<RuleParserParser$RulesContext>)(Iterable<?>)(items3)) {
            RuleParserParser$RuleDefContext ruleParserParser$RuleDefContext = ruleParserParser$RulesContext.ruleDef();
            if (ruleParserParser$RuleDefContext != null) {
               Rule rule = this.visitRuleDef(ruleParserParser$RuleDefContext);
               items4.add(rule);
            }

            RuleParserParser$LoopRuleDefContext ruleParserParser$LoopRuleDefContext = ruleParserParser$RulesContext.loopRuleDef();
            if (ruleParserParser$LoopRuleDefContext != null) {
               LoopRule loopRule = this.visitLoopRuleDef(ruleParserParser$LoopRuleDefContext);
               items4.add(loopRule);
            }
         }
      }

      return ruleSet;
   }

   private String buildFunctionBody(RuleParserParser$ExpressionBodyContext ruleParserParser$ExpressionBodyContext) {
      StringBuffer stringBuffer = new StringBuffer();

      for (ParseTree parseTree : ruleParserParser$ExpressionBodyContext.children) {
         Interval sourceInterval = parseTree.getSourceInterval();
         int number = sourceInterval.a;
         List hiddenTokensToLeft = this.tokenStream.getHiddenTokensToLeft(number);
         if (hiddenTokensToLeft != null) {
            Token token = (Token)hiddenTokensToLeft.get(0);
            String text = token.getText();
            stringBuffer.append(text);
         }

         stringBuffer.append(parseTree.getText());
         List hiddenTokensToRight = this.tokenStream.getHiddenTokensToRight(number);
         if (hiddenTokensToRight != null) {
            Token token2 = (Token)hiddenTokensToRight.get(0);
            String text2 = token2.getText();
            stringBuffer.append(text2);
         }
      }

      return stringBuffer.toString();
   }
   public Library visitResource(RuleParserParser$ResourceContext ctx) {
      return (Library)this.doBuilder(ctx);
   }
   public LoopRule visitLoopRuleDef(RuleParserParser$LoopRuleDefContext ctx) {
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Configure.getDateFormat());
      LoopRule loopRule = new LoopRule();
      String text = ctx.STRING().getText();
      text = text.substring(1, text.length() - 1);
      loopRule.setName(text);
      RuleParserParser$LoopTargetContext ruleParserParser$LoopTargetContext = ctx.loopTarget();
      RuleParserParser$ComplexValueContext ruleParserParser$ComplexValueContext = ruleParserParser$LoopTargetContext.complexValue();
      LoopTarget loopTarget = new LoopTarget();
      loopTarget.setValue(BuildUtils.buildValue(ruleParserParser$ComplexValueContext));
      loopRule.setLoopTarget(loopTarget);
      RuleParserParser$LoopStartContext ruleParserParser$LoopStartContext = ctx.loopStart();
      if (ruleParserParser$LoopStartContext != null) {
         List items = ruleParserParser$LoopStartContext.action();
         if (items != null) {
            LoopStart loopStart = new LoopStart();
            loopStart.setActions(this.buildActions(items));
            loopRule.setLoopStart(loopStart);
         }
      }

      RuleParserParser$LoopEndContext ruleParserParser$LoopEndContext = ctx.loopEnd();
      if (ruleParserParser$LoopEndContext != null) {
         List items2 = ruleParserParser$LoopEndContext.action();
         if (items2 != null) {
            LoopEnd loopEnd = new LoopEnd();
            loopEnd.setActions(this.buildActions(items2));
            loopRule.setLoopEnd(loopEnd);
         }
      }

      List items3 = ctx.attribute();
      if (items3 != null) {
         for (RuleParserParser$AttributeContext ruleParserParser$AttributeContext : (Iterable<RuleParserParser$AttributeContext>)(Iterable<?>)(items3)) {
            if (ruleParserParser$AttributeContext.salienceAttribute() != null) {
               loopRule.setSalience(Integer.valueOf(ruleParserParser$AttributeContext.salienceAttribute().NUMBER().getText()));
            } else if (ruleParserParser$AttributeContext.loopAttribute() != null) {
               loopRule.setLoop(Boolean.valueOf(ruleParserParser$AttributeContext.loopAttribute().Boolean().getText()));
            } else if (ruleParserParser$AttributeContext.effectiveDateAttribute() != null) {
               try {
                  String substring = ruleParserParser$AttributeContext.effectiveDateAttribute().STRING().getText();
                  substring = substring.substring(1, substring.length() - 1);
                  loopRule.setEffectiveDate(simpleDateFormat.parse(substring));
               } catch (ParseException parseException) {
                  throw new RuleException(parseException);
               }
            } else if (ruleParserParser$AttributeContext.expiresDateAttribute() != null) {
               try {
                  String substring2 = ruleParserParser$AttributeContext.expiresDateAttribute().STRING().getText();
                  substring2 = substring2.substring(1, substring2.length() - 1);
                  loopRule.setExpiresDate(simpleDateFormat.parse(substring2));
               } catch (ParseException parseException2) {
                  throw new RuleException(parseException2);
               }
            } else if (ruleParserParser$AttributeContext.enabledAttribute() != null) {
               loopRule.setEnabled(Boolean.valueOf(ruleParserParser$AttributeContext.enabledAttribute().Boolean().getText()));
            } else if (ruleParserParser$AttributeContext.debugAttribute() != null) {
               loopRule.setDebug(Boolean.valueOf(ruleParserParser$AttributeContext.debugAttribute().Boolean().getText()));
            } else if (ruleParserParser$AttributeContext.activationGroupAttribute() != null) {
               String substring3 = ruleParserParser$AttributeContext.activationGroupAttribute().STRING().getText();
               substring3 = substring3.substring(1, substring3.length() - 1);
               loopRule.setMutexGroup(substring3);
            } else if (ruleParserParser$AttributeContext.agendaGroupAttribute() != null) {
               String substring4 = ruleParserParser$AttributeContext.agendaGroupAttribute().STRING().getText();
               substring4 = substring4.substring(1, substring4.length() - 1);
               loopRule.setPendedGroup(substring4);
            } else if (ruleParserParser$AttributeContext.autoFocusAttribute() != null) {
               loopRule.setAutoFocus(Boolean.valueOf(ruleParserParser$AttributeContext.autoFocusAttribute().Boolean().getText()));
            }
         }
      }

      ArrayList items4 = new ArrayList();
      loopRule.setUnits(items4);

      for (RuleParserParser$LoopRuleUnitContext ruleParserParser$LoopRuleUnitContext : ctx.loopRuleUnit()) {
         LoopRuleUnit loopRuleUnit = new LoopRuleUnit();
         items4.add(loopRuleUnit);
         if (ruleParserParser$LoopRuleUnitContext.STRING() != null) {
            String substring5 = ruleParserParser$LoopRuleUnitContext.STRING().getText();
            if (substring5.length() > 2) {
               substring5 = substring5.substring(1, text.length() - 1);
               loopRuleUnit.setName(substring5);
            } else {
               loopRuleUnit.setName("");
            }
         }

         RuleParserParser$LeftContext ruleParserParser$LeftContext = ruleParserParser$LoopRuleUnitContext.left();
         ParseTree child = ruleParserParser$LeftContext.getChild(1);
         Lhs lhs = new Lhs();
         loopRuleUnit.setLhs(lhs);
         Criterion criterion = this.buildCriterion(child);
         lhs.setCriterion(criterion);
         Rhs rhs = new Rhs();
         rhs.setActions(this.visitRight(ruleParserParser$LoopRuleUnitContext.right()));
         loopRuleUnit.setRhs(rhs);
         Other other = new Other();
         other.setActions(this.visitOther(ruleParserParser$LoopRuleUnitContext.other()));
         loopRuleUnit.setOther(other);
      }

      return loopRule;
   }
   public Rule visitRuleDef(RuleParserParser$RuleDefContext ctx) {
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Configure.getDateFormat());
      Rule rule = new Rule();
      String text = ctx.STRING().getText();
      text = text.substring(1, text.length() - 1);
      rule.setName(text);
      List items = ctx.attribute();
      if (items != null) {
         for (RuleParserParser$AttributeContext ruleParserParser$AttributeContext : (Iterable<RuleParserParser$AttributeContext>)(Iterable<?>)(items)) {
            if (ruleParserParser$AttributeContext.salienceAttribute() != null) {
               rule.setSalience(Integer.valueOf(ruleParserParser$AttributeContext.salienceAttribute().NUMBER().getText()));
            } else if (ruleParserParser$AttributeContext.loopAttribute() != null) {
               rule.setLoop(Boolean.valueOf(ruleParserParser$AttributeContext.loopAttribute().Boolean().getText()));
            } else if (ruleParserParser$AttributeContext.effectiveDateAttribute() != null) {
               try {
                  String substring = ruleParserParser$AttributeContext.effectiveDateAttribute().STRING().getText();
                  substring = substring.substring(1, substring.length() - 1);
                  rule.setEffectiveDate(simpleDateFormat.parse(substring));
               } catch (ParseException parseException) {
                  throw new RuleException(parseException);
               }
            } else if (ruleParserParser$AttributeContext.expiresDateAttribute() != null) {
               try {
                  String substring2 = ruleParserParser$AttributeContext.expiresDateAttribute().STRING().getText();
                  substring2 = substring2.substring(1, substring2.length() - 1);
                  rule.setExpiresDate(simpleDateFormat.parse(substring2));
               } catch (ParseException parseException2) {
                  throw new RuleException(parseException2);
               }
            } else if (ruleParserParser$AttributeContext.enabledAttribute() != null) {
               rule.setEnabled(Boolean.valueOf(ruleParserParser$AttributeContext.enabledAttribute().Boolean().getText()));
            } else if (ruleParserParser$AttributeContext.debugAttribute() != null) {
               rule.setDebug(Boolean.valueOf(ruleParserParser$AttributeContext.debugAttribute().Boolean().getText()));
            } else if (ruleParserParser$AttributeContext.activationGroupAttribute() != null) {
               String substring3 = ruleParserParser$AttributeContext.activationGroupAttribute().STRING().getText();
               substring3 = substring3.substring(1, substring3.length() - 1);
               rule.setMutexGroup(substring3);
            } else if (ruleParserParser$AttributeContext.agendaGroupAttribute() != null) {
               String substring4 = ruleParserParser$AttributeContext.agendaGroupAttribute().STRING().getText();
               substring4 = substring4.substring(1, substring4.length() - 1);
               rule.setPendedGroup(substring4);
            } else if (ruleParserParser$AttributeContext.autoFocusAttribute() != null) {
               rule.setAutoFocus(Boolean.valueOf(ruleParserParser$AttributeContext.autoFocusAttribute().Boolean().getText()));
            }
         }
      }

      RuleParserParser$LeftContext ruleParserParser$LeftContext = ctx.left();
      ParseTree child = ruleParserParser$LeftContext.getChild(1);
      Lhs lhs = new Lhs();
      rule.setLhs(lhs);
      Criterion criterion = this.buildCriterion(child);
      lhs.setCriterion(criterion);
      Rhs rhs = new Rhs();
      rhs.setActions(this.visitRight(ctx.right()));
      rule.setRhs(rhs);
      Other other = new Other();
      other.setActions(this.visitOther(ctx.other()));
      rule.setOther(other);
      return rule;
   }
   public Criteria visitSingleCondition(RuleParserParser$SingleConditionContext ctx) {
      return (Criteria)this.doBuilder(ctx);
   }
   public Criterion visitParenConditions(RuleParserParser$ParenConditionsContext ctx) {
      ParseTree child = ctx.getChild(1);
      return this.buildCriterion(child);
   }
   public Criterion visitMultiConditions(RuleParserParser$MultiConditionsContext ctx) {
      Junction junction = null;
      Criterion criterion = null;
      Junction junction2 = this.junctionsByParseTree.get(ctx);
      int childCount = ctx.getChildCount();

      for (int index = 0; index < childCount; index++) {
         ParseTree child = ctx.getChild(index);
         if (child instanceof RuleParserParser$JoinContext) {
            RuleParserParser$JoinContext ruleParserParser$JoinContext = (RuleParserParser$JoinContext)child;
            if (ruleParserParser$JoinContext.AND() != null) {
               if (junction2 == null) {
                  junction2 = new And();
                  junction = junction2;
                  junction2.addCriterion(criterion);
               } else if (!(junction2 instanceof And)) {
                  And and = new And();
                  junction2.addCriterion(and);
                  junction2 = and;
               }
            } else if (junction2 == null) {
               junction2 = new Or();
               junction = junction2;
               junction2.addCriterion(criterion);
            } else if (!(junction2 instanceof Or)) {
               Or or = new Or();
               junction2.addCriterion(or);
               junction2 = or;
            }
         } else {
            boolean flag = false;
            if (child instanceof RuleParserParser$MultiConditionsContext) {
               flag = true;
            }

            if (junction2 != null && flag) {
               this.junctionsByParseTree.put(child, junction2);
            }

            criterion = this.buildCriterion(child);
            if (junction2 != null && !flag) {
               junction2.addCriterion(criterion);
            }
         }
      }

      return junction != null ? junction : criterion;
   }
   public List<Action> visitRight(RuleParserParser$RightContext ctx) {
      if (ctx != null && ctx.action() != null) {
         List items = ctx.action();
         return this.buildActions(items);
      } else {
         return null;
      }
   }

   private List<Action> buildActions(List<RuleParserParser$ActionContext> ruleParserParser$ActionContexts) {
      ArrayList items = new ArrayList();

      for (RuleParserParser$ActionContext ruleParserParser$ActionContext : ruleParserParser$ActionContexts) {
         Action action = (Action)this.doBuilder(ruleParserParser$ActionContext);
         items.add(action);
      }

      return items;
   }
   public List<Action> visitOther(RuleParserParser$OtherContext ctx) {
      if (ctx != null && ctx.action() != null) {
         ArrayList visitOtherResult = new ArrayList();

         for (RuleParserParser$ActionContext ruleParserParser$ActionContext : ctx.action()) {
            Action action = (Action)this.doBuilder(ruleParserParser$ActionContext);
            visitOtherResult.add(action);
         }

         return visitOtherResult;
      } else {
         return null;
      }
   }

   public Criterion buildCriterion(ParseTree parseTree) {
      Criterion criterion = null;
      if (parseTree instanceof RuleParserParser$ParenConditionsContext) {
         criterion = this.visitParenConditions((RuleParserParser$ParenConditionsContext)parseTree);
      } else if (parseTree instanceof RuleParserParser$SingleConditionContext) {
         criterion = this.visitSingleCondition((RuleParserParser$SingleConditionContext)parseTree);
      } else if (parseTree instanceof RuleParserParser$MultiConditionsContext) {
         criterion = this.visitMultiConditions((RuleParserParser$MultiConditionsContext)parseTree);
      }

      return criterion;
   }

   private Object doBuilder(ParserRuleContext parserRuleContext) {
      for (ContextBuilder contextBuilder : this.builders) {
         if (contextBuilder.support(parserRuleContext)) {
            return contextBuilder.build(parserRuleContext);
         }
      }

      return null;
   }
}
