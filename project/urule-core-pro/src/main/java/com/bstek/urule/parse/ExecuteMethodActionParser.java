package com.bstek.urule.parse;

import com.bstek.urule.action.Action;
import com.bstek.urule.action.ExecuteMethodAction;
import com.bstek.urule.action.InvokeFile;
import com.bstek.urule.action.InvokeKnowledgePackage;
import com.bstek.urule.model.library.action.SpringBean;
import com.bstek.urule.runtime.ActionUtils;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

public class ExecuteMethodActionParser extends ActionParser {
   public Action parse(Element var1) {
      ExecuteMethodAction var2 = new ExecuteMethodAction();
      var2.setCategoryUuid(var1.attributeValue("category-uuid"));
      var2.setUuid(var1.attributeValue("uuid"));
      String var3 = var1.attributeValue("bean");
      var2.setBeanId(var3);
      var2.setBeanLabel(var1.attributeValue("bean-label"));
      var2.setMethodLabel(var1.attributeValue("method-label"));
      var2.setMethodName(var1.attributeValue("method-name"));
      SpringBean var4 = ActionUtils.getBuiltinAction(var3);
      if (var4 != null) {
         var2.setBeanELabel(var4.getEname());
      }

      InvokeKnowledgePackage var5 = this.b(var1);
      if (var5 != null) {
         var2.setInvokeKnowledgePackage(var5);
      }

      InvokeFile var6 = this.a(var1);
      if (var6 != null) {
         var2.setInvokeFile(var6);
      }

      List var7 = this.a(var1, this.a);
      var2.setParameters(var7);
      return var2;
   }

   private InvokeFile a(Element var1) {
      for (Object var3 : var1.elements()) {
         if (var3 != null && var3 instanceof Element) {
            Element var4 = (Element)var3;
            if (var4.getName().equals("invoke-file")) {
               String var5 = var4.attributeValue("path");
               String var6 = var4.attributeValue("version");
               long var7 = Long.valueOf(var4.attributeValue("id"));
               InvokeFile var9 = new InvokeFile();
               var9.setId(var7);
               var9.setPath(var5);
               if (StringUtils.isNotBlank(var6)) {
                  var9.setVersion(var6);
               }

               return var9;
            }
         }
      }

      return null;
   }

   private InvokeKnowledgePackage b(Element var1) {
      for (Object var3 : var1.elements()) {
         if (var3 != null && var3 instanceof Element) {
            Element var4 = (Element)var3;
            if (var4.getName().equals("knowledge")) {
               String var5 = var4.attributeValue("project");
               String var6 = var4.attributeValue("name");
               String var7 = var4.attributeValue("code");
               String var8 = var4.attributeValue("package-id");
               if (var8 == null) {
                  var8 = var4.attributeValue("id");
               }

               long var9 = Long.valueOf(var8);
               return new InvokeKnowledgePackage(var5, var6, var9, var7);
            }
         }
      }

      return null;
   }

   @Override
   public boolean support(String var1) {
      return var1.equals("execute-method");
   }
}
