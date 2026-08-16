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
   private VariableTreeNodeParser a;
   private RulesRebuilder b;

   public DecisionTree parse(Element var1) {
      DecisionTree var2 = new DecisionTree();
      String var3 = var1.attributeValue("salience");
      if (StringUtils.isNotEmpty(var3)) {
         var2.setSalience(Integer.valueOf(var3));
      }

      String var4 = var1.attributeValue("effective-date");
      SimpleDateFormat var5 = new SimpleDateFormat(Configure.getDateFormat());
      if (StringUtils.isNotEmpty(var4)) {
         try {
            var2.setEffectiveDate(var5.parse(var4));
         } catch (ParseException var17) {
            throw new RuleException(var17);
         }
      }

      String var6 = var1.attributeValue("expires-date");
      if (StringUtils.isNotEmpty(var6)) {
         try {
            var2.setExpiresDate(var5.parse(var6));
         } catch (ParseException var16) {
            throw new RuleException(var16);
         }
      }

      String var7 = var1.attributeValue("enabled");
      if (StringUtils.isNotEmpty(var7)) {
         var2.setEnabled(Boolean.valueOf(var7));
      }

      String var8 = var1.attributeValue("debug");
      if (StringUtils.isNotEmpty(var8)) {
         var2.setDebug(Boolean.valueOf(var8));
      }

      ArrayList var9 = new ArrayList();

      for (Object var11 : var1.elements()) {
         if (var11 != null && var11 instanceof Element) {
            Element var12 = (Element)var11;
            String var13 = var12.getName();
            if (this.a.support(var13)) {
               var2.setVariableTreeNode(this.a.parse(var12));
            }

            Library var14 = this.a(var12);
            if (var14 != null) {
               var9.add(var14);
            } else if (var13.equals("quick-test-data")) {
               String var15 = var12.getTextTrim();
               var2.setQuickTestData(var15);
            } else if (var13.equals("remark")) {
               var2.setRemark(var12.getText());
            }
         }
      }

      var2.setLibraries(var9);
      List var18 = null;
      PredefineGroupDefinition var19 = var2.getPredefineGroup();
      if (var19 != null) {
         var18 = var19.getPredefines();
      }

      ResourceLibrary var20 = this.b.getResourceLibraryBuilder().buildResourceLibrary(var9, var18);
      this.a(var20, var2.getVariableTreeNode());
      return var2;
   }

   private void a(ResourceLibrary var1, TreeNode var2) {
      if (var2 != null) {
         if (var2 instanceof VariableTreeNode) {
            VariableTreeNode var3 = (VariableTreeNode)var2;
            Left var4 = var3.getLeft();
            if (var4 != null) {
               LeftPart var5 = var4.getLeftPart();
               if (var5 != null && var5 instanceof VariableLeftPart) {
                  VariableLeftPart var6 = (VariableLeftPart)var5;
                  VariableData var7 = var1.getVariableByUuid(var6.getCategoryUuid(), var6.getUuid());
                  if (var6.getKeyCategoryUuid() != null) {
                     VariableData var8 = var1.getVariableByUuid(var6.getKeyCategoryUuid(), var6.getKeyUuid());
                     var6.setDatatype(var8.getVariable().getType());
                     var6.setKeyName(var7.getVariable().getName());
                     var6.setKeyLabel(var7.getVariable().getLabel());
                     var6.setVariableName(var8.getVariable().getName());
                     var6.setVariableLabel(var8.getVariable().getLabel());
                  } else {
                     var6.setDatatype(var7.getVariable().getType());
                     var6.setVariableLabel(var7.getVariable().getLabel());
                     var6.setVariableName(var7.getVariable().getName());
                     var6.setVariableCategory(var7.getCategory().getName());
                  }
               }
            }

            List var14 = var3.getConditionTreeNodes();
            if (var14 != null) {
               for (ConditionTreeNode var21 : (Iterable<ConditionTreeNode>)(Iterable<?>)(var14)) {
                  this.a(var1, var21);
               }
            }
         } else if (var2 instanceof ConditionTreeNode) {
            ConditionTreeNode var10 = (ConditionTreeNode)var2;
            Value var12 = var10.getValue();
            if (var12 != null) {
               this.b.rebuildValue(var12, var1, false);
            }

            List var15 = var10.getActionTreeNodes();
            if (var15 != null) {
               for (ActionTreeNode var22 : (Iterable<ActionTreeNode>)(Iterable<?>)(var15)) {
                  this.a(var1, var22);
               }
            }

            List var19 = var10.getConditionTreeNodes();
            if (var19 != null) {
               for (ConditionTreeNode var25 : (Iterable<ConditionTreeNode>)(Iterable<?>)(var19)) {
                  this.a(var1, var25);
               }
            }

            List var24 = var10.getVariableTreeNodes();
            if (var24 != null) {
               for (VariableTreeNode var9 : (Iterable<VariableTreeNode>)(Iterable<?>)(var24)) {
                  this.a(var1, var9);
               }
            }
         } else if (var2 instanceof ActionTreeNode) {
            ActionTreeNode var11 = (ActionTreeNode)var2;
            List var13 = var11.getActions();
            if (var13 != null) {
               for (Action var20 : (Iterable<Action>)(Iterable<?>)(var13)) {
                  if (var20 != null) {
                     this.b.rebuildAction(var20, var1, false);
                  }
               }
            }
         }
      }
   }

   public void setVariableTreeNodeParser(VariableTreeNodeParser var1) {
      this.a = var1;
   }

   @Override
   public boolean support(String var1) {
      return var1.equals("decision-tree");
   }

   public void setRulesRebuilder(RulesRebuilder var1) {
      this.b = var1;
   }
}
