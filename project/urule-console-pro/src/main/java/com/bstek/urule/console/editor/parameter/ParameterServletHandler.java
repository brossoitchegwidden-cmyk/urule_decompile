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
   private VariableLibraryDeserializer variableLibraryDeserializer = (VariableLibraryDeserializer)Utils.getApplicationContext().getBean("urule.variableLibraryDeserializer");

   public void excel(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      InputStream inputStream = FileUtils.uploadFile(req).getInputStream();
      List sheets = ExcelImportUtils.parseSheets(inputStream);
      List parameters = ExcelImportUtils.parseParameters(sheets);
      inputStream.close();
      this.writeObjectToJson(resp, parameters);
   }

   public void variables(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long longValue = Long.valueOf(req.getParameter("projectId"));
      List items = FileManager.ins.newQuery().type(ResourceType.VariableLibrary.name()).deleted(false).asc("NAME_").list(longValue);
      ArrayList items2 = new ArrayList();

      for(RuleFile ruleFile : (Iterable<RuleFile>)(Iterable<?>)(items)) {
         String content = FileManager.ins.loadContent(ruleFile.getId());
         ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(content.getBytes("utf-8"));
         Element element = this.parseXml(byteArrayInputStream);
         List items3 = this.variableLibraryDeserializer.deserialize(element);
         items2.addAll(items3);
         ((InputStream)byteArrayInputStream).close();
      }

      this.writeObjectToJson(resp, items2);
   }

   protected Element parseXml(InputStream stream) {
      XXESAXReader xXESAXReader = new XXESAXReader();

      try {
         Document document = ((SAXReader)xXESAXReader).read(stream);
         Element rootElement = document.getRootElement();
         return rootElement;
      } catch (DocumentException documentException) {
         throw new RuleException(documentException);
      }
   }

   public String url() {
      return "/parameter";
   }
}
