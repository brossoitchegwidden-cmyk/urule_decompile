package com.bstek.urule.runtime.rete;

import com.bstek.urule.Utils;
import com.bstek.urule.model.rule.lhs.Criteria;
import com.bstek.urule.model.rule.lhs.EvaluateResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CriteriaActivity extends AbstractActivity {
   private boolean debug;
   private Criteria criteria;

   public CriteriaActivity(Criteria criteria, boolean debug) {
      this.criteria = criteria;
      this.debug = debug;
   }

   public List<FactTracker> enter(EvaluationContext context, Object fact, FactTracker tracker) {
      CriteriaActivityState activityState = context.getActivityState(this.activityId);
      Set tokensSet = activityState.getTokensSet();
      tokensSet.addAll(tracker.getTokens());
      this.processEvaluationContext(context, fact, tracker);
      Set classSet = activityState.getClassSet();
      if (!activityState.isPassed() && this.criteria.necessaryClassEval(classSet)) {
         activityState.setPassed(true);
         tracker.setTokens(tokensSet);
         ArrayList enterResult = new ArrayList();

         for (Map valuesByKey : activityState.getFactMapList()) {
            EvaluateResponse evaluateResponse = this.criteria.evaluate(context, valuesByKey);
            if (this.debug) {
               context.getLogger().logCriteria(this.criteria, evaluateResponse);
            }

            if (evaluateResponse.getResult()) {
               FactTracker factTracker = tracker.newSubFactTracker();
               factTracker.addCriteria(this.criteria);
               factTracker.addFactMap(valuesByKey);
               List items = this.visitPahs(context, fact, factTracker);
               if (items != null) {
                  enterResult.addAll(items);
               }
            }
         }

         return enterResult;
      } else {
         return null;
      }
   }

   private void processEvaluationContext(EvaluationContext evaluationContext, Object objectValue, FactTracker factTracker) {
      Map factMap = factTracker.getFactMap();
      CriteriaActivityState activityState = evaluationContext.getActivityState(this.activityId);
      Set classSet = activityState.getClassSet();
      List factMapList = activityState.getFactMapList();
      if (factMap.size() == 0) {
         String className = Utils.getClassName(objectValue);
         if (!activityState.isPassed() && classSet.contains(className)) {
            HashMap valuesByKey = new HashMap();
            valuesByKey.putAll((Map)factMapList.get(0));
            valuesByKey.put(className, objectValue);
            factMapList.add(valuesByKey);
         } else {
            classSet.add(className);
            if (factMapList.size() == 0) {
               HashMap valuesByKey2 = new HashMap();
               valuesByKey2.put(className, objectValue);
               factMapList.add(valuesByKey2);
            } else {
               for (Map valuesByKey3 : (Iterable<Map>)(Iterable<?>)(factMapList)) {
                  valuesByKey3.put(className, objectValue);
               }
            }
         }
      } else if (classSet.size() == 0) {
         classSet.addAll(factMap.keySet());
         HashMap valuesByKey4 = new HashMap();
         valuesByKey4.putAll(factMap);
         factMapList.add(valuesByKey4);
      } else {
         for (String text : (Iterable<String>)(Iterable<?>)(factMap.keySet())) {
            for (Map valuesByKey5 : (Iterable<Map>)(Iterable<?>)(factMapList)) {
               valuesByKey5.put(text, factMap.get(text));
            }
         }
      }
   }
}
