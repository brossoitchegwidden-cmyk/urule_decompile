package com.bstek.urule.builder.table;

import com.bstek.urule.dsl.CellScriptRuleParserBaseVisitor;
import com.bstek.urule.dsl.RuleParserLexer;
import com.bstek.urule.dsl.RuleParserParser;
import com.bstek.urule.dsl.ScriptDecisionTableErrorListener;
import com.bstek.urule.exception.RuleException;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

public class CellScriptDSLBuilder {
   public String buildCriteriaScript(String var1, String var2) {
      RuleParserLexer var3 = new RuleParserLexer(CharStreams.fromString(var1));
      CommonTokenStream var4 = new CommonTokenStream(var3);
      RuleParserParser var5 = new RuleParserParser(var4);
      ScriptDecisionTableErrorListener var6 = new ScriptDecisionTableErrorListener();
      var5.addErrorListener(var6);
      CellScriptRuleParserBaseVisitor var7 = new CellScriptRuleParserBaseVisitor(var2);
      String var8 = (String)var7.visit(var5.decisionTableCellCondition());
      String var9 = var6.getErrorMessage();
      if (var9 != null) {
         throw new RuleException("Script Parse error:" + var9);
      } else {
         return var8;
      }
   }
}
