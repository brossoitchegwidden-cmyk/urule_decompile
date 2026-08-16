package com.bstek.urule.dsl.builder;

import org.antlr.v4.runtime.ParserRuleContext;

public interface ContextBuilder {
   Object build(ParserRuleContext var1);

   boolean support(ParserRuleContext var1);
}
