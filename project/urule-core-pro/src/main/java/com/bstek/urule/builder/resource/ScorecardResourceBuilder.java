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
   private ReteBuilder reteBuilder;
   private ResourceLibraryBuilder resourceLibraryBuilder;
   private ScorecardDeserializer scorecardDeserializer;
   private RulesRebuilder rulesRebuilder;

   public ScoreRule build(Element root, String file) {
      ScorecardDefinition scorecardDefinition = this.scorecardDeserializer.deserialize(root);
      ScoreRule scoreRule = new ScoreRule();
      scoreRule.setName(scorecardDefinition.getName());
      scoreRule.setFile(file);
      scoreRule.setEffectiveDate(scorecardDefinition.getEffectiveDate());
      scoreRule.setExpiresDate(scorecardDefinition.getExpiresDate());
      scoreRule.setEnabled(scorecardDefinition.getEnabled());
      scoreRule.setSalience(scorecardDefinition.getSalience());
      scoreRule.setDebug(scorecardDefinition.getDebug());
      scoreRule.setScoringBean(scorecardDefinition.getScoringBean());
      scoreRule.setScoringType(scorecardDefinition.getScoringType());
      scoreRule.setAssignTargetType(scorecardDefinition.getAssignTargetType());
      scoreRule.setKeyLabel(scorecardDefinition.getKeyLabel());
      scoreRule.setKeyName(scorecardDefinition.getKeyName());
      scoreRule.setDatatype(scorecardDefinition.getDatatype());
      scoreRule.setVariableCategory(scorecardDefinition.getVariableCategory());
      scoreRule.setVariableName(scorecardDefinition.getVariableName());
      scoreRule.setVariableLabel(scorecardDefinition.getVariableLabel());
      scoreRule.setLibraries(scorecardDefinition.getLibraries());
      List rows = scorecardDefinition.getRows();
      ArrayList items = new ArrayList();

      for (AttributeRow attributeRow : (Iterable<AttributeRow>)(Iterable<?>)(rows)) {
         List conditionRows = attributeRow.getConditionRows();
         int rowNumber = attributeRow.getRowNumber();
         Rule rule = this.buildRule(scorecardDefinition, rowNumber, rowNumber);
         items.add(rule);
         rule.setFile(file);
         rule.setDebug(scorecardDefinition.getDebug());

         for (ConditionRow conditionRow : (Iterable<ConditionRow>)(Iterable<?>)(conditionRows)) {
            int rowNumber2 = conditionRow.getRowNumber();
            Rule rule2 = this.buildRule(scorecardDefinition, rowNumber, rowNumber2);
            rule2.setFile(file);
            rule2.setDebug(scorecardDefinition.getDebug());
            items.add(rule2);
         }
      }

      this.rulesRebuilder.rebuildRules(scorecardDefinition.getLibraries(), items, scorecardDefinition.getPredefines());
      ResourceLibrary resourceLibrary = this.resourceLibraryBuilder.buildResourceLibrary(scorecardDefinition.getLibraries(), scorecardDefinition.getPredefines());
      Rete rete = this.reteBuilder.buildRete(items, resourceLibrary);
      KnowledgeBase knowledgeBase = new KnowledgeBase(rete);
      KnowledgePackageWrapper knowledgePackageWrapper = new KnowledgePackageWrapper(knowledgeBase.getKnowledgePackage());
      scoreRule.setKnowledgePackageWrapper(knowledgePackageWrapper);
      return scoreRule;
   }

   private Rule buildRule(ScorecardDefinition scorecardDefinition, int attributeRow, int rowNumber) {
      Rule rule = this.buildScoreRule(scorecardDefinition, attributeRow, rowNumber);
      rule.getRhs().getActions().addAll(this.buildCustomColumnActions(scorecardDefinition, rowNumber));
      return rule;
   }

   private List<Action> buildCustomColumnActions(ScorecardDefinition scorecardDefinition, int rowNumber) {
      List cells = scorecardDefinition.getCells();
      List customCols = scorecardDefinition.getCustomCols();
      ArrayList items = new ArrayList();

      for (CustomCol customCol : (Iterable<CustomCol>)(Iterable<?>)(customCols)) {
         ScoringAction scoringAction = new ScoringAction(rowNumber, customCol.getName(), null);
         CardCell cardCell = this.fetchCell(cells, rowNumber, customCol.getColNumber());
         scoringAction.setValue(cardCell.getValue());
         items.add(scoringAction);
      }

      return items;
   }

   private Rule buildScoreRule(ScorecardDefinition scorecardDefinition, int attributeRow, int rowNumber) {
      List cells = scorecardDefinition.getCells();
      CardCell cardCell = this.fetchCell(cells, attributeRow, 1);
      CardCell cardCell2 = this.fetchCell(cells, rowNumber, 2);
      CardCell cardCell3 = this.fetchCell(cells, rowNumber, 3);
      Rule rule = this.buildScoreRule(scorecardDefinition, cardCell, cardCell2, cardCell3);
      rule.setName("sc-r" + rowNumber);
      return rule;
   }

   private Rule buildScoreRule(ScorecardDefinition scorecardDefinition, CardCell cardCell, CardCell cardCell2, CardCell cardCell3) {
      Rule rule = new Rule();
      Lhs lhs = new Lhs();
      rule.setLhs(lhs);
      Junction junction = cardCell2.getJoint().getJunction();

      for (Condition condition : cardCell2.getJoint().getConditions()) {
         Criteria criteria = new Criteria();
         criteria.setOp(condition.getOp());
         Left left = new Left();
         VariableLeftPart variableLeftPart = new VariableLeftPart();
         variableLeftPart.setCategoryUuid(scorecardDefinition.getAttributeColVariableCategoryUuid());
         variableLeftPart.setVariableCategory(scorecardDefinition.getAttributeColVariableCategory());
         variableLeftPart.setDatatype(cardCell.getDatatype());
         variableLeftPart.setVariableName(cardCell.getVariableName());
         variableLeftPart.setVariableLabel(cardCell.getVariableLabel());
         variableLeftPart.setUuid(cardCell.getUuid());
         variableLeftPart.setKeyLabel(cardCell.getKeyLabel());
         variableLeftPart.setKeyName(cardCell.getKeyName());
         left.setLeftPart(variableLeftPart);
         criteria.setLeft(left);
         left.setType(LeftType.variable);
         criteria.setValue(condition.getValue());
         junction.addCriterion(criteria);
      }

      if (junction.getCriterions() != null && junction.getCriterions().size() > 0) {
         lhs.setCriterion(junction);
      }

      Rhs rhs = new Rhs();
      rule.setRhs(rhs);
      ScoringAction scoringAction = new ScoringAction(cardCell2.getRow(), "scoring_value", cardCell.getWeight());
      scoringAction.setValue(cardCell3.getValue());
      rhs.addAction(scoringAction);
      return rule;
   }

   private CardCell fetchCell(List<CardCell> cardCells, int row, int column) {
      for (CardCell cardCell : cardCells) {
         if (cardCell.getRow() == row && cardCell.getCol() == column) {
            return cardCell;
         }
      }

      throw new RuleException("CardCell [" + row + "," + column + "] not exist.");
   }

   @Override
   public ResourceType getType() {
      return ResourceType.Scorecard;
   }

   @Override
   public boolean support(Element root) {
      return this.scorecardDeserializer.support(root);
   }

   public void setScorecardDeserializer(ScorecardDeserializer scorecardDeserializer) {
      this.scorecardDeserializer = scorecardDeserializer;
   }

   public void setResourceLibraryBuilder(ResourceLibraryBuilder resourceLibraryBuilder) {
      this.resourceLibraryBuilder = resourceLibraryBuilder;
   }

   public void setReteBuilder(ReteBuilder reteBuilder) {
      this.reteBuilder = reteBuilder;
   }

   public void setRulesRebuilder(RulesRebuilder rulesRebuilder) {
      this.rulesRebuilder = rulesRebuilder;
   }
}
