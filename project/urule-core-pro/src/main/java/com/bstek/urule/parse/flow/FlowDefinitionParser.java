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
   private RulesRebuilder rulesRebuilder;
   private Collection<FlowNodeParser> flowNodeParsers;

   public FlowDefinition parse(Element element) {
      FlowDefinition flowDefinition = new FlowDefinition();
      flowDefinition.setId(element.attributeValue("id"));
      String text = element.attributeValue("debug");
      if (StringUtils.isNotBlank(text)) {
         flowDefinition.setDebug(Boolean.valueOf(text));
      }

      ArrayList items = new ArrayList();

      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            String name = element2.getName();
            Library library = this.parseLibrary(element2);
            if (library != null) {
               flowDefinition.addLibrary(library);
            } else if (name.equals("quick-test-data")) {
               String textTrim = element2.getTextTrim();
               flowDefinition.setQuickTestData(textTrim);
            } else {
               for (FlowNodeParser flowNodeParser : this.flowNodeParsers) {
                  if (flowNodeParser.support(element2.getName())) {
                     FlowNode flowNode = (FlowNode)flowNodeParser.parse(element2);
                     items.add(flowNode);
                     if (flowNode instanceof StartNode) {
                        flowDefinition.setStartNode((StartNode)flowNode);
                     }
                     break;
                  }
               }
            }
         }
      }

      flowDefinition.setNodes(items);
      flowDefinition.buildConnectionToNode();
      this.processFlowDefinition(flowDefinition);
      return flowDefinition;
   }

   private void processFlowDefinition(FlowDefinition flowDefinition) {
      List libraries = flowDefinition.getLibraries();
      if (libraries != null) {
         ResourceLibrary resourceLibrary = this.rulesRebuilder.getResourceLibraryBuilder().buildResourceLibrary(libraries, null);

         for (FlowNode flowNode : flowDefinition.getNodes()) {
            if (flowNode instanceof ScriptNode) {
               ScriptNode scriptNode = (ScriptNode)flowNode;
               List actionsData = scriptNode.getActionsData();
               if (actionsData != null) {
                  for (Action action : (Iterable<Action>)(Iterable<?>)(actionsData)) {
                     this.rulesRebuilder.rebuildAction(action, resourceLibrary, false);
                  }
               }
            }
         }
      }
   }

   @Override
   public boolean support(String name) {
      return name.equals("rule-flow");
   }

   public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
      this.flowNodeParsers = applicationContext.getBeansOfType(FlowNodeParser.class).values();
   }

   public void setRulesRebuilder(RulesRebuilder rulesRebuilder) {
      this.rulesRebuilder = rulesRebuilder;
   }
}
