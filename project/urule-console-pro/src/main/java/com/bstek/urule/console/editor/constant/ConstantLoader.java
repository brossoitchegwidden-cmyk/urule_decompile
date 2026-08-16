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

   public List load(String var1, long var2) {
      Connection var4 = JdbcUtils.getConnection();

      ArrayList var32;
      try {
         List var5 = this.a(var4, var1);
         var5.add(var2);
         String var6 = "select ID_,CONTENT_ from URULE_FILE where TYPE_=? and PROJECT_ID_ in (ids)";
         StringBuilder var7 = new StringBuilder();

         for(int var8 = 0; var8 < var5.size(); ++var8) {
            if (var8 > 0) {
               var7.append(",");
            }

            var7.append("?");
         }

         var6 = var6.replace("ids", var7.toString());
         PreparedStatement var28 = var4.prepareStatement(var6);
         var28.setString(1, ResourceType.ConstantLibrary.name());

         for(int var9 = 0; var9 < var5.size(); ++var9) {
            long var10 = (Long)var5.get(var9);
            var28.setLong(2 + var9, var10);
         }

         ResultSet var29 = var28.executeQuery();
         HashMap var30 = new HashMap();
         ArrayList var11 = new ArrayList();

         while(var29.next()) {
            long var12 = var29.getLong(1);
            var11.add(var12);
            var30.put(var12, var29.getString(2));
         }

         ArrayList var31 = new ArrayList();
         if (var11.size() > 0) {
            for(RuleFile var15 : (Iterable<RuleFile>)(Iterable<?>)(this.a((List)var11))) {
               long var16 = var15.getId();
               String var18 = var15.getPath();
               String var19 = (String)var30.get(var16);
               ConstantLibrary var20 = this.a(var19);
               ConstantInfo var21 = new ConstantInfo(var16, var18, var15.getType(), var20.getCategories());
               var31.add(var21);
            }
         }

         JdbcUtils.closeResultSet(var29);
         JdbcUtils.closeStatement(var28);
         var32 = var31;
      } catch (Exception var25) {
         throw new RuleException(var25);
      } finally {
         JdbcUtils.closeConnection(var4);
      }

      return var32;
   }

   private ConstantLibrary a(String var1) {
      ApplicationContext var2 = Utils.getApplicationContext();
      ConstantLibraryDeserializer var3 = (ConstantLibraryDeserializer)var2.getBean("urule.constantLibraryDeserializer");
      Element var4 = this.b(var1);
      if (var3.support(var4)) {
         return var3.deserialize(var4);
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
