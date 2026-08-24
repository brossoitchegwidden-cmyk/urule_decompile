package com.bstek.urule.parse.decisiontree;

import com.bstek.urule.Utils;
import com.bstek.urule.model.decisiontree.ActionTreeNode;
import com.bstek.urule.model.decisiontree.ConditionTreeNode;
import com.bstek.urule.model.decisiontree.TreeNodeType;
import com.bstek.urule.model.decisiontree.VariableTreeNode;
import com.bstek.urule.model.rule.Op;
import com.bstek.urule.parse.Parser;
import com.bstek.urule.parse.ValueParser;
import java.util.ArrayList;
import org.dom4j.Element;

public class ConditionTreeNodeParser implements Parser<ConditionTreeNode> {
   private ValueParser valueParser;
   private VariableTreeNodeParser variableTreeNodeParser;
   private ActionTreeNodeParser actionTreeNodeParser;

   public ConditionTreeNode parse(Element element) {
      ConditionTreeNode conditionTreeNode = new ConditionTreeNode();
      conditionTreeNode.setNodeType(TreeNodeType.condition);
      conditionTreeNode.setOp(Op.valueOf(element.attributeValue("op")));
      ArrayList items = new ArrayList();
      ArrayList items2 = new ArrayList();
      ArrayList items3 = new ArrayList();
      if (this.variableTreeNodeParser == null) {
         this.variableTreeNodeParser = (VariableTreeNodeParser)Utils.getApplicationContext().getBean("urule.variableTreeNodeParser");
      }

      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            String name = element2.getName();
            if (this.valueParser.support(name)) {
               conditionTreeNode.setValue(this.valueParser.parse(element2));
            } else if (this.support(name)) {
               ConditionTreeNode conditionTreeNode2 = this.parse(element2);
               conditionTreeNode2.setParentNode(conditionTreeNode);
               items.add(conditionTreeNode2);
            } else if (this.variableTreeNodeParser.support(name)) {
               VariableTreeNode variableTreeNode = this.variableTreeNodeParser.parse(element2);
               variableTreeNode.setParentNode(conditionTreeNode);
               items3.add(variableTreeNode);
            } else if (this.actionTreeNodeParser.support(name)) {
               ActionTreeNode actionTreeNode = this.actionTreeNodeParser.parse(element2);
               actionTreeNode.setParentNode(conditionTreeNode);
               items2.add(actionTreeNode);
            }
         }
      }

      if (items.size() > 0) {
         conditionTreeNode.setConditionTreeNodes(items);
      }

      if (items2.size() > 0) {
         conditionTreeNode.setActionTreeNodes(items2);
      }

      if (items3.size() > 0) {
         conditionTreeNode.setVariableTreeNodes(items3);
      }

      return conditionTreeNode;
   }

   public void setValueParser(ValueParser valueParser) {
      this.valueParser = valueParser;
   }

   public void setActionTreeNodeParser(ActionTreeNodeParser actionTreeNodeParser) {
      this.actionTreeNodeParser = actionTreeNodeParser;
   }

   @Override
   public boolean support(String name) {
      return name.equals("condition-tree-node");
   }
}
