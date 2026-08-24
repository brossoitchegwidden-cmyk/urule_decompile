package com.bstek.urule.console.editor.ruleset;

import com.bstek.urule.Utils;
import com.bstek.urule.action.Action;
import com.bstek.urule.action.ConsolePrintAction;
import com.bstek.urule.action.ExecuteCommonFunctionAction;
import com.bstek.urule.action.ExecuteMethodAction;
import com.bstek.urule.action.ScoringAction;
import com.bstek.urule.action.TemplateAction;
import com.bstek.urule.action.VariableAssignAction;
import com.bstek.urule.builder.CopyLibPhaseHolder;
import com.bstek.urule.builder.KnowledgeBuilder;
import com.bstek.urule.builder.ResourceBase;
import com.bstek.urule.builder.ResourceLibraryBuilder;
import com.bstek.urule.builder.resource.Resource;
import com.bstek.urule.console.util.ServiceUtils;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.library.ResourceLibrary;
import com.bstek.urule.model.rule.CommonFunctionValue;
import com.bstek.urule.model.rule.ComplexArithmetic;
import com.bstek.urule.model.rule.ConstantValue;
import com.bstek.urule.model.rule.Library;
import com.bstek.urule.model.rule.LibraryType;
import com.bstek.urule.model.rule.MethodValue;
import com.bstek.urule.model.rule.Other;
import com.bstek.urule.model.rule.Parameter;
import com.bstek.urule.model.rule.ParameterValue;
import com.bstek.urule.model.rule.ParenValue;
import com.bstek.urule.model.rule.Rhs;
import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.VariableCategoryValue;
import com.bstek.urule.model.rule.VariableValue;
import com.bstek.urule.model.rule.lhs.AccumulateLeftPart;
import com.bstek.urule.model.rule.lhs.CalculateItem;
import com.bstek.urule.model.rule.lhs.CommonFunctionLeftPart;
import com.bstek.urule.model.rule.lhs.CommonFunctionParameter;
import com.bstek.urule.model.rule.lhs.ConditionItem;
import com.bstek.urule.model.rule.lhs.ConditionTemplateCriterion;
import com.bstek.urule.model.rule.lhs.Criteria;
import com.bstek.urule.model.rule.lhs.Criterion;
import com.bstek.urule.model.rule.lhs.FunctionLeftPart;
import com.bstek.urule.model.rule.lhs.Junction;
import com.bstek.urule.model.rule.lhs.Left;
import com.bstek.urule.model.rule.lhs.LeftPart;
import com.bstek.urule.model.rule.lhs.Lhs;
import com.bstek.urule.model.rule.lhs.MethodLeftPart;
import com.bstek.urule.model.rule.lhs.VariableLeftPart;
import com.bstek.urule.model.rule.loop.LoopRule;
import com.bstek.urule.model.rule.loop.LoopRuleUnit;
import com.bstek.urule.model.rule.loop.LoopTarget;
import com.bstek.urule.model.scorecard.runtime.ScoreRule;
import com.bstek.urule.model.template.ActionTemplate;
import com.bstek.urule.model.template.ActionTemplateUnit;
import com.bstek.urule.model.template.ConditionTemplate;
import com.bstek.urule.model.template.ConditionTemplateUnit;
import com.bstek.urule.parse.deserializer.ActionTemplateDeserializer;
import com.bstek.urule.parse.deserializer.ConditionTemplateDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * Walks copied rule structures and collects the libraries referenced by their
 * conditions, values, actions, and templates.
 */
public class CopyLibsAnalysis {
   private ResourceLibraryBuilder resourceLibraryBuilder = (ResourceLibraryBuilder)Utils.getApplicationContext().getBean("urule.resourceLibraryBuilder");
   private ActionTemplateDeserializer actionTemplateDeserializer = (ActionTemplateDeserializer)Utils.getApplicationContext().getBean("urule.actionTemplateDeserializer");
   private ConditionTemplateDeserializer conditionTemplateDeserializer = (ConditionTemplateDeserializer)Utils.getApplicationContext().getBean("urule.conditionTemplateDeserializer");
   public static final CopyLibsAnalysis ins = new CopyLibsAnalysis();

   private CopyLibsAnalysis() {
   }

   public String doAnalysis(String libs, Object obj) throws Exception {
      HashMap valuesByKey = new HashMap();
      valuesByKey.put("parameters", new ArrayList());
      valuesByKey.put("variables", new ArrayList());
      valuesByKey.put("constants", new ArrayList());
      valuesByKey.put("actions", new ArrayList());
      valuesByKey.put("conditionTemplates", new ArrayList());
      valuesByKey.put("actionTemplates", new ArrayList());
      LibraryIndex libraryIndex = this.buildLibraryIndex(libs);
      if (obj instanceof List) {
         List obj2 = (List)obj;
         this.analyzeCriterion((Criterion)obj2.get(0), valuesByKey, libraryIndex);
      } else if (obj instanceof Criterion) {
         Criterion criterion = (Criterion)obj;
         this.analyzeCriterion(criterion, valuesByKey, libraryIndex);
      } else if (obj instanceof Action) {
         Action action = (Action)obj;
         this.analyzeAction(action, valuesByKey, libraryIndex);
      } else {
         if (!(obj instanceof Rule)) {
            throw new RuleException("Unknow object :" + obj.getClass().getName());
         }

         Rule rule = (Rule)obj;
         if (rule instanceof LoopRule) {
            LoopRule loopRule = (LoopRule)rule;
            LoopTarget loopTarget = loopRule.getLoopTarget();
            Value localValue = loopTarget.getValue();
            this.analyzeValue(localValue, valuesByKey, libraryIndex);

            for(LoopRuleUnit loopRuleUnit : loopRule.getUnits()) {
               this.analyzeLhs(valuesByKey, libraryIndex, loopRuleUnit.getLhs());
               this.analyzeRhs(valuesByKey, libraryIndex, loopRuleUnit.getRhs());
               Other other = loopRuleUnit.getOther();
               if (other != null) {
                  this.analyzeActions(valuesByKey, libraryIndex, other.getActions());
               }
            }
         } else if (rule instanceof ScoreRule) {
            ScoreRule scoreRule = (ScoreRule)rule;
            String variableCategory = scoreRule.getVariableCategory();
            LibInfo libInfo = libraryIndex.variableContains(variableCategory);
            this.addLibraryInfo(valuesByKey, libInfo);
         } else {
            this.analyzeRule(rule, valuesByKey, libraryIndex);
         }
      }

      ObjectMapper objectMapper = JsonMapper.builder().build();
      return objectMapper.writeValueAsString(valuesByKey);
   }

   private void analyzeRule(Rule rule, Map valuesByKey, LibraryIndex libraryIndex) {
      Lhs lhs = rule.getLhs();
      this.analyzeLhs(valuesByKey, libraryIndex, lhs);
      Rhs rhs = rule.getRhs();
      this.analyzeRhs(valuesByKey, libraryIndex, rhs);
      Other other = rule.getOther();
      if (other != null) {
         List actions = other.getActions();
         this.analyzeActions(valuesByKey, libraryIndex, actions);
      }

   }

   private void analyzeActions(Map valuesByKey, LibraryIndex libraryIndex, List actions) {
      if (actions != null) {
         for(Action action : (Iterable<Action>)(Iterable<?>)(actions)) {
            this.analyzeAction(action, valuesByKey, libraryIndex);
         }

      }
   }

   private void analyzeRhs(Map valuesByKey, LibraryIndex libraryIndex, Rhs rhs) {
      if (rhs != null) {
         List actions = rhs.getActions();
         this.analyzeActions(valuesByKey, libraryIndex, actions);
      }
   }

   private void analyzeLhs(Map valuesByKey, LibraryIndex libraryIndex, Lhs lhs) {
      if (lhs != null) {
         Criterion criterion = lhs.getCriterion();
         this.analyzeCriterion(criterion, valuesByKey, libraryIndex);
      }
   }

   private void analyzeAction(Action action, Map valuesByKey, LibraryIndex libraryIndex) {
      if (action instanceof ConsolePrintAction) {
         ConsolePrintAction consolePrintAction = (ConsolePrintAction)action;
         Value localValue = consolePrintAction.getValue();
         this.analyzeValue(localValue, valuesByKey, libraryIndex);
      } else if (action instanceof ExecuteCommonFunctionAction) {
         ExecuteCommonFunctionAction executeCommonFunctionAction = (ExecuteCommonFunctionAction)action;
         CommonFunctionParameter parameter = executeCommonFunctionAction.getParameter();
         this.analyzeCommonFunctionParameter(valuesByKey, libraryIndex, parameter);
      } else if (action instanceof ExecuteMethodAction) {
         ExecuteMethodAction executeMethodAction = (ExecuteMethodAction)action;
         String beanLabel = executeMethodAction.getBeanLabel();
         LibInfo libInfo = libraryIndex.actionContains(beanLabel);
         this.addLibraryInfo(valuesByKey, libInfo);
         List parameters = executeMethodAction.getParameters();
         if (parameters != null) {
            for(Parameter parameter2 : (Iterable<Parameter>)(Iterable<?>)(parameters)) {
               Value localValue2 = parameter2.getValue();
               this.analyzeValue(localValue2, valuesByKey, libraryIndex);
            }
         }
      } else if (action instanceof ScoringAction) {
         ScoringAction scoringAction = (ScoringAction)action;
         Value localValue3 = scoringAction.getValue();
         this.analyzeValue(localValue3, valuesByKey, libraryIndex);
      } else if (action instanceof TemplateAction) {
         TemplateAction templateAction = (TemplateAction)action;
         String id = templateAction.getId();
         LibInfo libInfo2 = libraryIndex.templateContains(id);
         this.addLibraryInfo(valuesByKey, libInfo2);
      } else if (action instanceof VariableAssignAction) {
         VariableAssignAction variableAssignAction = (VariableAssignAction)action;
         String variableCategory = variableAssignAction.getVariableCategory();
         LibInfo libInfo3 = libraryIndex.variableContains(variableCategory);
         this.addLibraryInfo(valuesByKey, libInfo3);
         Value localValue4 = variableAssignAction.getValue();
         this.analyzeValue(localValue4, valuesByKey, libraryIndex);
      }

   }

   private void analyzeCriterion(Criterion criterion, Map valuesByKey, LibraryIndex libraryIndex) {
      if (criterion != null) {
         if (criterion instanceof Junction) {
            Junction junction = (Junction)criterion;
            if (junction.getCriterions() != null) {
               for(Criterion criterion2 : junction.getCriterions()) {
                  this.analyzeCriterion(criterion2, valuesByKey, libraryIndex);
               }
            }
         } else if (criterion instanceof ConditionTemplateCriterion) {
            ConditionTemplateCriterion conditionTemplateCriterion = (ConditionTemplateCriterion)criterion;
            LibInfo libInfo = libraryIndex.templateContains(conditionTemplateCriterion.getId());
            this.addLibraryInfo(valuesByKey, libInfo);
         } else if (criterion instanceof Criteria) {
            Criteria criteria = (Criteria)criterion;
            Left left = criteria.getLeft();
            if (left != null) {
               LeftPart leftPart = left.getLeftPart();
               this.analyzeLeftPart(leftPart, valuesByKey, libraryIndex);
            }

            this.analyzeValue(criteria.getValue(), valuesByKey, libraryIndex);
         }

      }
   }

   private void analyzeLeftPart(LeftPart leftPart, Map valuesByKey, LibraryIndex libraryIndex) {
      if (leftPart != null) {
         if (leftPart instanceof AccumulateLeftPart) {
            AccumulateLeftPart accumulateLeftPart = (AccumulateLeftPart)leftPart;
            List calculateItems = accumulateLeftPart.getCalculateItems();
            if (calculateItems != null) {
               for(CalculateItem calculateItem : (Iterable<CalculateItem>)(Iterable<?>)(calculateItems)) {
                  String assignVariableCategory = calculateItem.getAssignVariableCategory();
                  if (assignVariableCategory != null) {
                     LibInfo libInfo = libraryIndex.variableContains(assignVariableCategory);
                     this.addLibraryInfo(valuesByKey, libInfo);
                     Value localValue = calculateItem.getValue();
                     this.analyzeValue(localValue, valuesByKey, libraryIndex);
                  }
               }
            }

            List conditionItems = accumulateLeftPart.getConditionItems();
            if (conditionItems != null) {
               for(ConditionItem conditionItem : (Iterable<ConditionItem>)(Iterable<?>)(conditionItems)) {
                  Value localValue2 = conditionItem.getValue();
                  this.analyzeValue(localValue2, valuesByKey, libraryIndex);
               }
            }

            Junction junction = accumulateLeftPart.getJunction();
            this.analyzeCriterion(junction, valuesByKey, libraryIndex);
            LoopTarget loopTarget = accumulateLeftPart.getLoopTarget();
            if (loopTarget != null) {
               this.analyzeValue(loopTarget.getValue(), valuesByKey, libraryIndex);
            }
         } else if (leftPart instanceof CommonFunctionLeftPart) {
            CommonFunctionLeftPart commonFunctionLeftPart = (CommonFunctionLeftPart)leftPart;
            CommonFunctionParameter parameter = commonFunctionLeftPart.getParameter();
            this.analyzeCommonFunctionParameter(valuesByKey, libraryIndex, parameter);
         } else if (leftPart instanceof FunctionLeftPart) {
            FunctionLeftPart functionLeftPart = (FunctionLeftPart)leftPart;
            List parameters = functionLeftPart.getParameters();
            if (parameters != null) {
               for(Parameter parameter2 : (Iterable<Parameter>)(Iterable<?>)(parameters)) {
                  Value localValue3 = parameter2.getValue();
                  this.analyzeValue(localValue3, valuesByKey, libraryIndex);
               }
            }
         } else if (leftPart instanceof MethodLeftPart) {
            MethodLeftPart methodLeftPart = (MethodLeftPart)leftPart;
            String beanLabel = methodLeftPart.getBeanLabel();
            LibInfo libInfo2 = libraryIndex.actionContains(beanLabel);
            this.addLibraryInfo(valuesByKey, libInfo2);
            List parameters2 = methodLeftPart.getParameters();
            if (parameters2 != null) {
               for(Parameter parameter3 : (Iterable<Parameter>)(Iterable<?>)(parameters2)) {
                  Value localValue4 = parameter3.getValue();
                  this.analyzeValue(localValue4, valuesByKey, libraryIndex);
               }
            }
         } else if (leftPart instanceof VariableLeftPart) {
            VariableLeftPart variableLeftPart = (VariableLeftPart)leftPart;
            String variableCategory = variableLeftPart.getVariableCategory();
            LibInfo libInfo3 = libraryIndex.variableContains(variableCategory);
            this.addLibraryInfo(valuesByKey, libInfo3);
         }

      }
   }

   private void analyzeCommonFunctionParameter(Map valuesByKey, LibraryIndex libraryIndex, CommonFunctionParameter commonFunctionParameter) {
      if (commonFunctionParameter != null) {
         Value objectParameter = commonFunctionParameter.getObjectParameter();
         this.analyzeValue(objectParameter, valuesByKey, libraryIndex);
      }

   }

   private void analyzeValue(Value value, Map valuesByKey, LibraryIndex libraryIndex) {
      if (value != null) {
         if (value instanceof CommonFunctionValue) {
            CommonFunctionValue commonFunctionValue = (CommonFunctionValue)value;
            String label = commonFunctionValue.getLabel();
            LibInfo libInfo = libraryIndex.actionContains(label);
            this.addLibraryInfo(valuesByKey, libInfo);
            CommonFunctionParameter parameter = commonFunctionValue.getParameter();
            this.analyzeCommonFunctionParameter(valuesByKey, libraryIndex, parameter);
         } else if (value instanceof ConstantValue) {
            ConstantValue constantValue = (ConstantValue)value;
            String constantCategory = constantValue.getConstantCategory();
            LibInfo libInfo2 = libraryIndex.constantContains(constantCategory);
            this.addLibraryInfo(valuesByKey, libInfo2);
         } else if (value instanceof MethodValue) {
            MethodValue methodValue = (MethodValue)value;
            String beanLabel = methodValue.getBeanLabel();
            LibInfo libInfo3 = libraryIndex.actionContains(beanLabel);
            this.addLibraryInfo(valuesByKey, libInfo3);
            List parameters = methodValue.getParameters();
            if (parameters != null) {
               for(Parameter parameter2 : (Iterable<Parameter>)(Iterable<?>)(parameters)) {
                  Value localValue = parameter2.getValue();
                  this.analyzeValue(localValue, valuesByKey, libraryIndex);
               }
            }
         } else if (value instanceof ParameterValue) {
            LibInfo libInfo4 = libraryIndex.variableContains("参数");
            this.addLibraryInfo(valuesByKey, libInfo4);
         } else if (value instanceof ParenValue) {
            ParenValue parenValue = (ParenValue)value;
            Value localValue2 = parenValue.getValue();
            this.analyzeValue(localValue2, valuesByKey, libraryIndex);
         } else if (value instanceof VariableCategoryValue) {
            VariableCategoryValue variableCategoryValue = (VariableCategoryValue)value;
            LibInfo libInfo5 = libraryIndex.variableContains(variableCategoryValue.getVariableCategory());
            this.addLibraryInfo(valuesByKey, libInfo5);
         } else if (value instanceof VariableValue) {
            VariableValue variableValue = (VariableValue)value;
            String variableCategory = variableValue.getVariableCategory();
            LibInfo libInfo6 = libraryIndex.variableContains(variableCategory);
            this.addLibraryInfo(valuesByKey, libInfo6);
         }

         ComplexArithmetic arithmetic = value.getArithmetic();
         if (arithmetic != null) {
            Value localValue3 = arithmetic.getValue();
            this.analyzeValue(localValue3, valuesByKey, libraryIndex);
         }

      }
   }

   private void addLibraryInfo(Map valuesByKey, LibInfo libInfo) {
      if (libInfo != null) {
         Object objectValue = null;
         if (valuesByKey.containsKey(libInfo.getType())) {
            objectValue = (List)valuesByKey.get(libInfo.getType());
         } else {
            objectValue = new ArrayList();
            valuesByKey.put(libInfo.getType(), objectValue);
         }

         if (!((List)objectValue).contains(libInfo.getMap())) {
            ((List)objectValue).add(libInfo.getMap());
         }
      }
   }

   private LibraryIndex buildLibraryIndex(String librariesJson) throws Exception {
      ObjectMapper objectMapper = JsonMapper.builder().build();
      LibraryIndex libraryIndex = new LibraryIndex();
      Map valuesByKey = (Map)objectMapper.readValue(librariesJson, HashMap.class);
      Iterator iterator = valuesByKey.keySet().iterator();

      while(true) {
         String text;
         boolean flag;
         LibraryType libraryType;
         while(true) {
            if (!iterator.hasNext()) {
               return libraryIndex;
            }

            text = (String)iterator.next();
            libraryType = null;
            flag = false;
            if (text.contentEquals("parameters")) {
               libraryType = LibraryType.Parameter;
               break;
            }

            if (text.contentEquals("variables")) {
               libraryType = LibraryType.Variable;
               break;
            }

            if (text.contentEquals("constants")) {
               libraryType = LibraryType.Constant;
               break;
            }

            if (text.contentEquals("actions")) {
               libraryType = LibraryType.Action;
               break;
            }

            if (text.contentEquals("conditionTemplates")) {
               libraryType = LibraryType.ActionTemplate;
               flag = true;
               break;
            }

            if (text.contentEquals("actionTemplates")) {
               libraryType = LibraryType.ConditionTemplate;
               flag = true;
               break;
            }
         }

         for(Map valuesByKey2 : (Iterable<Map>)(Iterable<?>)((List)valuesByKey.get(text))) {
            long id = (long)(Integer)valuesByKey2.get("id");
            Library library = new Library();
            library.setType(libraryType);
            library.setId(id);
            library.setPath(valuesByKey2.get("path").toString());
            library.setVersion((String)valuesByKey2.get("version"));
            if (flag) {
               KnowledgeBuilder knowledgeBuilder = ServiceUtils.getKnowledgeBuilder();
               ResourceBase resourceBase = knowledgeBuilder.newResourceBase();
               resourceBase.addResource(id, library.getVersion());
               Element resource = this.parseResource(((Resource)resourceBase.getResources().get(0)).getContent());
               if (libraryType.equals(LibraryType.ActionTemplate)) {
                  ActionTemplate actionTemplate = this.actionTemplateDeserializer.deserialize(resource);

                  for(ActionTemplateUnit actionTemplateUnit : actionTemplate.getTemplates()) {
                     libraryIndex.buildTemplateIndex(actionTemplateUnit.getId(), valuesByKey2);
                  }
               } else {
                  ConditionTemplate conditionTemplate = this.conditionTemplateDeserializer.deserialize(resource);

                  for(ConditionTemplateUnit conditionTemplateUnit : conditionTemplate.getTemplates()) {
                     libraryIndex.buildTemplateIndex(conditionTemplateUnit.getId(), valuesByKey2);
                  }
               }
            } else {
               ArrayList items = new ArrayList();
               items.add(library);
               CopyLibPhaseHolder.set();

               try {
                  ResourceLibrary resourceLibrary = this.resourceLibraryBuilder.buildResourceLibrary(items, (List)null);
                  libraryIndex.buildIndex(resourceLibrary, valuesByKey2, libraryType);
               } finally {
                  CopyLibPhaseHolder.clean();
               }
            }
         }
      }
   }

   protected Element parseResource(String content) {
      try {
         Document text = DocumentHelper.parseText(content);
         Element rootElement = text.getRootElement();
         return rootElement;
      } catch (DocumentException documentException) {
         throw new RuleException(documentException);
      }
   }
}
