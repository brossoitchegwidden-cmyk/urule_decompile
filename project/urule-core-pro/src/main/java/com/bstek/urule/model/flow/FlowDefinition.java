package com.bstek.urule.model.flow;

import com.bstek.urule.builder.KnowledgeBase;
import com.bstek.urule.builder.KnowledgeBuilder;
import com.bstek.urule.builder.ResourceBase;
import com.bstek.urule.dsl.DSLRuleSetBuilder;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.flow.ins.FlowContext;
import com.bstek.urule.model.flow.ins.FlowInstance;
import com.bstek.urule.model.flow.ins.ProcessInstance;
import com.bstek.urule.model.library.ResourceLibrary;
import com.bstek.urule.model.rule.Library;
import com.bstek.urule.model.rule.RuleSet;
import com.bstek.urule.runtime.KnowledgePackage;
import com.bstek.urule.runtime.KnowledgePackageImpl;
import com.bstek.urule.runtime.KnowledgePackageWrapper;
import com.bstek.urule.runtime.response.ExecutionResponseImpl;
import com.bstek.urule.runtime.service.KnowledgePackageService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FlowDefinition implements ProcessDefinition {
   private String id;
   private boolean debug;
   private String file;
   private List<Library> libraries;
   private String quickTestData;
   @JsonIgnore
   private StartNode startNode;
   @JsonIgnore
   private List<ResourceLibrary> relationResourceLibraries = new ArrayList<>();
   @JsonDeserialize(using = FlowNodeJsonDeserializer.class)
   private List<FlowNode> nodes;

   public ProcessInstance newInstance(FlowContext context) {
      long longValue = System.currentTimeMillis();
      ExecutionResponseImpl response = (ExecutionResponseImpl)context.getResponse();
      response.setFlowId(this.id);
      if (this.startNode == null) {
         throw new RuleException("StartNode must be define.");
      }

      FlowInstance flowInstance = new FlowInstance(this, this.debug);
      this.startNode.enter(null, context, flowInstance);
      response.setDuration(System.currentTimeMillis() - longValue);
      return flowInstance;
   }

   public void buildConnectionToNode() {
      for (FlowNode flowNode : this.nodes) {
         List connections = flowNode.getConnections();
         if (connections != null && connections.size() != 0) {
            for (Connection connection : (Iterable<Connection>)(Iterable<?>)(connections)) {
               String toName = connection.getToName();
               connection.setTo(this.getFlowNode(toName));
            }
         }
      }
   }

   public void initForActiveVersion() {
      if (this.nodes != null) {
         for (FlowNode flowNode : this.nodes) {
            if (flowNode instanceof BindingNode) {
               BindingNode bindingNode = (BindingNode)flowNode;
               KnowledgePackageWrapper knowledgePackageWrapper = bindingNode.getKnowledgePackageWrapper();
               if (knowledgePackageWrapper != null) {
                  KnowledgePackageImpl knowledgePackage = (KnowledgePackageImpl)knowledgePackageWrapper.getKnowledgePackage();
                  knowledgePackage.initForActiveVersion();
               }
            }
         }
      }
   }

   private FlowNode getFlowNode(String text) {
      for (FlowNode flowNode : this.nodes) {
         if (flowNode.getName().equals(text)) {
            return flowNode;
         }
      }

      throw new RuleException("Flow node [" + text + "] not found.");
   }

   public FlowDefinition newFlowDefinitionForSerialize(KnowledgeBuilder knowledgeBuilder, KnowledgePackageService knowledgePackageService, DSLRuleSetBuilder dslRuleSetBuilder) throws IOException {
      this.initNodeKnowledgePackage(knowledgeBuilder, knowledgePackageService, dslRuleSetBuilder);
      FlowDefinition flowDefinition = new FlowDefinition();
      flowDefinition.setLibraries(this.libraries);
      flowDefinition.setId(this.id);
      flowDefinition.setFile(this.file);
      flowDefinition.setDebug(this.debug);
      flowDefinition.setNodes(this.nodes);

      for (FlowNode flowNode : this.nodes) {
         flowNode.setX(null);
         flowNode.setY(null);
         flowNode.setWidth(null);
         flowNode.setHeight(null);
         if (flowNode instanceof DecisionNode) {
            DecisionNode decisionNode = (DecisionNode)flowNode;

            for (DecisionItem decisionItem : decisionNode.getItems()) {
               decisionItem.setLhs(null);
               decisionItem.setLhsXml(null);
               decisionItem.setScript(null);
            }
         } else if (flowNode instanceof ScriptNode) {
            ScriptNode scriptNode = (ScriptNode)flowNode;
            scriptNode.setActionXml(null);
            scriptNode.setActionsData(null);
         }

         if (flowNode instanceof StartNode) {
            flowDefinition.setStartNode((StartNode)flowNode);
         }
      }

      flowDefinition.setRelationResourceLibraries(this.relationResourceLibraries);
      return flowDefinition;
   }

   private void initNodeKnowledgePackage(KnowledgeBuilder knowledgeBuilder, KnowledgePackageService knowledgePackageService, DSLRuleSetBuilder dSLRuleSetBuilder) throws IOException {
      for (FlowNode flowNode : this.nodes) {
         if (flowNode instanceof RuleNode) {
            ResourceBase resourceBase = knowledgeBuilder.newResourceBase();
            RuleNode ruleNode = (RuleNode)flowNode;

            for (BindingFile bindingFile : ruleNode.getFiles()) {
               resourceBase.addResource(bindingFile.getId(), bindingFile.getVersion());
            }

            KnowledgeBase knowledgeBase = knowledgeBuilder.buildKnowledgeBase(resourceBase);
            KnowledgePackage knowledgePackage = knowledgeBase.getKnowledgePackage();
            if (knowledgeBase.getResourceLibrary() != null) {
               this.relationResourceLibraries.add(knowledgeBase.getResourceLibrary());
            }

            ruleNode.setKnowledgePackageWrapper(new KnowledgePackageWrapper(knowledgePackage));
         } else if (!(flowNode instanceof RulePackageNode)) {
            if (flowNode instanceof DecisionNode) {
               DecisionNode decisionNode = (DecisionNode)flowNode;
               if (decisionNode.getDecisionType().equals(DecisionType.Criteria)) {
                  RuleSet ruleSet = decisionNode.buildRuleSet(this.libraries, this);
                  KnowledgeBase knowledgeBase2 = knowledgeBuilder.buildKnowledgeBase(ruleSet);
                  decisionNode.setKnowledgePackageWrapper(new KnowledgePackageWrapper(knowledgeBase2.getKnowledgePackage()));
               }
            } else if (flowNode instanceof ScriptNode) {
               ScriptNode scriptNode = (ScriptNode)flowNode;
               RuleSet ruleSet2 = scriptNode.buildRuleSet(this.libraries, this);
               KnowledgeBase knowledgeBase3 = knowledgeBuilder.buildKnowledgeBase(ruleSet2);
               scriptNode.setKnowledgePackageWrapper(new KnowledgePackageWrapper(knowledgeBase3.getKnowledgePackage()));
            }
         }
      }
   }

   public List<ResourceLibrary> getRelationResourceLibraries() {
      return this.relationResourceLibraries;
   }

   public void setRelationResourceLibraries(List<ResourceLibrary> relationResourceLibraries) {
      this.relationResourceLibraries = relationResourceLibraries;
   }

   public void addLibrary(Library lib) {
      if (this.libraries == null) {
         this.libraries = new ArrayList<>();
      }

      this.libraries.add(lib);
   }

   @Override
   public List<Library> getLibraries() {
      return this.libraries;
   }

   public void setLibraries(List<Library> libraries) {
      this.libraries = libraries;
   }

   @Override
   public String getId() {
      return this.id;
   }

   public void setId(String id) {
      this.id = id;
   }

   @Override
   public boolean isDebug() {
      return this.debug;
   }

   public void setDebug(boolean debug) {
      this.debug = debug;
   }

   @Override
   public String getFile() {
      return this.file;
   }

   public void setFile(String file) {
      this.file = file;
   }

   public String getQuickTestData() {
      return this.quickTestData;
   }

   public void setQuickTestData(String quickTestData) {
      this.quickTestData = quickTestData;
   }

   @Override
   public List<FlowNode> getNodes() {
      return this.nodes;
   }

   public void setNodes(List<FlowNode> nodes) {
      if (this.startNode == null) {
         for (FlowNode flowNode : nodes) {
            if (flowNode instanceof StartNode) {
               this.setStartNode((StartNode)flowNode);
               break;
            }
         }
      }

      this.nodes = nodes;
   }

   public StartNode getStartNode() {
      return this.startNode;
   }

   public void setStartNode(StartNode startNode) {
      this.startNode = startNode;
   }
}
