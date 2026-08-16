package com.bstek.urule.console.editor.parameter;

import com.bstek.urule.Utils;
import com.bstek.urule.builder.resource.ResourceType;
import com.bstek.urule.console.ApiServletHandler;
import com.bstek.urule.console.database.manager.file.FileManager;
import com.bstek.urule.console.database.model.RuleFile;
import com.bstek.urule.console.editor.ExcelImportUtils;
import com.bstek.urule.console.util.FileUtils;
import com.bstek.urule.console.xml.XXESAXReader;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.parse.deserializer.VariableLibraryDeserializer;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ParameterServletHandler extends ApiServletHandler {
   private VariableLibraryDeserializer e = (VariableLibraryDeserializer)Utils.getApplicationContext().getBean("urule.variableLibraryDeserializer");

   public void excel(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      InputStream var3 = FileUtils.uploadFile(var1).getInputStream();
      List var4 = ExcelImportUtils.parseSheets(var3);
      List var5 = ExcelImportUtils.parseParameters(var4);
      var3.close();
      this.a(var2, var5);
   }

   public void variables(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = Long.valueOf(var1.getParameter("projectId"));
      List var5 = FileManager.ins.newQuery().type(ResourceType.VariableLibrary.name()).deleted(false).asc("NAME_").list(var3);
      ArrayList var6 = new ArrayList();

      for(RuleFile var8 : (Iterable<RuleFile>)(Iterable<?>)(var5)) {
         String var9 = FileManager.ins.loadContent(var8.getId());
         ByteArrayInputStream var10 = new ByteArrayInputStream(var9.getBytes("utf-8"));
         Element var11 = this.a(var10);
         List var12 = this.e.deserialize(var11);
         var6.addAll(var12);
         ((InputStream)var10).close();
      }

      this.a(var2, var6);
   }

   protected Element a(InputStream var1) {
      XXESAXReader var2 = new XXESAXReader();

      try {
         Document var3 = ((SAXReader)var2).read(var1);
         Element var4 = var3.getRootElement();
         return var4;
      } catch (DocumentException var5) {
         throw new RuleException(var5);
      }
   }

   public String url() {
      return "/parameter";
   }
}
