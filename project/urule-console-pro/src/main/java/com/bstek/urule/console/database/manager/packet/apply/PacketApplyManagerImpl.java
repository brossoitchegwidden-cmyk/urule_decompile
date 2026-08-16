package com.bstek.urule.console.database.manager.packet.apply;

import com.bstek.urule.console.database.IDGenerator;
import com.bstek.urule.console.database.IDType;
import com.bstek.urule.console.database.manager.packet.apply.detail.PacketApplyDetailManager;
import com.bstek.urule.console.database.model.ApplyStatus;
import com.bstek.urule.console.database.model.PacketApply;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class PacketApplyManagerImpl implements PacketApplyManager {
   protected PacketApplyManagerImpl() {
   }

   public void add(PacketApply var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         var1.setCreateDate(new Date());
         var1.setUpdateDate(new Date());
         String var3 = "insert into URULE_PACKET_APPLY(ID_,PACKET_ID_,DEPLOYED_PACKET_ID_,PROJECT_ID_,TYPE_,TITLE_,DESC_,APPROVER_,STATUS_,CREATE_USER_,CREATE_DATE_,UPDATE_DATE_) values(?,?,?,?,?,?,?,?,?,?,?,?)";
         PreparedStatement var4 = var2.prepareStatement(var3);
         var1.setId(IDGenerator.getInstance().nextId(IDType.PACKET_APPLY));
         var4.setLong(1, var1.getId());
         var4.setLong(2, var1.getPacketId());
         var4.setLong(3, var1.getDeployedPacketId());
         var4.setLong(4, var1.getProjectId());
         var4.setString(5, var1.getType().name());
         var4.setString(6, var1.getTitle());
         var4.setString(7, var1.getDesc());
         var4.setString(8, var1.getApprover());
         var4.setString(9, var1.getStatus().name());
         var4.setString(10, var1.getCreateUser());
         var4.setTimestamp(11, new Timestamp(var1.getCreateDate().getTime()));
         var4.setTimestamp(12, new Timestamp(var1.getUpdateDate().getTime()));
         var4.executeUpdate();
         JdbcUtils.closeStatement(var4);
      } catch (Exception var8) {
         throw new RuleException(var8);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public void delete(long var1) {
      Connection var3 = JdbcUtils.getConnection();

      try {
         String var4 = "delete from URULE_PACKET_APPLY where ID_=?";
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

   public void deleteByPacketId(long var1) {
      Connection var3 = JdbcUtils.getConnection();

      try {
         for(PacketApply var6 : (Iterable<PacketApply>)(Iterable<?>)(this.newQuery().packetId(var1).list())) {
            PacketApplyDetailManager.ins.deleteByApplyId(var6.getId());
         }

         String var12 = "delete from URULE_PACKET_APPLY where PACKET_ID_=?";
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

   public PacketApply load(long var1) {
      List var3 = this.newQuery().id(var1).list();
      return var3.size() > 0 ? (PacketApply)var3.get(0) : null;
   }

   public void update(long var1, ApplyStatus var3) {
      Connection var4 = JdbcUtils.getConnection();

      try {
         String var5 = "update URULE_PACKET_APPLY set STATUS_=?,UPDATE_DATE_=? where ID_=?";
         PreparedStatement var6 = var4.prepareStatement(var5);
         var6.setString(1, var3.name());
         var6.setTimestamp(2, new Timestamp((new Date()).getTime()));
         var6.setLong(3, var1);
         var6.executeUpdate();
         JdbcUtils.closeStatement(var6);
      } catch (Exception var10) {
         throw new RuleException(var10);
      } finally {
         JdbcUtils.closeConnection(var4);
      }

   }

   public void updateDeployedPacketId(long var1, long var3) {
      Connection var5 = JdbcUtils.getConnection();

      try {
         String var6 = "update URULE_PACKET_APPLY set DEPLOYED_PACKET_ID_=?,UPDATE_DATE_=? where ID_=?";
         PreparedStatement var7 = var5.prepareStatement(var6);
         var7.setLong(1, var3);
         var7.setTimestamp(2, new Timestamp((new Date()).getTime()));
         var7.setLong(3, var1);
         var7.executeUpdate();
         JdbcUtils.closeStatement(var7);
      } catch (Exception var11) {
         throw new RuleException(var11);
      } finally {
         JdbcUtils.closeConnection(var5);
      }

   }

   public PacketApplyQuery newQuery() {
      return new PacketApplyQueryImpl();
   }

   public void deleteByProjectId(long var1) {
      Connection var3 = JdbcUtils.getConnection();

      try {
         PacketApplyDetailManager.ins.deleteByProjectId(var1);
         String var4 = "delete from URULE_PACKET_APPLY where PROJECT_ID_=?";
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
}
