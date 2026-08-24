package com.bstek.urule.dsl.builder;

import org.antlr.v4.runtime.ParserRuleContext;

public interface ContextBuilder {
   Object build(ParserRuleContext context);

   boolean support(ParserRuleContext context);
}
