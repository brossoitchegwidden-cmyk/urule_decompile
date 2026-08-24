package com.bstek.urule.console.batch.processor;

import com.bstek.urule.console.batch.BatchContext;
import com.bstek.urule.model.GeneralEntity;
import java.util.Map;

public interface RuleProcessor {
   Map fireRules(BatchContext batchContext, GeneralEntity data) throws ProcessorException;
}
