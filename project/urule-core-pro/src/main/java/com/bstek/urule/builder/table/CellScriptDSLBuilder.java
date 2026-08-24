package com.bstek.urule.builder.table;

import com.bstek.urule.dsl.CellScriptRuleParserBaseVisitor;
import com.bstek.urule.dsl.RuleParserLexer;
import com.bstek.urule.dsl.RuleParserParser;
import com.bstek.urule.dsl.ScriptDecisionTableErrorListener;
import com.bstek.urule.exception.RuleException;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

public class CellScriptDSLBuilder {
   public String buildCriteriaScript(String script, String propertyName) {
      RuleParserLexer ruleParserLexer = new RuleParserLexer(CharStreams.fromString(script));
      CommonTokenStream commonTokenStream = new CommonTokenStream(ruleParserLexer);
      RuleParserParser ruleParserParser = new RuleParserParser(commonTokenStream);
      ScriptDecisionTableErrorListener scriptDecisionTableErrorListener = new ScriptDecisionTableErrorListener();
      ruleParserParser.addErrorListener(scriptDecisionTableErrorListener);
      CellScriptRuleParserBaseVisitor cellScriptRuleParserBaseVisitor = new CellScriptRuleParserBaseVisitor(propertyName);
      String criteriaScript = (String)cellScriptRuleParserBaseVisitor.visit(ruleParserParser.decisionTableCellCondition());
      String errorMessage = scriptDecisionTableErrorListener.getErrorMessage();
      if (errorMessage != null) {
         throw new RuleException("Script Parse error:" + errorMessage);
      } else {
         return criteriaScript;
      }
   }
}
