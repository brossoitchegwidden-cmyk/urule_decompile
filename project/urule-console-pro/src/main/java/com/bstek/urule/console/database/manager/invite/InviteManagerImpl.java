package com.bstek.urule.console.database.manager.invite;

import com.bstek.urule.console.database.IDGenerator;
import com.bstek.urule.console.database.IDType;
import com.bstek.urule.console.database.model.Invite;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;

public class InviteManagerImpl implements InviteManager {
   public Invite get(String var1) {
      Connection var2 = JdbcUtils.getConnection();

      Invite var7;
      try {
         String var3 = "select ID_, GROUP_ID_, TYPE_, SECRET_KEY_, EXPIR_DATE_, CREATE_USER_, CREATE_DATE_ from URULE_INVITE where SECRET_KEY_=?";
         PreparedStatement var4 = var2.prepareStatement(var3);
         var4.setString(1, var1);
         ResultSet var5 = var4.executeQuery();
         Invite var6 = null;
         if (var5.next()) {
            var6 = new Invite();
            var6.setId(var5.getLong(1));
            var6.setGroupId(var5.getString(2));
            var6.setType(var5.getString(3));
            var6.setSecretKey(var5.getString(4));
            var6.setExpirDate(var5.getTimestamp(5));
            var6.setCreateUser(var5.getString(6));
            var6.setCreateDate(var5.getTimestamp(7));
         }

         JdbcUtils.closeResultSet(var5);
         JdbcUtils.closeStatement(var4);
         var7 = var6;
      } catch (Exception var11) {
         throw new RuleException(var11);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

      return var7;
   }

   public void add(Invite var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         long var3 = IDGenerator.getInstance().nextId(IDType.INVITE);
         var1.setId(var3);
         var1.setCreateDate(new Date(System.currentTimeMillis()));
         PreparedStatement var5 = var2.prepareStatement("insert into URULE_INVITE (ID_, GROUP_ID_, TYPE_, SECRET_KEY_, EXPIR_DATE_, CREATE_USER_, CREATE_DATE_) values (?, ?, ?, ?, ?, ?, ?)");
         var5.setLong(1, var1.getId());
         var5.setString(2, var1.getGroupId());
         var5.setString(3, var1.getType());
         var5.setString(4, var1.getSecretKey());
         var5.setTimestamp(5, new Timestamp(var1.getExpirDate().getTime()));
         var5.setString(6, var1.getCreateUser());
         var5.setTimestamp(7, new Timestamp(var1.getCreateDate().getTime()));
         var5.executeUpdate();
         JdbcUtils.closeStatement(var5);
      } catch (Exception var9) {
         throw new RuleException(var9);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public void update(Invite var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         PreparedStatement var3 = var2.prepareStatement("update URULE_INVITE set SECRET_KEY_=?, EXPIR_DATE_=? where GROUP_ID_=?");
         var3.setString(1, var1.getSecretKey());
         var3.setTimestamp(2, var1.getExpirDate() == null ? null : new Timestamp(var1.getExpirDate().getTime()));
         var3.setString(3, var1.getGroupId());
         var3.executeUpdate();
         JdbcUtils.closeStatement(var3);
      } catch (Exception var7) {
         throw new RuleException(var7);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public void remove(String var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         PreparedStatement var3 = var2.prepareStatement("delete FROM URULE_INVITE where SECRET_KEY_=?");
         var3.setString(1, var1);
         var3.executeUpdate();
         JdbcUtils.closeStatement(var3);
      } catch (Exception var7) {
         throw new RuleException(var7);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }

   public void removeByGroupId(String var1) {
      Connection var2 = JdbcUtils.getConnection();

      try {
         PreparedStatement var3 = var2.prepareStatement("delete FROM URULE_INVITE where GROUP_ID_=?");
         var3.setString(1, var1);
         var3.executeUpdate();
         JdbcUtils.closeStatement(var3);
      } catch (Exception var7) {
         throw new RuleException(var7);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

   }
}
