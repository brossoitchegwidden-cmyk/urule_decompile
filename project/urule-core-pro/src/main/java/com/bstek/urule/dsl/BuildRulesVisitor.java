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
   private Map<ParseTree, Junction> a = new HashMap<>();
   private Collection<ContextBuilder> b;
   private CommonTokenStream c;

   public BuildRulesVisitor(Collection<ContextBuilder> var1, CommonTokenStream var2) {
      this.b = var1;
      this.c = var2;
   }

   public RuleSet buildRuleSet(RuleParserParser$RuleSetContext var1, String var2) {
      RuleSet var3 = this.visitRuleSet(var1);

      for (Rule var5 : var3.getRules()) {
         var5.setFile(var2);
      }

      return var3;
   }

   public RuleSet visitRuleSet(RuleParserParser$RuleSetContext var1) {
      RuleSet var2 = new RuleSet();
      RuleParserParser$RuleSetHeaderContext var3 = var1.ruleSetHeader();
      List var4 = var3.resource();
      if (var4 != null) {
         for (RuleParserParser$ResourceContext var6 : (Iterable<RuleParserParser$ResourceContext>)(Iterable<?>)(var4)) {
            var2.addLibrary(this.visitResource(var6));
         }
      }

      StringBuffer var15 = null;
      List var17 = var3.functionImport();
      if (var17 != null) {
         var15 = new StringBuffer();

         for (RuleParserParser$FunctionImportContext var8 : (Iterable<RuleParserParser$FunctionImportContext>)(Iterable<?>)(var17)) {
            var15.append("import ");
            var15.append(var8.packageDef().getText());
            var15.append(";");
         }
      }

      RuleParserParser$RuleSetBodyContext var18 = var1.ruleSetBody();
      List var19 = var18.rules();
      if (var19 != null) {
         ArrayList var9 = new ArrayList();
         var2.setRules(var9);

         for (RuleParserParser$RulesContext var11 : (Iterable<RuleParserParser$RulesContext>)(Iterable<?>)(var19)) {
            RuleParserParser$RuleDefContext var12 = var11.ruleDef();
            if (var12 != null) {
               Rule var13 = this.visitRuleDef(var12);
               var9.add(var13);
            }

            RuleParserParser$LoopRuleDefContext var20 = var11.loopRuleDef();
            if (var20 != null) {
               LoopRule var14 = this.visitLoopRuleDef(var20);
               var9.add(var14);
            }
         }
      }

      return var2;
   }

   private String a(RuleParserParser$ExpressionBodyContext var1) {
      StringBuffer var2 = new StringBuffer();

      for (ParseTree var4 : var1.children) {
         Interval var5 = var4.getSourceInterval();
         int var6 = var5.a;
         List var7 = this.c.getHiddenTokensToLeft(var6);
         if (var7 != null) {
            Token var8 = (Token)var7.get(0);
            String var9 = var8.getText();
            var2.append(var9);
         }

         var2.append(var4.getText());
         List var11 = this.c.getHiddenTokensToRight(var6);
         if (var11 != null) {
            Token var12 = (Token)var11.get(0);
            String var10 = var12.getText();
            var2.append(var10);
         }
      }

      return var2.toString();
   }

   public Library visitResource(RuleParserParser$ResourceContext var1) {
      return (Library)this.a(var1);
   }

   public LoopRule visitLoopRuleDef(RuleParserParser$LoopRuleDefContext var1) {
      SimpleDateFormat var2 = new SimpleDateFormat(Configure.getDateFormat());
      LoopRule var3 = new LoopRule();
      String var4 = var1.STRING().getText();
      var4 = var4.substring(1, var4.length() - 1);
      var3.setName(var4);
      RuleParserParser$LoopTargetContext var5 = var1.loopTarget();
      RuleParserParser$ComplexValueContext var6 = var5.complexValue();
      LoopTarget var7 = new LoopTarget();
      var7.setValue(BuildUtils.buildValue(var6));
      var3.setLoopTarget(var7);
      RuleParserParser$LoopStartContext var8 = var1.loopStart();
      if (var8 != null) {
         List var9 = var8.action();
         if (var9 != null) {
            LoopStart var10 = new LoopStart();
            var10.setActions(this.a(var9));
            var3.setLoopStart(var10);
         }
      }

      RuleParserParser$LoopEndContext var25 = var1.loopEnd();
      if (var25 != null) {
         List var26 = var25.action();
         if (var26 != null) {
            LoopEnd var11 = new LoopEnd();
            var11.setActions(this.a(var26));
            var3.setLoopEnd(var11);
         }
      }

      List var27 = var1.attribute();
      if (var27 != null) {
         for (RuleParserParser$AttributeContext var12 : (Iterable<RuleParserParser$AttributeContext>)(Iterable<?>)(var27)) {
            if (var12.salienceAttribute() != null) {
               var3.setSalience(Integer.valueOf(var12.salienceAttribute().NUMBER().getText()));
            } else if (var12.loopAttribute() != null) {
               var3.setLoop(Boolean.valueOf(var12.loopAttribute().Boolean().getText()));
            } else if (var12.effectiveDateAttribute() != null) {
               try {
                  String var13 = var12.effectiveDateAttribute().STRING().getText();
                  var13 = var13.substring(1, var13.length() - 1);
                  var3.setEffectiveDate(var2.parse(var13));
               } catch (ParseException var23) {
                  throw new RuleException(var23);
               }
            } else if (var12.expiresDateAttribute() != null) {
               try {
                  String var32 = var12.expiresDateAttribute().STRING().getText();
                  var32 = var32.substring(1, var32.length() - 1);
                  var3.setExpiresDate(var2.parse(var32));
               } catch (ParseException var22) {
                  throw new RuleException(var22);
               }
            } else if (var12.enabledAttribute() != null) {
               var3.setEnabled(Boolean.valueOf(var12.enabledAttribute().Boolean().getText()));
            } else if (var12.debugAttribute() != null) {
               var3.setDebug(Boolean.valueOf(var12.debugAttribute().Boolean().getText()));
            } else if (var12.activationGroupAttribute() != null) {
               String var34 = var12.activationGroupAttribute().STRING().getText();
               var34 = var34.substring(1, var34.length() - 1);
               var3.setMutexGroup(var34);
            } else if (var12.agendaGroupAttribute() != null) {
               String var36 = var12.agendaGroupAttribute().STRING().getText();
               var36 = var36.substring(1, var36.length() - 1);
               var3.setPendedGroup(var36);
            } else if (var12.autoFocusAttribute() != null) {
               var3.setAutoFocus(Boolean.valueOf(var12.autoFocusAttribute().Boolean().getText()));
            }
         }
      }

      ArrayList var29 = new ArrayList();
      var3.setUnits(var29);

      for (RuleParserParser$LoopRuleUnitContext var14 : var1.loopRuleUnit()) {
         LoopRuleUnit var15 = new LoopRuleUnit();
         var29.add(var15);
         if (var14.STRING() != null) {
            String var16 = var14.STRING().getText();
            if (var16.length() > 2) {
               var16 = var16.substring(1, var4.length() - 1);
               var15.setName(var16);
            } else {
               var15.setName("");
            }
         }

         RuleParserParser$LeftContext var40 = var14.left();
         ParseTree var17 = var40.getChild(1);
         Lhs var18 = new Lhs();
         var15.setLhs(var18);
         Criterion var19 = this.buildCriterion(var17);
         var18.setCriterion(var19);
         Rhs var20 = new Rhs();
         var20.setActions(this.visitRight(var14.right()));
         var15.setRhs(var20);
         Other var21 = new Other();
         var21.setActions(this.visitOther(var14.other()));
         var15.setOther(var21);
      }

      return var3;
   }

   public Rule visitRuleDef(RuleParserParser$RuleDefContext var1) {
      SimpleDateFormat var2 = new SimpleDateFormat(Configure.getDateFormat());
      Rule var3 = new Rule();
      String var4 = var1.STRING().getText();
      var4 = var4.substring(1, var4.length() - 1);
      var3.setName(var4);
      List var5 = var1.attribute();
      if (var5 != null) {
         for (RuleParserParser$AttributeContext var7 : (Iterable<RuleParserParser$AttributeContext>)(Iterable<?>)(var5)) {
            if (var7.salienceAttribute() != null) {
               var3.setSalience(Integer.valueOf(var7.salienceAttribute().NUMBER().getText()));
            } else if (var7.loopAttribute() != null) {
               var3.setLoop(Boolean.valueOf(var7.loopAttribute().Boolean().getText()));
            } else if (var7.effectiveDateAttribute() != null) {
               try {
                  String var8 = var7.effectiveDateAttribute().STRING().getText();
                  var8 = var8.substring(1, var8.length() - 1);
                  var3.setEffectiveDate(var2.parse(var8));
               } catch (ParseException var13) {
                  throw new RuleException(var13);
               }
            } else if (var7.expiresDateAttribute() != null) {
               try {
                  String var18 = var7.expiresDateAttribute().STRING().getText();
                  var18 = var18.substring(1, var18.length() - 1);
                  var3.setExpiresDate(var2.parse(var18));
               } catch (ParseException var12) {
                  throw new RuleException(var12);
               }
            } else if (var7.enabledAttribute() != null) {
               var3.setEnabled(Boolean.valueOf(var7.enabledAttribute().Boolean().getText()));
            } else if (var7.debugAttribute() != null) {
               var3.setDebug(Boolean.valueOf(var7.debugAttribute().Boolean().getText()));
            } else if (var7.activationGroupAttribute() != null) {
               String var20 = var7.activationGroupAttribute().STRING().getText();
               var20 = var20.substring(1, var20.length() - 1);
               var3.setMutexGroup(var20);
            } else if (var7.agendaGroupAttribute() != null) {
               String var22 = var7.agendaGroupAttribute().STRING().getText();
               var22 = var22.substring(1, var22.length() - 1);
               var3.setPendedGroup(var22);
            } else if (var7.autoFocusAttribute() != null) {
               var3.setAutoFocus(Boolean.valueOf(var7.autoFocusAttribute().Boolean().getText()));
            }
         }
      }

      RuleParserParser$LeftContext var15 = var1.left();
      ParseTree var16 = var15.getChild(1);
      Lhs var24 = new Lhs();
      var3.setLhs(var24);
      Criterion var9 = this.buildCriterion(var16);
      var24.setCriterion(var9);
      Rhs var10 = new Rhs();
      var10.setActions(this.visitRight(var1.right()));
      var3.setRhs(var10);
      Other var11 = new Other();
      var11.setActions(this.visitOther(var1.other()));
      var3.setOther(var11);
      return var3;
   }

   public Criteria visitSingleCondition(RuleParserParser$SingleConditionContext var1) {
      return (Criteria)this.a(var1);
   }

   public Criterion visitParenConditions(RuleParserParser$ParenConditionsContext var1) {
      ParseTree var2 = var1.getChild(1);
      return this.buildCriterion(var2);
   }

   public Criterion visitMultiConditions(RuleParserParser$MultiConditionsContext var1) {
      Junction var2 = null;
      Criterion var3 = null;
      Junction var4 = this.a.get(var1);
      int var5 = var1.getChildCount();

      for (int var6 = 0; var6 < var5; var6++) {
         ParseTree var7 = var1.getChild(var6);
         if (var7 instanceof RuleParserParser$JoinContext) {
            RuleParserParser$JoinContext var8 = (RuleParserParser$JoinContext)var7;
            if (var8.AND() != null) {
               if (var4 == null) {
                  var4 = new And();
                  var2 = var4;
                  var4.addCriterion(var3);
               } else if (!(var4 instanceof And)) {
                  And var9 = new And();
                  var4.addCriterion(var9);
                  var4 = var9;
               }
            } else if (var4 == null) {
               var4 = new Or();
               var2 = var4;
               var4.addCriterion(var3);
            } else if (!(var4 instanceof Or)) {
               Or var11 = new Or();
               var4.addCriterion(var11);
               var4 = var11;
            }
         } else {
            boolean var10 = false;
            if (var7 instanceof RuleParserParser$MultiConditionsContext) {
               var10 = true;
            }

            if (var4 != null && var10) {
               this.a.put(var7, var4);
            }

            var3 = this.buildCriterion(var7);
            if (var4 != null && !var10) {
               var4.addCriterion(var3);
            }
         }
      }

      return var2 != null ? var2 : var3;
   }

   public List<Action> visitRight(RuleParserParser$RightContext var1) {
      if (var1 != null && var1.action() != null) {
         List var2 = var1.action();
         return this.a(var2);
      } else {
         return null;
      }
   }

   private List<Action> a(List<RuleParserParser$ActionContext> var1) {
      ArrayList var2 = new ArrayList();

      for (RuleParserParser$ActionContext var4 : var1) {
         Action var5 = (Action)this.a(var4);
         var2.add(var5);
      }

      return var2;
   }

   public List<Action> visitOther(RuleParserParser$OtherContext var1) {
      if (var1 != null && var1.action() != null) {
         ArrayList var2 = new ArrayList();

         for (RuleParserParser$ActionContext var4 : var1.action()) {
            Action var5 = (Action)this.a(var4);
            var2.add(var5);
         }

         return var2;
      } else {
         return null;
      }
   }

   public Criterion buildCriterion(ParseTree var1) {
      Criterion var2 = null;
      if (var1 instanceof RuleParserParser$ParenConditionsContext) {
         var2 = this.visitParenConditions((RuleParserParser$ParenConditionsContext)var1);
      } else if (var1 instanceof RuleParserParser$SingleConditionContext) {
         var2 = this.visitSingleCondition((RuleParserParser$SingleConditionContext)var1);
      } else if (var1 instanceof RuleParserParser$MultiConditionsContext) {
         var2 = this.visitMultiConditions((RuleParserParser$MultiConditionsContext)var1);
      }

      return var2;
   }

   private Object a(ParserRuleContext var1) {
      for (ContextBuilder var3 : this.b) {
         if (var3.support(var1)) {
            return var3.build(var1);
         }
      }

      return null;
   }
}
