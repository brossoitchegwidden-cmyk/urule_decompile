package com.bstek.urule.builder;

import com.bstek.urule.Utils;
import com.bstek.urule.action.Action;
import com.bstek.urule.action.ExecuteMethodAction;
import com.bstek.urule.action.InvokeFile;
import com.bstek.urule.action.TemplateAction;
import com.bstek.urule.builder.resource.Resource;
import com.bstek.urule.builder.resource.ResourceBuilder;
import com.bstek.urule.builder.resource.ResourceType;
import com.bstek.urule.builder.resource.RuleSetResourceBuilder;
import com.bstek.urule.builder.rete.IdGenerator;
import com.bstek.urule.builder.rete.ReteBuilder;
import com.bstek.urule.builder.table.CrosstabRulesBuilder;
import com.bstek.urule.builder.table.DecisionTableRulesBuilder;
import com.bstek.urule.dsl.DSLRuleSetBuilder;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.crosstab.CrosstabDefinition;
import com.bstek.urule.model.decisiontree.DecisionTree;
import com.bstek.urule.model.flow.FlowDefinition;
import com.bstek.urule.model.library.ResourceLibrary;
import com.bstek.urule.model.library.variable.VariableCategory;
import com.bstek.urule.model.rete.Rete;
import com.bstek.urule.model.rule.Library;
import com.bstek.urule.model.rule.Other;
import com.bstek.urule.model.rule.ParentFile;
import com.bstek.urule.model.rule.PredefineExecutionUnit;
import com.bstek.urule.model.rule.PredefineGroupDefinition;
import com.bstek.urule.model.rule.Rhs;
import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.model.rule.RuleSet;
import com.bstek.urule.model.rule.lhs.ConditionTemplateCriterion;
import com.bstek.urule.model.rule.lhs.Criterion;
import com.bstek.urule.model.rule.lhs.Junction;
import com.bstek.urule.model.rule.lhs.Lhs;
import com.bstek.urule.model.rule.loop.LoopEnd;
import com.bstek.urule.model.rule.loop.LoopRule;
import com.bstek.urule.model.rule.loop.LoopRuleUnit;
import com.bstek.urule.model.rule.loop.LoopStart;
import com.bstek.urule.model.scorecard.runtime.ScoreRule;
import com.bstek.urule.model.table.DecisionTable;
import com.bstek.urule.model.template.ActionTemplateUnit;
import com.bstek.urule.model.template.ConditionTemplateUnit;
import com.bstek.urule.parse.RuleFileHolder;
import com.bstek.urule.runtime.KnowledgePackage;
import com.bstek.urule.runtime.KnowledgePackageWrapper;
import com.bstek.urule.runtime.service.KnowledgePackageService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.dom4j.Element;

public class KnowledgeBuilder extends AbstractBuilder {
   private ResourceLibraryBuilder c;
   private ReteBuilder d;
   private RulesRebuilder e;
   private DecisionTreeRulesBuilder f;
   private DecisionTableRulesBuilder g;
   private DSLRuleSetBuilder h;
   private CrosstabRulesBuilder i;
   private RuleSetResourceBuilder j;
   public static final String BEAN_ID = "urule.knowledgeBuilder";

   public KnowledgeBase buildKnowledgeBase(ResourceBase var1) throws IOException {
      KnowledgePackageService var2 = (KnowledgePackageService)this.a.getBean("urule.knowledgePackageService");
      ArrayList var3 = new ArrayList();
      ArrayList var4 = new ArrayList();
      HashMap var5 = new HashMap();
      HashMap var6 = new HashMap();
      ArrayList var7 = new ArrayList();
      HashMap var8 = new HashMap();
      HashMap var9 = new HashMap();
      ArrayList var10 = new ArrayList();

      for (Resource var12 : var1.getResources()) {
         String var13 = var12.getPath();
         if (this.h.support(var12)) {
            RuleSet var30 = this.h.build(var12.getContent(), var13);
            this.a(var5, var30.getLibraries());
            if (var30.getRules() != null) {
               var3.addAll(var30.getRules());
            }
         } else {
            long var14 = var12.getId();
            var13 = var13 + "," + var14;
            if (var12.getVersion() != null) {
               var13 = var13 + ":" + var12.getVersion();
            }

            if (!var8.containsKey(var14)) {
               Element var16 = this.a(var12.getContent());

               for (ResourceBuilder var18 : this.b) {
                  if (var18.support(var16)) {
                     RuleFileHolder.resetRuleFile(var13);
                     Object var19 = var18.build(var16, var13);
                     RuleFileHolder.clean();
                     ResourceType var20 = var18.getType();
                     if (!var20.equals(ResourceType.RuleSet)) {
                        if (var20.equals(ResourceType.DecisionTree)) {
                           DecisionTree var35 = (DecisionTree)var19;
                           this.a(var5, var35.getLibraries());
                           RuleSet var44 = this.f.buildRules(var35, var13);
                           this.a(var5, var44.getLibraries());
                           if (var44.getRules() != null) {
                              PredefineGroupDefinition var51 = var35.getPredefineGroup();
                              if (var51 != null) {
                                 var51.setFilePath(var12.getPath());
                                 PredefineExecutionUnit var24 = new PredefineExecutionUnit(var51, var44.getRules(), false);
                                 if (var24.isContainsRules()) {
                                    var10.add(var24);
                                 }
                              }

                              var3.addAll(var44.getRules());
                           }
                        } else if (var20.equals(ResourceType.DecisionTable)) {
                           DecisionTable var36 = (DecisionTable)var19;
                           this.a(var5, var36.getLibraries());
                           List var45 = this.g.buildRules(var36, var13);
                           PredefineGroupDefinition var52 = var36.getPredefineGroup();
                           if (var52 != null) {
                              var52.setFilePath(var12.getPath());
                              PredefineExecutionUnit var56 = new PredefineExecutionUnit(var52, var45, false);
                              if (var56.isContainsRules()) {
                                 var10.add(var56);
                              }
                           }

                           var3.addAll(var45);
                        } else if (var20.equals(ResourceType.CrossDecisionTable)) {
                           CrosstabDefinition var37 = (CrosstabDefinition)var19;
                           this.a(var5, var37.getLibraries());
                           List var46 = this.i.buildRules(var37, var13);
                           PredefineGroupDefinition var53 = var37.getPredefineGroup();
                           if (var53 != null) {
                              var53.setFilePath(var12.getPath());
                              PredefineExecutionUnit var57 = new PredefineExecutionUnit(var53, var46, false);
                              if (var57.isContainsRules()) {
                                 var10.add(var57);
                              }
                           }

                           var3.addAll(var46);
                        } else if (var20.equals(ResourceType.Flow)) {
                           FlowDefinition var38 = (FlowDefinition)var19;
                           var38.setFile(var13);
                           var38 = var38.newFlowDefinitionForSerialize(this, var2, this.h);
                           var7.addAll(var38.getRelationResourceLibraries());
                           this.a(var5, var38.getLibraries());
                           var6.put(var38.getId(), var38);
                        } else if (var20.equals(ResourceType.Scorecard)) {
                           ScoreRule var40 = (ScoreRule)var19;
                           var40.setFile(var13);
                           ArrayList var47 = new ArrayList();
                           var47.add(var40);
                           PredefineGroupDefinition var54 = var40.getPredefineGroup();
                           if (var54 != null) {
                              var54.setFilePath(var12.getPath());
                              PredefineExecutionUnit var58 = new PredefineExecutionUnit(var54, var47, false);
                              if (var58.isContainsRules()) {
                                 var10.add(var58);
                              }
                           }

                           var3.addAll(var47);
                           this.a(var5, var40.getLibraries());
                        } else if (var20.equals(ResourceType.ComplexScorecard)) {
                           ScoreRule var41 = (ScoreRule)var19;
                           var41.setFile(var13);
                           ArrayList var48 = new ArrayList();
                           var48.add(var41);
                           PredefineGroupDefinition var55 = var41.getPredefineGroup();
                           if (var55 != null) {
                              var55.setFilePath(var12.getPath());
                              PredefineExecutionUnit var59 = new PredefineExecutionUnit(var55, var48, false);
                              if (var59.isContainsRules()) {
                                 var10.add(var59);
                              }
                           }

                           var3.addAll(var48);
                           this.a(var5, var41.getLibraries());
                        }
                        break;
                     }

                     RuleSet var21 = (RuleSet)var19;

                     for (ParentFile var23 : var21.getParents()) {
                        var21 = this.a(var21, var5, var9, var23);
                     }

                     this.a(var5, var21.getLibraries());

                     for (Rule var49 : var21.getRules()) {
                        var49.setFile(var13);
                        this.a(var49);
                     }

                     var8.put(var14, var21);
                     PredefineGroupDefinition var43 = var21.getPredefineGroup();
                     if (var43 != null) {
                        var43.setFilePath(var12.getPath());
                        PredefineExecutionUnit var50 = new PredefineExecutionUnit(var43, var21.getRules(), var21.isAlone());
                        if (var50.isContainsRules()) {
                           var10.add(var50);
                        }
                     }
                     break;
                  }
               }
            }
         }
      }

      this.buildRules(var3);
      this.a(var8, var3, var4, var9);
      ResourceLibrary var25 = this.c.buildResourceLibrary(var5.values(), null);
      this.a(var25, var7);
      this.buildRulesConditionActionTemplate(var3, var25);
      this.buildRulesConditionActionTemplate(var4, var25);
      this.buildLoopRules(var3, var25);
      this.buildLoopRules(var4, var25);
      HashMap var26 = new HashMap();

      for (PredefineExecutionUnit var31 : (Iterable<PredefineExecutionUnit>)(Iterable<?>)(var10)) {
         var31.buildRete(var25, this, var26);
      }

      Rete var29 = this.d.buildRete(var3, var25);
      if (var26.size() > 0) {
         if (var29.getPendedGroupRetesMap() != null) {
            var29.getPendedGroupRetesMap().putAll(var26);
         } else {
            var29.setPendedGroupRetesMap(var26);
         }
      }

      ArrayList var32 = new ArrayList();
      Collections.sort(var4);
      Collections.sort(var10);

      for (Rule var33 : (Iterable<Rule>)(Iterable<?>)(var4)) {
         Rete var34 = this.d.buildRete(var33, var25);
         var32.add(var34);
      }

      IdGenerator.clean();
      return new KnowledgeBase(var29, var32, var6, var10);
   }

   public void buildRules(List<Rule> var1) {
      for (Rule var3 : var1) {
         this.a(var3);
      }
   }

   private void a(Rule var1) {
      Other var2 = var1.getOther();
      if (var2 != null) {
         this.a(var2.getActions());
      }

      Rhs var3 = var1.getRhs();
      if (var3 != null) {
         this.a(var3.getActions());
      }

      if (var1 instanceof LoopRule) {
         LoopRule var4 = (LoopRule)var1;
         List var5 = var4.getUnits();
         if (var5 != null) {
            for (LoopRuleUnit var7 : (Iterable<LoopRuleUnit>)(Iterable<?>)(var5)) {
               if (var7.getOther() != null) {
                  this.a(var7.getOther().getActions());
               }

               if (var7.getRhs() != null) {
                  this.a(var7.getRhs().getActions());
               }
            }
         }

         LoopStart var8 = var4.getLoopStart();
         if (var8 != null && var8.getActions() != null) {
            this.a(var8.getActions());
         }

         LoopEnd var9 = var4.getLoopEnd();
         if (var9 != null && var9.getActions() != null) {
            this.a(var9.getActions());
         }
      }
   }

   private void a(List<Action> var1) {
      if (var1 != null) {
         for (Action var3 : var1) {
            if (var3 instanceof ExecuteMethodAction) {
               ExecuteMethodAction var4 = (ExecuteMethodAction)var3;
               InvokeFile var5 = var4.getInvokeFile();
               if (var5 != null) {
                  KnowledgeBuilder var6 = (KnowledgeBuilder)Utils.getApplicationContext().getBean("urule.knowledgeBuilder");
                  ResourceBase var7 = var6.newResourceBase();
                  var7.addResource(var5.getId(), var5.getVersion());

                  try {
                     KnowledgeBase var8 = var6.buildKnowledgeBase(var7);
                     KnowledgePackage var9 = var8.getKnowledgePackage();
                     var5.setKnowledgePackageWrapper(new KnowledgePackageWrapper(var9));
                  } catch (IOException var10) {
                     throw new RuleException(var10);
                  }
               }
            }
         }
      }
   }

   private void a(Map<Long, RuleSet> var1, List<Rule> var2, List<Rule> var3, Map<Long, ParentFile> var4) {
      for (Entry var6 : var1.entrySet()) {
         long var7 = (Long)var6.getKey();
         if (!var4.containsKey(var7)) {
            RuleSet var9 = (RuleSet)var6.getValue();
            if (var9.getRules() != null) {
               List var10 = var9.getRules();
               this.e.convertNamedJunctions(var10);
               if (var9.isAlone()) {
                  var3.addAll(var10);
               } else {
                  var2.addAll(var10);
               }
            }
         }
      }
   }

   private RuleSet a(RuleSet var1, Map<Long, Library> var2, Map<Long, ParentFile> var3, ParentFile var4) {
      var3.put(var4.getId(), var4);
      ResourceBase var5 = this.newResourceBase();
      var5.addResource(var4.getId(), var4.getVersion());
      Resource var6 = var5.getResources().get(0);
      String var7 = var4.getPath();
      if (var6.getVersion() != null) {
         var7 = var7 + ":" + var6.getVersion();
      }

      Element var8 = this.a(var6.getContent());
      RuleSet var9 = this.j.build(var8, var7);
      this.a(var2, var9.getLibraries());

      for (ParentFile var11 : var9.getParents()) {
         var9 = this.a(var9, var2, var3, var11);
      }

      this.a(var9, var1);
      return var1;
   }

   private void a(RuleSet var1, RuleSet var2) {
      List var3 = var2.getRules();
      if (var3 == null) {
         var3 = new ArrayList();
         var2.setRules(var3);
      }

      if (var1.getRules() != null) {
         for (Rule var6 : var1.getRules()) {
            boolean var7 = this.a(var6.getName(), var3);
            if (!var7) {
               var3.add(var6);
            }
         }
      }
   }

   private boolean a(String var1, List<Rule> var2) {
      if (var1 == null) {
         return false;
      }

      for (Rule var4 : var2) {
         if (var4.getName() != null && var4.getName().contentEquals(var1)) {
            return true;
         }
      }

      return false;
   }

   private void a(ResourceLibrary var1, List<ResourceLibrary> var2) {
      for (ResourceLibrary var4 : var2) {
         for (VariableCategory var6 : var4.getVariableCategories()) {
            this.a(var1, var6);
         }
      }
   }

   private void a(ResourceLibrary var1, VariableCategory var2) {
      boolean var3 = false;

      for (VariableCategory var5 : var1.getVariableCategories()) {
         if (var5.getName().equals(var2.getName())) {
            var3 = true;
            break;
         }
      }

      if (!var3) {
         var1.addVariableCategory(var2);
      }
   }

   public void buildLoopRules(List<Rule> var1, ResourceLibrary var2) {
      for (Rule var4 : var1) {
         if (var4 instanceof LoopRule) {
            LoopRule var5 = (LoopRule)var4;
            List var6 = this.buildRules(var5);
            Rete var7 = this.d.buildRete(var6, var2);
            KnowledgeBase var8 = new KnowledgeBase(var7);
            KnowledgePackageWrapper var9 = new KnowledgePackageWrapper(var8.getKnowledgePackage());
            var5.setKnowledgePackageWrapper(var9);
         }
      }
   }

   public List<Rule> buildRules(LoopRule var1) {
      ArrayList var2 = new ArrayList();

      for (LoopRuleUnit var5 : var1.getUnits()) {
         Rule var6 = new Rule();
         var6.setFile(var1.getFile());
         var6.setDebug(var1.getDebug());
         var6.setName(var1.getName() + "->" + var5.getName());
         var6.setLhs(var5.getLhs());
         var6.setRhs(var5.getRhs());
         var6.setOther(var5.getOther());
         var2.add(var6);
      }

      var1.setUnits(null);
      return var2;
   }

   public KnowledgeBase buildKnowledgeBase(RuleSet var1) {
      ArrayList var2 = new ArrayList();
      HashMap var3 = new HashMap();
      this.a(var3, var1.getLibraries());
      if (var1.getRules() != null) {
         var2.addAll(var1.getRules());
      }

      List var4 = null;
      PredefineGroupDefinition var5 = var1.getPredefineGroup();
      if (var5 != null) {
         var4 = var5.getPredefines();
      }

      ResourceLibrary var6 = this.c.buildResourceLibrary(var3.values(), var4);
      Rete var7 = this.d.buildRete(var2, var6);
      return new KnowledgeBase(var7);
   }

   private void a(Map<Long, Library> var1, List<Library> var2) {
      if (var2 != null) {
         for (Library var4 : var2) {
            long var5 = var4.getId();
            if (!var1.containsKey(var5)) {
               var1.put(var5, var4);
            }
         }
      }
   }

   private List<Action> a(List<Action> var1, ResourceLibrary var2) {
      ArrayList var3 = new ArrayList();
      if (var1 == null) {
         return var3;
      }

      for (Action var5 : var1) {
         if (!(var5 instanceof TemplateAction)) {
            var3.add(var5);
         } else {
            TemplateAction var6 = (TemplateAction)var5;
            String var7 = var6.getId();
            ActionTemplateUnit var8 = var2.getActionTemplateUnit(var7);
            List var9 = var8.getActions();
            var3.addAll(var9);
         }
      }

      return var3;
   }

   public void buildRulesConditionActionTemplate(List<Rule> var1, ResourceLibrary var2) {
      for (Rule var4 : var1) {
         if (var4 instanceof LoopRule) {
            this.a((LoopRule)var4, var2);
         } else {
            Lhs var5 = var4.getLhs();
            if (var5 != null) {
               Criterion var6 = var5.getCriterion();
               if (var6 != null) {
                  Criterion var7 = this.a(var6, var2);
                  if (var7 != null) {
                     var5.setCriterion(var7);
                  } else if (var6 instanceof Junction) {
                     this.a((Junction)var6, var2);
                  }
               }
            }

            Rhs var9 = var4.getRhs();
            if (var9 != null) {
               List var10 = this.a(var9.getActions(), var2);
               var9.setActions(var10);
            }

            if (var4.getOther() != null) {
               Other var11 = var4.getOther();
               List var8 = this.a(var11.getActions(), var2);
               var11.setActions(var8);
            }
         }
      }
   }

   private void a(LoopRule var1, ResourceLibrary var2) {
      List var3 = var1.getUnits();
      if (var3 != null) {
         for (LoopRuleUnit var5 : (Iterable<LoopRuleUnit>)(Iterable<?>)(var3)) {
            Lhs var6 = var5.getLhs();
            if (var6 != null) {
               Criterion var7 = var6.getCriterion();
               if (var7 != null) {
                  Criterion var8 = this.a(var7, var2);
                  if (var8 != null) {
                     var6.setCriterion(var8);
                  } else if (var7 instanceof Junction) {
                     this.a((Junction)var7, var2);
                  }
               }
            }

            Rhs var14 = var5.getRhs();
            if (var14 != null) {
               List var15 = this.a(var14.getActions(), var2);
               var14.setActions(var15);
            }

            if (var5.getOther() != null) {
               Other var16 = var5.getOther();
               List var9 = this.a(var16.getActions(), var2);
               var16.setActions(var9);
            }
         }

         LoopStart var10 = var1.getLoopStart();
         if (var10 != null && var10.getActions() != null) {
            List var11 = this.a(var10.getActions(), var2);
            var10.setActions(var11);
         }

         LoopEnd var12 = var1.getLoopEnd();
         if (var12 != null && var12.getActions() != null) {
            List var13 = this.a(var12.getActions(), var2);
            var12.setActions(var13);
         }
      }
   }

   private void a(Junction var1, ResourceLibrary var2) {
      List var3 = var1.getCriterions();
      int var4 = 0;

      for (int var5 = var3.size(); var4 < var5; var4++) {
         Criterion var6 = (Criterion)var3.get(var4);
         Criterion var7 = this.a(var6, var2);
         if (var7 != null) {
            var3.set(var4, var7);
         } else if (var6 instanceof Junction) {
            this.a((Junction)var6, var2);
         }
      }
   }

   private Criterion a(Criterion var1, ResourceLibrary var2) {
      if (var1 instanceof ConditionTemplateCriterion) {
         ConditionTemplateCriterion var3 = (ConditionTemplateCriterion)var1;
         String var4 = var3.getId();
         ConditionTemplateUnit var5 = var2.getConditionTemplateUnit(var4);
         return var5.getCriterion();
      } else {
         return null;
      }
   }

   public void setRulesRebuilder(RulesRebuilder var1) {
      this.e = var1;
   }

   public void setReteBuilder(ReteBuilder var1) {
      this.d = var1;
   }

   public ReteBuilder getReteBuilder() {
      return this.d;
   }

   public void setDecisionTableRulesBuilder(DecisionTableRulesBuilder var1) {
      this.g = var1;
   }

   public void setDslRuleSetBuilder(DSLRuleSetBuilder var1) {
      this.h = var1;
   }

   public void setResourceLibraryBuilder(ResourceLibraryBuilder var1) {
      this.c = var1;
   }

   public void setDecisionTreeRulesBuilder(DecisionTreeRulesBuilder var1) {
      this.f = var1;
   }

   public void setCrosstabRulesBuilder(CrosstabRulesBuilder var1) {
      this.i = var1;
   }

   public void setRuleSetResourceBuilder(RuleSetResourceBuilder var1) {
      this.j = var1;
   }
}
