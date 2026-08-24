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

   public KnowledgePackageWrapper(KnowledgePackage knowledgePackage) {
      this.knowledgePackage = knowledgePackage;
      this.id = UUID.randomUUID().toString();
      this.initNodes();
   }

   private void initNodes() {
      Rete rete = this.knowledgePackage.getRete();
      this.initReteNodes(rete);
      List aloneRetes = this.knowledgePackage.getAloneRetes();
      if (aloneRetes != null) {
         for (Rete rete2 : (Iterable<Rete>)(Iterable<?>)(aloneRetes)) {
            this.initReteNodes(rete2);
         }
      }
   }

   private void initReteNodes(Rete rete) {
      List objectTypeNodes = rete.getObjectTypeNodes();
      ArrayList items = new ArrayList();
      items.addAll(objectTypeNodes);
      this.buildChildrenNodes(items, rete.getMutexGroupRetesMap());
      this.buildChildrenNodes(items, rete.getPendedGroupRetesMap());
      this.queryReteNodes(items);
      this.initAllRuleData(rete);
      rete.initReteInstance();
   }

   private void initAllRuleData(Rete rete) {
      ArrayList items = new ArrayList();
      rete.setAllRuleData(items);

      for (ReteNode reteNode : this.allNodes) {
         if (reteNode instanceof TerminalNode) {
            TerminalNode terminalNode = (TerminalNode)reteNode;
            Rule rule = terminalNode.getRule();
            if (!rule.isTargetResource(ResourceType.Flow)) {
               items.add(new RuleData(rule));
            }
         }
      }
   }

   private void buildChildrenNodes(List<ReteNode> reteNodes, Map<String, List<ReteUnit>> valuesByKey) {
      if (valuesByKey != null) {
         for (List items : valuesByKey.values()) {
            for (ReteUnit reteUnit : (Iterable<ReteUnit>)(Iterable<?>)(items)) {
               if (reteUnit.getRete() != null) {
                  reteNodes.addAll(reteUnit.getRete().getObjectTypeNodes());
               } else if (reteUnit instanceof MutexReteUnit) {
                  MutexReteUnit mutexReteUnit = (MutexReteUnit)reteUnit;

                  for (ReteUnit reteUnit2 : mutexReteUnit.getList()) {
                     if (reteUnit2.getRete() != null) {
                        reteNodes.addAll(reteUnit2.getRete().getObjectTypeNodes());
                     }
                  }
               }
            }
         }
      }
   }

   private void queryReteNodes(List<ReteNode> reteNodes) {
      if (reteNodes != null) {
         for (ReteNode reteNode : reteNodes) {
            if (!this.allNodes.contains(reteNode) && !(reteNode instanceof ObjectTypeNode)) {
               this.allNodes.add(reteNode);
               this.allNodeMap.put(reteNode.getId(), reteNode);
            }

            if (reteNode instanceof BaseReteNode) {
               BaseReteNode baseReteNode = (BaseReteNode)reteNode;
               this.queryReteNodes(baseReteNode.getChildrenNodes());
            }
         }
      }
   }

   public void buildDeserialize() {
      this.buildPredefineExecuteUnits();
      Rete rete = this.knowledgePackage.getRete();

      for (ReteNode reteNode : this.allNodes) {
         if (!this.allNodeMap.containsKey(reteNode.getId())) {
            this.allNodeMap.put(reteNode.getId(), reteNode);
         }
      }

      this.buildDeserialize(rete);
      List aloneRetes = this.knowledgePackage.getAloneRetes();
      if (aloneRetes != null) {
         for (Rete rete2 : (Iterable<Rete>)(Iterable<?>)(aloneRetes)) {
            this.buildDeserialize(rete2);
         }
      }

      HashMap valuesByKey = new HashMap();
      List variableCategories = this.knowledgePackage.getVariableCategories();
      if (variableCategories != null) {
         for (VariableCategory variableCategory : (Iterable<VariableCategory>)(Iterable<?>)(variableCategories)) {
            valuesByKey.put(variableCategory.getClazz(), variableCategory.newVariableCategoryWithDefaultValue());
         }
      }

      ((KnowledgePackageImpl)this.knowledgePackage).setVariableCategoryWithDefaultValueClassMap(valuesByKey);
   }

   private void buildPredefineExecuteUnits() {
      List predefineExecutionUnits = this.knowledgePackage.getPredefineExecutionUnits();
      if (predefineExecutionUnits != null) {
         for (PredefineExecutionUnit predefineExecutionUnit : (Iterable<PredefineExecutionUnit>)(Iterable<?>)(predefineExecutionUnits)) {
            PredefineGroup group = predefineExecutionUnit.getGroup();
            KnowledgePackageWrapper knowledgePackageWrapper = group.getKnowledgePackageWrapper();
            if (knowledgePackageWrapper != null) {
               knowledgePackageWrapper.buildDeserialize();
            }
         }
      }
   }

   private void buildDeserialize(Rete rete) {
      this.rebuildReteLine(rete);
      this.buildRetesMap(rete.getMutexGroupRetesMap());
      this.buildRetesMap(rete.getPendedGroupRetesMap());
      Map flowMap = this.knowledgePackage.getFlowMap();
      if (flowMap != null && flowMap.size() > 0) {
         for (FlowDefinition flowDefinition : (Iterable<FlowDefinition>)(Iterable<?>)(flowMap.values())) {
            flowDefinition.buildConnectionToNode();
         }
      }

      this.initAllRuleData(rete);
      rete.initReteInstance();
   }

   private void buildRetesMap(Map<String, List<ReteUnit>> valuesByKey) {
      if (valuesByKey != null) {
         for (List items : valuesByKey.values()) {
            for (ReteUnit reteUnit : (Iterable<ReteUnit>)(Iterable<?>)(items)) {
               if (reteUnit instanceof MutexReteUnit) {
                  MutexReteUnit mutexReteUnit = (MutexReteUnit)reteUnit;

                  for (ReteUnit reteUnit2 : mutexReteUnit.getList()) {
                     Rete rete = reteUnit2.getRete();
                     if (rete != null) {
                        this.rebuildReteLine(rete);
                     }
                  }
               } else {
                  Rete rete2 = reteUnit.getRete();
                  if (rete2 != null) {
                     this.rebuildReteLine(rete2);
                  }
               }
            }
         }
      }
   }

   private void rebuildReteLine(Rete rete) {
      for (ObjectTypeNode objectTypeNode : rete.getObjectTypeNodes()) {
         List lines = objectTypeNode.getLines();

         for (Line line : (Iterable<Line>)(Iterable<?>)(lines)) {
            line.setFrom(objectTypeNode);
         }

         this.rebuildLine(lines, this.allNodes);
      }
   }

   private void rebuildLine(List<Line> lines, List<ReteNode> reteNodes) {
      if (lines != null) {
         for (Line line : lines) {
            if (line.getFrom() == null) {
               int fromNodeId = line.getFromNodeId();
               ReteNode targetNode = this.findTargetNode(reteNodes, fromNodeId);
               line.setFrom(targetNode);
               if (targetNode instanceof BaseReteNode) {
                  BaseReteNode baseReteNode2 = (BaseReteNode)targetNode;
                  this.rebuildLine(baseReteNode2.getLines(), reteNodes);
               }
            }

            if (line.getTo() == null) {
               int toNodeId = line.getToNodeId();
               ReteNode targetNode2 = this.findTargetNode(reteNodes, toNodeId);
               line.setTo(targetNode2);
               if (targetNode2 instanceof BaseReteNode) {
                  BaseReteNode baseReteNode = (BaseReteNode)targetNode2;
                  this.rebuildLine(baseReteNode.getLines(), reteNodes);
               }
            }
         }
      }
   }

   private ReteNode findTargetNode(List<ReteNode> reteNodes, int number) {
      if (this.allNodeMap.containsKey(number)) {
         return this.allNodeMap.get(number);
      } else {
         throw new RuleException("Node[" + number + "] not exist.");
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
