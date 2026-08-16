package com.bstek.urule.parse.decisiontree;

import com.bstek.urule.model.decisiontree.ConditionTreeNode;
import com.bstek.urule.model.decisiontree.TreeNodeType;
import com.bstek.urule.model.decisiontree.VariableTreeNode;
import com.bstek.urule.parse.LeftParser;
import com.bstek.urule.parse.Parser;
import java.util.ArrayList;
import org.dom4j.Element;

public class VariableTreeNodeParser implements Parser<VariableTreeNode> {
   public static final String BEAN_ID = "urule.variableTreeNodeParser";
   private LeftParser a;
   private ConditionTreeNodeParser b;

   public VariableTreeNode parse(Element var1) {
      VariableTreeNode var2 = new VariableTreeNode();
      var2.setNodeType(TreeNodeType.variable);
      ArrayList var3 = new ArrayList();

      for (Object var5 : var1.elements()) {
         if (var5 != null && var5 instanceof Element) {
            Element var6 = (Element)var5;
            String var7 = var6.getName();
            if (var7.equals("left")) {
               var2.setLeft(this.a.parse(var6));
            } else if (this.b.support(var7)) {
               ConditionTreeNode var8 = this.b.parse(var6);
               var8.setParentNode(var2);
               var3.add(var8);
            }
         }
      }

      if (var3.size() > 0) {
         var2.setConditionTreeNodes(var3);
      }

      return var2;
   }

   public void setConditionTreeNodeParser(ConditionTreeNodeParser var1) {
      this.b = var1;
   }

   public void setLeftParser(LeftParser var1) {
      this.a = var1;
   }

   @Override
   public boolean support(String var1) {
      return var1.equals("variable-tree-node");
   }
}
