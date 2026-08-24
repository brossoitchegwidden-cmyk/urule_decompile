package com.bstek.urule.model.rete;

import com.bstek.urule.model.Node;
import com.bstek.urule.model.library.ResourceLibrary;
import com.bstek.urule.runtime.rete.MutexReteInstanceUnit;
import com.bstek.urule.runtime.rete.ObjectTypeActivity;
import com.bstek.urule.runtime.rete.ReteInstance;
import com.bstek.urule.runtime.rete.ReteInstanceUnit;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Rete implements Node {
   private List<ObjectTypeNode> objectTypeNodes;
   private Map<String, List<ReteUnit>> mutexGroupRetesMap;
   private Map<String, List<ReteUnit>> pendedGroupRetesMap;
   @JsonIgnore
   private List<RuleData> allRuleData;
   @JsonIgnore
   private ResourceLibrary resourceLibrary;
   @JsonIgnore
   private ReteInstance reteInstance;

   public Rete() {
   }

   public Rete(List<ObjectTypeNode> objectTypeNodes, ResourceLibrary resourceLibrary) {
      this.objectTypeNodes = objectTypeNodes;
      this.resourceLibrary = resourceLibrary;
   }

   public List<ObjectTypeNode> getObjectTypeNodes() {
      return this.objectTypeNodes;
   }

   public ResourceLibrary getResourceLibrary() {
      return this.resourceLibrary;
   }

   public Map<String, List<ReteUnit>> getMutexGroupRetesMap() {
      return this.mutexGroupRetesMap;
   }

   public void setMutexGroupRetesMap(Map<String, List<ReteUnit>> mutexGroupRetesMap) {
      this.mutexGroupRetesMap = mutexGroupRetesMap;
   }

   public Map<String, List<ReteUnit>> getPendedGroupRetesMap() {
      return this.pendedGroupRetesMap;
   }

   public void setPendedGroupRetesMap(Map<String, List<ReteUnit>> pendedGroupRetesMap) {
      this.pendedGroupRetesMap = pendedGroupRetesMap;
   }

   public List<RuleData> getAllRuleData() {
      return this.allRuleData;
   }

   public void setAllRuleData(List<RuleData> allRuleData) {
      this.allRuleData = allRuleData;
   }

   public synchronized void initReteInstance() {
      if (this.reteInstance == null) {
         this.reteInstance = this.buildNewReteInstance();
      }
   }

   public synchronized ReteInstance getReteInstance() {
      if (this.reteInstance == null) {
         this.initReteInstance();
      }

      return this.reteInstance;
   }

   private ReteInstance buildNewReteInstance() {
      ArrayList items = new ArrayList();
      HashMap valuesByKey = new HashMap();

      for (ObjectTypeNode objectTypeNode : this.objectTypeNodes) {
         items.add((ObjectTypeActivity)objectTypeNode.newActivity(valuesByKey));
      }

      Map groupRetesInstance = this.buildGroupRetesInstance(this.mutexGroupRetesMap);
      Map groupRetesInstance2 = this.buildGroupRetesInstance(this.pendedGroupRetesMap);
      return new ReteInstance(items, groupRetesInstance, groupRetesInstance2, this.allRuleData);
   }

   private Map<String, List<ReteInstanceUnit>> buildGroupRetesInstance(Map<String, List<ReteUnit>> valuesByKey) {
      if (valuesByKey == null) {
         return null;
      }

      HashMap groupRetesInstance = new HashMap();

      for (String text : valuesByKey.keySet()) {
         for (ReteUnit reteUnit : (Iterable<ReteUnit>)(Iterable<?>)((List)valuesByKey.get(text))) {
            List items = (List)groupRetesInstance.get(text);
            if (items == null) {
               items = new ArrayList();
               groupRetesInstance.put(text, items);
            }

            Rete rete = reteUnit.getRete();
            if (rete != null) {
               ReteInstance reteInstance = rete.getReteInstance();
               ReteInstanceUnit reteInstanceUnit = new ReteInstanceUnit(reteInstance, reteUnit.getRuleName());
               reteInstanceUnit.setEffectiveDate(reteUnit.getEffectiveDate());
               reteInstanceUnit.setExpiresDate(reteUnit.getExpiresDate());
               items.add(reteInstanceUnit);
            } else if (reteUnit instanceof MutexReteUnit) {
               MutexReteUnit mutexReteUnit = (MutexReteUnit)reteUnit;
               List list = mutexReteUnit.getList();
               ArrayList items2 = new ArrayList();

               for (ReteUnit reteUnit2 : (Iterable<ReteUnit>)(Iterable<?>)(list)) {
                  Rete rete2 = reteUnit2.getRete();
                  ReteInstance reteInstance2 = rete2.getReteInstance();
                  items2.add(reteInstance2);
               }

               String mutexGroupName = mutexReteUnit.getMutexGroupName();
               MutexReteInstanceUnit mutexReteInstanceUnit = new MutexReteInstanceUnit(mutexGroupName, items2);
               mutexReteInstanceUnit.setEffectiveDate(reteUnit.getEffectiveDate());
               mutexReteInstanceUnit.setExpiresDate(reteUnit.getExpiresDate());
               items.add(mutexReteInstanceUnit);
            }
         }
      }

      return groupRetesInstance;
   }
}
