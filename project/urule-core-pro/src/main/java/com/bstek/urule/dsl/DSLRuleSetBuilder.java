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
   private Collection<ContextBuilder> contextBuilders;
   private RulesRebuilder rulesRebuilder;

   public RuleSet build(String script, String path) throws IOException {
      RuleParserLexer ruleParserLexer = new RuleParserLexer(CharStreams.fromString(script));
      CommonTokenStream commonTokenStream = new CommonTokenStream(ruleParserLexer);
      RuleParserParser ruleParserParser = new RuleParserParser(commonTokenStream);
      ScriptDecisionTableErrorListener scriptDecisionTableErrorListener = new ScriptDecisionTableErrorListener();
      ruleParserParser.addErrorListener(scriptDecisionTableErrorListener);
      BuildRulesVisitor buildRulesVisitor = new BuildRulesVisitor(this.contextBuilders, commonTokenStream);
      RuleSet ruleSet = buildRulesVisitor.buildRuleSet(ruleParserParser.ruleSet(), path);
      this.processRuleSet(ruleSet);
      String errorMessage = scriptDecisionTableErrorListener.getErrorMessage();
      if (errorMessage != null) {
         throw new RuleException("Script parse error:" + errorMessage);
      } else {
         return ruleSet;
      }
   }

   public Criterion buildCriterion(String script) throws IOException {
      RuleParserLexer ruleParserLexer = new RuleParserLexer(CharStreams.fromString(script));
      CommonTokenStream commonTokenStream = new CommonTokenStream(ruleParserLexer);
      RuleParserParser ruleParserParser = new RuleParserParser(commonTokenStream);
      ScriptDecisionTableErrorListener scriptDecisionTableErrorListener = new ScriptDecisionTableErrorListener();
      ruleParserParser.addErrorListener(scriptDecisionTableErrorListener);
      BuildRulesVisitor buildRulesVisitor = new BuildRulesVisitor(this.contextBuilders, commonTokenStream);
      Criterion criterion = buildRulesVisitor.buildCriterion(ruleParserParser.condition());
      String errorMessage = scriptDecisionTableErrorListener.getErrorMessage();
      if (errorMessage != null) {
         throw new RuleException("Script parse error:" + errorMessage);
      } else {
         return criterion;
      }
   }

   public AbstractValue buildValue(String script) throws IOException {
      RuleParserLexer ruleParserLexer = new RuleParserLexer(CharStreams.fromString(script));
      CommonTokenStream commonTokenStream = new CommonTokenStream(ruleParserLexer);
      RuleParserParser ruleParserParser = new RuleParserParser(commonTokenStream);
      ScriptDecisionTableErrorListener scriptDecisionTableErrorListener = new ScriptDecisionTableErrorListener();
      ruleParserParser.addErrorListener(scriptDecisionTableErrorListener);
      AbstractValue abstractValue = BuildUtils.buildValue(ruleParserParser.complexValue());
      String errorMessage = scriptDecisionTableErrorListener.getErrorMessage();
      if (errorMessage != null) {
         throw new RuleException("Script parse error:" + errorMessage);
      } else {
         return abstractValue;
      }
   }

   private void processRuleSet(RuleSet ruleSet) {
      List libraries = ruleSet.getLibraries();
      List rules = ruleSet.getRules();
      this.rulesRebuilder.rebuildRulesForDSL(libraries, rules, ruleSet.getPredefineGroup().getPredefines());
   }

   public void setRulesRebuilder(RulesRebuilder rulesRebuilder) {
      this.rulesRebuilder = rulesRebuilder;
   }

   public boolean support(Resource resource) {
      return false;
   }

   public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
      this.contextBuilders = applicationContext.getBeansOfType(ContextBuilder.class).values();
   }
}
