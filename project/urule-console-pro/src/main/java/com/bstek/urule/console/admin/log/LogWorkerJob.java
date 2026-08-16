package com.bstek.urule.console.admin.log;

import com.bstek.urule.console.database.manager.group.GroupManager;
import com.bstek.urule.console.database.manager.log.KnowledgeLogManager;
import com.bstek.urule.console.database.manager.log.LoginLogManager;
import com.bstek.urule.console.database.manager.log.OperationLogManager;
import com.bstek.urule.console.database.manager.packet.PacketManager;
import com.bstek.urule.console.database.manager.project.ProjectManager;
import com.bstek.urule.console.database.model.Group;
import com.bstek.urule.console.database.model.KnowledgeLog;
import com.bstek.urule.console.database.model.LoginLog;
import com.bstek.urule.console.database.model.OperationLog;
import com.bstek.urule.console.database.model.Packet;
import com.bstek.urule.console.database.model.Project;
import com.bstek.urule.console.database.model.URuleLog;
import com.bstek.urule.console.database.util.JdbcUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LogWorkerJob implements Runnable {
   public void run() {
      while(true) {
         try {
            Thread.sleep(10L);
            this.a();
         } catch (InterruptedException var2) {
            System.out.println("......The thread [LogWorkerJob] is terminate unexpectedly......");
            var2.printStackTrace();
         }
      }
   }

   private void a() {
      int var1 = 0;

      ArrayList var2;
      for(var2 = new ArrayList(); !LogQueue.isQueueEmpty() && var1 < 200; ++var1) {
         var2.add(LogQueue.pollLog());
      }

      if (var2 != null && !var2.isEmpty()) {
         Connection var3 = JdbcUtils.getConnection();

         try {
            this.a(var3, var2);
         } catch (Exception var8) {
            var8.printStackTrace();
         } finally {
            JdbcUtils.closeConnection(var3);
         }
      }

   }

   private void a(Connection var1, List var2) throws SQLException {
      PreparedStatement var3 = var1.prepareStatement("insert into URULE_LOG_KNOWLEDGE (ID_, USER_, KNOWLEDGE_ID_, KNOWLEDGE_NAME_, VERSION_, IN_PARAMS_, OUT_PARAMS_, LOGS_, TIME_, IP_, USER_AGENT_, START_TIME_, END_TIME_, GROUP_ID_, GROUP_NAME_, PROJECT_ID_, PROJECT_NAME_, CREATE_DATE_) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
      PreparedStatement var4 = var1.prepareStatement("insert into URULE_LOG_USERLOGIN (ID_, USER_ID_, USER_NAME_, IP_, USER_AGENT_, CREATE_DATE_) values (?, ?, ?, ?, ?, ?)");
      PreparedStatement var5 = var1.prepareStatement("insert into URULE_LOG_OPERATION (ID_, USER_ID_, USER_NAME_, GROUP_ID_, GROUP_NAME_, PROJECT_ID_, PROJECT_NAME_, CATEGORY_, ACTION_, ITEM_ID_, CONTENT_, CREATE_DATE_) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
      boolean var6 = false;
      boolean var7 = false;
      boolean var8 = false;

      for(URuleLog var10 : (Iterable<URuleLog>)(Iterable<?>)(var2)) {
         if (var10 instanceof LoginLog) {
            var7 = true;
            LoginLogManager.ins.addBatch(var4, (LoginLog)var10);
         } else if (var10 instanceof OperationLog) {
            OperationLog var11 = (OperationLog)var10;
            Group var12 = GroupManager.ins.get(var11.getGroupId());
            if (null != var12) {
               var11.setGroupName(var12.getName());
            }

            if (null != var11.getProjectId()) {
               Project var13 = ProjectManager.ins.get(var11.getProjectId());
               if (null != var13) {
                  var11.setProjectName(var13.getName());
               }
            }

            var8 = true;
            OperationLogManager.ins.addBatch(var5, var11);
         } else if (var10 instanceof KnowledgeLog) {
            KnowledgeLog var15 = (KnowledgeLog)var10;
            Packet var16 = PacketManager.ins.load(var15.getKnowledgeId());
            if (var16 != null) {
               var15.setKnowledgeName(var16.getName());
               var15.setProjectId(var16.getProjectId());
               Project var17 = ProjectManager.ins.get(var16.getProjectId());
               if (var17 != null) {
                  var15.setGroupId(var17.getGroupId());
                  var15.setProjectName(var17.getName());
                  Group var14 = GroupManager.ins.get(var17.getGroupId());
                  if (null != var14) {
                     var15.setGroupName(var14.getName());
                  }
               }

               var6 = true;
               KnowledgeLogManager.ins.addBatch(var3, var15);
            }
         }
      }

      if (var7) {
         var4.executeBatch();
         JdbcUtils.closeStatement(var4);
      }

      if (var8) {
         var5.executeBatch();
         JdbcUtils.closeStatement(var5);
      }

      if (var6) {
         var3.executeBatch();
         JdbcUtils.closeStatement(var3);
      }

   }
}
