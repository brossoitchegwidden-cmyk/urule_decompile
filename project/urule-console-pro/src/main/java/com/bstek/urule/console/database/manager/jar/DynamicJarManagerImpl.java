package com.bstek.urule.console.database.manager.jar;

import com.bstek.urule.Utils;
import com.bstek.urule.console.database.IDGenerator;
import com.bstek.urule.console.database.IDType;
import com.bstek.urule.console.database.model.DynamicJar;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.console.util.MD5Utils;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.runtime.DynamicSpringConfigLoader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import org.apache.commons.io.IOUtils;

public class DynamicJarManagerImpl implements DynamicJarManager {
   protected DynamicJarManagerImpl() {
   }

   public int createJarFiles(String var1) {
      int var2 = 0;
      StringBuilder var3 = new StringBuilder();

      for(DynamicJar var6 : (Iterable<DynamicJar>)(Iterable<?>)(DynamicJarManager.ins.newQuery().list())) {
         byte[] var7 = DynamicJarManager.ins.loadJar(var6.getId());
         if (var7 != null) {
            ++var2;
            this.a(var6.getName(), var7, var1);
            var3.append(var6.getId());
         }
      }

      String var8 = MD5Utils.stringToMD5(var3.toString());
      DynamicSpringConfigLoader var9 = (DynamicSpringConfigLoader)Utils.getApplicationContext().getBean("urule.dynamicSpringConfigLoader");
      var9.resetDynamicJarsIdDigest(var8);
      return var2;
   }

   private void a(String var1, byte[] var2, String var3) {
      File var4 = new File(var3);
      if (!var4.exists()) {
         var4.mkdirs();
      }

      try {
         String var5 = var3 + "/" + var1;
         ByteArrayInputStream var6 = new ByteArrayInputStream(var2);
         File var7 = new File(var5);
         if (!var7.exists()) {
            var7.createNewFile();
         }

         FileOutputStream var8 = new FileOutputStream(var7);
         IOUtils.copy(var6, var8);
         IOUtils.closeQuietly(var8);
         IOUtils.closeQuietly(var6);
      } catch (Exception var9) {
         throw new RuleException(var9);
      }
   }

   public byte[] loadJar(long var1) {
      String var3 = "select JAR_ from URULE_DYNAMIC_JAR where ID_=?";
      Connection var4 = JdbcUtils.getConnection();

      byte[] var8;
      try {
         PreparedStatement var5 = var4.prepareStatement(var3);
         var5.setLong(1, var1);
         ResultSet var6 = var5.executeQuery();
         byte[] var7 = null;
         if (var6.next()) {
            var7 = var6.getBytes(1);
         }

         JdbcUtils.closeResultSet(var6);
         JdbcUtils.closeStatement(var5);
         var8 = var7;
      } catch (Exception var12) {
         throw new RuleException(var12);
      } finally {
         JdbcUtils.closeConnection(var4);
      }

      return var8;
   }

   public DynamicJar load(long var1) {
      List var3 = this.newQuery().id(var1).list();
      return var3.size() > 0 ? (DynamicJar)var3.get(0) : null;
   }

   public void add(DynamicJar var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         var1.setCreateDate(new Date());
         var1.setUpdateDate(new Date());
         var1.setId(IDGenerator.getInstance().nextId(IDType.DYNAMIC_JAR));
         PreparedStatement var3 = var2.prepareStatement("insert into URULE_DYNAMIC_JAR(ID_, NAME_,DESC_, CREATE_DATE_, UPDATE_DATE_,CREATE_USER_,UPDATE_USER_,GROUP_ID_) values (?, ?, ?, ?, ?, ?, ?,?)");
         var3.setLong(1, var1.getId());
         var3.setString(2, var1.getName());
         var3.setString(3, var1.getDesc());
         var3.setTimestamp(4, new Timestamp(var1.getCreateDate().getTime()));
         var3.setTimestamp(5, new Timestamp(var1.getUpdateDate().getTime()));
         var3.setString(6, var1.getCreateUser());
         var3.setString(7, var1.getCreateUser());
         var3.setString(8, var1.getGroupId());
         var3.executeUpdate();
         JdbcUtils.closeStatement(var3);
      } catch (Exception var7) {
         throw new RuleException(var7);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public void update(DynamicJar var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         var1.setUpdateDate(new Date());
         PreparedStatement var3 = var2.prepareStatement("update URULE_DYNAMIC_JAR set DESC_=?, UPDATE_DATE_=? , UPDATE_USER_=? where ID_=?");
         var3.setString(1, var1.getDesc());
         var3.setTimestamp(2, new Timestamp(var1.getUpdateDate().getTime()));
         var3.setString(3, var1.getUpdateUser());
         var3.setLong(4, var1.getId());
         var3.executeUpdate();
         JdbcUtils.closeStatement(var3);
      } catch (Exception var7) {
         throw new RuleException(var7);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public void delete(long var1) {
      Connection var3 = JdbcUtils.getConnection();

      try {
         PreparedStatement var4 = var3.prepareStatement("delete FROM URULE_DYNAMIC_JAR where ID_=?");
         var4.setLong(1, var1);
         var4.executeUpdate();
         JdbcUtils.closeStatement(var4);
      } catch (Exception var8) {
         throw new RuleException(var8);
      } finally {
         JdbcUtils.closeConnection(var3);
      }

   }

   public void updateJar(long var1, String var3, String var4, byte[] var5) {
      Connection var6 = JdbcUtils.getConnection();

      try {
         PreparedStatement var7 = var6.prepareStatement("update URULE_DYNAMIC_JAR set NAME_=?,JAR_=?,UPDATE_DATE_=? , UPDATE_USER_=?  where ID_=?");
         var7.setString(1, var3);
         var7.setBytes(2, var5);
         var7.setTimestamp(3, new Timestamp((new Date()).getTime()));
         var7.setString(4, var4);
         var7.setLong(5, var1);
         var7.executeUpdate();
         JdbcUtils.closeStatement(var7);
      } catch (Exception var11) {
         throw new RuleException(var11);
      } finally {
         JdbcUtils.closeConnection(var6);
      }

   }

   public DynamicJarQuery newQuery() {
      return new DynamicJarQueryImpl();
   }
}
