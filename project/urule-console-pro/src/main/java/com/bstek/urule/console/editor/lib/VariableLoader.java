package com.bstek.urule.console.editor.lib;

import com.bstek.urule.Utils;
import com.bstek.urule.builder.resource.ResourceType;
import com.bstek.urule.console.database.manager.file.FileManager;
import com.bstek.urule.console.database.manager.file.FileQuery;
import com.bstek.urule.console.database.manager.project.ProjectQueryImpl;
import com.bstek.urule.console.database.model.RuleFile;
import com.bstek.urule.console.database.util.JdbcUtils;
import com.bstek.urule.console.xml.DocumentHelper;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.library.variable.VariableCategory;
import com.bstek.urule.parse.deserializer.ParameterLibraryDeserializer;
import com.bstek.urule.parse.deserializer.VariableLibraryDeserializer;
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

public class VariableLoader {
   public static final VariableLoader ins = new VariableLoader();

   private VariableLoader() {
   }

   public List load(String var1, long var2) {
      Connection var4 = JdbcUtils.getConnection();

      ArrayList var31;
      try {
         List var5 = this.a(var4, var1);
         var5.add(var2);
         String var6 = "select ID_,CONTENT_ from URULE_FILE where (TYPE_=? or TYPE_=?) and PROJECT_ID_ in (ids)";
         StringBuilder var7 = new StringBuilder();

         for(int var8 = 0; var8 < var5.size(); ++var8) {
            if (var8 > 0) {
               var7.append(",");
            }

            var7.append("?");
         }

         var6 = var6.replace("ids", var7.toString());
         PreparedStatement var27 = var4.prepareStatement(var6);
         var27.setString(1, ResourceType.VariableLibrary.name());
         var27.setString(2, ResourceType.ParameterLibrary.name());

         for(int var9 = 0; var9 < var5.size(); ++var9) {
            long var10 = (Long)var5.get(var9);
            var27.setLong(3 + var9, var10);
         }

         ResultSet var28 = var27.executeQuery();
         HashMap var29 = new HashMap();
         ArrayList var11 = new ArrayList();

         while(var28.next()) {
            long var12 = var28.getLong(1);
            var11.add(var12);
            var29.put(var12, var28.getString(2));
         }

         ArrayList var30 = new ArrayList();
         if (var11.size() > 0) {
            for(RuleFile var15 : (Iterable<RuleFile>)(Iterable<?>)(this.a((List)var11))) {
               long var16 = var15.getId();
               String var18 = var15.getPath();
               String var19 = (String)var29.get(var16);
               List var20 = this.a(var19);
               var30.add(new VariableInfo(var16, var18, var15.getType(), var20));
            }
         }

         JdbcUtils.closeResultSet(var28);
         JdbcUtils.closeStatement(var27);
         var31 = var30;
      } catch (Exception var24) {
         throw new RuleException(var24);
      } finally {
         JdbcUtils.closeConnection(var4);
      }

      return var31;
   }

   private List a(String var1) {
      ApplicationContext var2 = Utils.getApplicationContext();
      VariableLibraryDeserializer var3 = (VariableLibraryDeserializer)var2.getBean("urule.variableLibraryDeserializer");
      ParameterLibraryDeserializer var4 = (ParameterLibraryDeserializer)var2.getBean("urule.parameterLibraryDeserializer");
      Element var5 = this.b(var1);
      if (var3.support(var5)) {
         return var3.deserialize(var5);
      } else if (var4.support(var5)) {
         List var6 = var4.deserialize(var5);
         VariableCategory var7 = new VariableCategory();
         var7.setVariables(var6);
         var7.setName("参数");
         var7.setUuid(var7.getName());
         ArrayList var8 = new ArrayList();
         var8.add(var7);
         return var8;
      } else {
         throw new RuleException("Unknow content 【" + var1 + "】");
      }
   }

   private Element b(String var1) {
      try {
         Document var2 = DocumentHelper.parseText(var1);
         Element var3 = var2.getRootElement();
         return var3;
      } catch (DocumentException var4) {
         throw new RuleException(var4);
      }
   }

   private List a(List var1) {
      FileQuery var2 = FileManager.ins.newQuery();
      var2.ids(var1);
      return var2.list((Long)null);
   }

   private List a(Connection var1, String var2) throws Exception {
      ProjectQueryImpl var3 = new ProjectQueryImpl();
      var3.type("common");
      var3.groupId(var2);
      return var3.listIds();
   }
}
