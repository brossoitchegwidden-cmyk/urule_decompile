package com.bstek.urule.runtime;

import com.bstek.urule.builder.resource.ResourceType;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.flow.FlowDefinition;
import com.bstek.urule.model.library.variable.VariableCategory;
import com.bstek.urule.model.rete.BaseReteNode;
import com.bstek.urule.model.rete.Line;
import com.bstek.urule.model.rete.MutexReteUnit;
import com.bstek.urule.model.rete.ObjectTypeNode;
import com.bstek.urule.model.rete.Rete;
import com.bstek.urule.model.rete.ReteNode;
import com.bstek.urule.model.rete.ReteNodeJsonDeserializer;
import com.bstek.urule.model.rete.ReteUnit;
import com.bstek.urule.model.rete.RuleData;
import com.bstek.urule.model.rete.TerminalNode;
import com.bstek.urule.model.rule.PredefineExecutionUnit;
import com.bstek.urule.model.rule.PredefineGroup;
import com.bstek.urule.model.rule.Rule;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class KnowledgePackageWrapper {
   @JsonDeserialize(as = KnowledgePackageImpl.class)
   private KnowledgePackage knowledgePackage;
   @JsonDeserialize(using = ReteNodeJsonDeserializer.class)
   private List<ReteNode> allNodes = new ArrayList<>();
   private Map<Integer, ReteNode> allNodeMap = new HashMap<>();
   private String id;

   public KnowledgePackageWrapper() {
      this.id = UUID.randomUUID().toString();
   }

   public KnowledgePackageWrapper(KnowledgePackage var1) {
      this.knowledgePackage = var1;
      this.id = UUID.randomUUID().toString();
      this.initNodes();
   }

   private void initNodes() {
      Rete var1 = this.knowledgePackage.getRete();
      this.initReteNodes(var1);
      List var2 = this.knowledgePackage.getAloneRetes();
      if (var2 != null) {
         for (Rete var4 : (Iterable<Rete>)(Iterable<?>)(var2)) {
            this.initReteNodes(var4);
         }
      }
   }

   private void initReteNodes(Rete var1) {
      List var2 = var1.getObjectTypeNodes();
      ArrayList var3 = new ArrayList();
      var3.addAll(var2);
      this.buildChildrenNodes(var3, var1.getMutexGroupRetesMap());
      this.buildChildrenNodes(var3, var1.getPendedGroupRetesMap());
      this.queryReteNodes(var3);
      this.initAllRuleData(var1);
      var1.initReteInstance();
   }

   private void initAllRuleData(Rete var1) {
      ArrayList var2 = new ArrayList();
      var1.setAllRuleData(var2);

      for (ReteNode var4 : this.allNodes) {
         if (var4 instanceof TerminalNode) {
            TerminalNode var5 = (TerminalNode)var4;
            Rule var6 = var5.getRule();
            if (!var6.isTargetResource(ResourceType.Flow)) {
               var2.add(new RuleData(var6));
            }
         }
      }
   }

   private void buildChildrenNodes(List<ReteNode> var1, Map<String, List<ReteUnit>> var2) {
      if (var2 != null) {
         for (List var4 : var2.values()) {
            for (ReteUnit var6 : (Iterable<ReteUnit>)(Iterable<?>)(var4)) {
               if (var6.getRete() != null) {
                  var1.addAll(var6.getRete().getObjectTypeNodes());
               } else if (var6 instanceof MutexReteUnit) {
                  MutexReteUnit var7 = (MutexReteUnit)var6;

                  for (ReteUnit var10 : var7.getList()) {
                     if (var10.getRete() != null) {
                        var1.addAll(var10.getRete().getObjectTypeNodes());
                     }
                  }
               }
            }
         }
      }
   }

   private void queryReteNodes(List<ReteNode> var1) {
      if (var1 != null) {
         for (ReteNode var3 : var1) {
            if (!this.allNodes.contains(var3) && !(var3 instanceof ObjectTypeNode)) {
               this.allNodes.add(var3);
               this.allNodeMap.put(var3.getId(), var3);
            }

            if (var3 instanceof BaseReteNode) {
               BaseReteNode var4 = (BaseReteNode)var3;
               this.queryReteNodes(var4.getChildrenNodes());
            }
         }
      }
   }

   public void buildDeserialize() {
      this.buildPredefineExecuteUnits();
      Rete var1 = this.knowledgePackage.getRete();

      for (ReteNode var3 : this.allNodes) {
         if (!this.allNodeMap.containsKey(var3.getId())) {
            this.allNodeMap.put(var3.getId(), var3);
         }
      }

      this.buildDeserialize(var1);
      List var7 = this.knowledgePackage.getAloneRetes();
      if (var7 != null) {
         for (Rete var4 : (Iterable<Rete>)(Iterable<?>)(var7)) {
            this.buildDeserialize(var4);
         }
      }

      HashMap var9 = new HashMap();
      List var10 = this.knowledgePackage.getVariableCategories();
      if (var10 != null) {
         for (VariableCategory var6 : (Iterable<VariableCategory>)(Iterable<?>)(var10)) {
            var9.put(var6.getClazz(), var6.newVariableCategoryWithDefaultValue());
         }
      }

      ((KnowledgePackageImpl)this.knowledgePackage).setVariableCategoryWithDefaultValueClassMap(var9);
   }

   private void buildPredefineExecuteUnits() {
      List var1 = this.knowledgePackage.getPredefineExecutionUnits();
      if (var1 != null) {
         for (PredefineExecutionUnit var3 : (Iterable<PredefineExecutionUnit>)(Iterable<?>)(var1)) {
            PredefineGroup var4 = var3.getGroup();
            KnowledgePackageWrapper var5 = var4.getKnowledgePackageWrapper();
            if (var5 != null) {
               var5.buildDeserialize();
            }
         }
      }
   }

   private void buildDeserialize(Rete var1) {
      this.rebuildReteLine(var1);
      this.buildRetesMap(var1.getMutexGroupRetesMap());
      this.buildRetesMap(var1.getPendedGroupRetesMap());
      Map var2 = this.knowledgePackage.getFlowMap();
      if (var2 != null && var2.size() > 0) {
         for (FlowDefinition var4 : (Iterable<FlowDefinition>)(Iterable<?>)(var2.values())) {
            var4.buildConnectionToNode();
         }
      }

      this.initAllRuleData(var1);
      var1.initReteInstance();
   }

   private void buildRetesMap(Map<String, List<ReteUnit>> var1) {
      if (var1 != null) {
         for (List var3 : var1.values()) {
            for (ReteUnit var5 : (Iterable<ReteUnit>)(Iterable<?>)(var3)) {
               if (var5 instanceof MutexReteUnit) {
                  MutexReteUnit var11 = (MutexReteUnit)var5;

                  for (ReteUnit var9 : var11.getList()) {
                     Rete var10 = var9.getRete();
                     if (var10 != null) {
                        this.rebuildReteLine(var10);
                     }
                  }
               } else {
                  Rete var6 = var5.getRete();
                  if (var6 != null) {
                     this.rebuildReteLine(var6);
                  }
               }
            }
         }
      }
   }

   private void rebuildReteLine(Rete var1) {
      for (ObjectTypeNode var4 : var1.getObjectTypeNodes()) {
         List var5 = var4.getLines();

         for (Line var7 : (Iterable<Line>)(Iterable<?>)(var5)) {
            var7.setFrom(var4);
         }

         this.rebuildLine(var5, this.allNodes);
      }
   }

   private void rebuildLine(List<Line> var1, List<ReteNode> var2) {
      if (var1 != null) {
         for (Line var4 : var1) {
            if (var4.getFrom() == null) {
               int var5 = var4.getFromNodeId();
               ReteNode var6 = this.findTargetNode(var2, var5);
               var4.setFrom(var6);
               if (var6 instanceof BaseReteNode) {
                  BaseReteNode var7 = (BaseReteNode)var6;
                  this.rebuildLine(var7.getLines(), var2);
               }
            }

            if (var4.getTo() == null) {
               int var8 = var4.getToNodeId();
               ReteNode var9 = this.findTargetNode(var2, var8);
               var4.setTo(var9);
               if (var9 instanceof BaseReteNode) {
                  BaseReteNode var10 = (BaseReteNode)var9;
                  this.rebuildLine(var10.getLines(), var2);
               }
            }
         }
      }
   }

   private ReteNode findTargetNode(List<ReteNode> var1, int var2) {
      if (this.allNodeMap.containsKey(var2)) {
         return this.allNodeMap.get(var2);
      } else {
         throw new RuleException("Node[" + var2 + "] not exist.");
      }
   }

   public List<ReteNode> getAllNodes() {
      return this.allNodes;
   }

   public KnowledgePackage getKnowledgePackage() {
      return this.knowledgePackage;
   }

   public String getId() {
      return this.id;
   }
}
