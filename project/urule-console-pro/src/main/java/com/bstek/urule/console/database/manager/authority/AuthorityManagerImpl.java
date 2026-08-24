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
   public List getAuthoritysByRole(String roleType, long roleId) {
      Connection connection = JdbcUtils.getConnection();
      String text = "select ID_, ROLE_ID_, RESOURCE_CODE_, AUTH_, ROLE_TYPE_, RESOURCE_TYPE_ from URULE_AUTHORITY where ROLE_ID_=? and ROLE_TYPE_=?";

      ArrayList authoritysByRole;
      try {
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setLong(1, roleId);
         preparedStatement.setString(2, roleType);
         ArrayList items = new ArrayList();
         ResultSet resultSet = preparedStatement.executeQuery();

         while(resultSet.next()) {
            Authority authority = new Authority();
            authority.setId(resultSet.getLong(1));
            authority.setRoleId(resultSet.getLong(2));
            authority.setResourceCode(resultSet.getString(3));
            authority.setAuth(resultSet.getInt(4));
            authority.setRoleType(resultSet.getString(5));
            authority.setResourceType(resultSet.getString(6));
            items.add(authority);
         }

         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         authoritysByRole = items;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return authoritysByRole;
   }
   public void add(Connection conn, Authority authority) {
      String text = "insert into URULE_AUTHORITY (ID_, ROLE_ID_, RESOURCE_CODE_, AUTH_, ROLE_TYPE_, RESOURCE_TYPE_) values (?, ?, ?, ?, ?, ?)";

      try {
         PreparedStatement preparedStatement = conn.prepareStatement(text);
         long longValue = IDGenerator.getInstance().nextId(IDType.AUTHORITY);
         authority.setId(longValue);
         preparedStatement.setLong(1, authority.getId());
         preparedStatement.setLong(2, authority.getRoleId());
         preparedStatement.setString(3, authority.getResourceCode());
         preparedStatement.setInt(4, authority.getAuth());
         preparedStatement.setString(5, authority.getRoleType());
         preparedStatement.setString(6, authority.getResourceType());
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      }
   }
   public void remove(Connection conn, long id) {
      try {
         PreparedStatement preparedStatement = conn.prepareStatement("delete FROM URULE_AUTHORITY where ID_=?");
         preparedStatement.setLong(1, id);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      }
   }
   public void removeByRole(String roleType, long roleId) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("delete FROM URULE_AUTHORITY where ROLE_TYPE_=? and ROLE_ID_=?");
         preparedStatement.setString(1, roleType);
         preparedStatement.setLong(2, roleId);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }
   public Authority get(String roleType, long roleId, String code) {
      Authority authority = null;
      Connection connection = JdbcUtils.getConnection();
      String text = "select ID_, ROLE_ID_, RESOURCE_CODE_, AUTH_, ROLE_TYPE_, RESOURCE_TYPE_ from URULE_AUTHORITY where ROLE_ID_=? and ROLE_TYPE_=? and RESOURCE_CODE_=?";

      Authority getResult;
      try {
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setLong(1, roleId);
         preparedStatement.setString(2, roleType);
         preparedStatement.setString(3, code);
         ArrayList items = new ArrayList();
         ResultSet resultSet = preparedStatement.executeQuery();
         if (resultSet.next()) {
            authority = new Authority();
            authority.setId(resultSet.getLong(1));
            authority.setRoleId(resultSet.getLong(2));
            authority.setResourceCode(resultSet.getString(3));
            authority.setAuth(resultSet.getInt(4));
            authority.setRoleType(resultSet.getString(5));
            authority.setResourceType(resultSet.getString(6));
            items.add(authority);
         }

         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         getResult = authority;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return getResult;
   }
   public List getAuthoritysByCode(String roleType, String code) {
      Connection connection = JdbcUtils.getConnection();
      String text = "select ID_, ROLE_ID_, RESOURCE_CODE_, AUTH_, ROLE_TYPE_, RESOURCE_TYPE_ from URULE_AUTHORITY where RESOURCE_CODE_=? and ROLE_TYPE_=?";

      ArrayList authoritysByCode;
      try {
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setString(1, code);
         preparedStatement.setString(2, roleType);
         ArrayList items = new ArrayList();
         ResultSet resultSet = preparedStatement.executeQuery();

         while(resultSet.next()) {
            Authority authority = new Authority();
            authority.setId(resultSet.getLong(1));
            authority.setRoleId(resultSet.getLong(2));
            authority.setResourceCode(resultSet.getString(3));
            authority.setAuth(resultSet.getInt(4));
            authority.setRoleType(resultSet.getString(5));
            authority.setResourceType(resultSet.getString(6));
            items.add(authority);
         }

         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         authoritysByCode = items;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return authoritysByCode;
   }
   public void remove(Connection conn, String roleType, long roleId, String resourceCode, String resourceType) {
      try {
         PreparedStatement preparedStatement = conn.prepareStatement("delete FROM URULE_AUTHORITY where ROLE_TYPE_=? and ROLE_ID_=? and RESOURCE_CODE_=?  and RESOURCE_TYPE_=?");
         preparedStatement.setString(1, roleType);
         preparedStatement.setLong(2, roleId);
         preparedStatement.setString(3, resourceCode);
         preparedStatement.setString(4, resourceType);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      }
   }
}
