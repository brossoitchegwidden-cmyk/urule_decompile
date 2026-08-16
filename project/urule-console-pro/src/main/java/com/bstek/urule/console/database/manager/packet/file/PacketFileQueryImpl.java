package com.bstek.urule.console.database.manager.packet.file;

import com.bstek.urule.console.database.manager.file.FileManager;
import com.bstek.urule.console.database.model.PacketFile;
import com.bstek.urule.console.database.model.RuleFile;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PacketFileQueryImpl implements PacketFileQuery {
   private Long a;
   private Long b;
   private Long c;
   private List d = new ArrayList();

   protected PacketFileQueryImpl() {
   }

   public List list() {
      String var1 = "select ID_,PACKET_ID_,FILE_ID_,PROJECT_ID_,PATH_,VERSION_,DESC_,CREATE_USER_,UPDATE_USER_,CREATE_DATE_,UPDATE_DATE_ from URULE_PACKET_FILE";
      StringBuilder var2 = this.a();
      if (var2.length() > 0) {
         var1 = var1 + " where" + var2.toString();
      }

      Connection var3 = JdbcUtils.getConnection();

      List var7;
      try {
         PreparedStatement var4 = var3.prepareStatement(var1);
         JdbcUtils.fillPreparedStatementParameters(this.d, var4);
         ResultSet var5 = var4.executeQuery();
         List var6 = this.a(var5);
         JdbcUtils.closeResultSet(var5);
         JdbcUtils.closeStatement(var4);
         var7 = var6;
      } catch (Exception var11) {
         throw new RuleException(var11);
      } finally {
         JdbcUtils.closeConnection(var3);
      }

      return var7;
   }

   private List a(ResultSet var1) throws Exception {
      ArrayList var2 = new ArrayList();

      while(var1.next()) {
         PacketFile var3 = new PacketFile();
         var3.setId(var1.getLong(1));
         var3.setPacketId(var1.getLong(2));
         var3.setFileId(var1.getLong(3));
         var3.setProjectId(var1.getLong(4));

         try {
            RuleFile var4 = FileManager.ins.get(var3.getFileId());
            var3.setPath(var4.getPath());
         } catch (RuleException var5) {
            var3.setPath("文件已删除");
         }

         var3.setVersion(var1.getString(6));
         var3.setDesc(var1.getString(7));
         var3.setCreateUser(var1.getString(8));
         var3.setUpdateUser(var1.getString(9));
         var3.setCreateDate(new Date(var1.getTimestamp(10).getTime()));
         var3.setUpdateDate(new Date(var1.getTimestamp(11).getTime()));
         var2.add(var3);
      }

      return var2;
   }

   private StringBuilder a() {
      this.d.clear();
      StringBuilder var1 = new StringBuilder();
      if (this.a != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" ID_=?");
         this.d.add(this.a);
      }

      if (this.b != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" PACKET_ID_=?");
         this.d.add(this.b);
      }

      if (this.c != null) {
         if (var1.length() > 0) {
            var1.append(" and");
         }

         var1.append(" PROJECT_ID_=?");
         this.d.add(this.c);
      }

      return var1;
   }

   public PacketFileQuery id(long var1) {
      this.a = var1;
      return this;
   }

   public PacketFileQuery packetId(long var1) {
      this.b = var1;
      return this;
   }

   public PacketFileQuery projectId(long var1) {
      this.c = var1;
      return this;
   }
}
