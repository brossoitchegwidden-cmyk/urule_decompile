package com.bstek.urule.parse.decisiontree;

import com.bstek.urule.Configure;
import com.bstek.urule.action.Action;
import com.bstek.urule.builder.RulesRebuilder;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.decisiontree.ActionTreeNode;
import com.bstek.urule.model.decisiontree.ConditionTreeNode;
import com.bstek.urule.model.decisiontree.DecisionTree;
import com.bstek.urule.model.decisiontree.TreeNode;
import com.bstek.urule.model.decisiontree.VariableTreeNode;
import com.bstek.urule.model.library.ResourceLibrary;
import com.bstek.urule.model.library.variable.VariableData;
import com.bstek.urule.model.rule.Library;
import com.bstek.urule.model.rule.PredefineGroupDefinition;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.lhs.Left;
import com.bstek.urule.model.rule.lhs.LeftPart;
import com.bstek.urule.model.rule.lhs.VariableLeftPart;
import com.bstek.urule.parse.LibrariesParser;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

public class DecisionTreeParser extends LibrariesParser<DecisionTree> {
   private VariableTreeNodeParser variableTreeNodeParser;
   private RulesRebuilder rulesRebuilder;

   public DecisionTree parse(Element element) {
      DecisionTree decisionTree = new DecisionTree();
      String text = element.attributeValue("salience");
      if (StringUtils.isNotEmpty(text)) {
         decisionTree.setSalience(Integer.valueOf(text));
      }

      String text2 = element.attributeValue("effective-date");
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Configure.getDateFormat());
      if (StringUtils.isNotEmpty(text2)) {
         try {
            decisionTree.setEffectiveDate(simpleDateFormat.parse(text2));
         } catch (ParseException parseException) {
            throw new RuleException(parseException);
         }
      }

      String text3 = element.attributeValue("expires-date");
      if (StringUtils.isNotEmpty(text3)) {
         try {
            decisionTree.setExpiresDate(simpleDateFormat.parse(text3));
         } catch (ParseException parseException2) {
            throw new RuleException(parseException2);
         }
      }

      String text4 = element.attributeValue("enabled");
      if (StringUtils.isNotEmpty(text4)) {
         decisionTree.setEnabled(Boolean.valueOf(text4));
      }

      String text5 = element.attributeValue("debug");
      if (StringUtils.isNotEmpty(text5)) {
         decisionTree.setDebug(Boolean.valueOf(text5));
      }

      ArrayList items = new ArrayList();

      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            String name = element2.getName();
            if (this.variableTreeNodeParser.support(name)) {
               decisionTree.setVariableTreeNode(this.variableTreeNodeParser.parse(element2));
            }

            Library library = this.parseLibrary(element2);
            if (library != null) {
               items.add(library);
            } else if (name.equals("quick-test-data")) {
               String textTrim = element2.getTextTrim();
               decisionTree.setQuickTestData(textTrim);
            } else if (name.equals("remark")) {
               decisionTree.setRemark(element2.getText());
            }
         }
      }

      decisionTree.setLibraries(items);
      List predefines = null;
      PredefineGroupDefinition predefineGroup = decisionTree.getPredefineGroup();
      if (predefineGroup != null) {
         predefines = predefineGroup.getPredefines();
      }

      ResourceLibrary resourceLibrary = this.rulesRebuilder.getResourceLibraryBuilder().buildResourceLibrary(items, predefines);
      this.processResourceLibrary(resourceLibrary, decisionTree.getVariableTreeNode());
      return decisionTree;
   }

   private void processResourceLibrary(ResourceLibrary resourceLibrary, TreeNode treeNode) {
      if (treeNode != null) {
         if (treeNode instanceof VariableTreeNode) {
            VariableTreeNode variableTreeNode2 = (VariableTreeNode)treeNode;
            Left left = variableTreeNode2.getLeft();
            if (left != null) {
               LeftPart leftPart = left.getLeftPart();
               if (leftPart != null && leftPart instanceof VariableLeftPart) {
                  VariableLeftPart variableLeftPart = (VariableLeftPart)leftPart;
                  VariableData variableByUuid = resourceLibrary.getVariableByUuid(variableLeftPart.getCategoryUuid(), variableLeftPart.getUuid());
                  if (variableLeftPart.getKeyCategoryUuid() != null) {
                     VariableData variableByUuid2 = resourceLibrary.getVariableByUuid(variableLeftPart.getKeyCategoryUuid(), variableLeftPart.getKeyUuid());
                     variableLeftPart.setDatatype(variableByUuid2.getVariable().getType());
                     variableLeftPart.setKeyName(variableByUuid.getVariable().getName());
                     variableLeftPart.setKeyLabel(variableByUuid.getVariable().getLabel());
                     variableLeftPart.setVariableName(variableByUuid2.getVariable().getName());
                     variableLeftPart.setVariableLabel(variableByUuid2.getVariable().getLabel());
                  } else {
                     variableLeftPart.setDatatype(variableByUuid.getVariable().getType());
                     variableLeftPart.setVariableLabel(variableByUuid.getVariable().getLabel());
                     variableLeftPart.setVariableName(variableByUuid.getVariable().getName());
                     variableLeftPart.setVariableCategory(variableByUuid.getCategory().getName());
                  }
               }
            }

            List conditionTreeNodes = variableTreeNode2.getConditionTreeNodes();
            if (conditionTreeNodes != null) {
               for (ConditionTreeNode conditionTreeNode : (Iterable<ConditionTreeNode>)(Iterable<?>)(conditionTreeNodes)) {
                  this.processResourceLibrary(resourceLibrary, conditionTreeNode);
               }
            }
         } else if (treeNode instanceof ConditionTreeNode) {
            ConditionTreeNode conditionTreeNode2 = (ConditionTreeNode)treeNode;
            Value localValue = conditionTreeNode2.getValue();
            if (localValue != null) {
               this.rulesRebuilder.rebuildValue(localValue, resourceLibrary, false);
            }

            List actionTreeNodes = conditionTreeNode2.getActionTreeNodes();
            if (actionTreeNodes != null) {
               for (ActionTreeNode actionTreeNode : (Iterable<ActionTreeNode>)(Iterable<?>)(actionTreeNodes)) {
                  this.processResourceLibrary(resourceLibrary, actionTreeNode);
               }
            }

            List conditionTreeNodes2 = conditionTreeNode2.getConditionTreeNodes();
            if (conditionTreeNodes2 != null) {
               for (ConditionTreeNode conditionTreeNode3 : (Iterable<ConditionTreeNode>)(Iterable<?>)(conditionTreeNodes2)) {
                  this.processResourceLibrary(resourceLibrary, conditionTreeNode3);
               }
            }

            List variableTreeNodes = conditionTreeNode2.getVariableTreeNodes();
            if (variableTreeNodes != null) {
               for (VariableTreeNode variableTreeNode : (Iterable<VariableTreeNode>)(Iterable<?>)(variableTreeNodes)) {
                  this.processResourceLibrary(resourceLibrary, variableTreeNode);
               }
            }
         } else if (treeNode instanceof ActionTreeNode) {
            ActionTreeNode actionTreeNode2 = (ActionTreeNode)treeNode;
            List actions = actionTreeNode2.getActions();
            if (actions != null) {
               for (Action action : (Iterable<Action>)(Iterable<?>)(actions)) {
                  if (action != null) {
                     this.rulesRebuilder.rebuildAction(action, resourceLibrary, false);
                  }
               }
            }
         }
      }
   }

   public void setVariableTreeNodeParser(VariableTreeNodeParser variableTreeNodeParser) {
      this.variableTreeNodeParser = variableTreeNodeParser;
   }

   @Override
   public boolean support(String name) {
      return name.equals("decision-tree");
   }

   public void setRulesRebuilder(RulesRebuilder rulesRebuilder) {
      this.rulesRebuilder = rulesRebuilder;
   }
}
