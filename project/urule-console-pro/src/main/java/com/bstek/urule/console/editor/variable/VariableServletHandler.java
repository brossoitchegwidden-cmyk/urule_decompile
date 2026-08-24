package com.bstek.urule.console.editor.variable;

import com.bstek.urule.ClassUtils;
import com.bstek.urule.console.ApiServletHandler;
import com.bstek.urule.console.editor.ExcelImportUtils;
import com.bstek.urule.console.util.FileUtils;
import com.bstek.urule.console.xml.DocumentHelper;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.library.variable.Act;
import com.bstek.urule.model.library.variable.Variable;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.Element;

public class VariableServletHandler extends ApiServletHandler {
   public void excel(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      InputStream inputStream = FileUtils.uploadFile(req).getInputStream();
      List sheets = ExcelImportUtils.parseSheets(inputStream);
      Map variables = ExcelImportUtils.parseVariables(sheets);
      inputStream.close();
      this.writeObjectToJson(resp, variables);
   }

   public void importXml(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
      ServletFileUpload servletFileUpload = new ServletFileUpload(diskFileItemFactory);
      InputStream inputStream = null;

      try {
         List request = servletFileUpload.parseRequest(req);
         if (request.size() != 1) {
            throw new ServletException("Upload xml file is invalid.");
         }

         FileItem fileItem = (FileItem)request.get(0);
         inputStream = fileItem.getInputStream();
         String text2 = IOUtils.toString(inputStream, "utf-8");
         ArrayList items = new ArrayList();
         Document text = DocumentHelper.parseText(text2);
         Element rootElement = text.getRootElement();
         String text3 = rootElement.attributeValue("clazz");

         for(Object objectValue : rootElement.elements()) {
            if (objectValue != null && objectValue instanceof Element) {
               Element element = (Element)objectValue;
               Variable variable = new Variable();
               variable.setAct(Act.InOut);
               variable.setUuid(UUID.randomUUID().toString());
               variable.setDefaultValue(element.attributeValue("defaultValue"));
               variable.setLabel(element.attributeValue("label"));
               variable.setName(element.attributeValue("name"));
               variable.setType(Datatype.valueOf(element.attributeValue("type")));
               items.add(variable);
            }
         }

         HashMap valuesByKey = new HashMap();
         valuesByKey.put("clazz", text3);
         valuesByKey.put("variables", items);
         this.writeObjectToJson(resp, valuesByKey);
      } catch (Exception exception) {
         throw new ServletException(exception);
      } finally {
         IOUtils.closeQuietly(inputStream);
      }

   }

   public void generateFields(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("clazz");
      Class targetClass = ClassUtils.getTargetClass(parameter);
      List items = ClassUtils.classToVariables(targetClass);
      this.writeObjectToJson(resp, items);
   }

   public String url() {
      return "/variable";
   }
}
