package com.bstek.urule.runtime;

import com.bstek.urule.model.flow.FlowDefinition;
import com.bstek.urule.model.library.variable.VariableCategory;
import com.bstek.urule.model.rete.Rete;
import com.bstek.urule.model.rule.PredefineExecutionUnit;
import com.bstek.urule.runtime.monitor.MonitorObject;
import com.bstek.urule.runtime.rete.ReteInstance;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface KnowledgePackage {
   Rete getRete();

   List<Rete> getAloneRetes();

   String getPackageInfo();

   boolean isMonitor();

   String getVersion();

   String getVersionComment();

   Date getVersionCreateDate();

   String getVersionCreateUser();

   List<MonitorObject> getInputData();

   List<MonitorObject> getOutputData();

   Map<String, String> getVariableCateogoryMap();

   List<VariableCategory> getVariableCategories();

   VariableCategory getVariableCategoryWithDefaultValue(String clazz);

   Map<String, FlowDefinition> getFlowMap();

   Map<String, String> getParameters();

   ReteInstance loadReteInstance();

   List<ReteInstance> getAloneReteInstances();

   List<PredefineExecutionUnit> getPredefineExecutionUnits();

   long getTimestamp();

   void resetTimestamp();

   String getId();
}
