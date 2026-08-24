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

/**
 * Builds an executable knowledge base from rule resources and resolves all
 * dependent libraries, parent rule sets, templates and nested invocations.
 */
public class KnowledgeBuilder extends AbstractBuilder {
   private ResourceLibraryBuilder resourceLibraryBuilder;
   private ReteBuilder reteBuilder;
   private RulesRebuilder rulesRebuilder;
   private DecisionTreeRulesBuilder decisionTreeRulesBuilder;
   private DecisionTableRulesBuilder decisionTableRulesBuilder;
   private DSLRuleSetBuilder dslRuleSetBuilder;
   private CrosstabRulesBuilder crosstabRulesBuilder;
   private RuleSetResourceBuilder ruleSetResourceBuilder;
   public static final String BEAN_ID = "urule.knowledgeBuilder";

   public KnowledgeBase buildKnowledgeBase(ResourceBase resourceBase) throws IOException {
      KnowledgePackageService knowledgePackageService = (KnowledgePackageService)this.applicationContext.getBean("urule.knowledgePackageService");
      ArrayList rules = new ArrayList();
      ArrayList standaloneRules = new ArrayList();
      HashMap librariesById = new HashMap();
      HashMap flowsById = new HashMap();
      ArrayList relatedResourceLibraries = new ArrayList();
      HashMap ruleSetsById = new HashMap();
      HashMap parentFilesById = new HashMap();
      ArrayList predefineExecutionUnits = new ArrayList();

      for (Resource resource : resourceBase.getResources()) {
         String path = resource.getPath();
         if (this.dslRuleSetBuilder.support(resource)) {
            RuleSet ruleSet = this.dslRuleSetBuilder.build(resource.getContent(), path);
            this.addToLibraryMap(librariesById, ruleSet.getLibraries());
            if (ruleSet.getRules() != null) {
               rules.addAll(ruleSet.getRules());
            }
         } else {
            long id = resource.getId();
            path = path + "," + id;
            if (resource.getVersion() != null) {
               path = path + ":" + resource.getVersion();
            }

            if (!ruleSetsById.containsKey(id)) {
               Element resourceElement = this.parseResource(resource.getContent());

               for (ResourceBuilder resourceBuilder : this.resourceBuilders) {
                  if (resourceBuilder.support(resourceElement)) {
                     RuleFileHolder.resetRuleFile(path);
                     Object definition = resourceBuilder.build(resourceElement, path);
                     RuleFileHolder.clean();
                     ResourceType type = resourceBuilder.getType();
                     if (!type.equals(ResourceType.RuleSet)) {
                        if (type.equals(ResourceType.DecisionTree)) {
                           DecisionTree decisionTree = (DecisionTree)definition;
                           this.addToLibraryMap(librariesById, decisionTree.getLibraries());
                           RuleSet decisionTreeRuleSet = this.decisionTreeRulesBuilder.buildRules(decisionTree, path);
                           this.addToLibraryMap(librariesById, decisionTreeRuleSet.getLibraries());
                           if (decisionTreeRuleSet.getRules() != null) {
                              PredefineGroupDefinition predefineGroup = decisionTree.getPredefineGroup();
                              if (predefineGroup != null) {
                                 predefineGroup.setFilePath(resource.getPath());
                                 PredefineExecutionUnit predefineExecutionUnit = new PredefineExecutionUnit(predefineGroup, decisionTreeRuleSet.getRules(), false);
                                 if (predefineExecutionUnit.isContainsRules()) {
                                    predefineExecutionUnits.add(predefineExecutionUnit);
                                 }
                              }

                              rules.addAll(decisionTreeRuleSet.getRules());
                           }
                        } else if (type.equals(ResourceType.DecisionTable)) {
                           DecisionTable decisionTable = (DecisionTable)definition;
                           this.addToLibraryMap(librariesById, decisionTable.getLibraries());
                           List decisionTableRules = this.decisionTableRulesBuilder.buildRules(decisionTable, path);
                           PredefineGroupDefinition predefineGroup = decisionTable.getPredefineGroup();
                           if (predefineGroup != null) {
                              predefineGroup.setFilePath(resource.getPath());
                              PredefineExecutionUnit executionUnit = new PredefineExecutionUnit(predefineGroup, decisionTableRules, false);
                              if (executionUnit.isContainsRules()) {
                                 predefineExecutionUnits.add(executionUnit);
                              }
                           }

                           rules.addAll(decisionTableRules);
                        } else if (type.equals(ResourceType.CrossDecisionTable)) {
                           CrosstabDefinition crosstabDefinition = (CrosstabDefinition)definition;
                           this.addToLibraryMap(librariesById, crosstabDefinition.getLibraries());
                           List crosstabRules = this.crosstabRulesBuilder.buildRules(crosstabDefinition, path);
                           PredefineGroupDefinition predefineGroup = crosstabDefinition.getPredefineGroup();
                           if (predefineGroup != null) {
                              predefineGroup.setFilePath(resource.getPath());
                              PredefineExecutionUnit executionUnit = new PredefineExecutionUnit(predefineGroup, crosstabRules, false);
                              if (executionUnit.isContainsRules()) {
                                 predefineExecutionUnits.add(executionUnit);
                              }
                           }

                           rules.addAll(crosstabRules);
                        } else if (type.equals(ResourceType.Flow)) {
                           FlowDefinition flowDefinition = (FlowDefinition)definition;
                           flowDefinition.setFile(path);
                           flowDefinition = flowDefinition.newFlowDefinitionForSerialize(this, knowledgePackageService, this.dslRuleSetBuilder);
                           relatedResourceLibraries.addAll(flowDefinition.getRelationResourceLibraries());
                           this.addToLibraryMap(librariesById, flowDefinition.getLibraries());
                           flowsById.put(flowDefinition.getId(), flowDefinition);
                        } else if (type.equals(ResourceType.Scorecard)) {
                           ScoreRule scoreRule = (ScoreRule)definition;
                           scoreRule.setFile(path);
                           ArrayList items = new ArrayList();
                           items.add(scoreRule);
                           PredefineGroupDefinition predefineGroup2 = scoreRule.getPredefineGroup();
                           if (predefineGroup2 != null) {
                              predefineGroup2.setFilePath(resource.getPath());
                              PredefineExecutionUnit predefineExecutionUnit2 = new PredefineExecutionUnit(predefineGroup2, items, false);
                              if (predefineExecutionUnit2.isContainsRules()) {
                                 predefineExecutionUnits.add(predefineExecutionUnit2);
                              }
                           }

                           rules.addAll(items);
                           this.addToLibraryMap(librariesById, scoreRule.getLibraries());
                        } else if (type.equals(ResourceType.ComplexScorecard)) {
                           ScoreRule scoreRule2 = (ScoreRule)definition;
                           scoreRule2.setFile(path);
                           ArrayList items2 = new ArrayList();
                           items2.add(scoreRule2);
                           PredefineGroupDefinition predefineGroup3 = scoreRule2.getPredefineGroup();
                           if (predefineGroup3 != null) {
                              predefineGroup3.setFilePath(resource.getPath());
                              PredefineExecutionUnit predefineExecutionUnit3 = new PredefineExecutionUnit(predefineGroup3, items2, false);
                              if (predefineExecutionUnit3.isContainsRules()) {
                                 predefineExecutionUnits.add(predefineExecutionUnit3);
                              }
                           }

                           rules.addAll(items2);
                           this.addToLibraryMap(librariesById, scoreRule2.getLibraries());
                        }
                        break;
                     }

                     RuleSet parentRuleSet = (RuleSet)definition;

                     for (ParentFile parentFile : parentRuleSet.getParents()) {
                        parentRuleSet = this.loadParentRuleSet(parentRuleSet, librariesById, parentFilesById, parentFile);
                     }

                     this.addToLibraryMap(librariesById, parentRuleSet.getLibraries());

                     for (Rule rule : parentRuleSet.getRules()) {
                        rule.setFile(path);
                        this.resolveInvokeFiles(rule);
                     }

                     ruleSetsById.put(id, parentRuleSet);
                     PredefineGroupDefinition predefineGroup4 = parentRuleSet.getPredefineGroup();
                     if (predefineGroup4 != null) {
                        predefineGroup4.setFilePath(resource.getPath());
                        PredefineExecutionUnit predefineExecutionUnit4 = new PredefineExecutionUnit(predefineGroup4, parentRuleSet.getRules(), parentRuleSet.isAlone());
                        if (predefineExecutionUnit4.isContainsRules()) {
                           predefineExecutionUnits.add(predefineExecutionUnit4);
                        }
                     }
                     break;
                  }
               }
            }
         }
      }

      this.buildRules(rules);
      this.collectRuleSetRules(ruleSetsById, rules, standaloneRules, parentFilesById);
      ResourceLibrary resourceLibrary = this.resourceLibraryBuilder.buildResourceLibrary(librariesById.values(), null);
      this.mergeResourceLibraries(resourceLibrary, relatedResourceLibraries);
      this.buildRulesConditionActionTemplate(rules, resourceLibrary);
      this.buildRulesConditionActionTemplate(standaloneRules, resourceLibrary);
      this.buildLoopRules(rules, resourceLibrary);
      this.buildLoopRules(standaloneRules, resourceLibrary);
      HashMap pendedGroupRetesById = new HashMap();

      for (PredefineExecutionUnit executionUnit : (Iterable<PredefineExecutionUnit>)(Iterable<?>)(predefineExecutionUnits)) {
         executionUnit.buildRete(resourceLibrary, this, pendedGroupRetesById);
      }

      Rete rete = this.reteBuilder.buildRete(rules, resourceLibrary);
      if (pendedGroupRetesById.size() > 0) {
         if (rete.getPendedGroupRetesMap() != null) {
            rete.getPendedGroupRetesMap().putAll(pendedGroupRetesById);
         } else {
            rete.setPendedGroupRetesMap(pendedGroupRetesById);
         }
      }

      ArrayList standaloneRetes = new ArrayList();
      Collections.sort(standaloneRules);
      Collections.sort(predefineExecutionUnits);

      for (Rule standaloneRule : (Iterable<Rule>)(Iterable<?>)(standaloneRules)) {
         Rete standaloneRete = this.reteBuilder.buildRete(standaloneRule, resourceLibrary);
         standaloneRetes.add(standaloneRete);
      }

      IdGenerator.clean();
      return new KnowledgeBase(rete, standaloneRetes, flowsById, predefineExecutionUnits);
   }

   public void buildRules(List<Rule> rules) {
      for (Rule rule : rules) {
         this.resolveInvokeFiles(rule);
      }
   }

   private void resolveInvokeFiles(Rule rule) {
      Other other = rule.getOther();
      if (other != null) {
         this.resolveInvokeFiles(other.getActions());
      }

      Rhs rhs = rule.getRhs();
      if (rhs != null) {
         this.resolveInvokeFiles(rhs.getActions());
      }

      if (rule instanceof LoopRule) {
         LoopRule loopRule = (LoopRule)rule;
         List units = loopRule.getUnits();
         if (units != null) {
            for (LoopRuleUnit loopRuleUnit : (Iterable<LoopRuleUnit>)(Iterable<?>)(units)) {
               if (loopRuleUnit.getOther() != null) {
                  this.resolveInvokeFiles(loopRuleUnit.getOther().getActions());
               }

               if (loopRuleUnit.getRhs() != null) {
                  this.resolveInvokeFiles(loopRuleUnit.getRhs().getActions());
               }
            }
         }

         LoopStart loopStart = loopRule.getLoopStart();
         if (loopStart != null && loopStart.getActions() != null) {
            this.resolveInvokeFiles(loopStart.getActions());
         }

         LoopEnd loopEnd = loopRule.getLoopEnd();
         if (loopEnd != null && loopEnd.getActions() != null) {
            this.resolveInvokeFiles(loopEnd.getActions());
         }
      }
   }

   private void resolveInvokeFiles(List<Action> actions) {
      if (actions != null) {
         for (Action action : actions) {
            if (action instanceof ExecuteMethodAction) {
               ExecuteMethodAction executeMethodAction = (ExecuteMethodAction)action;
               InvokeFile invokeFile = executeMethodAction.getInvokeFile();
               if (invokeFile != null) {
                  KnowledgeBuilder knowledgeBuilder = (KnowledgeBuilder)Utils.getApplicationContext().getBean("urule.knowledgeBuilder");
                  ResourceBase resourceBase = knowledgeBuilder.newResourceBase();
                  resourceBase.addResource(invokeFile.getId(), invokeFile.getVersion());

                  try {
                     KnowledgeBase knowledgeBase = knowledgeBuilder.buildKnowledgeBase(resourceBase);
                     KnowledgePackage knowledgePackage = knowledgeBase.getKnowledgePackage();
                     invokeFile.setKnowledgePackageWrapper(new KnowledgePackageWrapper(knowledgePackage));
                  } catch (IOException iOException) {
                     throw new RuleException(iOException);
                  }
               }
            }
         }
      }
   }

   private void collectRuleSetRules(Map<Long, RuleSet> ruleSetsById, List<Rule> rules, List<Rule> standaloneRules, Map<Long, ParentFile> parentFilesById) {
      for (Entry entry : ruleSetsById.entrySet()) {
         long key = (Long)entry.getKey();
         if (!parentFilesById.containsKey(key)) {
            RuleSet ruleSet = (RuleSet)entry.getValue();
            if (ruleSet.getRules() != null) {
               List rules2 = ruleSet.getRules();
               this.rulesRebuilder.convertNamedJunctions(rules2);
               if (ruleSet.isAlone()) {
                  standaloneRules.addAll(rules2);
               } else {
                  rules.addAll(rules2);
               }
            }
         }
      }
   }

   private RuleSet loadParentRuleSet(RuleSet ruleSet, Map<Long, Library> librariesById, Map<Long, ParentFile> parentFilesById, ParentFile parentFile) {
      parentFilesById.put(parentFile.getId(), parentFile);
      ResourceBase resourceBase = this.newResourceBase();
      resourceBase.addResource(parentFile.getId(), parentFile.getVersion());
      Resource resource = resourceBase.getResources().get(0);
      String path = parentFile.getPath();
      if (resource.getVersion() != null) {
         path = path + ":" + resource.getVersion();
      }

      Element resource2 = this.parseResource(resource.getContent());
      RuleSet parentRuleSet = this.ruleSetResourceBuilder.build(resource2, path);
      this.addToLibraryMap(librariesById, parentRuleSet.getLibraries());

      for (ParentFile parentFile2 : parentRuleSet.getParents()) {
         parentRuleSet = this.loadParentRuleSet(parentRuleSet, librariesById, parentFilesById, parentFile2);
      }

      this.mergeRuleSets(parentRuleSet, ruleSet);
      return ruleSet;
   }

   private void mergeRuleSets(RuleSet ruleSet, RuleSet ruleSet2) {
      List rules = ruleSet2.getRules();
      if (rules == null) {
         rules = new ArrayList();
         ruleSet2.setRules(rules);
      }

      if (ruleSet.getRules() != null) {
         for (Rule rule : ruleSet.getRules()) {
            boolean flag = this.containsRule(rule.getName(), rules);
            if (!flag) {
               rules.add(rule);
            }
         }
      }
   }

   private boolean containsRule(String text, List<Rule> rules) {
      if (text == null) {
         return false;
      }

      for (Rule rule : rules) {
         if (rule.getName() != null && rule.getName().contentEquals(text)) {
            return true;
         }
      }

      return false;
   }

   private void mergeResourceLibraries(ResourceLibrary resourceLibrary, List<ResourceLibrary> resourceLibraries) {
      for (ResourceLibrary resourceLibrary2 : resourceLibraries) {
         for (VariableCategory variableCategory : resourceLibrary2.getVariableCategories()) {
            this.addVariableCategoryIfAbsent(resourceLibrary, variableCategory);
         }
      }
   }

   private void addVariableCategoryIfAbsent(ResourceLibrary resourceLibrary, VariableCategory variableCategory) {
      boolean flag = false;

      for (VariableCategory variableCategory2 : resourceLibrary.getVariableCategories()) {
         if (variableCategory2.getName().equals(variableCategory.getName())) {
            flag = true;
            break;
         }
      }

      if (!flag) {
         resourceLibrary.addVariableCategory(variableCategory);
      }
   }

   public void buildLoopRules(List<Rule> rules, ResourceLibrary resourceLibrary) {
      for (Rule rule : rules) {
         if (rule instanceof LoopRule) {
            LoopRule loopRule = (LoopRule)rule;
            List rules2 = this.buildRules(loopRule);
            Rete rete = this.reteBuilder.buildRete(rules2, resourceLibrary);
            KnowledgeBase knowledgeBase = new KnowledgeBase(rete);
            KnowledgePackageWrapper knowledgePackageWrapper = new KnowledgePackageWrapper(knowledgeBase.getKnowledgePackage());
            loopRule.setKnowledgePackageWrapper(knowledgePackageWrapper);
         }
      }
   }

   public List<Rule> buildRules(LoopRule loopRule) {
      ArrayList rules = new ArrayList();

      for (LoopRuleUnit loopRuleUnit : loopRule.getUnits()) {
         Rule rule = new Rule();
         rule.setFile(loopRule.getFile());
         rule.setDebug(loopRule.getDebug());
         rule.setName(loopRule.getName() + "->" + loopRuleUnit.getName());
         rule.setLhs(loopRuleUnit.getLhs());
         rule.setRhs(loopRuleUnit.getRhs());
         rule.setOther(loopRuleUnit.getOther());
         rules.add(rule);
      }

      loopRule.setUnits(null);
      return rules;
   }

   public KnowledgeBase buildKnowledgeBase(RuleSet ruleSet) {
      ArrayList items = new ArrayList();
      HashMap valuesByKey = new HashMap();
      this.addToLibraryMap(valuesByKey, ruleSet.getLibraries());
      if (ruleSet.getRules() != null) {
         items.addAll(ruleSet.getRules());
      }

      List predefines = null;
      PredefineGroupDefinition predefineGroup = ruleSet.getPredefineGroup();
      if (predefineGroup != null) {
         predefines = predefineGroup.getPredefines();
      }

      ResourceLibrary resourceLibrary = this.resourceLibraryBuilder.buildResourceLibrary(valuesByKey.values(), predefines);
      Rete rete = this.reteBuilder.buildRete(items, resourceLibrary);
      return new KnowledgeBase(rete);
   }

   private void addToLibraryMap(Map<Long, Library> valuesByKey, List<Library> libraries) {
      if (libraries != null) {
         for (Library library : libraries) {
            long id = library.getId();
            if (!valuesByKey.containsKey(id)) {
               valuesByKey.put(id, library);
            }
         }
      }
   }

   private List<Action> expandActionTemplates(List<Action> actions, ResourceLibrary resourceLibrary) {
      ArrayList items = new ArrayList();
      if (actions == null) {
         return items;
      }

      for (Action action : actions) {
         if (!(action instanceof TemplateAction)) {
            items.add(action);
         } else {
            TemplateAction templateAction = (TemplateAction)action;
            String id = templateAction.getId();
            ActionTemplateUnit actionTemplateUnit = resourceLibrary.getActionTemplateUnit(id);
            List actions2 = actionTemplateUnit.getActions();
            items.addAll(actions2);
         }
      }

      return items;
   }

   public void buildRulesConditionActionTemplate(List<Rule> rules, ResourceLibrary resourceLibrary) {
      for (Rule rule : rules) {
         if (rule instanceof LoopRule) {
            this.expandLoopRuleTemplates((LoopRule)rule, resourceLibrary);
         } else {
            Lhs lhs = rule.getLhs();
            if (lhs != null) {
               Criterion criterion = lhs.getCriterion();
               if (criterion != null) {
                  Criterion criterion2 = this.expandConditionTemplate(criterion, resourceLibrary);
                  if (criterion2 != null) {
                     lhs.setCriterion(criterion2);
                  } else if (criterion instanceof Junction) {
                     this.expandJunctionTemplates((Junction)criterion, resourceLibrary);
                  }
               }
            }

            Rhs rhs = rule.getRhs();
            if (rhs != null) {
               List items = this.expandActionTemplates(rhs.getActions(), resourceLibrary);
               rhs.setActions(items);
            }

            if (rule.getOther() != null) {
               Other other = rule.getOther();
               List items2 = this.expandActionTemplates(other.getActions(), resourceLibrary);
               other.setActions(items2);
            }
         }
      }
   }

   private void expandLoopRuleTemplates(LoopRule loopRule, ResourceLibrary resourceLibrary) {
      List units = loopRule.getUnits();
      if (units != null) {
         for (LoopRuleUnit loopRuleUnit : (Iterable<LoopRuleUnit>)(Iterable<?>)(units)) {
            Lhs lhs = loopRuleUnit.getLhs();
            if (lhs != null) {
               Criterion criterion = lhs.getCriterion();
               if (criterion != null) {
                  Criterion criterion2 = this.expandConditionTemplate(criterion, resourceLibrary);
                  if (criterion2 != null) {
                     lhs.setCriterion(criterion2);
                  } else if (criterion instanceof Junction) {
                     this.expandJunctionTemplates((Junction)criterion, resourceLibrary);
                  }
               }
            }

            Rhs rhs = loopRuleUnit.getRhs();
            if (rhs != null) {
               List items = this.expandActionTemplates(rhs.getActions(), resourceLibrary);
               rhs.setActions(items);
            }

            if (loopRuleUnit.getOther() != null) {
               Other other = loopRuleUnit.getOther();
               List items2 = this.expandActionTemplates(other.getActions(), resourceLibrary);
               other.setActions(items2);
            }
         }

         LoopStart loopStart = loopRule.getLoopStart();
         if (loopStart != null && loopStart.getActions() != null) {
            List items3 = this.expandActionTemplates(loopStart.getActions(), resourceLibrary);
            loopStart.setActions(items3);
         }

         LoopEnd loopEnd = loopRule.getLoopEnd();
         if (loopEnd != null && loopEnd.getActions() != null) {
            List items4 = this.expandActionTemplates(loopEnd.getActions(), resourceLibrary);
            loopEnd.setActions(items4);
         }
      }
   }

   private void expandJunctionTemplates(Junction junction, ResourceLibrary resourceLibrary) {
      List criterions = junction.getCriterions();
      int number = 0;

      for (int index = criterions.size(); number < index; number++) {
         Criterion criterion = (Criterion)criterions.get(number);
         Criterion criterion2 = this.expandConditionTemplate(criterion, resourceLibrary);
         if (criterion2 != null) {
            criterions.set(number, criterion2);
         } else if (criterion instanceof Junction) {
            this.expandJunctionTemplates((Junction)criterion, resourceLibrary);
         }
      }
   }

   private Criterion expandConditionTemplate(Criterion criterion, ResourceLibrary resourceLibrary) {
      if (criterion instanceof ConditionTemplateCriterion) {
         ConditionTemplateCriterion conditionTemplateCriterion = (ConditionTemplateCriterion)criterion;
         String id = conditionTemplateCriterion.getId();
         ConditionTemplateUnit conditionTemplateUnit = resourceLibrary.getConditionTemplateUnit(id);
         return conditionTemplateUnit.getCriterion();
      } else {
         return null;
      }
   }

   public void setRulesRebuilder(RulesRebuilder rulesRebuilder) {
      this.rulesRebuilder = rulesRebuilder;
   }

   public void setReteBuilder(ReteBuilder reteBuilder) {
      this.reteBuilder = reteBuilder;
   }

   public ReteBuilder getReteBuilder() {
      return this.reteBuilder;
   }

   public void setDecisionTableRulesBuilder(DecisionTableRulesBuilder decisionTableRulesBuilder) {
      this.decisionTableRulesBuilder = decisionTableRulesBuilder;
   }

   public void setDslRuleSetBuilder(DSLRuleSetBuilder dslRuleSetBuilder) {
      this.dslRuleSetBuilder = dslRuleSetBuilder;
   }

   public void setResourceLibraryBuilder(ResourceLibraryBuilder resourceLibraryBuilder) {
      this.resourceLibraryBuilder = resourceLibraryBuilder;
   }

   public void setDecisionTreeRulesBuilder(DecisionTreeRulesBuilder decisionTreeRulesBuilder) {
      this.decisionTreeRulesBuilder = decisionTreeRulesBuilder;
   }

   public void setCrosstabRulesBuilder(CrosstabRulesBuilder crosstabRulesBuilder) {
      this.crosstabRulesBuilder = crosstabRulesBuilder;
   }

   public void setRuleSetResourceBuilder(RuleSetResourceBuilder ruleSetResourceBuilder) {
      this.ruleSetResourceBuilder = ruleSetResourceBuilder;
   }
}
