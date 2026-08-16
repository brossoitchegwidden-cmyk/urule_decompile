package com.bstek.urule.console.database.manager.packet.file;

import com.bstek.urule.console.database.IDGenerator;
import com.bstek.urule.console.database.IDType;
import com.bstek.urule.console.database.model.PacketFile;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class PacketFileManagerImpl implements PacketFileManager {
   protected PacketFileManagerImpl() {
   }

   public PacketFile load(long var1) {
      List var3 = this.newQuery().id(var1).list();
      return var3.size() > 0 ? (PacketFile)var3.get(0) : null;
   }

   public void add(PacketFile var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         String var3 = "insert into URULE_PACKET_FILE(ID_,PACKET_ID_,FILE_ID_,PROJECT_ID_,PATH_,VERSION_,DESC_,CREATE_USER_,UPDATE_USER_,CREATE_DATE_,UPDATE_DATE_) values(?,?,?,?,?,?,?,?,?,?,?)";
         PreparedStatement var4 = var2.prepareStatement(var3);
         var1.setId(IDGenerator.getInstance().nextId(IDType.PACKET_FILE));
         var4.setLong(1, var1.getId());
         var4.setLong(2, var1.getPacketId());
         var4.setLong(3, var1.getFileId());
         var4.setLong(4, var1.getProjectId());
         var4.setString(5, var1.getPath());
         var4.setString(6, var1.getVersion());
         var4.setString(7, var1.getDesc());
         var4.setString(8, var1.getCreateUser());
         var4.setString(9, var1.getCreateUser());
         var4.setTimestamp(10, new Timestamp((new Date()).getTime()));
         var4.setTimestamp(11, new Timestamp((new Date()).getTime()));
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

   public void delete(long var1) {
      Connection var3 = JdbcUtils.getConnection();

      try {
         String var4 = "delete from URULE_PACKET_FILE where ID_=?";
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
         String var4 = "delete from URULE_PACKET_FILE where PACKET_ID_=?";
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

   public void update(PacketFile var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         String var3 = "update URULE_PACKET_FILE set FILE_ID_=?,PATH_=?,VERSION_=?,DESC_=?,UPDATE_USER_=?,UPDATE_DATE_=? where ID_=?";
         PreparedStatement var4 = var2.prepareStatement(var3);
         var4.setLong(1, var1.getFileId());
         var4.setString(2, var1.getPath());
         var4.setString(3, var1.getVersion());
         var4.setString(4, var1.getDesc());
         var4.setString(5, var1.getUpdateUser());
         var4.setTimestamp(6, new Timestamp((new Date()).getTime()));
         var4.setLong(7, var1.getId());
         var4.executeUpdate();
         JdbcUtils.closeStatement(var4);
      } catch (Exception var8) {
         throw new RuleException(var8);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public void deleteByProjectId(long var1) {
      Connection var3 = JdbcUtils.getConnection();

      try {
         String var4 = "delete from URULE_PACKET_FILE where PROJECT_ID_=?";
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

   public PacketFileQuery newQuery() {
      return new PacketFileQueryImpl();
   }
}
