package com.bstek.urule.console.database.service.knowledge;

import com.bstek.urule.console.database.manager.packet.PacketManager;
import com.bstek.urule.console.database.manager.project.ProjectQueryImpl;
import com.bstek.urule.console.database.model.Packet;
import com.bstek.urule.console.database.model.Project;
import com.bstek.urule.exception.RuleException;
import java.util.List;

public class PackageData {
   private long a;
   private String b;

   protected PackageData(String var1) {
      String[] var2 = var1.split("/");
      if (var2.length == 0) {
         this.a = Long.valueOf(var1);
      } else if (var2.length != 2) {
         throw new RuleException("请求的知识包 [" + var1 + "] 格式不正确.");
      }

      String var3 = var2[0];
      String var4 = var2[1];
      int var5 = var4.indexOf(":");
      if (var5 > 0) {
         this.b = var4.substring(var5 + 1);
         var4 = var4.substring(0, var5);
      }

      this.a(var3, var4);
      this.a();
   }

   private void a(String var1, String var2) {
      ProjectQueryImpl var3 = new ProjectQueryImpl();
      var3.name(var1);
      List var4 = var3.list();
      if (var4.size() == 0) {
         throw new RuleException("名为【" + var1 + "】项目不存在！");
      } else {
         long var5 = ((Project)var4.get(0)).getId();
         List var7 = PacketManager.ins.newQuery().projectId(var5).list();
         if (var7.size() == 0) {
            throw new RuleException("项目ID[" + var5 + "]中名为【" + var2 + "】的知识包不存在！");
         } else {
            this.a = ((Packet)var7.get(0)).getId();
         }
      }
   }

   private void a() {
      Packet var1 = PacketManager.ins.load(this.a);
      if (var1 == null) {
         throw new RuleException("知识包【" + this.a + "】不存在！");
      } else if (!var1.isEnable()) {
         throw new RuleException("知识包 【" + this.a + "】 已被停用!");
      }
   }

   public long getPackageId() {
      return this.a;
   }

   public String getVersion() {
      return this.b;
   }
}
