package com.bstek.urule.dsl;

import com.bstek.urule.builder.RulesRebuilder;
import com.bstek.urule.builder.resource.Resource;
import com.bstek.urule.dsl.builder.BuildUtils;
import com.bstek.urule.dsl.builder.ContextBuilder;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.rule.AbstractValue;
import com.bstek.urule.model.rule.RuleSet;
import com.bstek.urule.model.rule.lhs.Criterion;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class DSLRuleSetBuilder implements ApplicationContextAware {
   public static final String BEAN_ID = "urule.dslRuleSetBuilder";
   private Collection<ContextBuilder> a;
   private RulesRebuilder b;

   public RuleSet build(String var1, String var2) throws IOException {
      RuleParserLexer var3 = new RuleParserLexer(CharStreams.fromString(var1));
      CommonTokenStream var4 = new CommonTokenStream(var3);
      RuleParserParser var5 = new RuleParserParser(var4);
      ScriptDecisionTableErrorListener var6 = new ScriptDecisionTableErrorListener();
      var5.addErrorListener(var6);
      BuildRulesVisitor var7 = new BuildRulesVisitor(this.a, var4);
      RuleSet var8 = var7.buildRuleSet(var5.ruleSet(), var2);
      this.a(var8);
      String var9 = var6.getErrorMessage();
      if (var9 != null) {
         throw new RuleException("Script parse error:" + var9);
      } else {
         return var8;
      }
   }

   public Criterion buildCriterion(String var1) throws IOException {
      RuleParserLexer var2 = new RuleParserLexer(CharStreams.fromString(var1));
      CommonTokenStream var3 = new CommonTokenStream(var2);
      RuleParserParser var4 = new RuleParserParser(var3);
      ScriptDecisionTableErrorListener var5 = new ScriptDecisionTableErrorListener();
      var4.addErrorListener(var5);
      BuildRulesVisitor var6 = new BuildRulesVisitor(this.a, var3);
      Criterion var7 = var6.buildCriterion(var4.condition());
      String var8 = var5.getErrorMessage();
      if (var8 != null) {
         throw new RuleException("Script parse error:" + var8);
      } else {
         return var7;
      }
   }

   public AbstractValue buildValue(String var1) throws IOException {
      RuleParserLexer var2 = new RuleParserLexer(CharStreams.fromString(var1));
      CommonTokenStream var3 = new CommonTokenStream(var2);
      RuleParserParser var4 = new RuleParserParser(var3);
      ScriptDecisionTableErrorListener var5 = new ScriptDecisionTableErrorListener();
      var4.addErrorListener(var5);
      AbstractValue var6 = BuildUtils.buildValue(var4.complexValue());
      String var7 = var5.getErrorMessage();
      if (var7 != null) {
         throw new RuleException("Script parse error:" + var7);
      } else {
         return var6;
      }
   }

   private void a(RuleSet var1) {
      List var2 = var1.getLibraries();
      List var3 = var1.getRules();
      this.b.rebuildRulesForDSL(var2, var3, var1.getPredefineGroup().getPredefines());
   }

   public void setRulesRebuilder(RulesRebuilder var1) {
      this.b = var1;
   }

   public boolean support(Resource var1) {
      return false;
   }

   public void setApplicationContext(ApplicationContext var1) throws BeansException {
      this.a = var1.getBeansOfType(ContextBuilder.class).values();
   }
}
