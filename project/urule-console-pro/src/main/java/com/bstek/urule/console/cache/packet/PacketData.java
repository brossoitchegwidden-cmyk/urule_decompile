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
   private PacketConfig a;
   private KnowledgePackageWrapper b;

   public PacketData(Packet var1, KnowledgePackageWrapper var2) {
      this.a = this.a(var1);
      this.b = var2;
      KnowledgePackageImpl var3 = (KnowledgePackageImpl)var2.getKnowledgePackage();
      var3.setInputData(this.a.getAuditInput());
      var3.setOutputData(this.a.getAuditOutput());
   }

   public PacketConfig getPacket() {
      return this.a;
   }

   public KnowledgePackageWrapper getKnowledgePackageWrapper() {
      return this.b;
   }

   private PacketConfig a(Packet var1) {
      PacketConfig var2 = new PacketConfig();
      var2.setId(var1.getId());
      var2.setCode(var1.getCode());
      var2.setProjectId(var1.getProjectId());
      var2.setEnable(var1.isEnable());
      var2.setRestEnable(var1.isRestEnable());
      var2.setRestSecurityEnable(var1.isRestSecurityEnable());
      var2.setRestSecurityUser(var1.getRestSecurityUser());
      var2.setRestSecurityPassword(var1.getRestSecurityPassword());
      var2.setRestInput(this.a(var1.getRestInput()));
      var2.setRestOutput(this.a(var1.getRestOutput()));
      var2.setAuditInput(this.a(var1.getAuditInput()));
      var2.setAuditOutput(this.a(var1.getAuditOutput()));
      var2.setAuditEnable(var1.isAuditEnable());
      return var2;
   }

   private List a(String var1) {
      ArrayList var2 = new ArrayList();
      if (StringUtils.isEmpty(var1)) {
         return var2;
      } else {
         ObjectMapper var3 = JsonMapper.builder().build();

         try {
            for(Map var6 : (Iterable<Map>)(Iterable<?>)((List)var3.readValue(var1, ArrayList.class))) {
               MonitorObject var7 = new MonitorObject();
               var7.setName(var6.get("name").toString());
               var7.setClazz(var6.get("clazz").toString());
               var2.add(var7);
               ArrayList var8 = new ArrayList();
               var7.setFields(var8);

               for(Map var11 : (Iterable<Map>)(Iterable<?>)((List)var6.get("fields"))) {
                  MonitorObjectField var12 = new MonitorObjectField();
                  var12.setName(var11.get("name").toString());
                  var12.setLabel(var11.get("label").toString());
                  var12.setType(var11.get("type").toString());
                  var8.add(var12);
               }
            }

            return var2;
         } catch (Exception var13) {
            throw new RuleException(var13);
         }
      }
   }
}
