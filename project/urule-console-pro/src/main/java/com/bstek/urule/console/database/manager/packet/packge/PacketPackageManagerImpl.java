package com.bstek.urule.console.database.manager.packet.packge;

import com.bstek.urule.console.database.IDGenerator;
import com.bstek.urule.console.database.IDType;
import com.bstek.urule.console.database.model.PacketPackage;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;

public class PacketPackageManagerImpl implements PacketPackageManager {
   protected PacketPackageManagerImpl() {
   }

   public void add(PacketPackage var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         String var3 = "insert into URULE_PACKET_PACKAGE(ID_,PACKET_ID_,DESC_,CREATE_USER_,UPDATE_USER_,CREATE_DATE_,UPDATE_DATE_,PROJECT_ID_,CONTENT_) values(?,?,?,?,?,?,?,?,?)";
         PreparedStatement var4 = var2.prepareStatement(var3);
         var1.setId(IDGenerator.getInstance().nextId(IDType.PACKET_PACKAGE));
         var4.setLong(1, var1.getId());
         var4.setLong(2, var1.getPacketId());
         var4.setString(3, var1.getDesc());
         var4.setString(4, var1.getCreateUser());
         var4.setString(5, var1.getCreateUser());
         var4.setTimestamp(6, new Timestamp((new Date()).getTime()));
         var4.setTimestamp(7, new Timestamp((new Date()).getTime()));
         var4.setLong(8, var1.getProjectId());
         var4.setString(9, var1.getContent());
         var1.setCreateDate(new Date());
         var1.setUpdateDate(new Date());
         var4.executeUpdate();
         JdbcUtils.closeStatement(var4);
      } catch (Exception var8) {
         throw new RuleException(var8);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public void update(PacketPackage var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         String var3 = "update URULE_PACKET_PACKAGE set DESC_=?,UPDATE_USER_=?,UPDATE_DATE_=?,CONTENT_=?  where ID_=?";
         PreparedStatement var4 = var2.prepareStatement(var3);
         var4.setString(1, var1.getDesc());
         var4.setString(2, var1.getUpdateUser());
         var4.setTimestamp(3, new Timestamp((new Date()).getTime()));
         var4.setString(4, var1.getContent());
         var4.setLong(5, var1.getId());
         var4.executeUpdate();
         JdbcUtils.closeStatement(var4);
      } catch (Exception var8) {
         throw new RuleException(var8);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public void deleteByPacketId(long var1) {
      Connection var3 = JdbcUtils.getConnection();

      try {
         String var4 = "delete from URULE_PACKET_PACKAGE where PACKET_ID_=?";
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

   public void deleteByProjectId(long var1) {
      Connection var3 = JdbcUtils.getConnection();

      try {
         String var4 = "delete from URULE_PACKET_PACKAGE where PROJECT_ID_=?";
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

   public PacketPackage loadByPacketId(long var1) {
      Connection var3 = JdbcUtils.getConnection();

      PacketPackage var8;
      try {
         String var4 = "select ID_,DESC_,CREATE_USER_,UPDATE_USER_,CREATE_DATE_,UPDATE_DATE_,PROJECT_ID_ from URULE_PACKET_PACKAGE where PACKET_ID_=?";
         PreparedStatement var5 = var3.prepareStatement(var4);
         var5.setLong(1, var1);
         ResultSet var6 = var5.executeQuery();
         PacketPackage var7 = null;

         while(var6.next()) {
            var7 = new PacketPackage();
            var7.setId(var6.getLong(1));
            var7.setPacketId(var1);
            var7.setDesc(var6.getString(2));
            var7.setCreateUser(var6.getString(3));
            var7.setUpdateUser(var6.getString(4));
            var7.setCreateDate(new Date(var6.getTimestamp(5).getTime()));
            var7.setUpdateDate(new Date(var6.getTimestamp(6).getTime()));
            var7.setProjectId(var6.getLong(7));
         }

         JdbcUtils.closeResultSet(var6);
         JdbcUtils.closeStatement(var5);
         var8 = var7;
      } catch (Exception var12) {
         throw new RuleException(var12);
      } finally {
         JdbcUtils.closeConnection(var3);
      }

      return var8;
   }

   public String loadContent(long var1) {
      Connection var3 = JdbcUtils.getConnection();

      String var8;
      try {
         String var4 = "select CONTENT_ from URULE_PACKET_PACKAGE where ID_=?";
         PreparedStatement var5 = var3.prepareStatement(var4);
         var5.setLong(1, var1);
         ResultSet var6 = var5.executeQuery();

         String var7;
         for(var7 = null; var6.next(); var7 = var6.getString(1)) {
         }

         JdbcUtils.closeResultSet(var6);
         JdbcUtils.closeStatement(var5);
         var8 = var7;
      } catch (Exception var12) {
         throw new RuleException(var12);
      } finally {
         JdbcUtils.closeConnection(var3);
      }

      return var8;
   }
}
