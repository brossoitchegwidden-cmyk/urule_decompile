package com.bstek.urule.runtime.rete;

import com.bstek.urule.model.rete.RuleData;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ReteInstance {
   private List<RuleData> allRuleData;
   private String id = UUID.randomUUID().toString();
   private List<ObjectTypeActivity> objectTypeActivities;
   private Map<String, List<ReteInstanceUnit>> mutexGroupReteInstancesMap;
   private Map<String, List<ReteInstanceUnit>> pendedGroupReteInstancesMap;

   public ReteInstance(List<ObjectTypeActivity> objectTypeActivities, Map<String, List<ReteInstanceUnit>> mutexGroupReteInstancesMap, Map<String, List<ReteInstanceUnit>> pendedGroupReteInstancesMap, List<RuleData> allRuleData) {
      this.objectTypeActivities = objectTypeActivities;
      this.mutexGroupReteInstancesMap = mutexGroupReteInstancesMap;
      this.pendedGroupReteInstancesMap = pendedGroupReteInstancesMap;
      this.allRuleData = allRuleData;
   }

   public Collection<FactTracker> enter(EvaluationContext context, Object obj) {
      Collection enterResult = null;

      for (ObjectTypeActivity objectTypeActivity : this.objectTypeActivities) {
         if (objectTypeActivity.support(obj)) {
            enterResult = objectTypeActivity.enter(context, obj, new FactTracker());
            break;
         }
      }

      return enterResult;
   }

   public Map<String, List<ReteInstanceUnit>> getMutexGroupReteInstancesMap() {
      return this.mutexGroupReteInstancesMap;
   }

   public Map<String, List<ReteInstanceUnit>> getPendedGroupReteInstancesMap() {
      return this.pendedGroupReteInstancesMap;
   }

   public List<RuleData> getAllRuleData() {
      return this.allRuleData;
   }

   public String getId() {
      return this.id;
   }
}
