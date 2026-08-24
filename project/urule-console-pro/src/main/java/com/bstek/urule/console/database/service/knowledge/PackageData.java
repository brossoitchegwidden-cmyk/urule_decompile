package com.bstek.urule.console.database.service.knowledge;

import com.bstek.urule.console.database.manager.packet.PacketManager;
import com.bstek.urule.console.database.manager.project.ProjectQueryImpl;
import com.bstek.urule.console.database.model.Packet;
import com.bstek.urule.console.database.model.Project;
import com.bstek.urule.exception.RuleException;
import java.util.List;

public class PackageData {
   private long packageId;
   private String version;

   protected PackageData(String packageInfo) {
      String[] parts = packageInfo.split("/");
      if (parts.length == 0) {
         this.packageId = Long.valueOf(packageInfo);
      } else if (parts.length != 2) {
         throw new RuleException("请求的知识包 [" + packageInfo + "] 格式不正确.");
      }

      String text = parts[0];
      String substring = parts[1];
      int number = substring.indexOf(":");
      if (number > 0) {
         this.version = substring.substring(number + 1);
         substring = substring.substring(0, number);
      }

      this.resolvePacketByNames(text, substring);
      this.validatePacketEnabled();
   }

   private void resolvePacketByNames(String text, String text2) {
      ProjectQueryImpl projectQueryImpl = new ProjectQueryImpl();
      projectQueryImpl.name(text);
      List items = projectQueryImpl.list();
      if (items.size() == 0) {
         throw new RuleException("名为【" + text + "】项目不存在！");
      } else {
         long id = ((Project)items.get(0)).getId();
         List items2 = PacketManager.ins.newQuery().projectId(id).list();
         if (items2.size() == 0) {
            throw new RuleException("项目ID[" + id + "]中名为【" + text2 + "】的知识包不存在！");
         } else {
            this.packageId = ((Packet)items2.get(0)).getId();
         }
      }
   }

   private void validatePacketEnabled() {
      Packet packet = PacketManager.ins.load(this.packageId);
      if (packet == null) {
         throw new RuleException("知识包【" + this.packageId + "】不存在！");
      } else if (!packet.isEnable()) {
         throw new RuleException("知识包 【" + this.packageId + "】 已被停用!");
      }
   }

   public long getPackageId() {
      return this.packageId;
   }

   public String getVersion() {
      return this.version;
   }
}
