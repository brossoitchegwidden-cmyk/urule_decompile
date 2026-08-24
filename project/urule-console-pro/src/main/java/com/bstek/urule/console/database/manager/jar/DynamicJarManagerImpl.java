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

   public int createJarFiles(String path) {
      int jarFiles = 0;
      StringBuilder stringBuilder = new StringBuilder();

      for(DynamicJar dynamicJar : (Iterable<DynamicJar>)(Iterable<?>)(DynamicJarManager.ins.newQuery().list())) {
         byte[] jar = DynamicJarManager.ins.loadJar(dynamicJar.getId());
         if (jar != null) {
            ++jarFiles;
            this.writeJarFile(dynamicJar.getName(), jar, path);
            stringBuilder.append(dynamicJar.getId());
         }
      }

      String text = MD5Utils.stringToMD5(stringBuilder.toString());
      DynamicSpringConfigLoader dynamicSpringConfigLoader = (DynamicSpringConfigLoader)Utils.getApplicationContext().getBean("urule.dynamicSpringConfigLoader");
      dynamicSpringConfigLoader.resetDynamicJarsIdDigest(text);
      return jarFiles;
   }

   private void writeJarFile(String text, byte[] bytes, String text2) {
      File file = new File(text2);
      if (!file.exists()) {
         file.mkdirs();
      }

      try {
         String text3 = text2 + "/" + text;
         ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
         File file2 = new File(text3);
         if (!file2.exists()) {
            file2.createNewFile();
         }

         FileOutputStream fileOutputStream = new FileOutputStream(file2);
         IOUtils.copy(byteArrayInputStream, fileOutputStream);
         IOUtils.closeQuietly(fileOutputStream);
         IOUtils.closeQuietly(byteArrayInputStream);
      } catch (Exception exception) {
         throw new RuleException(exception);
      }
   }

   public byte[] loadJar(long id) {
      String text = "select JAR_ from URULE_DYNAMIC_JAR where ID_=?";
      Connection connection = JdbcUtils.getConnection();

      byte[] jar;
      try {
         PreparedStatement preparedStatement = connection.prepareStatement(text);
         preparedStatement.setLong(1, id);
         ResultSet resultSet = preparedStatement.executeQuery();
         byte[] bytes = null;
         if (resultSet.next()) {
            bytes = resultSet.getBytes(1);
         }

         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         jar = bytes;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return jar;
   }

   public DynamicJar load(long id) {
      List items = this.newQuery().id(id).list();
      return items.size() > 0 ? (DynamicJar)items.get(0) : null;
   }

   public void add(DynamicJar jar) {
      Connection connection = JdbcUtils.getConnection();

      try {
         jar.setCreateDate(new Date());
         jar.setUpdateDate(new Date());
         jar.setId(IDGenerator.getInstance().nextId(IDType.DYNAMIC_JAR));
         PreparedStatement preparedStatement = connection.prepareStatement("insert into URULE_DYNAMIC_JAR(ID_, NAME_,DESC_, CREATE_DATE_, UPDATE_DATE_,CREATE_USER_,UPDATE_USER_,GROUP_ID_) values (?, ?, ?, ?, ?, ?, ?,?)");
         preparedStatement.setLong(1, jar.getId());
         preparedStatement.setString(2, jar.getName());
         preparedStatement.setString(3, jar.getDesc());
         preparedStatement.setTimestamp(4, new Timestamp(jar.getCreateDate().getTime()));
         preparedStatement.setTimestamp(5, new Timestamp(jar.getUpdateDate().getTime()));
         preparedStatement.setString(6, jar.getCreateUser());
         preparedStatement.setString(7, jar.getCreateUser());
         preparedStatement.setString(8, jar.getGroupId());
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public void update(DynamicJar jar) {
      Connection connection = JdbcUtils.getConnection();

      try {
         jar.setUpdateDate(new Date());
         PreparedStatement preparedStatement = connection.prepareStatement("update URULE_DYNAMIC_JAR set DESC_=?, UPDATE_DATE_=? , UPDATE_USER_=? where ID_=?");
         preparedStatement.setString(1, jar.getDesc());
         preparedStatement.setTimestamp(2, new Timestamp(jar.getUpdateDate().getTime()));
         preparedStatement.setString(3, jar.getUpdateUser());
         preparedStatement.setLong(4, jar.getId());
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public void delete(long id) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("delete FROM URULE_DYNAMIC_JAR where ID_=?");
         preparedStatement.setLong(1, id);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public void updateJar(long id, String fileName, String updateUser, byte[] bytes) {
      Connection connection = JdbcUtils.getConnection();

      try {
         PreparedStatement preparedStatement = connection.prepareStatement("update URULE_DYNAMIC_JAR set NAME_=?,JAR_=?,UPDATE_DATE_=? , UPDATE_USER_=?  where ID_=?");
         preparedStatement.setString(1, fileName);
         preparedStatement.setBytes(2, bytes);
         preparedStatement.setTimestamp(3, new Timestamp((new Date()).getTime()));
         preparedStatement.setString(4, updateUser);
         preparedStatement.setLong(5, id);
         preparedStatement.executeUpdate();
         JdbcUtils.closeStatement(preparedStatement);
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

   }

   public DynamicJarQuery newQuery() {
      return new DynamicJarQueryImpl();
   }
}
