package com.bstek.urule.console.database.manager.authority;

import com.bstek.urule.console.database.IDGenerator;
import com.bstek.urule.console.database.IDType;
import com.bstek.urule.console.database.model.Authority;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AuthorityManagerImpl implements AuthorityManager {
   public List getAuthoritysByRole(String var1, long var2) {
      Connection var4 = JdbcUtils.getConnection();
      String var5 = "select ID_, ROLE_ID_, RESOURCE_CODE_, AUTH_, ROLE_TYPE_, RESOURCE_TYPE_ from URULE_AUTHORITY where ROLE_ID_=? and ROLE_TYPE_=?";

      ArrayList var15;
      try {
         PreparedStatement var6 = var4.prepareStatement(var5);
         var6.setLong(1, var2);
         var6.setString(2, var1);
         ArrayList var7 = new ArrayList();
         ResultSet var8 = var6.executeQuery();

         while(var8.next()) {
            Authority var9 = new Authority();
            var9.setId(var8.getLong(1));
            var9.setRoleId(var8.getLong(2));
            var9.setResourceCode(var8.getString(3));
            var9.setAuth(var8.getInt(4));
            var9.setRoleType(var8.getString(5));
            var9.setResourceType(var8.getString(6));
            var7.add(var9);
         }

         JdbcUtils.closeResultSet(var8);
         JdbcUtils.closeStatement(var6);
         var15 = var7;
      } catch (Exception var13) {
         throw new RuleException(var13);
      } finally {
         JdbcUtils.closeConnection(var4);
      }

      return var15;
   }

   public void add(Connection var1, Authority var2) {
      String var3 = "insert into URULE_AUTHORITY (ID_, ROLE_ID_, RESOURCE_CODE_, AUTH_, ROLE_TYPE_, RESOURCE_TYPE_) values (?, ?, ?, ?, ?, ?)";

      try {
         PreparedStatement var4 = var1.prepareStatement(var3);
         long var5 = IDGenerator.getInstance().nextId(IDType.AUTHORITY);
         var2.setId(var5);
         var4.setLong(1, var2.getId());
         var4.setLong(2, var2.getRoleId());
         var4.setString(3, var2.getResourceCode());
         var4.setInt(4, var2.getAuth());
         var4.setString(5, var2.getRoleType());
         var4.setString(6, var2.getResourceType());
         var4.executeUpdate();
         JdbcUtils.closeStatement(var4);
      } catch (Exception var7) {
         throw new RuleException(var7);
      }
   }

   public void remove(Connection var1, long var2) {
      try {
         PreparedStatement var4 = var1.prepareStatement("delete FROM URULE_AUTHORITY where ID_=?");
         var4.setLong(1, var2);
         var4.executeUpdate();
         JdbcUtils.closeStatement(var4);
      } catch (Exception var5) {
         throw new RuleException(var5);
      }
   }

   public void removeByRole(String var1, long var2) {
      Connection var4 = JdbcUtils.getConnection();

      try {
         PreparedStatement var5 = var4.prepareStatement("delete FROM URULE_AUTHORITY where ROLE_TYPE_=? and ROLE_ID_=?");
         var5.setString(1, var1);
         var5.setLong(2, var2);
         var5.executeUpdate();
         JdbcUtils.closeStatement(var5);
      } catch (Exception var9) {
         throw new RuleException(var9);
      } finally {
         JdbcUtils.closeConnection(var4);
      }

   }

   public Authority get(String var1, long var2, String var4) {
      Authority var5 = null;
      Connection var6 = JdbcUtils.getConnection();
      String var7 = "select ID_, ROLE_ID_, RESOURCE_CODE_, AUTH_, ROLE_TYPE_, RESOURCE_TYPE_ from URULE_AUTHORITY where ROLE_ID_=? and ROLE_TYPE_=? and RESOURCE_CODE_=?";

      Authority var11;
      try {
         PreparedStatement var8 = var6.prepareStatement(var7);
         var8.setLong(1, var2);
         var8.setString(2, var1);
         var8.setString(3, var4);
         ArrayList var9 = new ArrayList();
         ResultSet var10 = var8.executeQuery();
         if (var10.next()) {
            var5 = new Authority();
            var5.setId(var10.getLong(1));
            var5.setRoleId(var10.getLong(2));
            var5.setResourceCode(var10.getString(3));
            var5.setAuth(var10.getInt(4));
            var5.setRoleType(var10.getString(5));
            var5.setResourceType(var10.getString(6));
            var9.add(var5);
         }

         JdbcUtils.closeResultSet(var10);
         JdbcUtils.closeStatement(var8);
         var11 = var5;
      } catch (Exception var15) {
         throw new RuleException(var15);
      } finally {
         JdbcUtils.closeConnection(var6);
      }

      return var11;
   }

   public List getAuthoritysByCode(String var1, String var2) {
      Connection var3 = JdbcUtils.getConnection();
      String var4 = "select ID_, ROLE_ID_, RESOURCE_CODE_, AUTH_, ROLE_TYPE_, RESOURCE_TYPE_ from URULE_AUTHORITY where RESOURCE_CODE_=? and ROLE_TYPE_=?";

      ArrayList var14;
      try {
         PreparedStatement var5 = var3.prepareStatement(var4);
         var5.setString(1, var2);
         var5.setString(2, var1);
         ArrayList var6 = new ArrayList();
         ResultSet var7 = var5.executeQuery();

         while(var7.next()) {
            Authority var8 = new Authority();
            var8.setId(var7.getLong(1));
            var8.setRoleId(var7.getLong(2));
            var8.setResourceCode(var7.getString(3));
            var8.setAuth(var7.getInt(4));
            var8.setRoleType(var7.getString(5));
            var8.setResourceType(var7.getString(6));
            var6.add(var8);
         }

         JdbcUtils.closeResultSet(var7);
         JdbcUtils.closeStatement(var5);
         var14 = var6;
      } catch (Exception var12) {
         throw new RuleException(var12);
      } finally {
         JdbcUtils.closeConnection(var3);
      }

      return var14;
   }

   public void remove(Connection var1, String var2, long var3, String var5, String var6) {
      try {
         PreparedStatement var7 = var1.prepareStatement("delete FROM URULE_AUTHORITY where ROLE_TYPE_=? and ROLE_ID_=? and RESOURCE_CODE_=?  and RESOURCE_TYPE_=?");
         var7.setString(1, var2);
         var7.setLong(2, var3);
         var7.setString(3, var5);
         var7.setString(4, var6);
         var7.executeUpdate();
         JdbcUtils.closeStatement(var7);
      } catch (Exception var8) {
         throw new RuleException(var8);
      }
   }
}
