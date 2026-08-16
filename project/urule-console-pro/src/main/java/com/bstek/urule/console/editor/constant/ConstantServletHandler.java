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
   public void excel(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      InputStream var3 = FileUtils.uploadFile(var1).getInputStream();
      List var4 = ExcelImportUtils.parseSheets(var3);
      Map var5 = ExcelImportUtils.parseVariables(var4);
      var3.close();
      this.a(var2, var5);
   }

   public void generateConstants(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("clazz");
      Class var4 = ClassUtils.getTargetClass(var3);
      if (!var4.isEnum()) {
         throw new RuleException("[" + var4 + "]");
      } else {
         Enum[] var6 = (Enum[])var4.getEnumConstants();
         Method var7 = this.a(var4);
         ArrayList var8 = new ArrayList();

         for(Enum var12 : var6) {
            String var13 = var12.name();
            EnumData var14 = new EnumData();
            var14.setName(var13);
            if (var7 != null) {
               Object var15 = var7.invoke(var12);
               if (var15 != null) {
                  var14.setLabel(var15.toString());
               }
            }

            if (var14.getLabel() == null) {
               var14.setLabel(var14.getName());
            }

            var8.add(var14);
         }

         this.a(var2, var8);
      }
   }

   private Method a(Class var1) throws Exception {
      Method var2 = null;

      for(Method var6 : var1.getMethods()) {
         if (var6.getName().equals("getLabel")) {
            var2 = var6;
            break;
         }
      }

      return var2;
   }

   public String url() {
      return "/constant";
   }
}
