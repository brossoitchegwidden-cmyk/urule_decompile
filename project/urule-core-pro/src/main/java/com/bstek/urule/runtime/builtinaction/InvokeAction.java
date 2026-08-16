package com.bstek.urule.runtime.builtinaction;

import com.bstek.urule.model.library.action.annotation.ActionBean;
import com.bstek.urule.model.library.action.annotation.ActionMethod;
import com.bstek.urule.model.library.action.annotation.ActionMethodParameter;

@ActionBean(name = "调用", ename = "Invoke")
public class InvokeAction {
   @ActionMethod(name = "知识包")
   @ActionMethodParameter(names = "知识包", enames = "packetName")
   public void invokeKnowledgePackage(String var1) {
      System.out.println("invoke knowledge package : " + var1);
   }

   @ActionMethod(name = "规则文件")
   @ActionMethodParameter(names = "文件", enames = "filePath")
   public void invokeFile(String var1) {
      System.out.println("invoke rule file : " + var1);
   }
}
