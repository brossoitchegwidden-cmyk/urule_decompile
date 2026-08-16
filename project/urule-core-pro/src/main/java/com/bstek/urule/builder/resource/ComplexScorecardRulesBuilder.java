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
   private ReteBuilder a;
   private ResourceLibraryBuilder b;
   private RulesRebuilder c;
   private CellContentBuilder d;
   private ComplexScorecardDeserializer e;

   public ScoreRule build(Element var1, String var2) {
      ComplexScorecardDefinition var3 = this.e.deserialize(var1);
      ScoreRule var4 = new ScoreRule();
      var4.setFile(var2);
      var4.setName("cc");
      var4.setEffectiveDate(var3.getEffectiveDate());
      var4.setExpiresDate(var3.getExpiresDate());
      var4.setEnabled(var3.getEnabled());
      var4.setSalience(var3.getSalience());
      var4.setDebug(var3.getDebug());
      var4.setScoringBean(var3.getScoringBean());
      var4.setScoringType(var3.getScoringType());
      var4.setAssignTargetType(var3.getAssignTargetType());
      var4.setKeyName(var3.getKeyName());
      var4.setKeyLabel(var3.getKeyLabel());
      var4.setDatatype(var3.getDatatype());
      var4.setVariableCategory(var3.getVariableCategory());
      var4.setVariableName(var3.getVariableName());
      var4.setVariableLabel(var3.getVariableLabel());
      var4.setLibraries(var3.getLibraries());
      ArrayList var5 = new ArrayList();
      List var6 = var3.getRows();
      List var7 = var3.getColumns();

      for (Row var9 : (Iterable<Row>)(Iterable<?>)(var6)) {
         Rule var10 = new Rule();
         var10.setFile(var2);
         var10.setDebug(var3.getDebug());
         var10.setSalience(var3.getSalience());
         var10.setExpiresDate(var3.getExpiresDate());
         var10.setEffectiveDate(var3.getEffectiveDate());
         var10.setEnabled(var3.getEnabled());
         var10.setName("cc-r" + var9.getNum());
         Lhs var11 = new Lhs();
         And var12 = new And();
         var10.setLhs(var11);
         Rhs var13 = new Rhs();
         var10.setRhs(var13);
         var5.add(var10);
         Value var14 = null;

         for (ComplexColumn var16 : (Iterable<ComplexColumn>)(Iterable<?>)(var7)) {
            Cell var17 = this.a(var3, var9.getNum(), var16.getNum());
            ComplexColumnType var18 = var16.getType();
            switch (var18) {
               case Criteria:
                  Criterion var19 = this.d.buildCriterion(var17, var16);
                  if (var19 != null) {
                     var12.addCriterion(var19);
                  }
                  break;
               case Score:
                  var14 = var17.getValue();
                  if (var14 != null) {
                     ScoringAction var27 = new ScoringAction(var9.getNum(), "scoring_value", null);
                     var27.setValue(var14);
                     var13.addAction(var27);
                  }
                  break;
               case Custom:
                  var14 = var17.getValue();
                  if (var14 != null) {
                     ScoringAction var20 = new ScoringAction(var9.getNum(), var16.getCustomLabel(), null);
                     var20.setValue(var14);
                     var13.addAction(var20);
                  }
            }
         }

         if (var12.getCriterions() != null && var12.getCriterions().size() > 0) {
            var11.setCriterion(var12);
         }
      }

      this.c.rebuildRules(var3.getLibraries(), var5, var3.getPredefines());
      ResourceLibrary var21 = this.b.buildResourceLibrary(var3.getLibraries(), var3.getPredefines());
      Rete var22 = this.a.buildRete(var5, var21);
      KnowledgeBase var23 = new KnowledgeBase(var22);
      KnowledgePackageWrapper var24 = new KnowledgePackageWrapper(var23.getKnowledgePackage());
      var4.setKnowledgePackageWrapper(var24);
      return var4;
   }

   private Cell a(ComplexScorecardDefinition var1, int var2, int var3) {
      Map var4 = var1.getCellMap();
      Cell var5 = null;

      for (int var6 = var2; var6 > -1; var6--) {
         String var7 = var1.buildCellKey(var6, var3);
         if (var4.containsKey(var7)) {
            var5 = (Cell)var4.get(var7);
            break;
         }
      }

      if (var5 == null) {
         throw new RuleException("Decision table cell[" + var2 + "," + var3 + "] not exist.");
      } else {
         return var5;
      }
   }

   public void setCellContentBuilder(CellContentBuilder var1) {
      this.d = var1;
   }

   public void setComplexScorecardDeserializer(ComplexScorecardDeserializer var1) {
      this.e = var1;
   }

   public void setResourceLibraryBuilder(ResourceLibraryBuilder var1) {
      this.b = var1;
   }

   public void setReteBuilder(ReteBuilder var1) {
      this.a = var1;
   }

   public void setRulesRebuilder(RulesRebuilder var1) {
      this.c = var1;
   }

   @Override
   public boolean support(Element var1) {
      return this.e.support(var1);
   }

   @Override
   public ResourceType getType() {
      return ResourceType.ComplexScorecard;
   }
}
