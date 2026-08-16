package com.bstek.urule.parse.flow;

import com.bstek.urule.action.Action;
import com.bstek.urule.builder.RulesRebuilder;
import com.bstek.urule.model.flow.FlowDefinition;
import com.bstek.urule.model.flow.FlowNode;
import com.bstek.urule.model.flow.ScriptNode;
import com.bstek.urule.model.flow.StartNode;
import com.bstek.urule.model.library.ResourceLibrary;
import com.bstek.urule.model.rule.Library;
import com.bstek.urule.parse.LibrariesParser;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class FlowDefinitionParser extends LibrariesParser<FlowDefinition> implements ApplicationContextAware {
   private RulesRebuilder a;
   private Collection<FlowNodeParser> b;

   public FlowDefinition parse(Element var1) {
      FlowDefinition var2 = new FlowDefinition();
      var2.setId(var1.attributeValue("id"));
      String var3 = var1.attributeValue("debug");
      if (StringUtils.isNotBlank(var3)) {
         var2.setDebug(Boolean.valueOf(var3));
      }

      ArrayList var4 = new ArrayList();

      for (Object var6 : var1.elements()) {
         if (var6 != null && var6 instanceof Element) {
            Element var7 = (Element)var6;
            String var8 = var7.getName();
            Library var9 = this.a(var7);
            if (var9 != null) {
               var2.addLibrary(var9);
            } else if (var8.equals("quick-test-data")) {
               String var13 = var7.getTextTrim();
               var2.setQuickTestData(var13);
            } else {
               for (FlowNodeParser var11 : this.b) {
                  if (var11.support(var7.getName())) {
                     FlowNode var12 = (FlowNode)var11.parse(var7);
                     var4.add(var12);
                     if (var12 instanceof StartNode) {
                        var2.setStartNode((StartNode)var12);
                     }
                     break;
                  }
               }
            }
         }
      }

      var2.setNodes(var4);
      var2.buildConnectionToNode();
      this.a(var2);
      return var2;
   }

   private void a(FlowDefinition var1) {
      List var2 = var1.getLibraries();
      if (var2 != null) {
         ResourceLibrary var3 = this.a.getResourceLibraryBuilder().buildResourceLibrary(var2, null);

         for (FlowNode var6 : var1.getNodes()) {
            if (var6 instanceof ScriptNode) {
               ScriptNode var7 = (ScriptNode)var6;
               List var8 = var7.getActionsData();
               if (var8 != null) {
                  for (Action var10 : (Iterable<Action>)(Iterable<?>)(var8)) {
                     this.a.rebuildAction(var10, var3, false);
                  }
               }
            }
         }
      }
   }

   @Override
   public boolean support(String var1) {
      return var1.equals("rule-flow");
   }

   public void setApplicationContext(ApplicationContext var1) throws BeansException {
      this.b = var1.getBeansOfType(FlowNodeParser.class).values();
   }

   public void setRulesRebuilder(RulesRebuilder var1) {
      this.a = var1;
   }
}
