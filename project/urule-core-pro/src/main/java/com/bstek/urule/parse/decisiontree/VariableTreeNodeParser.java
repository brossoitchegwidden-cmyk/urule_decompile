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
   private LeftParser leftParser;
   private ConditionTreeNodeParser conditionTreeNodeParser;

   public VariableTreeNode parse(Element element) {
      VariableTreeNode variableTreeNode = new VariableTreeNode();
      variableTreeNode.setNodeType(TreeNodeType.variable);
      ArrayList items = new ArrayList();

      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            String name = element2.getName();
            if (name.equals("left")) {
               variableTreeNode.setLeft(this.leftParser.parse(element2));
            } else if (this.conditionTreeNodeParser.support(name)) {
               ConditionTreeNode conditionTreeNode = this.conditionTreeNodeParser.parse(element2);
               conditionTreeNode.setParentNode(variableTreeNode);
               items.add(conditionTreeNode);
            }
         }
      }

      if (items.size() > 0) {
         variableTreeNode.setConditionTreeNodes(items);
      }

      return variableTreeNode;
   }

   public void setConditionTreeNodeParser(ConditionTreeNodeParser conditionTreeNodeParser) {
      this.conditionTreeNodeParser = conditionTreeNodeParser;
   }

   public void setLeftParser(LeftParser leftParser) {
      this.leftParser = leftParser;
   }

   @Override
   public boolean support(String name) {
      return name.equals("variable-tree-node");
   }
}
