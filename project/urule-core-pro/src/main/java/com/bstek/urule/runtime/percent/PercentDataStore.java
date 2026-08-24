package com.bstek.urule.runtime.percent;

import com.bstek.urule.model.flow.DecisionItem;
import com.bstek.urule.model.flow.ProcessDefinition;
import java.util.List;

public interface PercentDataStore {
   String BEAN_ID = "urule.percentDataStore";

   PercentUnit getDecisionNodePercent(ProcessDefinition pd, List<DecisionItem> items, String decisionNodeName);
}
