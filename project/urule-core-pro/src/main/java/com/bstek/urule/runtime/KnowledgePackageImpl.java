package com.bstek.urule.runtime;

import com.bstek.urule.model.Node;
import com.bstek.urule.model.flow.FlowDefinition;
import com.bstek.urule.model.library.variable.VariableCategory;
import com.bstek.urule.model.rete.BaseReteNode;
import com.bstek.urule.model.rete.Line;
import com.bstek.urule.model.rete.MutexReteUnit;
import com.bstek.urule.model.rete.Rete;
import com.bstek.urule.model.rete.ReteNode;
import com.bstek.urule.model.rete.ReteUnit;
import com.bstek.urule.model.rule.PredefineExecutionUnit;
import com.bstek.urule.model.rule.PredefineGroup;
import com.bstek.urule.runtime.monitor.MonitorObject;
import com.bstek.urule.runtime.rete.ReteInstance;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class KnowledgePackageImpl implements KnowledgePackage {
   private String packageInfo;
   private boolean monitor;
   private List<MonitorObject> inputData;
   private List<MonitorObject> outputData;
   private String version;
   private String versionComment;
   private Date versionCreateDate;
   private String versionCreateUser;
   private Rete rete;
   private List<Rete> aloneRetes;
   @JsonIgnore
   private List<ReteInstance> aloneReteInstances;
   private List<PredefineExecutionUnit> predefineExecutionUnits;
   private Map<String, String> variableCategoryMap = new HashMap<>();
   @JsonIgnore
   private Map<String, VariableCategory> variableCategoryWithDefaultValueClassMap = new HashMap<>();
   private Map<String, FlowDefinition> flowMap;
   private Map<String, String> parameters;
   private long timestamp;
   private String id = UUID.randomUUID().toString();
   private List<VariableCategory> variableCategories;

   public KnowledgePackageImpl() {
      this.timestamp = System.currentTimeMillis();
   }

   public void initForActiveVersion() {
      this.initSingleReteForActiveVersion(this.rete);
      if (this.flowMap != null) {
         for (FlowDefinition flowDefinition : this.flowMap.values()) {
            flowDefinition.initForActiveVersion();
         }
      }

      if (this.aloneRetes != null) {
         for (Rete rete : this.aloneRetes) {
            this.initSingleReteForActiveVersion(rete);
         }
      }

      if (this.predefineExecutionUnits != null) {
         for (PredefineExecutionUnit predefineExecutionUnit : this.predefineExecutionUnits) {
            PredefineGroup group = predefineExecutionUnit.getGroup();
            KnowledgePackageWrapper knowledgePackageWrapper = group.getKnowledgePackageWrapper();
            if (knowledgePackageWrapper != null) {
               KnowledgePackageImpl knowledgePackage = (KnowledgePackageImpl)knowledgePackageWrapper.getKnowledgePackage();
               knowledgePackage.initForActiveVersion();
            }
         }
      }
   }

   private void initSingleReteForActiveVersion(Rete rete) {
      this.initReteForActiveVersion(rete);
      this.initReteUnitsForActiveVersion(rete.getMutexGroupRetesMap());
      this.initReteUnitsForActiveVersion(rete.getPendedGroupRetesMap());
   }

   private void initReteUnitsForActiveVersion(Map<String, List<ReteUnit>> valuesByKey) {
      if (valuesByKey != null) {
         for (List items : valuesByKey.values()) {
            for (ReteUnit reteUnit : (Iterable<ReteUnit>)(Iterable<?>)(items)) {
               if (reteUnit instanceof MutexReteUnit) {
                  MutexReteUnit mutexReteUnit = (MutexReteUnit)reteUnit;

                  for (ReteUnit reteUnit2 : mutexReteUnit.getList()) {
                     Rete rete = reteUnit2.getRete();
                     if (rete != null) {
                        this.initReteForActiveVersion(rete);
                     }
                  }
               } else {
                  Rete rete2 = reteUnit.getRete();
                  this.initReteForActiveVersion(rete2);
               }
            }
         }
      }
   }

   private void initReteForActiveVersion(Rete rete) {
      for (BaseReteNode baseReteNode : rete.getObjectTypeNodes()) {
         this.buildChildrenNodes(baseReteNode);
      }

      if (this.flowMap != null) {
         for (FlowDefinition flowDefinition : this.flowMap.values()) {
            flowDefinition.initForActiveVersion();
         }
      }
   }

   private void buildChildrenNodes(BaseReteNode baseReteNode) {
      List lines = baseReteNode.getLines();
      if (lines != null) {
         for (Line line : (Iterable<Line>)(Iterable<?>)(lines)) {
            Node to = line.getTo();
            if (to instanceof ReteNode) {
               baseReteNode.getChildrenNodes().add((ReteNode)to);
            }

            if (to instanceof BaseReteNode) {
               BaseReteNode baseReteNode2 = (BaseReteNode)to;
               this.buildChildrenNodes(baseReteNode2);
            }
         }
      }
   }

   @Override
   public String getId() {
      return this.id;
   }

   @Override
   public Rete getRete() {
      return this.rete;
   }

   public void setRete(Rete rete) {
      this.rete = rete;
   }

   @Override
   public List<Rete> getAloneRetes() {
      return this.aloneRetes;
   }

   public void setAloneRetes(List<Rete> aloneRetes) {
      this.aloneRetes = aloneRetes;
   }

   @Override
   public long getTimestamp() {
      return this.timestamp;
   }

   public void setTimestamp(long timestamp) {
      this.timestamp = timestamp;
   }

   @Override
   public void resetTimestamp() {
      this.timestamp = System.currentTimeMillis();
   }

   @Override
   public String getPackageInfo() {
      return this.packageInfo;
   }

   public void setPackageInfo(String packageInfo) {
      this.packageInfo = packageInfo;
   }

   public void setMonitor(boolean monitor) {
      this.monitor = monitor;
   }

   @Override
   public boolean isMonitor() {
      return this.monitor;
   }

   @Override
   public List<MonitorObject> getInputData() {
      return this.inputData;
   }

   public void setInputData(List<MonitorObject> inputData) {
      this.inputData = inputData;
   }

   @Override
   public List<MonitorObject> getOutputData() {
      return this.outputData;
   }

   public void setOutputData(List<MonitorObject> outputData) {
      this.outputData = outputData;
   }

   @Override
   public String getVersion() {
      return this.version;
   }

   public void setVersion(String version) {
      this.version = version;
   }

   @Override
   public String getVersionComment() {
      return this.versionComment;
   }

   public void setVersionComment(String versionComment) {
      this.versionComment = versionComment;
   }

   @Override
   public Date getVersionCreateDate() {
      return this.versionCreateDate;
   }

   public void setVersionCreateDate(Date versionCreateDate) {
      this.versionCreateDate = versionCreateDate;
   }

   @Override
   public String getVersionCreateUser() {
      return this.versionCreateUser;
   }

   public void setVersionCreateUser(String versionCreateUser) {
      this.versionCreateUser = versionCreateUser;
   }

   @Override
   public Map<String, String> getVariableCateogoryMap() {
      return this.variableCategoryMap;
   }

   public void setVariableCategoryMap(Map<String, String> variableCategoryMap) {
      this.variableCategoryMap = variableCategoryMap;
   }

   @Override
   public Map<String, FlowDefinition> getFlowMap() {
      return this.flowMap;
   }

   public void setFlowMap(Map<String, FlowDefinition> flowMap) {
      this.flowMap = flowMap;
   }

   private void initAloneReteInstances() {
      this.aloneReteInstances = new ArrayList<>();
      if (this.aloneRetes != null) {
         for (Rete rete : this.aloneRetes) {
            this.aloneReteInstances.add(rete.getReteInstance());
         }
      }
   }

   @Override
   public ReteInstance loadReteInstance() {
      return this.rete.getReteInstance();
   }

   @Override
   public List<ReteInstance> getAloneReteInstances() {
      if (this.aloneReteInstances == null) {
         this.initAloneReteInstances();
      }

      return this.aloneReteInstances;
   }

   public void setParameters(Map<String, String> parameters) {
      this.parameters = parameters;
   }

   public void setVariableCategories(List<VariableCategory> variableCategories) {
      this.variableCategories = variableCategories;
   }

   @Override
   public List<VariableCategory> getVariableCategories() {
      return this.variableCategories;
   }

   @Override
   public VariableCategory getVariableCategoryWithDefaultValue(String clazz) {
      return this.variableCategoryWithDefaultValueClassMap.get(clazz);
   }

   public void setVariableCategoryWithDefaultValueClassMap(Map<String, VariableCategory> variableCategoryClassMap) {
      this.variableCategoryWithDefaultValueClassMap = variableCategoryClassMap;
   }

   @Override
   public List<PredefineExecutionUnit> getPredefineExecutionUnits() {
      return this.predefineExecutionUnits;
   }

   public void setPredefineExecutionUnits(List<PredefineExecutionUnit> predefineExecutionUnits) {
      this.predefineExecutionUnits = predefineExecutionUnits;
   }

   @Override
   public Map<String, String> getParameters() {
      return this.parameters;
   }
}
