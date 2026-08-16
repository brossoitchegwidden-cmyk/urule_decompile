package com.bstek.urule.console.database.manager.packet.deploy.file;

import com.bstek.urule.console.database.model.PacketDeployFile;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.console.util.FileUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PacketDeployFileQueryImpl implements PacketDeployFileQuery {
   private Long a;
   private Long b;
   private Long c;
   private List d = new ArrayList();

   protected PacketDeployFileQueryImpl() {
   }

   public List list() {
      String var1 = "select ID_,DEPLOYED_PACKET_ID_,FILE_ID_,PROJECT_ID_,PATH_,VERSION_,CREATE_USER_,CREATE_DATE_,DIGEST_ from URULE_DEPLOYED_PACKET_FILE";
      return this.a(var1, false);
   }

   public List listWithContent() {
      String var1 = "select ID_,DEPLOYED_PACKET_ID_,FILE_ID_,PROJECT_ID_,PATH_,VERSION_,CONTENT_,CREATE_USER_,CREATE_DATE_,DIGEST_ from URULE_DEPLOYED_PACKET_FILE";
      return this.a(var1, true);
   }

   private List a(String var1, boolean var2) {
      this.d.clear();
      StringBuilder var3 = this.a();
      if (var3.length() > 0) {
         var1 = var1 + " where " + var3.toString();
      }

      var1 = var1 + " order by CREATE_DATE_ desc";
      Connection var4 = JdbcUtils.getConnection();
      PreparedStatement var5 = null;
      ResultSet var6 = null;

      ArrayList var19;
      try {
         var5 = var4.prepareStatement(var1);

         for(int var7 = 0; var7 < this.d.size(); ++var7) {
            Object var8 = this.d.get(var7);
            if (var8 instanceof Long) {
               Long var9 = (Long)var8;
               var5.setLong(var7 + 1, var9);
            } else if (var8 instanceof String) {
               String var20 = (String)var8;
               var5.setString(var7 + 1, var20);
            } else {
               var5.setObject(var7 + 1, var8);
            }
         }

         var6 = var5.executeQuery();

         ArrayList var16 = new ArrayList();
         while(var6.next()) {
            PacketDeployFile var18;
            if (var2) {
               var18 = this.b(var6);
            } else {
               var18 = this.a(var6);
            }

            var16.add(var18);
         }

         var19 = var16;
      } catch (Exception var13) {
         throw new RuleException(var13);
      } finally {
         JdbcUtils.closeResultSet(var6);
         JdbcUtils.closeStatement(var5);
         JdbcUtils.closeConnection(var4);
      }

      return var19;
   }

   private PacketDeployFile a(ResultSet var1) throws SQLException {
      PacketDeployFile var2 = new PacketDeployFile();
      var2.setId(var1.getLong(1));
      var2.setPacketDeployId(var1.getLong(2));
      var2.setFileId(var1.getLong(3));
      var2.setProjectId(var1.getLong(4));
      var2.setPath(var1.getString(5));
      var2.setVersion(var1.getString(6));
      var2.setCreateUser(var1.getString(7));
      var2.setCreateDate(new Date(var1.getTimestamp(8).getTime()));
      var2.setDigest(var1.getString(9));
      return var2;
   }

   private PacketDeployFile b(ResultSet var1) throws SQLException {
      PacketDeployFile var2 = new PacketDeployFile();
      var2.setId(var1.getLong(1));
      var2.setPacketDeployId(var1.getLong(2));
      var2.setFileId(var1.getLong(3));
      var2.setProjectId(var1.getLong(4));
      var2.setPath(var1.getString(5));
      var2.setVersion(var1.getString(6));
      var2.setContent(FileUtils.formatXml(var1.getString(7)));
      var2.setCreateUser(var1.getString(8));
      var2.setCreateDate(new Date(var1.getTimestamp(9).getTime()));
      var2.setDigest(var1.getString(10));
      return var2;
   }

   private StringBuilder a() {
      StringBuilder var1 = new StringBuilder();
      if (this.a != null) {
         if (var1.length() == 0) {
            var1.append(" ID_=?");
         } else {
            var1.append(" and ID_=?");
         }

         this.d.add(this.a);
      }

      if (this.b != null) {
         if (var1.length() == 0) {
            var1.append(" DEPLOYED_PACKET_ID_=?");
         } else {
            var1.append(" and DEPLOYED_PACKET_ID_=?");
         }

         this.d.add(this.b);
      }

      if (this.c != null) {
         if (var1.length() == 0) {
            var1.append(" PROJECT_ID_=?");
         } else {
            var1.append(" and PROJECT_ID_=?");
         }

         this.d.add(this.c);
      }

      return var1;
   }

   public PacketDeployFileQuery id(long var1) {
      this.a = var1;
      return this;
   }

   public PacketDeployFileQuery packetDeployId(long var1) {
      this.b = var1;
      return this;
   }

   public PacketDeployFileQuery projectId(long var1) {
      this.c = var1;
      return this;
   }
}
