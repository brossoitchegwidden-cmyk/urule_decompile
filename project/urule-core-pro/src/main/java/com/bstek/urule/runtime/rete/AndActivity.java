package com.bstek.urule.runtime.rete;

import com.bstek.urule.model.rule.lhs.Criteria;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AndActivity extends JoinActivity {
   @Override
   public Collection<FactTracker> enter(EvaluationContext context, Object obj, FactTracker tracker) {
      AndActivityState andActivityState = context.getAndActivityState(this.activityId);
      Set tokensSet = andActivityState.getTokensSet();
      tokensSet.addAll(tracker.getTokens());
      this.storePathState(tracker, andActivityState);
      if (!andActivityState.isPassed() && !this.allPassed(context)) {
         return null;
      }

      andActivityState.setPassed(true);
      Set uniqueItems = this.collectCriterias(tracker, andActivityState);
      List items = this.buildPathFactMaps(tracker, andActivityState);
      ArrayList enterResult = new ArrayList();

      for (Map valuesByKey : (Iterable<Map>)(Iterable<?>)(items)) {
         FactTracker factTracker = new FactTracker();
         factTracker.setTokens(tokensSet);
         factTracker.addFactMap(valuesByKey);
         factTracker.addCriterias(uniqueItems);
         List items2 = this.visitPahs(context, obj, factTracker);
         if (items2 != null) {
            enterResult.addAll(items2);
         }
      }

      return enterResult;
   }

   private void storePathState(FactTracker factTracker, AndActivityState andActivityState) {
      Path currentPath = factTracker.getCurrentPath();
      Map pathFactMaps = andActivityState.getPathFactMaps();
      Map pathCriteriaMap = andActivityState.getPathCriteriaMap();
      pathCriteriaMap.put(currentPath, factTracker.getCriterias());
      Map factMap = factTracker.getFactMap();
      if (factMap.size() > 0) {
         List items = null;
         if (pathFactMaps.containsKey(currentPath)) {
            items = (List)pathFactMaps.get(currentPath);
         } else {
            items = new ArrayList();
            pathFactMaps.put(currentPath, items);
         }

         items.add(factMap);
      }
   }

   private List<Map<String, Object>> buildPathFactMaps(FactTracker factTracker, AndActivityState andActivityState) {
      Path currentPath = factTracker.getCurrentPath();
      Map pathFactMaps = andActivityState.getPathFactMaps();
      List items = new ArrayList();
      items.add(factTracker.getFactMap());
      Iterator iterator = pathFactMaps.keySet().iterator();

      while (iterator.hasNext() && items.size() != 0) {
         items = this.mergeNextPathFactMaps(currentPath, iterator, items, pathFactMaps);
      }

      return items;
   }

   private List<Map<String, Object>> mergeNextPathFactMaps(Path path, Iterator<Path> iterator, List<Map<String, Object>> maps, Map<Path, List<Map<String, Object>>> valuesByKey) {
      Path path2 = (Path)iterator.next();
      if (path2 == path) {
         return maps;
      }

      ArrayList items = new ArrayList();
      List items2 = (List)valuesByKey.get(path2);

      for (Map valuesByKey2 : maps) {
         for (Map valuesByKey3 : (Iterable<Map>)(Iterable<?>)(items2)) {
            boolean flag = this.areFactMapsCompatible(valuesByKey2, valuesByKey3);
            if (flag) {
               HashMap valuesByKey4 = new HashMap();
               valuesByKey4.putAll(valuesByKey2);
               valuesByKey4.putAll(valuesByKey3);
               items.add(valuesByKey4);
            }
         }
      }

      return items;
   }

   private boolean areFactMapsCompatible(Map<String, Object> valuesByKey, Map<String, Object> valuesByKey2) {
      boolean flag = true;

      for (String text : valuesByKey2.keySet()) {
         if (valuesByKey.containsKey(text)) {
            Object objectValue = valuesByKey.get(text);
            Object objectValue2 = valuesByKey2.get(text);
            if (objectValue != objectValue2) {
               flag = false;
               break;
            }
         }
      }

      return flag;
   }

   private Set<Criteria> collectCriterias(FactTracker factTracker, AndActivityState andActivityState) {
      Map pathCriteriaMap = andActivityState.getPathCriteriaMap();
      HashSet uniqueItems = new HashSet();
      uniqueItems.addAll(factTracker.getCriterias());
      Path currentPath = factTracker.getCurrentPath();

      for (Path path : (Iterable<Path>)(Iterable<?>)(pathCriteriaMap.keySet())) {
         if (path != currentPath) {
            uniqueItems.addAll((Collection)pathCriteriaMap.get(path));
         }
      }

      return uniqueItems;
   }
}
