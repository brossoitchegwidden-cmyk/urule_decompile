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

   public ProcessInstance newInstance(FlowContext var1) {
      long var2 = System.currentTimeMillis();
      ExecutionResponseImpl var4 = (ExecutionResponseImpl)var1.getResponse();
      var4.setFlowId(this.id);
      if (this.startNode == null) {
         throw new RuleException("StartNode must be define.");
      }

      FlowInstance var5 = new FlowInstance(this, this.debug);
      this.startNode.enter(null, var1, var5);
      var4.setDuration(System.currentTimeMillis() - var2);
      return var5;
   }

   public void buildConnectionToNode() {
      for (FlowNode var2 : this.nodes) {
         List var3 = var2.getConnections();
         if (var3 != null && var3.size() != 0) {
            for (Connection var5 : (Iterable<Connection>)(Iterable<?>)(var3)) {
               String var6 = var5.getToName();
               var5.setTo(this.getFlowNode(var6));
            }
         }
      }
   }

   public void initForActiveVersion() {
      if (this.nodes != null) {
         for (FlowNode var2 : this.nodes) {
            if (var2 instanceof BindingNode) {
               BindingNode var3 = (BindingNode)var2;
               KnowledgePackageWrapper var4 = var3.getKnowledgePackageWrapper();
               if (var4 != null) {
                  KnowledgePackageImpl var5 = (KnowledgePackageImpl)var4.getKnowledgePackage();
                  var5.initForActiveVersion();
               }
            }
         }
      }
   }

   private FlowNode getFlowNode(String var1) {
      for (FlowNode var3 : this.nodes) {
         if (var3.getName().equals(var1)) {
            return var3;
         }
      }

      throw new RuleException("Flow node [" + var1 + "] not found.");
   }

   public FlowDefinition newFlowDefinitionForSerialize(KnowledgeBuilder var1, KnowledgePackageService var2, DSLRuleSetBuilder var3) throws IOException {
      this.initNodeKnowledgePackage(var1, var2, var3);
      FlowDefinition var4 = new FlowDefinition();
      var4.setLibraries(this.libraries);
      var4.setId(this.id);
      var4.setFile(this.file);
      var4.setDebug(this.debug);
      var4.setNodes(this.nodes);

      for (FlowNode var6 : this.nodes) {
         var6.setX(null);
         var6.setY(null);
         var6.setWidth(null);
         var6.setHeight(null);
         if (var6 instanceof DecisionNode) {
            DecisionNode var10 = (DecisionNode)var6;

            for (DecisionItem var9 : var10.getItems()) {
               var9.setLhs(null);
               var9.setLhsXml(null);
               var9.setScript(null);
            }
         } else if (var6 instanceof ScriptNode) {
            ScriptNode var7 = (ScriptNode)var6;
            var7.setActionXml(null);
            var7.setActionsData(null);
         }

         if (var6 instanceof StartNode) {
            var4.setStartNode((StartNode)var6);
         }
      }

      var4.setRelationResourceLibraries(this.relationResourceLibraries);
      return var4;
   }

   private void initNodeKnowledgePackage(KnowledgeBuilder var1, KnowledgePackageService var2, DSLRuleSetBuilder var3) throws IOException {
      for (FlowNode var5 : this.nodes) {
         if (var5 instanceof RuleNode) {
            ResourceBase var12 = var1.newResourceBase();
            RuleNode var14 = (RuleNode)var5;

            for (BindingFile var10 : var14.getFiles()) {
               var12.addResource(var10.getId(), var10.getVersion());
            }

            KnowledgeBase var17 = var1.buildKnowledgeBase(var12);
            KnowledgePackage var18 = var17.getKnowledgePackage();
            if (var17.getResourceLibrary() != null) {
               this.relationResourceLibraries.add(var17.getResourceLibrary());
            }

            var14.setKnowledgePackageWrapper(new KnowledgePackageWrapper(var18));
         } else if (!(var5 instanceof RulePackageNode)) {
            if (var5 instanceof DecisionNode) {
               DecisionNode var6 = (DecisionNode)var5;
               if (var6.getDecisionType().equals(DecisionType.Criteria)) {
                  RuleSet var7 = var6.buildRuleSet(this.libraries, this);
                  KnowledgeBase var8 = var1.buildKnowledgeBase(var7);
                  var6.setKnowledgePackageWrapper(new KnowledgePackageWrapper(var8.getKnowledgePackage()));
               }
            } else if (var5 instanceof ScriptNode) {
               ScriptNode var11 = (ScriptNode)var5;
               RuleSet var13 = var11.buildRuleSet(this.libraries, this);
               KnowledgeBase var15 = var1.buildKnowledgeBase(var13);
               var11.setKnowledgePackageWrapper(new KnowledgePackageWrapper(var15.getKnowledgePackage()));
            }
         }
      }
   }

   public List<ResourceLibrary> getRelationResourceLibraries() {
      return this.relationResourceLibraries;
   }

   public void setRelationResourceLibraries(List<ResourceLibrary> var1) {
      this.relationResourceLibraries = var1;
   }

   public void addLibrary(Library var1) {
      if (this.libraries == null) {
         this.libraries = new ArrayList<>();
      }

      this.libraries.add(var1);
   }

   @Override
   public List<Library> getLibraries() {
      return this.libraries;
   }

   public void setLibraries(List<Library> var1) {
      this.libraries = var1;
   }

   @Override
   public String getId() {
      return this.id;
   }

   public void setId(String var1) {
      this.id = var1;
   }

   @Override
   public boolean isDebug() {
      return this.debug;
   }

   public void setDebug(boolean var1) {
      this.debug = var1;
   }

   @Override
   public String getFile() {
      return this.file;
   }

   public void setFile(String var1) {
      this.file = var1;
   }

   public String getQuickTestData() {
      return this.quickTestData;
   }

   public void setQuickTestData(String var1) {
      this.quickTestData = var1;
   }

   @Override
   public List<FlowNode> getNodes() {
      return this.nodes;
   }

   public void setNodes(List<FlowNode> var1) {
      if (this.startNode == null) {
         for (FlowNode var3 : var1) {
            if (var3 instanceof StartNode) {
               this.setStartNode((StartNode)var3);
               break;
            }
         }
      }

      this.nodes = var1;
   }

   public StartNode getStartNode() {
      return this.startNode;
   }

   public void setStartNode(StartNode var1) {
      this.startNode = var1;
   }
}
