package com.bstek.urule.console.database.manager.packet.deploy.file;

import com.bstek.urule.console.database.IDGenerator;
import com.bstek.urule.console.database.IDType;
import com.bstek.urule.console.database.model.PacketDeployFile;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class PacketDeployFileManagerImpl implements PacketDeployFileManager {
   protected PacketDeployFileManagerImpl() {
   }

   public PacketDeployFile load(long var1) {
      List var3 = this.newQuery().id(var1).listWithContent();
      return var3.size() > 0 ? (PacketDeployFile)var3.get(0) : null;
   }

   public List loadFiles(long var1) {
      return this.newQuery().packetDeployId(var1).list();
   }

   public List loadFilesWithContent(long var1) {
      return this.newQuery().packetDeployId(var1).listWithContent();
   }

   public void deleteByDeployId(long var1) {
      Connection var3 = JdbcUtils.getConnection();

      try {
         String var4 = "delete from URULE_DEPLOYED_PACKET_FILE where DEPLOYED_PACKET_ID_=?";
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

   public void add(PacketDeployFile var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         String var3 = "insert into URULE_DEPLOYED_PACKET_FILE(ID_,DEPLOYED_PACKET_ID_,FILE_ID_,PROJECT_ID_,PATH_,VERSION_,CONTENT_,CREATE_USER_,CREATE_DATE_,DIGEST_) values(?,?,?,?,?,?,?,?,?,?)";
         PreparedStatement var4 = var2.prepareStatement(var3);
         var1.setId(IDGenerator.getInstance().nextId(IDType.DEPLOYED_PACKET_FILE));
         var4.setLong(1, var1.getId());
         var4.setLong(2, var1.getPacketDeployId());
         var4.setLong(3, var1.getFileId());
         var4.setLong(4, var1.getProjectId());
         var4.setString(5, var1.getPath());
         var4.setString(6, var1.getVersion());
         var4.setString(7, var1.getContent());
         var4.setString(8, var1.getCreateUser());
         var4.setTimestamp(9, new Timestamp((new Date()).getTime()));
         var4.setString(10, var1.getDigest());
         var1.setCreateDate(new Date());
         var4.executeUpdate();
         JdbcUtils.closeStatement(var4);
      } catch (Exception var8) {
         throw new RuleException(var8);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public PacketDeployFileQuery newQuery() {
      return new PacketDeployFileQueryImpl();
   }

   public void deleteByProjectId(long var1) {
      Connection var3 = JdbcUtils.getConnection();

      try {
         String var4 = "delete from URULE_DEPLOYED_PACKET_FILE where PROJECT_ID_=?";
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
