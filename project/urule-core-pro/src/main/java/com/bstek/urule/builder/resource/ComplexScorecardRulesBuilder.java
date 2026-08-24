package com.bstek.urule.builder.resource;

import com.bstek.urule.action.ScoringAction;
import com.bstek.urule.builder.KnowledgeBase;
import com.bstek.urule.builder.ResourceLibraryBuilder;
import com.bstek.urule.builder.RulesRebuilder;
import com.bstek.urule.builder.rete.ReteBuilder;
import com.bstek.urule.builder.table.CellContentBuilder;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.library.ResourceLibrary;
import com.bstek.urule.model.rete.Rete;
import com.bstek.urule.model.rule.Rhs;
import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.lhs.And;
import com.bstek.urule.model.rule.lhs.Criterion;
import com.bstek.urule.model.rule.lhs.Lhs;
import com.bstek.urule.model.scorecard.ComplexColumn;
import com.bstek.urule.model.scorecard.ComplexColumnType;
import com.bstek.urule.model.scorecard.ComplexScorecardDefinition;
import com.bstek.urule.model.scorecard.runtime.ScoreRule;
import com.bstek.urule.model.table.Cell;
import com.bstek.urule.model.table.Row;
import com.bstek.urule.parse.deserializer.ComplexScorecardDeserializer;
import com.bstek.urule.runtime.KnowledgePackageWrapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.dom4j.Element;

public class ComplexScorecardRulesBuilder implements ResourceBuilder<ScoreRule> {
   private ReteBuilder reteBuilder;
   private ResourceLibraryBuilder resourceLibraryBuilder;
   private RulesRebuilder rulesRebuilder;
   private CellContentBuilder cellContentBuilder;
   private ComplexScorecardDeserializer complexScorecardDeserializer;

   public ScoreRule build(Element root, String file) {
      ComplexScorecardDefinition complexScorecardDefinition = this.complexScorecardDeserializer.deserialize(root);
      ScoreRule scoreRule = new ScoreRule();
      scoreRule.setFile(file);
      scoreRule.setName("cc");
      scoreRule.setEffectiveDate(complexScorecardDefinition.getEffectiveDate());
      scoreRule.setExpiresDate(complexScorecardDefinition.getExpiresDate());
      scoreRule.setEnabled(complexScorecardDefinition.getEnabled());
      scoreRule.setSalience(complexScorecardDefinition.getSalience());
      scoreRule.setDebug(complexScorecardDefinition.getDebug());
      scoreRule.setScoringBean(complexScorecardDefinition.getScoringBean());
      scoreRule.setScoringType(complexScorecardDefinition.getScoringType());
      scoreRule.setAssignTargetType(complexScorecardDefinition.getAssignTargetType());
      scoreRule.setKeyName(complexScorecardDefinition.getKeyName());
      scoreRule.setKeyLabel(complexScorecardDefinition.getKeyLabel());
      scoreRule.setDatatype(complexScorecardDefinition.getDatatype());
      scoreRule.setVariableCategory(complexScorecardDefinition.getVariableCategory());
      scoreRule.setVariableName(complexScorecardDefinition.getVariableName());
      scoreRule.setVariableLabel(complexScorecardDefinition.getVariableLabel());
      scoreRule.setLibraries(complexScorecardDefinition.getLibraries());
      ArrayList items = new ArrayList();
      List rows = complexScorecardDefinition.getRows();
      List columns = complexScorecardDefinition.getColumns();

      for (Row row : (Iterable<Row>)(Iterable<?>)(rows)) {
         Rule rule = new Rule();
         rule.setFile(file);
         rule.setDebug(complexScorecardDefinition.getDebug());
         rule.setSalience(complexScorecardDefinition.getSalience());
         rule.setExpiresDate(complexScorecardDefinition.getExpiresDate());
         rule.setEffectiveDate(complexScorecardDefinition.getEffectiveDate());
         rule.setEnabled(complexScorecardDefinition.getEnabled());
         rule.setName("cc-r" + row.getNum());
         Lhs lhs = new Lhs();
         And and = new And();
         rule.setLhs(lhs);
         Rhs rhs = new Rhs();
         rule.setRhs(rhs);
         items.add(rule);
         Value localValue = null;

         for (ComplexColumn complexColumn : (Iterable<ComplexColumn>)(Iterable<?>)(columns)) {
            Cell cell = this.resolveCell(complexScorecardDefinition, row.getNum(), complexColumn.getNum());
            ComplexColumnType type = complexColumn.getType();
            switch (type) {
               case Criteria:
                  Criterion criterion = this.cellContentBuilder.buildCriterion(cell, complexColumn);
                  if (criterion != null) {
                     and.addCriterion(criterion);
                  }
                  break;
               case Score:
                  localValue = cell.getValue();
                  if (localValue != null) {
                     ScoringAction scoringAction = new ScoringAction(row.getNum(), "scoring_value", null);
                     scoringAction.setValue(localValue);
                     rhs.addAction(scoringAction);
                  }
                  break;
               case Custom:
                  localValue = cell.getValue();
                  if (localValue != null) {
                     ScoringAction scoringAction2 = new ScoringAction(row.getNum(), complexColumn.getCustomLabel(), null);
                     scoringAction2.setValue(localValue);
                     rhs.addAction(scoringAction2);
                  }
            }
         }

         if (and.getCriterions() != null && and.getCriterions().size() > 0) {
            lhs.setCriterion(and);
         }
      }

      this.rulesRebuilder.rebuildRules(complexScorecardDefinition.getLibraries(), items, complexScorecardDefinition.getPredefines());
      ResourceLibrary resourceLibrary = this.resourceLibraryBuilder.buildResourceLibrary(complexScorecardDefinition.getLibraries(), complexScorecardDefinition.getPredefines());
      Rete rete = this.reteBuilder.buildRete(items, resourceLibrary);
      KnowledgeBase knowledgeBase = new KnowledgeBase(rete);
      KnowledgePackageWrapper knowledgePackageWrapper = new KnowledgePackageWrapper(knowledgeBase.getKnowledgePackage());
      scoreRule.setKnowledgePackageWrapper(knowledgePackageWrapper);
      return scoreRule;
   }

   private Cell resolveCell(ComplexScorecardDefinition complexScorecardDefinition, int number, int number2) {
      Map cellMap = complexScorecardDefinition.getCellMap();
      Cell cell = null;

      for (int index = number; index > -1; index--) {
         String cellKey = complexScorecardDefinition.buildCellKey(index, number2);
         if (cellMap.containsKey(cellKey)) {
            cell = (Cell)cellMap.get(cellKey);
            break;
         }
      }

      if (cell == null) {
         throw new RuleException("Decision table cell[" + number + "," + number2 + "] not exist.");
      } else {
         return cell;
      }
   }

   public void setCellContentBuilder(CellContentBuilder cellContentBuilder) {
      this.cellContentBuilder = cellContentBuilder;
   }

   public void setComplexScorecardDeserializer(ComplexScorecardDeserializer complexScorecardDeserializer) {
      this.complexScorecardDeserializer = complexScorecardDeserializer;
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

   @Override
   public boolean support(Element root) {
      return this.complexScorecardDeserializer.support(root);
   }

   @Override
   public ResourceType getType() {
      return ResourceType.ComplexScorecard;
   }
}
