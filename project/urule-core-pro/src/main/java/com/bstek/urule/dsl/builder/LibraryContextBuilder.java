package com.bstek.urule.dsl.builder;

import com.bstek.urule.dsl.RuleParserParser$ResourceContext;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.rule.Library;
import com.bstek.urule.model.rule.LibraryType;
import org.antlr.v4.runtime.ParserRuleContext;

public class LibraryContextBuilder extends AbstractContextBuilder {
   public Library build(ParserRuleContext var1) {
      RuleParserParser$ResourceContext var2 = (RuleParserParser$ResourceContext)var1;
      if (var2.importActionLibrary() != null) {
         String var6 = BuildUtils.getSTRINGContent(var2.importActionLibrary().STRING());
         return new Library(-1L, var6, null, LibraryType.Action);
      } else if (var2.importConstantLibrary() != null) {
         String var5 = BuildUtils.getSTRINGContent(var2.importConstantLibrary().STRING());
         return new Library(-1L, var5, null, LibraryType.Constant);
      } else if (var2.importVariableLibrary() != null) {
         String var4 = BuildUtils.getSTRINGContent(var2.importVariableLibrary().STRING());
         return new Library(-1L, var4, null, LibraryType.Variable);
      } else if (var2.importParameterLibrary() != null) {
         String var3 = BuildUtils.getSTRINGContent(var2.importParameterLibrary().STRING());
         return new Library(-1L, var3, null, LibraryType.Parameter);
      } else {
         throw new RuleException("Unsupport context " + var2.getClass().getName() + "");
      }
   }

   @Override
   public boolean support(ParserRuleContext var1) {
      return var1 instanceof RuleParserParser$ResourceContext;
   }
}
