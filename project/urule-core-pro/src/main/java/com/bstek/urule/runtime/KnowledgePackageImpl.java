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
         for (FlowDefinition var2 : this.flowMap.values()) {
            var2.initForActiveVersion();
         }
      }

      if (this.aloneRetes != null) {
         for (Rete var8 : this.aloneRetes) {
            this.initSingleReteForActiveVersion(var8);
         }
      }

      if (this.predefineExecutionUnits != null) {
         for (PredefineExecutionUnit var9 : this.predefineExecutionUnits) {
            PredefineGroup var3 = var9.getGroup();
            KnowledgePackageWrapper var4 = var3.getKnowledgePackageWrapper();
            if (var4 != null) {
               KnowledgePackageImpl var5 = (KnowledgePackageImpl)var4.getKnowledgePackage();
               var5.initForActiveVersion();
            }
         }
      }
   }

   private void initSingleReteForActiveVersion(Rete var1) {
      this.initReteForActiveVersion(var1);
      this.initReteUnitsForActiveVersion(var1.getMutexGroupRetesMap());
      this.initReteUnitsForActiveVersion(var1.getPendedGroupRetesMap());
   }

   private void initReteUnitsForActiveVersion(Map<String, List<ReteUnit>> var1) {
      if (var1 != null) {
         for (List var3 : var1.values()) {
            for (ReteUnit var5 : (Iterable<ReteUnit>)(Iterable<?>)(var3)) {
               if (var5 instanceof MutexReteUnit) {
                  MutexReteUnit var10 = (MutexReteUnit)var5;

                  for (ReteUnit var8 : var10.getList()) {
                     Rete var9 = var8.getRete();
                     if (var9 != null) {
                        this.initReteForActiveVersion(var9);
                     }
                  }
               } else {
                  Rete var6 = var5.getRete();
                  this.initReteForActiveVersion(var6);
               }
            }
         }
      }
   }

   private void initReteForActiveVersion(Rete var1) {
      for (BaseReteNode var4 : var1.getObjectTypeNodes()) {
         this.buildChildrenNodes(var4);
      }

      if (this.flowMap != null) {
         for (FlowDefinition var6 : this.flowMap.values()) {
            var6.initForActiveVersion();
         }
      }
   }

   private void buildChildrenNodes(BaseReteNode var1) {
      List var2 = var1.getLines();
      if (var2 != null) {
         for (Line var4 : (Iterable<Line>)(Iterable<?>)(var2)) {
            Node var5 = var4.getTo();
            if (var5 instanceof ReteNode) {
               var1.getChildrenNodes().add((ReteNode)var5);
            }

            if (var5 instanceof BaseReteNode) {
               BaseReteNode var6 = (BaseReteNode)var5;
               this.buildChildrenNodes(var6);
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

   public void setRete(Rete var1) {
      this.rete = var1;
   }

   @Override
   public List<Rete> getAloneRetes() {
      return this.aloneRetes;
   }

   public void setAloneRetes(List<Rete> var1) {
      this.aloneRetes = var1;
   }

   @Override
   public long getTimestamp() {
      return this.timestamp;
   }

   public void setTimestamp(long var1) {
      this.timestamp = var1;
   }

   @Override
   public void resetTimestamp() {
      this.timestamp = System.currentTimeMillis();
   }

   @Override
   public String getPackageInfo() {
      return this.packageInfo;
   }

   public void setPackageInfo(String var1) {
      this.packageInfo = var1;
   }

   public void setMonitor(boolean var1) {
      this.monitor = var1;
   }

   @Override
   public boolean isMonitor() {
      return this.monitor;
   }

   @Override
   public List<MonitorObject> getInputData() {
      return this.inputData;
   }

   public void setInputData(List<MonitorObject> var1) {
      this.inputData = var1;
   }

   @Override
   public List<MonitorObject> getOutputData() {
      return this.outputData;
   }

   public void setOutputData(List<MonitorObject> var1) {
      this.outputData = var1;
   }

   @Override
   public String getVersion() {
      return this.version;
   }

   public void setVersion(String var1) {
      this.version = var1;
   }

   @Override
   public String getVersionComment() {
      return this.versionComment;
   }

   public void setVersionComment(String var1) {
      this.versionComment = var1;
   }

   @Override
   public Date getVersionCreateDate() {
      return this.versionCreateDate;
   }

   public void setVersionCreateDate(Date var1) {
      this.versionCreateDate = var1;
   }

   @Override
   public String getVersionCreateUser() {
      return this.versionCreateUser;
   }

   public void setVersionCreateUser(String var1) {
      this.versionCreateUser = var1;
   }

   @Override
   public Map<String, String> getVariableCateogoryMap() {
      return this.variableCategoryMap;
   }

   public void setVariableCategoryMap(Map<String, String> var1) {
      this.variableCategoryMap = var1;
   }

   @Override
   public Map<String, FlowDefinition> getFlowMap() {
      return this.flowMap;
   }

   public void setFlowMap(Map<String, FlowDefinition> var1) {
      this.flowMap = var1;
   }

   private void initAloneReteInstances() {
      this.aloneReteInstances = new ArrayList<>();
      if (this.aloneRetes != null) {
         for (Rete var2 : this.aloneRetes) {
            this.aloneReteInstances.add(var2.getReteInstance());
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

   public void setParameters(Map<String, String> var1) {
      this.parameters = var1;
   }

   public void setVariableCategories(List<VariableCategory> var1) {
      this.variableCategories = var1;
   }

   @Override
   public List<VariableCategory> getVariableCategories() {
      return this.variableCategories;
   }

   @Override
   public VariableCategory getVariableCategoryWithDefaultValue(String var1) {
      return this.variableCategoryWithDefaultValueClassMap.get(var1);
   }

   public void setVariableCategoryWithDefaultValueClassMap(Map<String, VariableCategory> var1) {
      this.variableCategoryWithDefaultValueClassMap = var1;
   }

   @Override
   public List<PredefineExecutionUnit> getPredefineExecutionUnits() {
      return this.predefineExecutionUnits;
   }

   public void setPredefineExecutionUnits(List<PredefineExecutionUnit> var1) {
      this.predefineExecutionUnits = var1;
   }

   @Override
   public Map<String, String> getParameters() {
      return this.parameters;
   }
}
