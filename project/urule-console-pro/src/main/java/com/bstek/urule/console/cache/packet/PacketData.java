package com.bstek.urule.console.cache.packet;

import com.bstek.urule.console.database.model.Packet;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.runtime.KnowledgePackageImpl;
import com.bstek.urule.runtime.KnowledgePackageWrapper;
import com.bstek.urule.runtime.monitor.MonitorObject;
import com.bstek.urule.runtime.monitor.MonitorObjectField;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.util.StringUtils;

public class PacketData {
   private PacketConfig packet;
   private KnowledgePackageWrapper knowledgePackageWrapper;

   public PacketData(Packet pk, KnowledgePackageWrapper knowledgePackageWrapper) {
      this.packet = this.buildPacketConfig(pk);
      this.knowledgePackageWrapper = knowledgePackageWrapper;
      KnowledgePackageImpl knowledgePackage = (KnowledgePackageImpl)knowledgePackageWrapper.getKnowledgePackage();
      knowledgePackage.setInputData(this.packet.getAuditInput());
      knowledgePackage.setOutputData(this.packet.getAuditOutput());
   }

   public PacketConfig getPacket() {
      return this.packet;
   }

   public KnowledgePackageWrapper getKnowledgePackageWrapper() {
      return this.knowledgePackageWrapper;
   }

   private PacketConfig buildPacketConfig(Packet packet) {
      PacketConfig packetConfig = new PacketConfig();
      packetConfig.setId(packet.getId());
      packetConfig.setCode(packet.getCode());
      packetConfig.setProjectId(packet.getProjectId());
      packetConfig.setEnable(packet.isEnable());
      packetConfig.setRestEnable(packet.isRestEnable());
      packetConfig.setRestSecurityEnable(packet.isRestSecurityEnable());
      packetConfig.setRestSecurityUser(packet.getRestSecurityUser());
      packetConfig.setRestSecurityPassword(packet.getRestSecurityPassword());
      packetConfig.setRestInput(this.parseMonitorObjects(packet.getRestInput()));
      packetConfig.setRestOutput(this.parseMonitorObjects(packet.getRestOutput()));
      packetConfig.setAuditInput(this.parseMonitorObjects(packet.getAuditInput()));
      packetConfig.setAuditOutput(this.parseMonitorObjects(packet.getAuditOutput()));
      packetConfig.setAuditEnable(packet.isAuditEnable());
      return packetConfig;
   }

   private List parseMonitorObjects(String text) {
      ArrayList items = new ArrayList();
      if (StringUtils.isEmpty(text)) {
         return items;
      } else {
         ObjectMapper objectMapper = JsonMapper.builder().build();

         try {
            for(Map valuesByKey : (Iterable<Map>)(Iterable<?>)((List)objectMapper.readValue(text, ArrayList.class))) {
               MonitorObject monitorObject = new MonitorObject();
               monitorObject.setName(valuesByKey.get("name").toString());
               monitorObject.setClazz(valuesByKey.get("clazz").toString());
               items.add(monitorObject);
               ArrayList items2 = new ArrayList();
               monitorObject.setFields(items2);

               for(Map valuesByKey2 : (Iterable<Map>)(Iterable<?>)((List)valuesByKey.get("fields"))) {
                  MonitorObjectField monitorObjectField = new MonitorObjectField();
                  monitorObjectField.setName(valuesByKey2.get("name").toString());
                  monitorObjectField.setLabel(valuesByKey2.get("label").toString());
                  monitorObjectField.setType(valuesByKey2.get("type").toString());
                  items2.add(monitorObjectField);
               }
            }

            return items;
         } catch (Exception exception) {
            throw new RuleException(exception);
         }
      }
   }
}
