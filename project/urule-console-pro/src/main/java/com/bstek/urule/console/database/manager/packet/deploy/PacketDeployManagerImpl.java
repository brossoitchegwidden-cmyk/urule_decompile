package com.bstek.urule.console.database.manager.packet.deploy;

import com.bstek.urule.console.database.IDGenerator;
import com.bstek.urule.console.database.IDType;
import com.bstek.urule.console.database.manager.packet.deploy.file.PacketDeployFileManager;
import com.bstek.urule.console.database.model.ApplyStatus;
import com.bstek.urule.console.database.model.PacketDeploy;
import com.bstek.urule.console.database.model.Page;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.console.util.FileUtils;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class PacketDeployManagerImpl implements PacketDeployManager {
   protected PacketDeployManagerImpl() {
   }

   public synchronized void add(PacketDeploy var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         String var3 = "insert into URULE_DEPLOYED_PACKET(ID_,PACKET_ID_,DESC_,APPLY_ID_,PROJECT_ID_,CONTENT_,VERSION_,CREATE_USER_,CREATE_DATE_,ENABLE_,STATUS_,DIGEST_) values(?,?,?,?,?,?,?,?,?,?,?,?)";
         PreparedStatement var4 = var2.prepareStatement(var3);
         if (var1.getId() == 0L) {
            var1.setId(IDGenerator.getInstance().nextId(IDType.DEPLOYED_PACKET));
         }

         if (StringUtils.isBlank(var1.getVersion())) {
            var1.setVersion(this.a(var1.getPacketId()));
         }

         var4.setLong(1, var1.getId());
         var4.setLong(2, var1.getPacketId());
         var4.setString(3, var1.getDesc());
         var4.setLong(4, var1.getApplyId());
         var4.setLong(5, var1.getProjectId());
         var4.setString(6, var1.getContent());
         var4.setString(7, var1.getVersion());
         var4.setString(8, var1.getCreateUser());
         var4.setTimestamp(9, new Timestamp((new Date()).getTime()));
         var4.setBoolean(10, var1.isEnable());
         var4.setString(11, var1.getStatus().name());
         var4.setString(12, var1.getDigest());
         var1.setCreateDate(new Date());
         var4.executeUpdate();
         JdbcUtils.closeStatement(var4);
      } catch (Exception var8) {
         throw new RuleException(var8);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   private String a(long var1) {
      Page var3 = this.newQuery().packetId(var1).paging(1, 1);
      if (var3.getData().size() > 0) {
         String var4 = ((PacketDeploy)var3.getData().get(0)).getVersion();
         return FileUtils.getMaxVersion(var4);
      } else {
         return "1.0.0";
      }
   }

   public void delete(long var1) {
      Connection var3 = JdbcUtils.getConnection();

      try {
         String var4 = "delete from URULE_DEPLOYED_PACKET where ID_=?";
         PreparedStatement var5 = var3.prepareStatement(var4);
         var5.setLong(1, var1);
         var5.executeUpdate();
         JdbcUtils.closeStatement(var5);
         PacketDeployFileManager.ins.deleteByDeployId(var1);
      } catch (Exception var9) {
         throw new RuleException(var9);
      } finally {
         JdbcUtils.closeConnection(var3);
      }

   }

   public void deleteByApplyId(long var1) {
      Connection var3 = JdbcUtils.getConnection();

      try {
         for(PacketDeploy var6 : (Iterable<PacketDeploy>)(Iterable<?>)(this.newQuery().applyId(var1).list())) {
            PacketDeployFileManager.ins.deleteByDeployId(var6.getId());
         }

         String var12 = "delete from URULE_DEPLOYED_PACKET where APPLY_ID_=?";
         PreparedStatement var13 = var3.prepareStatement(var12);
         var13.setLong(1, var1);
         var13.executeUpdate();
         JdbcUtils.closeStatement(var13);
      } catch (Exception var10) {
         throw new RuleException(var10);
      } finally {
         JdbcUtils.closeConnection(var3);
      }

   }

   public void deleteByPacketId(long var1) {
      Connection var3 = JdbcUtils.getConnection();

      try {
         for(PacketDeploy var6 : (Iterable<PacketDeploy>)(Iterable<?>)(this.newQuery().packetId(var1).list())) {
            PacketDeployFileManager.ins.deleteByDeployId(var6.getId());
         }

         String var12 = "delete from URULE_DEPLOYED_PACKET where PACKET_ID_=?";
         PreparedStatement var13 = var3.prepareStatement(var12);
         var13.setLong(1, var1);
         var13.executeUpdate();
         JdbcUtils.closeStatement(var13);
      } catch (Exception var10) {
         throw new RuleException(var10);
      } finally {
         JdbcUtils.closeConnection(var3);
      }

   }

   public void updateEnable(long var1, boolean var3) {
      Connection var4 = JdbcUtils.getConnection();

      try {
         String var5 = "update URULE_DEPLOYED_PACKET set ENABLE_=? where ID_=?";
         PreparedStatement var6 = var4.prepareStatement(var5);
         var6.setBoolean(1, var3);
         var6.setLong(2, var1);
         var6.executeUpdate();
         JdbcUtils.closeStatement(var6);
      } catch (Exception var10) {
         throw new RuleException(var10);
      } finally {
         JdbcUtils.closeConnection(var4);
      }

   }

   public void updateStatus(long var1, ApplyStatus var3) {
      Connection var4 = JdbcUtils.getConnection();

      try {
         String var5 = "update URULE_DEPLOYED_PACKET set STATUS_=? where ID_=?";
         PreparedStatement var6 = var4.prepareStatement(var5);
         var6.setString(1, var3.name());
         var6.setLong(2, var1);
         var6.executeUpdate();
         JdbcUtils.closeStatement(var6);
      } catch (Exception var10) {
         throw new RuleException(var10);
      } finally {
         JdbcUtils.closeConnection(var4);
      }

   }

   public void disableAll(long var1) {
      Connection var3 = JdbcUtils.getConnection();

      try {
         String var4 = "update URULE_DEPLOYED_PACKET set ENABLE_=? where PACKET_ID_=?";
         PreparedStatement var5 = var3.prepareStatement(var4);
         var5.setBoolean(1, false);
         var5.setLong(2, var1);
         var5.executeUpdate();
         JdbcUtils.closeStatement(var5);
      } catch (Exception var9) {
         throw new RuleException(var9);
      } finally {
         JdbcUtils.closeConnection(var3);
      }

   }

   public PacketDeploy load(long var1) {
      List var3 = this.newQuery().id(var1).listWithContent();
      return var3.size() > 0 ? (PacketDeploy)var3.get(0) : null;
   }

   public void deleteByProjectId(long var1) {
      Connection var3 = JdbcUtils.getConnection();

      try {
         PacketDeployFileManager.ins.deleteByProjectId(var1);
         String var4 = "delete from URULE_DEPLOYED_PACKET where PROJECT_ID_=?";
         PreparedStatement var5 = var3.prepareStatement(var4);
         var5.setLong(1, var1);
         var5.executeUpdate();
         JdbcUtils.closeStatement(var5);
      } catch (Exception var9) {
         throw new RuleException(var9);
      } finally {
         JdbcUtils.closeConnection(var3);
      }

   }

   public PacketDeployQuery newQuery() {
      return new PacketDeployQueryImpl();
   }
}
