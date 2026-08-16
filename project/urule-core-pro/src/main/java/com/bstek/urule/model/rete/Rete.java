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

   public Rete(List<ObjectTypeNode> var1, ResourceLibrary var2) {
      this.objectTypeNodes = var1;
      this.resourceLibrary = var2;
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

   public void setMutexGroupRetesMap(Map<String, List<ReteUnit>> var1) {
      this.mutexGroupRetesMap = var1;
   }

   public Map<String, List<ReteUnit>> getPendedGroupRetesMap() {
      return this.pendedGroupRetesMap;
   }

   public void setPendedGroupRetesMap(Map<String, List<ReteUnit>> var1) {
      this.pendedGroupRetesMap = var1;
   }

   public List<RuleData> getAllRuleData() {
      return this.allRuleData;
   }

   public void setAllRuleData(List<RuleData> var1) {
      this.allRuleData = var1;
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
      ArrayList var1 = new ArrayList();
      HashMap var2 = new HashMap();

      for (ObjectTypeNode var4 : this.objectTypeNodes) {
         var1.add((ObjectTypeActivity)var4.newActivity(var2));
      }

      Map var5 = this.buildGroupRetesInstance(this.mutexGroupRetesMap);
      Map var6 = this.buildGroupRetesInstance(this.pendedGroupRetesMap);
      return new ReteInstance(var1, var5, var6, this.allRuleData);
   }

   private Map<String, List<ReteInstanceUnit>> buildGroupRetesInstance(Map<String, List<ReteUnit>> var1) {
      if (var1 == null) {
         return null;
      }

      HashMap var2 = new HashMap();

      for (String var4 : var1.keySet()) {
         for (ReteUnit var7 : (Iterable<ReteUnit>)(Iterable<?>)((List)var1.get(var4))) {
            List var8 = (List)var2.get(var4);
            if (var8 == null) {
               var8 = new ArrayList();
               var2.put(var4, var8);
            }

            Rete var9 = var7.getRete();
            if (var9 != null) {
               ReteInstance var17 = var9.getReteInstance();
               ReteInstanceUnit var18 = new ReteInstanceUnit(var17, var7.getRuleName());
               var18.setEffectiveDate(var7.getEffectiveDate());
               var18.setExpiresDate(var7.getExpiresDate());
               var8.add(var18);
            } else if (var7 instanceof MutexReteUnit) {
               MutexReteUnit var10 = (MutexReteUnit)var7;
               List var11 = var10.getList();
               ArrayList var12 = new ArrayList();

               for (ReteUnit var14 : (Iterable<ReteUnit>)(Iterable<?>)(var11)) {
                  Rete var15 = var14.getRete();
                  ReteInstance var16 = var15.getReteInstance();
                  var12.add(var16);
               }

               String var19 = var10.getMutexGroupName();
               MutexReteInstanceUnit var20 = new MutexReteInstanceUnit(var19, var12);
               var20.setEffectiveDate(var7.getEffectiveDate());
               var20.setExpiresDate(var7.getExpiresDate());
               var8.add(var20);
            }
         }
      }

      return var2;
   }
}
