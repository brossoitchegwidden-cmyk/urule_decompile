package com.bstek.urule.builder.resource;

import com.bstek.urule.action.Action;
import com.bstek.urule.action.ScoringAction;
import com.bstek.urule.builder.KnowledgeBase;
import com.bstek.urule.builder.ResourceLibraryBuilder;
import com.bstek.urule.builder.RulesRebuilder;
import com.bstek.urule.builder.rete.ReteBuilder;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.library.ResourceLibrary;
import com.bstek.urule.model.rete.Rete;
import com.bstek.urule.model.rule.Rhs;
import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.model.rule.lhs.Criteria;
import com.bstek.urule.model.rule.lhs.Junction;
import com.bstek.urule.model.rule.lhs.Left;
import com.bstek.urule.model.rule.lhs.LeftType;
import com.bstek.urule.model.rule.lhs.Lhs;
import com.bstek.urule.model.rule.lhs.VariableLeftPart;
import com.bstek.urule.model.scorecard.AttributeRow;
import com.bstek.urule.model.scorecard.CardCell;
import com.bstek.urule.model.scorecard.ConditionRow;
import com.bstek.urule.model.scorecard.CustomCol;
import com.bstek.urule.model.scorecard.ScorecardDefinition;
import com.bstek.urule.model.scorecard.runtime.ScoreRule;
import com.bstek.urule.model.table.Condition;
import com.bstek.urule.parse.deserializer.ScorecardDeserializer;
import com.bstek.urule.runtime.KnowledgePackageWrapper;
import java.util.ArrayList;
import java.util.List;
import org.dom4j.Element;

public class ScorecardResourceBuilder implements ResourceBuilder<ScoreRule> {
   private ReteBuilder a;
   private ResourceLibraryBuilder b;
   private ScorecardDeserializer c;
   private RulesRebuilder d;

   public ScoreRule build(Element var1, String var2) {
      ScorecardDefinition var3 = this.c.deserialize(var1);
      ScoreRule var4 = new ScoreRule();
      var4.setName(var3.getName());
      var4.setFile(var2);
      var4.setEffectiveDate(var3.getEffectiveDate());
      var4.setExpiresDate(var3.getExpiresDate());
      var4.setEnabled(var3.getEnabled());
      var4.setSalience(var3.getSalience());
      var4.setDebug(var3.getDebug());
      var4.setScoringBean(var3.getScoringBean());
      var4.setScoringType(var3.getScoringType());
      var4.setAssignTargetType(var3.getAssignTargetType());
      var4.setKeyLabel(var3.getKeyLabel());
      var4.setKeyName(var3.getKeyName());
      var4.setDatatype(var3.getDatatype());
      var4.setVariableCategory(var3.getVariableCategory());
      var4.setVariableName(var3.getVariableName());
      var4.setVariableLabel(var3.getVariableLabel());
      var4.setLibraries(var3.getLibraries());
      List var5 = var3.getRows();
      ArrayList var6 = new ArrayList();

      for (AttributeRow var8 : (Iterable<AttributeRow>)(Iterable<?>)(var5)) {
         List var9 = var8.getConditionRows();
         int var10 = var8.getRowNumber();
         Rule var11 = this.a(var3, var10, var10);
         var6.add(var11);
         var11.setFile(var2);
         var11.setDebug(var3.getDebug());

         for (ConditionRow var13 : (Iterable<ConditionRow>)(Iterable<?>)(var9)) {
            int var14 = var13.getRowNumber();
            Rule var15 = this.a(var3, var10, var14);
            var15.setFile(var2);
            var15.setDebug(var3.getDebug());
            var6.add(var15);
         }
      }

      this.d.rebuildRules(var3.getLibraries(), var6, var3.getPredefines());
      ResourceLibrary var16 = this.b.buildResourceLibrary(var3.getLibraries(), var3.getPredefines());
      Rete var17 = this.a.buildRete(var6, var16);
      KnowledgeBase var18 = new KnowledgeBase(var17);
      KnowledgePackageWrapper var19 = new KnowledgePackageWrapper(var18.getKnowledgePackage());
      var4.setKnowledgePackageWrapper(var19);
      return var4;
   }

   private Rule a(ScorecardDefinition var1, int var2, int var3) {
      Rule var4 = this.b(var1, var2, var3);
      var4.getRhs().getActions().addAll(this.a(var1, var3));
      return var4;
   }

   private List<Action> a(ScorecardDefinition var1, int var2) {
      List var3 = var1.getCells();
      List var4 = var1.getCustomCols();
      ArrayList var5 = new ArrayList();

      for (CustomCol var7 : (Iterable<CustomCol>)(Iterable<?>)(var4)) {
         ScoringAction var8 = new ScoringAction(var2, var7.getName(), null);
         CardCell var9 = this.a(var3, var2, var7.getColNumber());
         var8.setValue(var9.getValue());
         var5.add(var8);
      }

      return var5;
   }

   private Rule b(ScorecardDefinition var1, int var2, int var3) {
      List var4 = var1.getCells();
      CardCell var5 = this.a(var4, var2, 1);
      CardCell var6 = this.a(var4, var3, 2);
      CardCell var7 = this.a(var4, var3, 3);
      Rule var8 = this.a(var1, var5, var6, var7);
      var8.setName("sc-r" + var3);
      return var8;
   }

   private Rule a(ScorecardDefinition var1, CardCell var2, CardCell var3, CardCell var4) {
      Rule var5 = new Rule();
      Lhs var6 = new Lhs();
      var5.setLhs(var6);
      Junction var7 = var3.getJoint().getJunction();

      for (Condition var9 : var3.getJoint().getConditions()) {
         Criteria var10 = new Criteria();
         var10.setOp(var9.getOp());
         Left var11 = new Left();
         VariableLeftPart var12 = new VariableLeftPart();
         var12.setCategoryUuid(var1.getAttributeColVariableCategoryUuid());
         var12.setVariableCategory(var1.getAttributeColVariableCategory());
         var12.setDatatype(var2.getDatatype());
         var12.setVariableName(var2.getVariableName());
         var12.setVariableLabel(var2.getVariableLabel());
         var12.setUuid(var2.getUuid());
         var12.setKeyLabel(var2.getKeyLabel());
         var12.setKeyName(var2.getKeyName());
         var11.setLeftPart(var12);
         var10.setLeft(var11);
         var11.setType(LeftType.variable);
         var10.setValue(var9.getValue());
         var7.addCriterion(var10);
      }

      if (var7.getCriterions() != null && var7.getCriterions().size() > 0) {
         var6.setCriterion(var7);
      }

      Rhs var13 = new Rhs();
      var5.setRhs(var13);
      ScoringAction var14 = new ScoringAction(var3.getRow(), "scoring_value", var2.getWeight());
      var14.setValue(var4.getValue());
      var13.addAction(var14);
      return var5;
   }

   private CardCell a(List<CardCell> var1, int var2, int var3) {
      for (CardCell var5 : var1) {
         if (var5.getRow() == var2 && var5.getCol() == var3) {
            return var5;
         }
      }

      throw new RuleException("CardCell [" + var2 + "," + var3 + "] not exist.");
   }

   @Override
   public ResourceType getType() {
      return ResourceType.Scorecard;
   }

   @Override
   public boolean support(Element var1) {
      return this.c.support(var1);
   }

   public void setScorecardDeserializer(ScorecardDeserializer var1) {
      this.c = var1;
   }

   public void setResourceLibraryBuilder(ResourceLibraryBuilder var1) {
      this.b = var1;
   }

   public void setReteBuilder(ReteBuilder var1) {
      this.a = var1;
   }

   public void setRulesRebuilder(RulesRebuilder var1) {
      this.d = var1;
   }
}
