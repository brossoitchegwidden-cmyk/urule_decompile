package com.bstek.urule.parse.decisiontree;

import com.bstek.urule.model.decisiontree.ActionTreeNode;
import com.bstek.urule.model.decisiontree.TreeNodeType;
import com.bstek.urule.parse.ActionParser;
import com.bstek.urule.parse.Parser;
import java.util.ArrayList;
import java.util.Collection;
import org.dom4j.Element;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ActionTreeNodeParser implements Parser<ActionTreeNode>, ApplicationContextAware {
   private Collection<ActionParser> actionParsers;

   public ActionTreeNode parse(Element element) {
      ActionTreeNode actionTreeNode = new ActionTreeNode();
      actionTreeNode.setNodeType(TreeNodeType.action);
      ArrayList items = new ArrayList();

      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            String name = element2.getName();

            for (ActionParser actionParser : this.actionParsers) {
               if (actionParser.support(name)) {
                  items.add(actionParser.parse(element2));
                  break;
               }
            }
         }
      }

      actionTreeNode.setActions(items);
      return actionTreeNode;
   }

   @Override
   public boolean support(String name) {
      return name.equals("action-tree-node");
   }

   public void setApplicationContext(ApplicationContext context) throws BeansException {
      this.actionParsers = context.getBeansOfType(ActionParser.class).values();
   }
}
