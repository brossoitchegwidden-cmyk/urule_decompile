package com.bstek.urule.console.util;

import com.bstek.urule.Utils;
import com.bstek.urule.builder.KnowledgeBuilder;
import com.bstek.urule.parse.ActionParser;
import com.bstek.urule.parse.LhsParser;
import com.bstek.urule.runtime.DynamicSpringConfigLoader;
import com.bstek.urule.runtime.RemoteDynamicJarsBuilder;
import java.util.Collection;

public class ServiceUtils {
   private static LhsParser a;
   private static Collection b;
   private static KnowledgeBuilder c;
   private static RemoteDynamicJarsBuilder d;
   private static DynamicSpringConfigLoader e;

   public static KnowledgeBuilder getKnowledgeBuilder() {
      if (c == null) {
         c = (KnowledgeBuilder)Utils.getApplicationContext().getBean("urule.knowledgeBuilder");
      }

      return c;
   }

   public static LhsParser getLhsParser() {
      if (a == null) {
         a = (LhsParser)Utils.getApplicationContext().getBean("urule.lhsParser");
      }

      return a;
   }

   public static Collection getActionParsers() {
      if (b == null) {
         b = Utils.getApplicationContext().getBeansOfType(ActionParser.class).values();
      }

      return b;
   }

   public static RemoteDynamicJarsBuilder getRemoteDynamicJarsBuilder() {
      if (d == null) {
         d = (RemoteDynamicJarsBuilder)Utils.getApplicationContext().getBean("urule.remoteDynamicJarsBuilder");
      }

      return d;
   }

   public static DynamicSpringConfigLoader getDynamicSpringConfigLoader() {
      if (e == null) {
         e = (DynamicSpringConfigLoader)Utils.getApplicationContext().getBean("urule.dynamicSpringConfigLoader");
      }

      return e;
   }
}
