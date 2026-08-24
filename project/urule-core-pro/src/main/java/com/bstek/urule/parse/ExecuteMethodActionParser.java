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
   public Action parse(Element element) {
      ExecuteMethodAction executeMethodAction = new ExecuteMethodAction();
      executeMethodAction.setCategoryUuid(element.attributeValue("category-uuid"));
      executeMethodAction.setUuid(element.attributeValue("uuid"));
      String text = element.attributeValue("bean");
      executeMethodAction.setBeanId(text);
      executeMethodAction.setBeanLabel(element.attributeValue("bean-label"));
      executeMethodAction.setMethodLabel(element.attributeValue("method-label"));
      executeMethodAction.setMethodName(element.attributeValue("method-name"));
      SpringBean builtinAction = ActionUtils.getBuiltinAction(text);
      if (builtinAction != null) {
         executeMethodAction.setBeanELabel(builtinAction.getEname());
      }

      InvokeKnowledgePackage invokeKnowledgePackage = this.resolveInvokeKnowledgePackage(element);
      if (invokeKnowledgePackage != null) {
         executeMethodAction.setInvokeKnowledgePackage(invokeKnowledgePackage);
      }

      InvokeFile invokeFile = this.resolveInvokeFile(element);
      if (invokeFile != null) {
         executeMethodAction.setInvokeFile(invokeFile);
      }

      List parameters = this.parseParameters(element, this.valueParser);
      executeMethodAction.setParameters(parameters);
      return executeMethodAction;
   }

   private InvokeFile resolveInvokeFile(Element element) {
      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            if (element2.getName().equals("invoke-file")) {
               String text = element2.attributeValue("path");
               String text2 = element2.attributeValue("version");
               long longValue = Long.valueOf(element2.attributeValue("id"));
               InvokeFile invokeFile = new InvokeFile();
               invokeFile.setId(longValue);
               invokeFile.setPath(text);
               if (StringUtils.isNotBlank(text2)) {
                  invokeFile.setVersion(text2);
               }

               return invokeFile;
            }
         }
      }

      return null;
   }

   private InvokeKnowledgePackage resolveInvokeKnowledgePackage(Element element) {
      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            if (element2.getName().equals("knowledge")) {
               String text = element2.attributeValue("project");
               String text2 = element2.attributeValue("name");
               String text3 = element2.attributeValue("code");
               String text4 = element2.attributeValue("package-id");
               if (text4 == null) {
                  text4 = element2.attributeValue("id");
               }

               long longValue = Long.valueOf(text4);
               return new InvokeKnowledgePackage(text, text2, longValue, text3);
            }
         }
      }

      return null;
   }

   @Override
   public boolean support(String name) {
      return name.equals("execute-method");
   }
}
