package com.bstek.urule.runtime.builtinaction;

import com.bstek.urule.model.library.action.annotation.ActionBean;
import com.bstek.urule.model.library.action.annotation.ActionMethod;
import com.bstek.urule.model.library.action.annotation.ActionMethodParameter;

@ActionBean(name = "调用", ename = "Invoke")
public class InvokeAction {
   private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(InvokeAction.class.getName());

   @ActionMethod(name = "知识包")
   @ActionMethodParameter(names = "知识包", enames = "packetName")
   public void invokeKnowledgePackage(String packageName) {
      LOGGER.info("Invoke knowledge package: " + packageName);
   }

   @ActionMethod(name = "规则文件")
   @ActionMethodParameter(names = "文件", enames = "filePath")
   public void invokeFile(String path) {
      LOGGER.info("Invoke rule file: " + path);
   }
}
