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
   public void excel(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      InputStream var3 = FileUtils.uploadFile(var1).getInputStream();
      List var4 = ExcelImportUtils.parseSheets(var3);
      Map var5 = ExcelImportUtils.parseVariables(var4);
      var3.close();
      this.a(var2, var5);
   }

   public void importXml(HttpServletRequest var1, HttpServletResponse var2) throws ServletException, IOException {
      DiskFileItemFactory var3 = new DiskFileItemFactory();
      ServletFileUpload var4 = new ServletFileUpload(var3);
      InputStream var5 = null;

      try {
         List var6 = var4.parseRequest(var1);
         if (var6.size() != 1) {
            throw new ServletException("Upload xml file is invalid.");
         }

         FileItem var7 = (FileItem)var6.get(0);
         var5 = var7.getInputStream();
         String var8 = IOUtils.toString(var5, "utf-8");
         ArrayList var9 = new ArrayList();
         Document var10 = DocumentHelper.parseText(var8);
         Element var11 = var10.getRootElement();
         String var12 = var11.attributeValue("clazz");

         for(Object var14 : var11.elements()) {
            if (var14 != null && var14 instanceof Element) {
               Element var15 = (Element)var14;
               Variable var16 = new Variable();
               var16.setAct(Act.InOut);
               var16.setUuid(UUID.randomUUID().toString());
               var16.setDefaultValue(var15.attributeValue("defaultValue"));
               var16.setLabel(var15.attributeValue("label"));
               var16.setName(var15.attributeValue("name"));
               var16.setType(Datatype.valueOf(var15.attributeValue("type")));
               var9.add(var16);
            }
         }

         HashMap var22 = new HashMap();
         var22.put("clazz", var12);
         var22.put("variables", var9);
         this.a(var2, var22);
      } catch (Exception var20) {
         throw new ServletException(var20);
      } finally {
         IOUtils.closeQuietly(var5);
      }

   }

   public void generateFields(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("clazz");
      Class var4 = ClassUtils.getTargetClass(var3);
      List var5 = ClassUtils.classToVariables(var4);
      this.a(var2, var5);
   }

   public String url() {
      return "/variable";
   }
}
