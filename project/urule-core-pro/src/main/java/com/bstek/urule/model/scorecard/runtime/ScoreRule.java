package com.bstek.urule.model.scorecard.runtime;

import com.bstek.urule.Utils;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.rule.Library;
import com.bstek.urule.model.rule.PredefineGroupDefinition;
import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.model.scorecard.AssignTargetType;
import com.bstek.urule.model.scorecard.ScoringType;
import com.bstek.urule.runtime.KnowledgePackageWrapper;
import com.bstek.urule.runtime.KnowledgeSession;
import com.bstek.urule.runtime.KnowledgeSessionFactory;
import com.bstek.urule.runtime.rete.Context;
import com.bstek.urule.runtime.rete.ValueCompute;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class ScoreRule extends Rule {
   private Logger log = Logger.getGlobal();
   private ScoringType scoringType;
   private String scoringBean;
   private AssignTargetType assignTargetType;
   private String keyLabel;
   private String keyName;
   private String variableCategory;
   private String variableName;
   private String variableLabel;
   private Datatype datatype;
   private PredefineGroupDefinition predefineGroup;
   @JsonIgnore
   private List<Library> libraries;
   private KnowledgePackageWrapper knowledgePackageWrapper;

   public void execute(Context context, Map<String, Object> factMap) {
      boolean debug = false;
      if (this.getDebug() != null) {
         debug = this.getDebug();
      }

      if (debug) {
         context.getLogger().logScoreCard(this.getName(), this.getFile());
      }

      KnowledgeSession workingMemory = (KnowledgeSession)context.getWorkingMemory();
      KnowledgeSession knowledgeSession = KnowledgeSessionFactory.newKnowledgeSession(this.knowledgePackageWrapper, context, workingMemory);
      knowledgeSession.fireRules(workingMemory.getParameters());
      context.addRuleData(knowledgeSession.getLogManager().getRuleData());
      HashMap valuesByKey = new HashMap();
      Map parameters = knowledgeSession.getParameters();
      List _score_card_runtime_value_ = (List)parameters.get("_score_card_runtime_value_");
      if (_score_card_runtime_value_ != null) {
         parameters.remove("_score_card_runtime_value_");

         for (ScoreRuntimeValue scoreRuntimeValue : (Iterable<ScoreRuntimeValue>)(Iterable<?>)(_score_card_runtime_value_)) {
            int rowNumber = scoreRuntimeValue.getRowNumber();
            if (debug) {
               context.getLogger().logExecuteScoreCard(rowNumber, scoreRuntimeValue.getValue());
            }

            RowItemImpl rowItemImpl = null;
            if (valuesByKey.containsKey(rowNumber)) {
               rowItemImpl = (RowItemImpl)valuesByKey.get(rowNumber);
            } else {
               rowItemImpl = new RowItemImpl();
               rowItemImpl.setRowNumber(rowNumber);
               valuesByKey.put(rowNumber, rowItemImpl);
            }

            if (scoreRuntimeValue.getName().equals("scoring_value")) {
               rowItemImpl.setScore(scoreRuntimeValue.getValue());
               rowItemImpl.setWeight(scoreRuntimeValue.getWeight());
            } else {
               CellItem cellItem = new CellItem(scoreRuntimeValue.getName(), scoreRuntimeValue.getValue());
               rowItemImpl.addCellItem(cellItem);
            }
         }
      }

      ArrayList items = new ArrayList();
      items.addAll(valuesByKey.values());
      ScorecardImpl scorecardImpl = new ScorecardImpl(this.getName(), items, debug);
      Object objectValue = null;
      if (this.scoringType.equals(ScoringType.sum)) {
         objectValue = scorecardImpl.executeSum(context);
      } else if (this.scoringType.equals(ScoringType.weightsum)) {
         objectValue = scorecardImpl.executeWeightSum(context);
      } else if (this.scoringType.equals(ScoringType.custom)) {
         if (debug) {
            context.getLogger().logScoreCardBean(this.scoringBean);
         }

         ScoringStrategy scoringStrategy = (ScoringStrategy)context.getApplicationContext().getBean(this.scoringBean);
         objectValue = scoringStrategy.calculate(scorecardImpl, context);
      }

      if (this.assignTargetType.equals(AssignTargetType.none)) {
         this.log.warning("Scorecard [" + scorecardImpl.getName() + "] not setting assignment object for score value, score value is :" + objectValue);
      } else {
         Object parameters2 = null;
         ValueCompute valueCompute = context.getValueCompute();
         String variableCategoryClass = context.getVariableCategoryClass(this.variableCategory);
         if (variableCategoryClass.equals(HashMap.class.getName())) {
            parameters2 = knowledgeSession.getParameters();
         } else {
            parameters2 = valueCompute.findObject(variableCategoryClass, factMap, context);
         }

         if (parameters2 == null) {
            throw new RuleException("Class[" + variableCategoryClass + "] not found in workingmemory.");
         }

         objectValue = this.datatype.convert(objectValue);
         if (this.keyName == null) {
            Utils.setObjectProperty(parameters2, this.variableName, objectValue);
         } else {
            Object objectProperty = Utils.getObjectProperty(parameters2, this.keyName);
            Utils.setObjectProperty(objectProperty, this.variableName, objectValue);
         }
      }

      workingMemory.getParameters().putAll(knowledgeSession.getParameters());
   }

   public String getKeyLabel() {
      return this.keyLabel;
   }

   public void setKeyLabel(String keyLabel) {
      this.keyLabel = keyLabel;
   }

   public String getKeyName() {
      return this.keyName;
   }

   public void setKeyName(String keyName) {
      this.keyName = keyName;
   }

   public ScoringType getScoringType() {
      return this.scoringType;
   }

   public void setScoringType(ScoringType scoringType) {
      this.scoringType = scoringType;
   }

   public String getScoringBean() {
      return this.scoringBean;
   }

   public void setScoringBean(String scoringBean) {
      this.scoringBean = scoringBean;
   }

   public AssignTargetType getAssignTargetType() {
      return this.assignTargetType;
   }

   public void setAssignTargetType(AssignTargetType assignTargetType) {
      this.assignTargetType = assignTargetType;
   }

   public String getVariableCategory() {
      return this.variableCategory;
   }

   public void setVariableCategory(String variableCategory) {
      this.variableCategory = variableCategory;
   }

   public String getVariableName() {
      return this.variableName;
   }

   public void setVariableName(String variableName) {
      this.variableName = variableName;
   }

   public String getVariableLabel() {
      return this.variableLabel;
   }

   public void setVariableLabel(String variableLabel) {
      this.variableLabel = variableLabel;
   }

   public Datatype getDatatype() {
      return this.datatype;
   }

   public void setDatatype(Datatype datatype) {
      this.datatype = datatype;
   }

   public List<Library> getLibraries() {
      return this.libraries;
   }

   public void setLibraries(List<Library> libraries) {
      this.libraries = libraries;
   }

   public PredefineGroupDefinition getPredefineGroup() {
      return this.predefineGroup;
   }

   public void setPredefineGroup(PredefineGroupDefinition predefineGroup) {
      this.predefineGroup = predefineGroup;
   }

   public KnowledgePackageWrapper getKnowledgePackageWrapper() {
      return this.knowledgePackageWrapper;
   }

   public void setKnowledgePackageWrapper(KnowledgePackageWrapper knowledgePackageWrapper) {
      this.knowledgePackageWrapper = knowledgePackageWrapper;
   }
}
