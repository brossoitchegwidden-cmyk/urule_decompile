package com.bstek.urule.console.editor.constant;

import com.bstek.urule.ClassUtils;
import com.bstek.urule.console.ApiServletHandler;
import com.bstek.urule.console.editor.ExcelImportUtils;
import com.bstek.urule.console.util.FileUtils;
import com.bstek.urule.exception.RuleException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ConstantServletHandler extends ApiServletHandler {
   public void excel(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      InputStream inputStream = FileUtils.uploadFile(req).getInputStream();
      List sheets = ExcelImportUtils.parseSheets(inputStream);
      Map variables = ExcelImportUtils.parseVariables(sheets);
      inputStream.close();
      this.writeObjectToJson(resp, variables);
   }

   public void generateConstants(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("clazz");
      Class targetClass = ClassUtils.getTargetClass(parameter);
      if (!targetClass.isEnum()) {
         throw new RuleException("[" + targetClass + "]");
      } else {
         Enum[] enumConstants = (Enum[])targetClass.getEnumConstants();
         Method method = this.resolveMethod(targetClass);
         ArrayList items = new ArrayList();

         for(Enum localValue : enumConstants) {
            String text = localValue.name();
            EnumData enumData = new EnumData();
            enumData.setName(text);
            if (method != null) {
               Object objectValue = method.invoke(localValue);
               if (objectValue != null) {
                  enumData.setLabel(objectValue.toString());
               }
            }

            if (enumData.getLabel() == null) {
               enumData.setLabel(enumData.getName());
            }

            items.add(enumData);
         }

         this.writeObjectToJson(resp, items);
      }
   }

   private Method resolveMethod(Class valueType) throws Exception {
      Method method = null;

      for(Method method2 : valueType.getMethods()) {
         if (method2.getName().equals("getLabel")) {
            method = method2;
            break;
         }
      }

      return method;
   }

   public String url() {
      return "/constant";
   }
}
