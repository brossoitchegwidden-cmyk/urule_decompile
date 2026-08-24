package com.bstek.urule.console.editor.constant;

import com.bstek.urule.Utils;
import com.bstek.urule.builder.resource.ResourceType;
import com.bstek.urule.console.database.manager.file.FileManager;
import com.bstek.urule.console.database.manager.file.FileQuery;
import com.bstek.urule.console.database.manager.project.ProjectQueryImpl;
import com.bstek.urule.console.database.model.RuleFile;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.console.xml.DocumentHelper;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.library.constant.ConstantLibrary;
import com.bstek.urule.parse.deserializer.ConstantLibraryDeserializer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.springframework.context.ApplicationContext;

public class ConstantLoader {
   public static final ConstantLoader ins = new ConstantLoader();

   private ConstantLoader() {
   }

   public List load(String groupId, long projectId) {
      Connection connection = JdbcUtils.getConnection();

      ArrayList loadResult;
      try {
         List items = this.loadCommonProjectIds(connection, groupId);
         items.add(projectId);
         String replacedText = "select ID_,CONTENT_ from URULE_FILE where TYPE_=? and PROJECT_ID_ in (ids)";
         StringBuilder stringBuilder = new StringBuilder();

         for(int index = 0; index < items.size(); ++index) {
            if (index > 0) {
               stringBuilder.append(",");
            }

            stringBuilder.append("?");
         }

         replacedText = replacedText.replace("ids", stringBuilder.toString());
         PreparedStatement preparedStatement = connection.prepareStatement(replacedText);
         preparedStatement.setString(1, ResourceType.ConstantLibrary.name());

         for(int index2 = 0; index2 < items.size(); ++index2) {
            long longValue = (Long)items.get(index2);
            preparedStatement.setLong(2 + index2, longValue);
         }

         ResultSet resultSet = preparedStatement.executeQuery();
         HashMap valuesByKey = new HashMap();
         ArrayList items2 = new ArrayList();

         while(resultSet.next()) {
            long longValue2 = resultSet.getLong(1);
            items2.add(longValue2);
            valuesByKey.put(longValue2, resultSet.getString(2));
         }

         ArrayList items3 = new ArrayList();
         if (items2.size() > 0) {
            for(RuleFile ruleFile : (Iterable<RuleFile>)(Iterable<?>)(this.loadRuleFilesById((List)items2))) {
               long id = ruleFile.getId();
               String path = ruleFile.getPath();
               String text = (String)valuesByKey.get(id);
               ConstantLibrary constantLibrary = this.deserializeConstantLibrary(text);
               ConstantInfo constantInfo = new ConstantInfo(id, path, ruleFile.getType(), constantLibrary.getCategories());
               items3.add(constantInfo);
            }
         }

         JdbcUtils.closeResultSet(resultSet);
         JdbcUtils.closeStatement(preparedStatement);
         loadResult = items3;
      } catch (Exception exception) {
         throw new RuleException(exception);
      } finally {
         JdbcUtils.closeConnection(connection);
      }

      return loadResult;
   }

   private ConstantLibrary deserializeConstantLibrary(String text) {
      ApplicationContext applicationContext = Utils.getApplicationContext();
      ConstantLibraryDeserializer constantLibraryDeserializer = (ConstantLibraryDeserializer)applicationContext.getBean("urule.constantLibraryDeserializer");
      Element element = this.parseRootElement(text);
      if (constantLibraryDeserializer.support(element)) {
         return constantLibraryDeserializer.deserialize(element);
      } else {
         throw new RuleException("Unknow content 【" + text + "】");
      }
   }

   private Element parseRootElement(String text2) {
      try {
         Document text = DocumentHelper.parseText(text2);
         Element rootElement = text.getRootElement();
         return rootElement;
      } catch (DocumentException documentException) {
         throw new RuleException(documentException);
      }
   }

   private List loadRuleFilesById(List items) {
      FileQuery fileQuery = FileManager.ins.newQuery();
      fileQuery.ids(items);
      return fileQuery.list((Long)null);
   }

   private List loadCommonProjectIds(Connection connection, String text) throws Exception {
      ProjectQueryImpl projectQueryImpl = new ProjectQueryImpl();
      projectQueryImpl.type("common");
      projectQueryImpl.groupId(text);
      return projectQueryImpl.listIds();
   }
}
