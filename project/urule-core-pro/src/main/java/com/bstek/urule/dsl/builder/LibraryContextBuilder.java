package com.bstek.urule.dsl.builder;

import com.bstek.urule.dsl.RuleParserParser$ResourceContext;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.rule.Library;
import com.bstek.urule.model.rule.LibraryType;
import org.antlr.v4.runtime.ParserRuleContext;

public class LibraryContextBuilder extends AbstractContextBuilder {
   public Library build(ParserRuleContext context) {
      RuleParserParser$ResourceContext ruleParserParser$ResourceContext = (RuleParserParser$ResourceContext)context;
      if (ruleParserParser$ResourceContext.importActionLibrary() != null) {
         String sTRINGContent = BuildUtils.getSTRINGContent(ruleParserParser$ResourceContext.importActionLibrary().STRING());
         return new Library(-1L, sTRINGContent, null, LibraryType.Action);
      } else if (ruleParserParser$ResourceContext.importConstantLibrary() != null) {
         String sTRINGContent2 = BuildUtils.getSTRINGContent(ruleParserParser$ResourceContext.importConstantLibrary().STRING());
         return new Library(-1L, sTRINGContent2, null, LibraryType.Constant);
      } else if (ruleParserParser$ResourceContext.importVariableLibrary() != null) {
         String sTRINGContent3 = BuildUtils.getSTRINGContent(ruleParserParser$ResourceContext.importVariableLibrary().STRING());
         return new Library(-1L, sTRINGContent3, null, LibraryType.Variable);
      } else if (ruleParserParser$ResourceContext.importParameterLibrary() != null) {
         String sTRINGContent4 = BuildUtils.getSTRINGContent(ruleParserParser$ResourceContext.importParameterLibrary().STRING());
         return new Library(-1L, sTRINGContent4, null, LibraryType.Parameter);
      } else {
         throw new RuleException("Unsupport context " + ruleParserParser$ResourceContext.getClass().getName() + "");
      }
   }

   @Override
   public boolean support(ParserRuleContext context) {
      return context instanceof RuleParserParser$ResourceContext;
   }
}
