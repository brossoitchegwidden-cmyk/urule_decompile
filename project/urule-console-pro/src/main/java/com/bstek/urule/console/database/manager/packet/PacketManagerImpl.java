package com.bstek.urule.console.database.manager.packet;

import com.bstek.urule.console.database.IDGenerator;
import com.bstek.urule.console.database.IDType;
import com.bstek.urule.console.database.manager.packet.apply.PacketApplyManager;
import com.bstek.urule.console.database.manager.packet.deploy.PacketDeployManager;
import com.bstek.urule.console.database.manager.packet.file.PacketFileManager;
import com.bstek.urule.console.database.manager.packet.packge.PacketPackageManager;
import com.bstek.urule.console.database.model.Packet;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class PacketManagerImpl implements PacketManager {
   protected PacketManagerImpl() {
   }

   public Packet load(long var1) {
      List var3 = this.newQuery().id(var1).list();
      return var3.size() > 0 ? (Packet)var3.get(0) : null;
   }

   public Packet load(String var1) {
      List var2 = this.newQuery().code(var1).list();
      return var2.size() > 0 ? (Packet)var2.get(0) : null;
   }

   public void add(Packet var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         var1.setCreateDate(new Date());
         var1.setUpdateDate(new Date());
         String var3 = "insert into URULE_PACKET(ID_,PROJECT_ID_,NAME_,CODE_,TYPE_,DESC_,INPUT_DATA_,OUTPUT_DATA_,ENABLE_,CREATE_USER_,UPDATE_USER_,CREATE_DATE_,UPDATE_DATE_,REST_INPUT_,REST_OUTPUT_,AUDIT_ENABLE_,AUDIT_INPUT_,AUDIT_OUTPUT_,REST_ENABLE_,REST_SECURITY_ENABLE_,REST_SECURITY_USER_,REST_SECURITY_PASSWORD_) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
         PreparedStatement var4 = var2.prepareStatement(var3);
         var1.setId(IDGenerator.getInstance().nextId(IDType.PACKET));
         var4.setLong(1, var1.getId());
         var4.setLong(2, var1.getProjectId());
         var4.setString(3, var1.getName());
         if (StringUtils.isBlank(var1.getCode())) {
            var1.setCode(UUID.randomUUID().toString().replaceAll("-", ""));
         }

         var4.setString(4, var1.getCode());
         var4.setString(5, var1.getType().name());
         var4.setString(6, var1.getDesc());
         var4.setString(7, var1.getInputData());
         var4.setString(8, var1.getOutputData());
         var4.setBoolean(9, var1.isEnable());
         var4.setString(10, var1.getCreateUser());
         var4.setString(11, var1.getCreateUser());
         var4.setTimestamp(12, new Timestamp(var1.getCreateDate().getTime()));
         var4.setTimestamp(13, new Timestamp(var1.getUpdateDate().getTime()));
         var4.setString(14, var1.getRestInput());
         var4.setString(15, var1.getRestOutput());
         var4.setBoolean(16, var1.isAuditEnable());
         var4.setString(17, var1.getAuditInput());
         var4.setString(18, var1.getAuditOutput());
         var4.setBoolean(19, var1.isRestEnable());
         var4.setBoolean(20, var1.isRestSecurityEnable());
         var4.setString(21, var1.getRestSecurityUser());
         var4.setString(22, var1.getRestSecurityPassword());
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
         String var4 = "delete from URULE_PACKET where ID_=?";
         PreparedStatement var5 = var3.prepareStatement(var4);
         var5.setLong(1, var1);
         var5.executeUpdate();
         JdbcUtils.closeStatement(var5);
         PacketFileManager.ins.deleteByPacketId(var1);
         PacketApplyManager.ins.deleteByPacketId(var1);
         PacketDeployManager.ins.deleteByPacketId(var1);
         PacketPackageManager.ins.deleteByPacketId(var1);
      } catch (Exception var9) {
         throw new RuleException(var9);
      } finally {
         JdbcUtils.closeConnection(var3);
      }

   }

   public void update(Packet var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         var1.setUpdateDate(new Date());
         String var3 = "update URULE_PACKET set NAME_=?,CODE_=?,DESC_=?,INPUT_DATA_=?,OUTPUT_DATA_=?,ENABLE_=?,UPDATE_DATE_=?,UPDATE_USER_=? where ID_=?";
         PreparedStatement var4 = var2.prepareStatement(var3);
         var4.setString(1, var1.getName());
         if (StringUtils.isBlank(var1.getCode())) {
            var1.setCode(UUID.randomUUID().toString().replaceAll("-", ""));
         }

         var4.setString(2, var1.getCode());
         var4.setString(3, var1.getDesc());
         var4.setString(4, var1.getInputData());
         var4.setString(5, var1.getOutputData());
         var4.setBoolean(6, var1.isEnable());
         var4.setTimestamp(7, new Timestamp(var1.getUpdateDate().getTime()));
         var4.setString(8, var1.getUpdateUser());
         var4.setLong(9, var1.getId());
         var4.executeUpdate();
         JdbcUtils.closeStatement(var4);
      } catch (Exception var8) {
         throw new RuleException(var8);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public void update(long var1, boolean var3) {
      Connection var4 = JdbcUtils.getConnection();

      try {
         String var5 = "update URULE_PACKET set ENABLE_=?,UPDATE_DATE_=? where ID_=?";
         PreparedStatement var6 = var4.prepareStatement(var5);
         var6.setBoolean(1, var3);
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

   public void updateRestConfig(Packet var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         String var3 = "update URULE_PACKET set REST_SECURITY_ENABLE_=?,REST_SECURITY_USER_=?,REST_SECURITY_PASSWORD_=?,REST_INPUT_=?,REST_OUTPUT_=?,UPDATE_DATE_=?,UPDATE_USER_=?,REST_ENABLE_=? where ID_=?";
         PreparedStatement var4 = var2.prepareStatement(var3);
         var4.setBoolean(1, var1.isRestSecurityEnable());
         var4.setString(2, var1.getRestSecurityUser());
         var4.setString(3, var1.getRestSecurityPassword());
         var4.setString(4, var1.getRestInput());
         var4.setString(5, var1.getRestOutput());
         var4.setTimestamp(6, new Timestamp(var1.getUpdateDate().getTime()));
         var4.setString(7, var1.getUpdateUser());
         var4.setBoolean(8, var1.isRestEnable());
         var4.setLong(9, var1.getId());
         var4.executeUpdate();
         JdbcUtils.closeStatement(var4);
      } catch (Exception var8) {
         throw new RuleException(var8);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public void updateAuditConfig(Packet var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         String var3 = "update URULE_PACKET set AUDIT_ENABLE_=?,UPDATE_DATE_=?,UPDATE_USER_=?,AUDIT_INPUT_=?,AUDIT_OUTPUT_=? where ID_=?";
         PreparedStatement var4 = var2.prepareStatement(var3);
         var4.setBoolean(1, var1.isAuditEnable());
         var4.setTimestamp(2, new Timestamp(var1.getUpdateDate().getTime()));
         var4.setString(3, var1.getUpdateUser());
         var4.setString(4, var1.getAuditInput());
         var4.setString(5, var1.getAuditOutput());
         var4.setLong(6, var1.getId());
         var4.executeUpdate();
         JdbcUtils.closeStatement(var4);
      } catch (Exception var8) {
         throw new RuleException(var8);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public PacketQuery newQuery() {
      return new PacketQueryImpl();
   }

   public void deleteByProjectId(long var1) {
      Connection var3 = JdbcUtils.getConnection();

      try {
         String var4 = "delete from URULE_PACKET where PROJECT_ID_=?";
         PreparedStatement var5 = var3.prepareStatement(var4);
         var5.setLong(1, var1);
         var5.executeUpdate();
         JdbcUtils.closeStatement(var5);
         PacketFileManager.ins.deleteByProjectId(var1);
         PacketPackageManager.ins.deleteByProjectId(var1);
      } catch (Exception var9) {
         throw new RuleException(var9);
      } finally {
         JdbcUtils.closeConnection(var3);
      }

   }

   public int getCount(long var1) {
      int var3 = 0;
      Connection var4 = JdbcUtils.getConnection();

      int var7;
      try {
         PreparedStatement var5 = var4.prepareStatement("select COUNT(*) RULE_COUNT_ from URULE_PACKET where PROJECT_ID_=?");
         var5.setLong(1, var1);
         ResultSet var6 = var5.executeQuery();
         if (var6.next()) {
            var3 = var6.getInt(1);
         }

         JdbcUtils.closeResultSet(var6);
         JdbcUtils.closeStatement(var5);
         var7 = var3;
      } catch (Exception var11) {
         throw new RuleException(var11);
      } finally {
         JdbcUtils.closeConnection(var4);
      }

      return var7;
   }
}
