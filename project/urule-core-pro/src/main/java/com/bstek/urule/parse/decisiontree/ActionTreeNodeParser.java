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
   private Collection<ActionParser> a;

   public ActionTreeNode parse(Element var1) {
      ActionTreeNode var2 = new ActionTreeNode();
      var2.setNodeType(TreeNodeType.action);
      ArrayList var3 = new ArrayList();

      for (Object var5 : var1.elements()) {
         if (var5 != null && var5 instanceof Element) {
            Element var6 = (Element)var5;
            String var7 = var6.getName();

            for (ActionParser var9 : this.a) {
               if (var9.support(var7)) {
                  var3.add(var9.parse(var6));
                  break;
               }
            }
         }
      }

      var2.setActions(var3);
      return var2;
   }

   @Override
   public boolean support(String var1) {
      return var1.equals("action-tree-node");
   }

   public void setApplicationContext(ApplicationContext var1) throws BeansException {
      this.a = var1.getBeansOfType(ActionParser.class).values();
   }
}
