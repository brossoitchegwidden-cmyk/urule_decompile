package com.bstek.urule.console.database.manager.group;

import com.bstek.urule.console.database.model.Group;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.exception.RuleException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class GroupQueryImpl implements GroupQuery {
   private String a;

   public List list(String var1) {
      Connection var2 = JdbcUtils.getConnection();

      ArrayList var13;
      try {
         String var3 = "SELECT URULE_GROUP.ID_, URULE_GROUP.NAME_, URULE_GROUP.DESC_, URULE_GROUP.CREATE_USER_, URULE_GROUP.CREATE_DATE_ FROM URULE_GROUP  LEFT JOIN URULE_GROUP_USER on URULE_GROUP.ID_=URULE_GROUP_USER.GROUP_ID_ WHERE URULE_GROUP_USER.USER_ID_=?";
         PreparedStatement var4 = var2.prepareStatement(var3);
         var4.setString(1, var1);
         ArrayList var5 = new ArrayList();
         ResultSet var6 = var4.executeQuery();

         while(var6.next()) {
            Group var7 = new Group();
            var7.setId(var6.getString(1));
            var7.setName(var6.getString(2));
            var7.setDesc(var6.getString(3));
            var7.setCreateUser(var6.getString(4));
            var7.setCreateDate(var6.getTimestamp(5));
            var5.add(var7);
         }

         JdbcUtils.closeResultSet(var6);
         JdbcUtils.closeStatement(var4);
         var13 = var5;
      } catch (Exception var11) {
         throw new RuleException(var11);
      } finally {
         JdbcUtils.closeConnection(var2);
      }

      return var13;
   }

   public List list() {
      Connection var1 = JdbcUtils.getConnection();

      ArrayList var13;
      try {
         String var2 = "SELECT URULE_GROUP.ID_, URULE_GROUP.NAME_, URULE_GROUP.DESC_, URULE_GROUP.CREATE_USER_, URULE_GROUP.CREATE_DATE_ FROM URULE_GROUP ";
         String var3 = "";
         if (StringUtils.isNotBlank(this.a)) {
            var3 = " WHERE NAME_=?";
         }

         PreparedStatement var4 = var1.prepareStatement(var2 + var3);
         if (StringUtils.isNotBlank(var3)) {
            var4.setString(1, this.a);
         }

         ArrayList var5 = new ArrayList();
         ResultSet var6 = var4.executeQuery();

         while(var6.next()) {
            Group var7 = new Group();
            var7.setId(var6.getString(1));
            var7.setName(var6.getString(2));
            var7.setDesc(var6.getString(3));
            var7.setCreateUser(var6.getString(4));
            var7.setCreateDate(var6.getTimestamp(5));
            var5.add(var7);
         }

         JdbcUtils.closeResultSet(var6);
         JdbcUtils.closeStatement(var4);
         var13 = var5;
      } catch (Exception var11) {
         throw new RuleException(var11);
      } finally {
         JdbcUtils.closeConnection(var1);
      }

      return var13;
   }

   public GroupQuery name(String var1) {
      this.a = var1;
      return this;
   }
}
