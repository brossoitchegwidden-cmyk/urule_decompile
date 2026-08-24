package com.bstek.urule.runtime.monitor;

import com.bstek.urule.Utils;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.runtime.FactManager;
import com.bstek.urule.runtime.KnowledgePackage;
import com.bstek.urule.runtime.KnowledgeSession;
import com.bstek.urule.runtime.log.LogManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonitorManager {
   private LogManager logManager;
   private long totalDuration;
   private FactManager factManager;
   private Collection<InvokeMonitor> invokeMonitors;
   private Map<String, MonitorDataImpl> monitorDataByPackageId = new HashMap<>();
   private List<KnowledgePackage> knowledgePackageList;

   public MonitorManager(KnowledgeSession knowledgeSession) {
      this.logManager = knowledgeSession.getLogManager();
      this.factManager = knowledgeSession.getFactManager();
      this.knowledgePackageList = knowledgeSession.getKnowledgePackageList();
      this.invokeMonitors = Utils.getApplicationContext().getBeansOfType(InvokeMonitor.class).values();
   }

   public void doMonitorInputData(Map<String, Object> sessionParameters) {
      if (this.invokeMonitors.size() != 0) {
         for (KnowledgePackage knowledgePackage : this.knowledgePackageList) {
            if (knowledgePackage.isMonitor()) {
               MonitorDataImpl monitorDataImpl = new MonitorDataImpl();
               monitorDataImpl.setPackageInfo(knowledgePackage.getPackageInfo());
               List inputData = knowledgePackage.getInputData();
               List items = this.buildIoData(inputData, knowledgePackage.getVariableCateogoryMap(), sessionParameters, true);
               monitorDataImpl.setInputData(items);
               this.monitorDataByPackageId.put(knowledgePackage.getId(), monitorDataImpl);
            }
         }
      }
   }

   public void doMonitor(Map<String, Object> sessionParameters) {
      if (this.invokeMonitors.size() != 0) {
         for (KnowledgePackage knowledgePackage : this.knowledgePackageList) {
            if (knowledgePackage.isMonitor()) {
               MonitorDataImpl monitorDataImpl = this.monitorDataByPackageId.get(knowledgePackage.getId());
               monitorDataImpl.addMatchedRuleList(this.logManager.buildMatchedRuleLog());
               monitorDataImpl.addNotMatchRuleList(this.logManager.buildNotMatchRuleData());
               monitorDataImpl.addLogs(this.logManager.getLogger().getLogs());
               monitorDataImpl.setVersion(knowledgePackage.getVersion());
               List outputData = knowledgePackage.getOutputData();
               List items = this.buildIoData(outputData, knowledgePackage.getVariableCateogoryMap(), sessionParameters, false);
               monitorDataImpl.setOutputData(items);
               monitorDataImpl.setTotalDuration(this.totalDuration);

               for (InvokeMonitor invokeMonitor : this.invokeMonitors) {
                  invokeMonitor.doMonitor(monitorDataImpl);
               }
            }
         }

         this.monitorDataByPackageId.clear();
      }
   }

   private List<IOData> buildIoData(List<MonitorObject> monitorObjects, Map<String, String> valuesByKey, Map<String, Object> valuesByKey2, boolean flag) {
      ArrayList items = new ArrayList();
      Map factMap = this.factManager.getFactMap();

      for (MonitorObject monitorObject : monitorObjects) {
         Object objectValue = null;
         String name2 = null;
         if (monitorObject.getName().equals("参数")) {
            objectValue = valuesByKey2;
            name2 = HashMap.class.getName();
         } else {
            name2 = (String)valuesByKey.get(monitorObject.getName());
            if (name2 == null) {
               if (flag) {
                  throw new RuleException("构建监控输入数据时，对象[" + monitorObject.getName() + "]不存在！");
               }

               throw new RuleException("构建监控输出数据时，对象[" + monitorObject.getName() + "]不存在！");
            }

            objectValue = factMap.get(name2);
            if (objectValue == null) {
               if (flag) {
                  throw new RuleException("构建监控输入数据时，对象[" + name2 + "]不存在！");
               }

               throw new RuleException("构建监控输出数据时，对象[" + name2 + "]不存在！");
            }
         }

         IOData iOData = new IOData();
         items.add(iOData);
         iOData.setName(monitorObject.getName());
         iOData.setClazz(name2);
         ArrayList items2 = new ArrayList();
         iOData.setFields(items2);

         for (MonitorObjectField monitorObjectField : monitorObject.getFields()) {
            IODataField iODataField = new IODataField();
            String name = monitorObjectField.getName();
            Object objectProperty = Utils.getObjectProperty(objectValue, name);
            iODataField.setName(name);
            iODataField.setLabel(monitorObjectField.getLabel());
            iODataField.setValue(objectProperty);
            items2.add(iODataField);
         }
      }

      return items;
   }

   public void setTotalDuration(long totalDuration) {
      this.totalDuration = totalDuration;
   }
}
