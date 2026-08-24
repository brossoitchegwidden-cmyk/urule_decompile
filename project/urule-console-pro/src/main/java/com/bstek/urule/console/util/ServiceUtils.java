package com.bstek.urule.console.util;

import com.bstek.urule.Utils;
import com.bstek.urule.builder.KnowledgeBuilder;
import com.bstek.urule.parse.ActionParser;
import com.bstek.urule.parse.LhsParser;
import com.bstek.urule.runtime.DynamicSpringConfigLoader;
import com.bstek.urule.runtime.RemoteDynamicJarsBuilder;
import java.util.Collection;

public class ServiceUtils {
   private static LhsParser lhsParser;
   private static Collection actionParsers;
   private static KnowledgeBuilder knowledgeBuilder;
   private static RemoteDynamicJarsBuilder remoteDynamicJarsBuilder;
   private static DynamicSpringConfigLoader dynamicSpringConfigLoader;

   public static KnowledgeBuilder getKnowledgeBuilder() {
      if (ServiceUtils.knowledgeBuilder == null) {
         ServiceUtils.knowledgeBuilder = (KnowledgeBuilder)Utils.getApplicationContext().getBean("urule.knowledgeBuilder");
      }

      return ServiceUtils.knowledgeBuilder;
   }

   public static LhsParser getLhsParser() {
      if (ServiceUtils.lhsParser == null) {
         ServiceUtils.lhsParser = (LhsParser)Utils.getApplicationContext().getBean("urule.lhsParser");
      }

      return ServiceUtils.lhsParser;
   }

   public static Collection getActionParsers() {
      if (ServiceUtils.actionParsers == null) {
         ServiceUtils.actionParsers = Utils.getApplicationContext().getBeansOfType(ActionParser.class).values();
      }

      return ServiceUtils.actionParsers;
   }

   public static RemoteDynamicJarsBuilder getRemoteDynamicJarsBuilder() {
      if (ServiceUtils.remoteDynamicJarsBuilder == null) {
         ServiceUtils.remoteDynamicJarsBuilder = (RemoteDynamicJarsBuilder)Utils.getApplicationContext().getBean("urule.remoteDynamicJarsBuilder");
      }

      return ServiceUtils.remoteDynamicJarsBuilder;
   }

   public static DynamicSpringConfigLoader getDynamicSpringConfigLoader() {
      if (ServiceUtils.dynamicSpringConfigLoader == null) {
         ServiceUtils.dynamicSpringConfigLoader = (DynamicSpringConfigLoader)Utils.getApplicationContext().getBean("urule.dynamicSpringConfigLoader");
      }

      return ServiceUtils.dynamicSpringConfigLoader;
   }
}
