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

   public void execute(Context var1, Map<String, Object> var2) {
      boolean var3 = false;
      if (this.getDebug() != null) {
         var3 = this.getDebug();
      }

      if (var3) {
         var1.getLogger().logScoreCard(this.getName(), this.getFile());
      }

      KnowledgeSession var4 = (KnowledgeSession)var1.getWorkingMemory();
      KnowledgeSession var5 = KnowledgeSessionFactory.newKnowledgeSession(this.knowledgePackageWrapper, var1, var4);
      var5.fireRules(var4.getParameters());
      var1.addRuleData(var5.getLogManager().getRuleData());
      HashMap var6 = new HashMap();
      Map var7 = var5.getParameters();
      List var8 = (List)var7.get("_score_card_runtime_value_");
      if (var8 != null) {
         var7.remove("_score_card_runtime_value_");

         for (ScoreRuntimeValue var10 : (Iterable<ScoreRuntimeValue>)(Iterable<?>)(var8)) {
            int var11 = var10.getRowNumber();
            if (var3) {
               var1.getLogger().logExecuteScoreCard(var11, var10.getValue());
            }

            RowItemImpl var12 = null;
            if (var6.containsKey(var11)) {
               var12 = (RowItemImpl)var6.get(var11);
            } else {
               var12 = new RowItemImpl();
               var12.setRowNumber(var11);
               var6.put(var11, var12);
            }

            if (var10.getName().equals("scoring_value")) {
               var12.setScore(var10.getValue());
               var12.setWeight(var10.getWeight());
            } else {
               CellItem var13 = new CellItem(var10.getName(), var10.getValue());
               var12.addCellItem(var13);
            }
         }
      }

      ArrayList var16 = new ArrayList();
      var16.addAll(var6.values());
      ScorecardImpl var17 = new ScorecardImpl(this.getName(), var16, var3);
      Object var18 = null;
      if (this.scoringType.equals(ScoringType.sum)) {
         var18 = var17.executeSum(var1);
      } else if (this.scoringType.equals(ScoringType.weightsum)) {
         var18 = var17.executeWeightSum(var1);
      } else if (this.scoringType.equals(ScoringType.custom)) {
         if (var3) {
            var1.getLogger().logScoreCardBean(this.scoringBean);
         }

         ScoringStrategy var21 = (ScoringStrategy)var1.getApplicationContext().getBean(this.scoringBean);
         var18 = var21.calculate(var17, var1);
      }

      if (this.assignTargetType.equals(AssignTargetType.none)) {
         this.log.warning("Scorecard [" + var17.getName() + "] not setting assignment object for score value, score value is :" + var18);
      } else {
         Object var22 = null;
         ValueCompute var24 = var1.getValueCompute();
         String var14 = var1.getVariableCategoryClass(this.variableCategory);
         if (var14.equals(HashMap.class.getName())) {
            var22 = var5.getParameters();
         } else {
            var22 = var24.findObject(var14, var2, var1);
         }

         if (var22 == null) {
            throw new RuleException("Class[" + var14 + "] not found in workingmemory.");
         }

         var18 = this.datatype.convert(var18);
         if (this.keyName == null) {
            Utils.setObjectProperty(var22, this.variableName, var18);
         } else {
            Object var15 = Utils.getObjectProperty(var22, this.keyName);
            Utils.setObjectProperty(var15, this.variableName, var18);
         }
      }

      var4.getParameters().putAll(var5.getParameters());
   }

   public String getKeyLabel() {
      return this.keyLabel;
   }

   public void setKeyLabel(String var1) {
      this.keyLabel = var1;
   }

   public String getKeyName() {
      return this.keyName;
   }

   public void setKeyName(String var1) {
      this.keyName = var1;
   }

   public ScoringType getScoringType() {
      return this.scoringType;
   }

   public void setScoringType(ScoringType var1) {
      this.scoringType = var1;
   }

   public String getScoringBean() {
      return this.scoringBean;
   }

   public void setScoringBean(String var1) {
      this.scoringBean = var1;
   }

   public AssignTargetType getAssignTargetType() {
      return this.assignTargetType;
   }

   public void setAssignTargetType(AssignTargetType var1) {
      this.assignTargetType = var1;
   }

   public String getVariableCategory() {
      return this.variableCategory;
   }

   public void setVariableCategory(String var1) {
      this.variableCategory = var1;
   }

   public String getVariableName() {
      return this.variableName;
   }

   public void setVariableName(String var1) {
      this.variableName = var1;
   }

   public String getVariableLabel() {
      return this.variableLabel;
   }

   public void setVariableLabel(String var1) {
      this.variableLabel = var1;
   }

   public Datatype getDatatype() {
      return this.datatype;
   }

   public void setDatatype(Datatype var1) {
      this.datatype = var1;
   }

   public List<Library> getLibraries() {
      return this.libraries;
   }

   public void setLibraries(List<Library> var1) {
      this.libraries = var1;
   }

   public PredefineGroupDefinition getPredefineGroup() {
      return this.predefineGroup;
   }

   public void setPredefineGroup(PredefineGroupDefinition var1) {
      this.predefineGroup = var1;
   }

   public KnowledgePackageWrapper getKnowledgePackageWrapper() {
      return this.knowledgePackageWrapper;
   }

   public void setKnowledgePackageWrapper(KnowledgePackageWrapper var1) {
      this.knowledgePackageWrapper = var1;
   }
}
