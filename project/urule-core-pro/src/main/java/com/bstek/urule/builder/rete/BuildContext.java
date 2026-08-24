package com.bstek.urule.builder.rete;

import com.bstek.urule.model.library.ResourceLibrary;
import com.bstek.urule.model.rete.ObjectTypeNode;
import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.model.rule.lhs.BaseCriteria;
import com.bstek.urule.model.rule.lhs.Criterion;
import java.util.List;

public interface BuildContext {
   List<String> getObjectType(BaseCriteria criteria);

   List<String> getObjectTypeByCriterions(List<Criterion> criterions);

   boolean assertSameType(BaseCriteria left, BaseCriteria right);

   ResourceLibrary getResourceLibrary();

   ObjectTypeNode buildObjectTypeNode(String className);

   int nextId();

   void setCurrentRule(Rule rule);

   Rule currentRule();

   boolean currentRuleIsDebug();

   IdGenerator getIdGenerator();
}
