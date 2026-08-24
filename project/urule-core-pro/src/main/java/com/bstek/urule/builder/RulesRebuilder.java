package com.bstek.urule.builder;

import com.bstek.urule.action.Action;
import com.bstek.urule.action.ConsolePrintAction;
import com.bstek.urule.action.ExecuteMethodAction;
import com.bstek.urule.action.PredefineAssignAction;
import com.bstek.urule.action.TemplateAction;
import com.bstek.urule.action.VariableAssignAction;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.library.ResourceLibrary;
import com.bstek.urule.model.library.action.ActionData;
import com.bstek.urule.model.library.constant.ConstantData;
import com.bstek.urule.model.library.variable.Variable;
import com.bstek.urule.model.library.variable.VariableCategory;
import com.bstek.urule.model.library.variable.VariableData;
import com.bstek.urule.model.rule.CommonFunctionValue;
import com.bstek.urule.model.rule.ComplexArithmetic;
import com.bstek.urule.model.rule.ConstantValue;
import com.bstek.urule.model.rule.Library;
import com.bstek.urule.model.rule.MathValue;
import com.bstek.urule.model.rule.MethodValue;
import com.bstek.urule.model.rule.Other;
import com.bstek.urule.model.rule.Parameter;
import com.bstek.urule.model.rule.ParameterValue;
import com.bstek.urule.model.rule.ParenValue;
import com.bstek.urule.model.rule.Predefine;
import com.bstek.urule.model.rule.PredefineValue;
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
import com.bstek.urule.model.rule.lhs.LeftType;
import com.bstek.urule.model.rule.lhs.Lhs;
import com.bstek.urule.model.rule.lhs.MethodLeftPart;
import com.bstek.urule.model.rule.lhs.PredefineLeftPart;
import com.bstek.urule.model.rule.lhs.VariableLeftPart;
import com.bstek.urule.model.rule.loop.LoopEnd;
import com.bstek.urule.model.rule.loop.LoopRule;
import com.bstek.urule.model.rule.loop.LoopRuleUnit;
import com.bstek.urule.model.rule.loop.LoopStart;
import com.bstek.urule.model.rule.loop.LoopTarget;
import com.bstek.urule.model.rule.math.AbsoluteMath;
import com.bstek.urule.model.rule.math.DownRoundMath;
import com.bstek.urule.model.rule.math.ExtremumMath;
import com.bstek.urule.model.rule.math.FractionMath;
import com.bstek.urule.model.rule.math.LnMath;
import com.bstek.urule.model.rule.math.LogMath;
import com.bstek.urule.model.rule.math.MathSign;
import com.bstek.urule.model.rule.math.NRadicalMath;
import com.bstek.urule.model.rule.math.PowerMath;
import com.bstek.urule.model.rule.math.RadicalMath;
import com.bstek.urule.model.rule.math.SigmaMath;
import com.bstek.urule.model.rule.math.TriangleFunctionMath;
import com.bstek.urule.model.rule.math.UpRoundMath;
import com.bstek.urule.model.template.ActionTemplateUnit;
import com.bstek.urule.model.template.ConditionTemplateUnit;
import com.bstek.urule.parse.RuleFileHolder;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;

public class RulesRebuilder {
   private ResourceLibraryBuilder resourceLibraryBuilder;

   public void rebuildRules(List<Library> libraries, List<Rule> rules, List<Predefine> predefines) {
      if (libraries != null) {
         if (rules != null) {
            ResourceLibrary resourceLibrary = this.resourceLibraryBuilder.buildResourceLibrary(libraries, predefines);
            if (predefines != null) {
               for (Predefine predefine : predefines) {
                  Junction junction = predefine.getJunction();
                  if (junction != null) {
                     this.rebuildCriterion(junction, resourceLibrary, false);
                  }

                  Value localValue = predefine.getValue();
                  if (localValue != null) {
                     this.rebuildValue(localValue, resourceLibrary, false);
                  }
               }
            }

            for (Rule rule : rules) {
               if (rule.getLhs() != null) {
                  Criterion criterion = rule.getLhs().getCriterion();
                  this.rebuildCriterion(criterion, resourceLibrary, false);
               }

               Rhs rhs = rule.getRhs();
               List actions2 = null;
               if (rhs != null) {
                  actions2 = rhs.getActions();
               }

               if (actions2 != null) {
                  for (Action action : (Iterable<Action>)(Iterable<?>)(actions2)) {
                     this.rebuildAction(action, resourceLibrary, false);
                     this.rebuildTemplateAction(action, resourceLibrary);
                  }
               }

               Other other = rule.getOther();
               if (other != null) {
                  List actions = other.getActions();
                  if (actions != null) {
                     for (Action action2 : (Iterable<Action>)(Iterable<?>)(actions)) {
                        this.rebuildAction(action2, resourceLibrary, false);
                        this.rebuildTemplateAction(action2, resourceLibrary);
                     }
                  }
               }

               if (rule instanceof LoopRule) {
                  LoopRule loopRule = (LoopRule)rule;
                  LoopTarget loopTarget = loopRule.getLoopTarget();
                  if (loopTarget != null) {
                     Value localValue2 = loopTarget.getValue();
                     this.rebuildValue(localValue2, resourceLibrary, false);
                  }

                  LoopStart loopStart = loopRule.getLoopStart();
                  if (loopStart != null && loopStart.getActions() != null) {
                     for (Action action3 : loopStart.getActions()) {
                        this.rebuildAction(action3, resourceLibrary, false);
                        this.rebuildTemplateAction(action3, resourceLibrary);
                     }
                  }

                  LoopEnd loopEnd = loopRule.getLoopEnd();
                  if (loopEnd != null && loopEnd.getActions() != null) {
                     for (Action action4 : loopEnd.getActions()) {
                        this.rebuildAction(action4, resourceLibrary, false);
                        this.rebuildTemplateAction(action4, resourceLibrary);
                     }
                  }

                  for (LoopRuleUnit loopRuleUnit : ((LoopRule)rule).getUnits()) {
                     if (loopRuleUnit.getLhs() != null) {
                        Criterion criterion2 = loopRuleUnit.getLhs().getCriterion();
                        this.rebuildCriterion(criterion2, resourceLibrary, false);
                     }

                     rhs = loopRuleUnit.getRhs();
                     if (rhs != null) {
                        actions2 = rhs.getActions();
                     }

                     if (actions2 != null) {
                        for (Action action5 : (Iterable<Action>)(Iterable<?>)(actions2)) {
                           this.rebuildAction(action5, resourceLibrary, false);
                           this.rebuildTemplateAction(action5, resourceLibrary);
                        }
                     }

                     other = loopRuleUnit.getOther();
                     if (other != null) {
                        List actions3 = other.getActions();
                        if (actions3 != null) {
                           for (Action action6 : (Iterable<Action>)(Iterable<?>)(actions3)) {
                              this.rebuildAction(action6, resourceLibrary, false);
                              this.rebuildTemplateAction(action6, resourceLibrary);
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public void rebuildRulesForDSL(List<Library> libraries, List<Rule> rules, List<Predefine> predefines) {
      if (libraries != null) {
         if (rules != null) {
            ResourceLibrary resourceLibrary = this.resourceLibraryBuilder.buildResourceLibrary(libraries, predefines);

            for (Rule rule : rules) {
               if (rule.getLhs() != null) {
                  Criterion criterion = rule.getLhs().getCriterion();
                  this.rebuildCriterion(criterion, resourceLibrary, true);
               }

               Rhs rhs = rule.getRhs();
               if (rhs != null && rhs.getActions() != null) {
                  for (Action action : rhs.getActions()) {
                     this.rebuildAction(action, resourceLibrary, true);
                  }
               }

               Other other = rule.getOther();
               if (other != null) {
                  List actions = other.getActions();
                  if (actions != null) {
                     for (Action action2 : (Iterable<Action>)(Iterable<?>)(actions)) {
                        this.rebuildAction(action2, resourceLibrary, true);
                     }
                  }
               }

               if (rule instanceof LoopRule) {
                  this.rebuildLoopRuleForDSL(resourceLibrary, rule);
               }
            }
         }
      }
   }

   private void rebuildTemplateAction(Action action, ResourceLibrary resourceLibrary) {
      if (action instanceof TemplateAction) {
         TemplateAction templateAction = (TemplateAction)action;
         ActionTemplateUnit actionTemplateUnit = resourceLibrary.getActionTemplateUnit(templateAction.getId());
         if (actionTemplateUnit == null) {
            throw new RuleException("文件【" + RuleFileHolder.getRuleFile() + "】中，引用动作模版【" + templateAction.getName() + "】不存在！");
         }

         templateAction.setName(actionTemplateUnit.getName());
         templateAction.setPath(actionTemplateUnit.getPath());
      }
   }

   private void rebuildLoopRuleForDSL(ResourceLibrary resourceLibrary, Rule rule) {
      LoopRule loopRule = (LoopRule)rule;
      LoopTarget loopTarget = loopRule.getLoopTarget();
      if (loopTarget != null) {
         Value localValue = loopTarget.getValue();
         this.rebuildValue(localValue, resourceLibrary, true);
      }

      LoopStart loopStart = loopRule.getLoopStart();
      if (loopStart != null && loopStart.getActions() != null) {
         for (Action action : loopStart.getActions()) {
            this.rebuildAction(action, resourceLibrary, true);
         }
      }

      LoopEnd loopEnd = loopRule.getLoopEnd();
      if (loopEnd != null && loopEnd.getActions() != null) {
         for (Action action2 : loopEnd.getActions()) {
            this.rebuildAction(action2, resourceLibrary, true);
         }
      }

      for (LoopRuleUnit loopRuleUnit : loopRule.getUnits()) {
         Lhs lhs = loopRuleUnit.getLhs();
         if (lhs != null) {
            Criterion criterion = lhs.getCriterion();
            this.rebuildCriterion(criterion, resourceLibrary, true);
         }

         Rhs rhs = loopRuleUnit.getRhs();
         if (rhs != null) {
            for (Action action3 : rhs.getActions()) {
               this.rebuildAction(action3, resourceLibrary, true);
            }
         }

         Other other = loopRuleUnit.getOther();
         if (other != null) {
            List actions = other.getActions();
            if (actions != null) {
               for (Action action4 : (Iterable<Action>)(Iterable<?>)(actions)) {
                  this.rebuildAction(action4, resourceLibrary, true);
               }
            }
         }
      }
   }

   public void convertNamedJunctions(List<Rule> rules) {
      for (Rule rule : rules) {
         if (rule.getLhs() != null) {
            Criterion criterion = rule.getLhs().getCriterion();
            Criterion criterion2 = this.buildCriterion(criterion);
            rule.getLhs().setCriterion(criterion2);
         }
      }
   }

   private Criterion buildCriterion(Criterion criterion) {
      if (!(criterion instanceof Junction)) {
         return criterion;
      }

      if (criterion instanceof Junction) {
         this.buildJunction((Junction)criterion);
      }

      return criterion;
   }

   private void buildJunction(Junction junction) {
      List criterions = junction.getCriterions();
      ArrayList items = new ArrayList();

      for (Criterion criterion : (Iterable<Criterion>)(Iterable<?>)(criterions)) {
         if (criterion instanceof Junction) {
            this.buildJunction((Junction)criterion);
         }

         items.add(criterion);
      }

      junction.setCriterions(items);
   }

   public void rebuildAction(Action action, ResourceLibrary resLibraries, boolean forDSL) {
      if (action != null) {
         if (action instanceof PredefineAssignAction) {
            PredefineAssignAction predefineAssignAction = (PredefineAssignAction)action;
            Predefine predefine = resLibraries.getPredefine(predefineAssignAction.getUuid());
            if (predefine != null) {
               predefineAssignAction.setName(predefine.getName());
               String type = predefine.getType();
               if (Datatype.isType(type)) {
                  predefineAssignAction.setDatatype(Datatype.valueOf(type));
               } else {
                  String propertyUuid = predefineAssignAction.getPropertyUuid();
                  if (propertyUuid != null) {
                     VariableData variableByUuid = resLibraries.getVariableByUuid(type, propertyUuid);
                     predefineAssignAction.setVariableCategory(variableByUuid.getCategory().getName());
                     predefineAssignAction.setPropertyName(variableByUuid.getVariable().getName());
                     predefineAssignAction.setPropertyDatatype(variableByUuid.getVariable().getType());
                     predefineAssignAction.setPropertyLabel(variableByUuid.getVariable().getLabel());
                     predefineAssignAction.setVariableCategoryUuid(variableByUuid.getCategory().getUuid());
                  } else {
                     VariableCategory variableCategoryByUuid = resLibraries.getVariableCategoryByUuid(type);
                     predefineAssignAction.setVariableCategory(variableCategoryByUuid.getName());
                     predefineAssignAction.setVariableCategoryUuid(variableCategoryByUuid.getUuid());
                  }
               }
            }

            Value localValue = predefineAssignAction.getValue();
            this.rebuildValue(localValue, resLibraries, forDSL);
         } else if (action instanceof VariableAssignAction) {
            List variableCategories = resLibraries.getVariableCategories();
            if (variableCategories == null) {
               return;
            }

            VariableAssignAction variableAssignAction = (VariableAssignAction)action;
            VariableData variableByUuid2 = resLibraries.getVariableByUuid(variableAssignAction.getCategoryUuid(), variableAssignAction.getUuid());
            if (variableByUuid2 == null) {
               throw new RuleException(
                  "文件【" + RuleFileHolder.getRuleFile() + "】中，变量赋值对象【" + variableAssignAction.getVariableCategory() + "." + variableAssignAction.getVariableLabel() + "】不存在"
               );
            }

            LeftType type2 = variableAssignAction.getType();
            if (type2 != null) {
               Variable variable = variableByUuid2.getVariable();
               if (variableAssignAction.getKeyUuid() == null) {
                  variableAssignAction.setVariableName(variable.getName());
                  variableAssignAction.setVariableLabel(variable.getLabel());
                  variableAssignAction.setDatatype(variable.getType());
               } else {
                  variableAssignAction.setKeyLabel(variable.getLabel());
                  variableAssignAction.setKeyName(variable.getName());
                  VariableData variableByUuid3 = resLibraries.getVariableByUuid(variableAssignAction.getKeyCategoryUuid(), variableAssignAction.getKeyUuid());
                  if (variableByUuid3 == null) {
                     throw new RuleException(
                        "文件【"
                           + RuleFileHolder.getRuleFile()
                           + "】中，对象【"
                           + variableAssignAction.getVariableCategory()
                           + "."
                           + variableAssignAction.getVariableLabel()
                           + "("
                           + variableAssignAction.getVariableName()
                           + ")】对应的属性在库中不存在"
                     );
                  }

                  variableAssignAction.setVariableName(variableByUuid3.getVariable().getName());
                  variableAssignAction.setVariableLabel(variableByUuid3.getVariable().getLabel());
                  variableAssignAction.setDatatype(variableByUuid3.getVariable().getType());
               }

               variableAssignAction.setDatatype(variable.getType());
               variableAssignAction.setVariableCategory(variableByUuid2.getCategory().getName());
            } else if (type2 == null) {
               String variableCategory = variableAssignAction.getVariableCategory();
               String variableLabel = variableAssignAction.getVariableLabel();
               if (variableLabel == null) {
                  throw new RuleException("文件【" + RuleFileHolder.getRuleFile() + "】中，赋值只能针对具体变量或参数，请检查规则中变量赋值操作中是否存在直接对对象赋值的操作.");
               }

               if (variableLabel.equals("return_value__")) {
                  variableAssignAction.setVariableName(variableLabel);
                  variableAssignAction.setDatatype(Datatype.Boolean);
               } else if (variableLabel.equals("return_to__")) {
                  variableAssignAction.setVariableName(variableLabel);
                  variableAssignAction.setDatatype(Datatype.String);
               } else {
                  VariableData variableByUuid4 = resLibraries.getVariableByUuid(variableAssignAction.getCategoryUuid(), variableAssignAction.getUuid());
                  if (variableByUuid4 == null) {
                     throw new RuleException("文件【" + RuleFileHolder.getRuleFile() + "】中，变量[" + variableCategory + "." + variableLabel + "]不存在");
                  }

                  Variable variable2 = variableByUuid4.getVariable();
                  variableAssignAction.setDatatype(variable2.getType());
                  variableAssignAction.setVariableLabel(variable2.getLabel());
                  variableAssignAction.setDatatype(variable2.getType());
                  variableAssignAction.setVariableName(variable2.getName());
                  variableAssignAction.setDatatype(variable2.getType());
                  variableAssignAction.setVariableCategory(variableByUuid4.getCategory().getName());
               }
            }

            Value localValue2 = variableAssignAction.getValue();
            this.rebuildValue(localValue2, resLibraries, forDSL);
         } else if (action instanceof PredefineAssignAction) {
            PredefineAssignAction predefineAssignAction2 = (PredefineAssignAction)action;
            Predefine predefine2 = resLibraries.getPredefine(predefineAssignAction2.getUuid());
            if (predefine2 != null) {
               predefineAssignAction2.setName(predefine2.getName());
               String type3 = predefine2.getType();
               if (Datatype.isType(type3)) {
                  predefineAssignAction2.setDatatype(Datatype.valueOf(type3));
               } else {
                  String propertyUuid2 = predefineAssignAction2.getPropertyUuid();
                  if (propertyUuid2 != null) {
                     VariableData variableByUuid5 = resLibraries.getVariableByUuid(type3, propertyUuid2);
                     predefineAssignAction2.setVariableCategory(variableByUuid5.getCategory().getName());
                     predefineAssignAction2.setPropertyName(variableByUuid5.getVariable().getName());
                     predefineAssignAction2.setPropertyLabel(variableByUuid5.getVariable().getLabel());
                     predefineAssignAction2.setVariableCategoryUuid(variableByUuid5.getCategory().getUuid());
                  } else {
                     VariableCategory variableCategoryByUuid2 = resLibraries.getVariableCategoryByUuid(type3);
                     predefineAssignAction2.setVariableCategory(variableCategoryByUuid2.getName());
                     predefineAssignAction2.setVariableCategoryUuid(variableCategoryByUuid2.getUuid());
                  }
               }
            }
         } else if (action instanceof ConsolePrintAction) {
            ConsolePrintAction consolePrintAction = (ConsolePrintAction)action;
            Value localValue3 = consolePrintAction.getValue();
            this.rebuildValue(localValue3, resLibraries, forDSL);
         } else if (action instanceof ExecuteMethodAction) {
            List actionLibraries = resLibraries.getActionLibraries();
            if (actionLibraries == null) {
               return;
            }

            ExecuteMethodAction executeMethodAction = (ExecuteMethodAction)action;
            ActionData actionData = null;
            if (executeMethodAction.getCategoryUuid() == null) {
               actionData = resLibraries.getActionByName(executeMethodAction.getBeanLabel(), executeMethodAction.getMethodLabel());
               if (actionData == null) {
                  actionData = resLibraries.getActionByBean(executeMethodAction.getBeanId(), executeMethodAction.getMethodName());
               }
            } else {
               actionData = resLibraries.getActionByUuid(executeMethodAction.getCategoryUuid(), executeMethodAction.getUuid());
            }

            if (actionData == null) {
               throw new RuleException("Bean [" + executeMethodAction.getBeanLabel() + "] not define methods.");
            }

            executeMethodAction.setBeanId(actionData.getBean().getId());
            executeMethodAction.setBeanLabel(actionData.getBean().getName());
            executeMethodAction.setMethodName(actionData.getMethod().getMethodName());
            executeMethodAction.setMethodLabel(actionData.getMethod().getName());
            List parameters = executeMethodAction.getParameters();
            this.rebuildParameters(resLibraries, parameters, actionData.getMethod().getParameters(), forDSL);
         }
      }
   }

   private void rebuildCommonFunctionParameter(CommonFunctionParameter commonFunctionParameter, ResourceLibrary resourceLibrary, boolean forDSL) {
      if (commonFunctionParameter != null) {
         String property = commonFunctionParameter.getProperty();
         if (!StringUtils.isEmpty(property)) {
            Value objectParameter = commonFunctionParameter.getObjectParameter();
            this.rebuildValue(objectParameter, resourceLibrary, forDSL);
            String text = null;
            if (objectParameter instanceof VariableValue) {
               VariableValue variableValue = (VariableValue)objectParameter;
               text = variableValue.getCategoryUuid();
            } else if (objectParameter instanceof VariableCategoryValue) {
               VariableCategoryValue variableCategoryValue = (VariableCategoryValue)objectParameter;
               text = variableCategoryValue.getUuid();
            } else {
               if (!(objectParameter instanceof ParameterValue)) {
                  throw new RuleException("Function parameter is invalid.");
               }

               ParameterValue parameterValue = (ParameterValue)objectParameter;
               text = parameterValue.getUuid();
            }

            for (VariableCategory variableCategory : resourceLibrary.getVariableCategories()) {
               if (text.equals(variableCategory.getUuid())) {
                  for (Variable variable : variableCategory.getVariables()) {
                     if (variable.getName().equals(property) || variable.getLabel().equals(property)) {
                        commonFunctionParameter.setProperty(variable.getName());
                        commonFunctionParameter.setPropertyLabel(variable.getLabel());
                        break;
                     }
                  }
               }
            }
         }
      }
   }

   private void rebuildParameters(ResourceLibrary resourceLibrary, List<Parameter> parameters, List<com.bstek.urule.model.library.action.Parameter> targetParameters, boolean forDSL) {
      if (parameters != null && targetParameters != null) {
         for (int index = 0; index < parameters.size() && index <= targetParameters.size() - 1; index++) {
            Parameter parameter = (Parameter)parameters.get(index);
            com.bstek.urule.model.library.action.Parameter targetParameter = (com.bstek.urule.model.library.action.Parameter)targetParameters.get(index);
            parameter.setType(targetParameter.getType());
            Value value = parameter.getValue();
            this.rebuildValue(value, resourceLibrary, forDSL);
         }
      }
   }

   public void rebuildCriterion(Criterion criterion, ResourceLibrary resLibraries, boolean forDSL) {
      if (criterion != null) {
         if (criterion instanceof Criteria) {
            Criteria criteria = (Criteria)criterion;
            this.rebuildCriteria(resLibraries, criteria, forDSL);
         } else if (criterion instanceof Junction) {
            Junction junction = (Junction)criterion;
            List criterions = junction.getCriterions();
            if (criterions != null) {
               for (Criterion criterion2 : (Iterable<Criterion>)(Iterable<?>)(criterions)) {
                  this.rebuildCriterion(criterion2, resLibraries, forDSL);
               }
            }
         } else if (criterion instanceof ConditionTemplateCriterion) {
            ConditionTemplateCriterion conditionTemplateCriterion = (ConditionTemplateCriterion)criterion;
            ConditionTemplateUnit conditionTemplateUnit = resLibraries.getConditionTemplateUnit(conditionTemplateCriterion.getId());
            if (conditionTemplateUnit == null) {
               throw new RuleException("文件【" + RuleFileHolder.getRuleFile() + "】中，引用条件模版【" + conditionTemplateCriterion.getName() + "】不存在!");
            }

            conditionTemplateCriterion.setName(conditionTemplateUnit.getName());
            conditionTemplateCriterion.setPath(conditionTemplateUnit.getPath());
         }
      }
   }

   private void rebuildCriteria(ResourceLibrary resourceLibrary, Criteria criteria, boolean forDSL) {
      Left left = criteria.getLeft();
      LeftPart leftPart = left.getLeftPart();
      if (leftPart instanceof VariableLeftPart) {
         VariableLeftPart variableLeftPart = (VariableLeftPart)leftPart;
         if (variableLeftPart.getKeyCategoryUuid() != null) {
            VariableData variableByUuid = resourceLibrary.getVariableByUuid(variableLeftPart.getKeyCategoryUuid(), variableLeftPart.getKeyUuid());
            Variable variable = variableByUuid.getVariable();
            variableLeftPart.setDatatype(variable.getType());
            variableLeftPart.setVariableLabel(variable.getLabel());
            variableLeftPart.setDatatype(variable.getType());
            variableLeftPart.setVariableName(variable.getName());
            variableByUuid = resourceLibrary.getVariableByUuid(variableLeftPart.getCategoryUuid(), variableLeftPart.getUuid());
            variableLeftPart.setKeyName(variableByUuid.getVariable().getName());
            variableLeftPart.setKeyLabel(variableByUuid.getVariable().getLabel());
         } else {
            VariableData variableData = null;
            if ("参数".equals(variableLeftPart.getVariableCategory())) {
               Variable parameterByUuid = resourceLibrary.getParameterByUuid(variableLeftPart.getKeyUuid(), variableLeftPart.getKeyName(), variableLeftPart.getKeyLabel());
               if (parameterByUuid != null && parameterByUuid.getType() == Datatype.Object) {
                  variableData = resourceLibrary.getVariableByUuid(parameterByUuid.getDataType(), variableLeftPart.getUuid());
                  if (StringUtils.isBlank(variableLeftPart.getKeyUuid())) {
                     variableLeftPart.setKeyUuid(parameterByUuid.getUuid());
                  }
               }

               if (variableData == null) {
                  variableData = resourceLibrary.getVariableByName(variableLeftPart.getCategoryUuid(), variableLeftPart.getVariableName());
               }
            } else {
               variableData = resourceLibrary.getVariableByUuid(variableLeftPart.getCategoryUuid(), variableLeftPart.getUuid());
            }

            if (variableData == null) {
               variableData = resourceLibrary.getVariableByName(variableLeftPart.getVariableCategory(), variableLeftPart.getVariableName());
            }

            if (variableData == null) {
               throw new RuleException("文件【" + RuleFileHolder.getRuleFile() + "】中，变量[" + variableLeftPart.getVariableCategory() + "." + variableLeftPart.getVariableLabel() + "]不存在");
            }

            Variable variable2 = variableData.getVariable();
            variableLeftPart.setDatatype(variable2.getType());
            variableLeftPart.setVariableLabel(variable2.getLabel());
            variableLeftPart.setDatatype(variable2.getType());
            variableLeftPart.setVariableName(variable2.getName());
            if (!"参数".equals(variableLeftPart.getVariableCategory())) {
               variableLeftPart.setVariableCategory(variableData.getCategory().getName());
            }
         }
      } else if (leftPart instanceof PredefineLeftPart) {
         PredefineLeftPart predefineLeftPart = (PredefineLeftPart)leftPart;
         Predefine predefine = resourceLibrary.getPredefine(predefineLeftPart.getUuid());
         if (predefine != null) {
            predefineLeftPart.setName(predefine.getName());
            String type = predefine.getType();
            if (Datatype.isType(type)) {
               predefineLeftPart.setDatatype(Datatype.valueOf(type));
            } else {
               String propertyUuid = predefineLeftPart.getPropertyUuid();
               if (propertyUuid != null) {
                  VariableData variableByUuid2 = resourceLibrary.getVariableByUuid(type, propertyUuid);
                  if (variableByUuid2 == null) {
                     throw new RuleException("变量属性不存在:" + propertyUuid);
                  }

                  predefineLeftPart.setVariableCategory(variableByUuid2.getCategory().getName());
                  predefineLeftPart.setPropertyName(variableByUuid2.getVariable().getName());
                  predefineLeftPart.setPropertyLabel(variableByUuid2.getVariable().getLabel());
                  predefineLeftPart.setVariableCategoryUuid(variableByUuid2.getCategory().getUuid());
               } else {
                  VariableCategory variableCategoryByUuid = resourceLibrary.getVariableCategoryByUuid(type);
                  if (variableCategoryByUuid == null) {
                     throw new RuleException("变量不存在:" + propertyUuid);
                  }

                  predefineLeftPart.setVariableCategory(variableCategoryByUuid.getName());
                  predefineLeftPart.setVariableCategoryUuid(variableCategoryByUuid.getUuid());
               }
            }
         }
      } else if (leftPart instanceof CommonFunctionLeftPart) {
         CommonFunctionLeftPart commonFunctionLeftPart = (CommonFunctionLeftPart)leftPart;
         CommonFunctionParameter parameter = commonFunctionLeftPart.getParameter();
         this.rebuildCommonFunctionParameter(parameter, resourceLibrary, forDSL);
      } else if (leftPart instanceof MethodLeftPart) {
         MethodLeftPart methodLeftPart = (MethodLeftPart)leftPart;
         ActionData actionData = null;
         if (methodLeftPart.getCategoryUuid() == null) {
            actionData = resourceLibrary.getActionByName(methodLeftPart.getBeanLabel(), methodLeftPart.getMethodLabel());
            if (actionData == null) {
               actionData = resourceLibrary.getActionByBean(methodLeftPart.getBeanId(), methodLeftPart.getMethodName());
            }
         } else {
            actionData = resourceLibrary.getActionByUuid(methodLeftPart.getCategoryUuid(), methodLeftPart.getUuid());
         }

         if (actionData == null) {
            throw new RuleException("Bean[" + methodLeftPart.getBeanLabel() + "] not exist.");
         }

         methodLeftPart.setBeanId(actionData.getBean().getId());
         methodLeftPart.setBeanLabel(actionData.getBean().getName());
         methodLeftPart.setMethodName(actionData.getMethod().getMethodName());
         methodLeftPart.setMethodLabel(actionData.getMethod().getName());
         List parameters = methodLeftPart.getParameters();
         this.rebuildParameters(resourceLibrary, parameters, actionData.getMethod().getParameters(), forDSL);
      } else if (leftPart instanceof FunctionLeftPart) {
         FunctionLeftPart functionLeftPart = (FunctionLeftPart)leftPart;
         List parameters2 = functionLeftPart.getParameters();
         if (parameters2 != null && parameters2.size() > 0) {
            for (Parameter parameter2 : (Iterable<Parameter>)(Iterable<?>)(parameters2)) {
               Value localValue = parameter2.getValue();
               if (localValue != null) {
                  this.rebuildValue(localValue, resourceLibrary, forDSL);
               }
            }
         }
      } else if (leftPart instanceof AccumulateLeftPart) {
         AccumulateLeftPart accumulateLeftPart = (AccumulateLeftPart)leftPart;
         List conditionItems = accumulateLeftPart.getConditionItems();
         if (conditionItems != null) {
            for (ConditionItem conditionItem : (Iterable<ConditionItem>)(Iterable<?>)(conditionItems)) {
               Value localValue2 = conditionItem.getValue();
               this.rebuildValue(localValue2, resourceLibrary, forDSL);
            }
         }

         List calculateItems = accumulateLeftPart.getCalculateItems();
         if (calculateItems != null) {
            for (CalculateItem calculateItem : (Iterable<CalculateItem>)(Iterable<?>)(calculateItems)) {
               String assignCategoryUuid = calculateItem.getAssignCategoryUuid();
               String assignVariableUuid = calculateItem.getAssignVariableUuid();
               if (StringUtils.isNotBlank(assignVariableUuid) && StringUtils.isNotBlank(assignCategoryUuid)) {
                  VariableData variableByUuid3 = resourceLibrary.getVariableByUuid(assignCategoryUuid, assignVariableUuid);
                  if (variableByUuid3 == null) {
                     throw new RuleException(
                        "文件【" + RuleFileHolder.getRuleFile() + "】中，引用变量[" + calculateItem.getAssignVariableCategory() + "." + calculateItem.getAssignVariableLabel() + "]不存在"
                     );
                  }

                  calculateItem.setAssignDatatype(variableByUuid3.getVariable().getType());
                  calculateItem.setAssignVariableCategory(variableByUuid3.getCategory().getName());
                  calculateItem.setAssignVariable(variableByUuid3.getVariable().getName());
                  calculateItem.setAssignVariableLabel(variableByUuid3.getVariable().getLabel());
               }
            }
         }

         LoopTarget loopTarget = accumulateLeftPart.getLoopTarget();
         Value localValue3 = loopTarget.getValue();
         this.rebuildValue(localValue3, resourceLibrary, forDSL);
      }

      ComplexArithmetic arithmetic = left.getArithmetic();
      if (arithmetic != null) {
         Value localValue4 = arithmetic.getValue();
         this.rebuildValue(localValue4, resourceLibrary, forDSL);
      }

      Value localValue5 = criteria.getValue();
      this.rebuildValue(localValue5, resourceLibrary, forDSL);
   }

   public void rebuildValue(Value value, ResourceLibrary resLibraries, boolean forDSL) {
      if (value != null) {
         if (value instanceof ParenValue) {
            ParenValue parenValue = (ParenValue)value;
            Value localValue = parenValue.getValue();
            this.rebuildValue(localValue, resLibraries, forDSL);
         } else if (value instanceof ConstantValue) {
            ConstantValue constantValue = (ConstantValue)value;
            ConstantData constantByUuid = resLibraries.getConstantByUuid(constantValue.getCategoryUuid(), constantValue.getUuid());
            if (constantByUuid == null) {
               throw new RuleException(
                  "文件【" + RuleFileHolder.getRuleFile() + "】中，引用常量[" + constantValue.getConstantCategory() + "." + constantValue.getConstantLabel() + "]不存在"
               );
            }

            constantValue.setConstantCategory(constantByUuid.getCategory().getLabel());
            constantValue.setConstantName(constantByUuid.getConstant().getName());
            constantValue.setConstantLabel(constantByUuid.getConstant().getLabel());
            constantValue.setDatatype(constantByUuid.getConstant().getType());
         } else if (value instanceof VariableValue) {
            VariableValue variableValue = (VariableValue)value;
            VariableData variableByUuid = resLibraries.getVariableByUuid(variableValue.getCategoryUuid(), variableValue.getUuid());
            if (variableByUuid == null) {
               throw new RuleException(
                  "文件【" + RuleFileHolder.getRuleFile() + "】中，引用变量[" + variableValue.getVariableCategory() + "." + variableValue.getVariableLabel() + "]不存在"
               );
            }

            Variable variable = variableByUuid.getVariable();
            variableValue.setDatatype(variable.getType());
            variableValue.setVariableLabel(variable.getLabel());
            variableValue.setDatatype(variable.getType());
            variableValue.setVariableName(variable.getName());
            variableValue.setDatatype(variable.getType());
            variableValue.setVariableCategory(variableByUuid.getCategory().getName());
         } else if (value instanceof PredefineValue) {
            PredefineValue predefineValue = (PredefineValue)value;
            String uuid = predefineValue.getUuid();
            Predefine predefine = resLibraries.getPredefine(uuid);
            if (predefine == null) {
               throw new RuleException("文件【" + RuleFileHolder.getRuleFile() + "】中，引用预定义对象[" + uuid + "]不存在");
            }

            predefineValue.setName(predefine.getName());
            String type = predefine.getType();
            if (Datatype.isType(type)) {
               predefineValue.setDatatype(Datatype.valueOf(type));
            } else {
               String propertyUuid = predefineValue.getPropertyUuid();
               if (propertyUuid != null) {
                  VariableData variableByUuid2 = resLibraries.getVariableByUuid(type, propertyUuid);
                  predefineValue.setVariableCategory(variableByUuid2.getCategory().getName());
                  predefineValue.setPropertyName(variableByUuid2.getVariable().getName());
                  predefineValue.setPropertyLabel(variableByUuid2.getVariable().getLabel());
                  predefineValue.setVariableCategoryUuid(variableByUuid2.getCategory().getUuid());
               } else {
                  VariableCategory variableCategoryByUuid = resLibraries.getVariableCategoryByUuid(type);
                  predefineValue.setVariableCategory(variableCategoryByUuid.getName());
                  predefineValue.setVariableCategoryUuid(variableCategoryByUuid.getUuid());
               }
            }
         } else if (value instanceof VariableCategoryValue) {
            VariableCategoryValue variableCategoryValue = (VariableCategoryValue)value;
            VariableCategory variableCategoryByUuid2 = resLibraries.getVariableCategoryByUuid(variableCategoryValue.getUuid());
            if (variableCategoryByUuid2 != null) {
               variableCategoryValue.setVariableCategory(variableCategoryByUuid2.getName());
            }
         } else if (value instanceof ParameterValue) {
            ParameterValue parameterValue = (ParameterValue)value;
            VariableData variableByUuid3 = resLibraries.getVariableByUuid("参数", parameterValue.getUuid());
            if (variableByUuid3 == null) {
               throw new RuleException("文件【" + RuleFileHolder.getRuleFile() + "】中，引用的参数[" + "参数" + "." + parameterValue.getVariableLabel() + "]不存在");
            }

            Variable variable2 = variableByUuid3.getVariable();
            if (parameterValue.getKeyUuid() == null) {
               parameterValue.setVariableLabel(variable2.getLabel());
               parameterValue.setVariableName(variable2.getName());
               parameterValue.setDatatype(variable2.getType());
            } else {
               parameterValue.setKeyLabel(variable2.getLabel());
               parameterValue.setKeyName(variable2.getName());
               variableByUuid3 = resLibraries.getVariableByUuid(parameterValue.getKeyCategoryUuid(), parameterValue.getKeyUuid());
               if (variableByUuid3 == null) {
                  throw new RuleException(
                     "文件【" + RuleFileHolder.getRuleFile() + "】中，引用的参数[" + "参数" + "." + parameterValue.getKeyLabel() + "." + parameterValue.getVariableLabel() + "]不存在"
                  );
               }

               variable2 = variableByUuid3.getVariable();
               parameterValue.setVariableLabel(variable2.getLabel());
               parameterValue.setVariableName(variable2.getName());
               parameterValue.setDatatype(variable2.getType());
            }
         } else if (value instanceof CommonFunctionValue) {
            CommonFunctionValue commonFunctionValue = (CommonFunctionValue)value;
            CommonFunctionParameter parameter = commonFunctionValue.getParameter();
            this.rebuildCommonFunctionParameter(parameter, resLibraries, forDSL);
         } else if (value instanceof MethodValue) {
            MethodValue methodValue = (MethodValue)value;
            ActionData actionData = null;
            if (methodValue.getCategoryUuid() != null) {
               actionData = resLibraries.getActionByUuid(methodValue.getCategoryUuid(), methodValue.getUuid());
            } else {
               actionData = resLibraries.getActionByName(methodValue.getBeanLabel(), methodValue.getMethodLabel());
               if (actionData == null) {
                  actionData = resLibraries.getActionByBean(methodValue.getBeanId(), methodValue.getMethodName());
               }
            }

            if (actionData == null) {
               throw new RuleException("Bean[" + methodValue.getBeanLabel() + "] not exist.");
            }

            methodValue.setBeanId(actionData.getBean().getId());
            methodValue.setBeanLabel(actionData.getBean().getName());
            methodValue.setMethodName(actionData.getMethod().getMethodName());
            methodValue.setMethodLabel(actionData.getMethod().getName());
            List parameters = methodValue.getParameters();
            this.rebuildParameters(resLibraries, parameters, actionData.getMethod().getParameters(), forDSL);
         } else if (value instanceof MathValue) {
            MathValue mathValue = (MathValue)value;
            MathSign mathSign = mathValue.getMathSign();
            this.rebuildMathSign(mathSign, resLibraries, forDSL);
         }

         ComplexArithmetic arithmetic = value.getArithmetic();
         if (arithmetic != null) {
            Value localValue2 = arithmetic.getValue();
            this.rebuildValue(localValue2, resLibraries, forDSL);
         }
      }
   }

   private void rebuildMathSign(MathSign mathSign, ResourceLibrary resourceLibrary, boolean forDSL) {
      if (mathSign != null) {
         if (mathSign instanceof AbsoluteMath) {
            AbsoluteMath absoluteMath = (AbsoluteMath)mathSign;
            this.rebuildValue(absoluteMath.getValue(), resourceLibrary, forDSL);
         } else if (mathSign instanceof DownRoundMath) {
            DownRoundMath downRoundMath = (DownRoundMath)mathSign;
            this.rebuildValue(downRoundMath.getValue(), resourceLibrary, forDSL);
         } else if (mathSign instanceof ExtremumMath) {
            ExtremumMath extremumMath = (ExtremumMath)mathSign;
            this.rebuildValue(extremumMath.getValue1(), resourceLibrary, forDSL);
            this.rebuildValue(extremumMath.getValue2(), resourceLibrary, forDSL);
         } else if (mathSign instanceof FractionMath) {
            FractionMath fractionMath = (FractionMath)mathSign;
            this.rebuildValue(fractionMath.getDenominator(), resourceLibrary, forDSL);
            this.rebuildValue(fractionMath.getNumerator(), resourceLibrary, forDSL);
         } else if (mathSign instanceof LnMath) {
            LnMath lnMath = (LnMath)mathSign;
            this.rebuildValue(lnMath.getValue(), resourceLibrary, forDSL);
         } else if (mathSign instanceof LogMath) {
            LogMath logMath = (LogMath)mathSign;
            this.rebuildValue(logMath.getValue(), resourceLibrary, forDSL);
            this.rebuildValue(logMath.getBaseValue(), resourceLibrary, forDSL);
         } else if (mathSign instanceof NRadicalMath) {
            NRadicalMath nRadicalMath = (NRadicalMath)mathSign;
            this.rebuildValue(nRadicalMath.getValue(), resourceLibrary, forDSL);
            this.rebuildValue(nRadicalMath.getPower(), resourceLibrary, forDSL);
         } else if (mathSign instanceof PowerMath) {
            PowerMath powerMath = (PowerMath)mathSign;
            this.rebuildValue(powerMath.getBase(), resourceLibrary, forDSL);
            this.rebuildValue(powerMath.getPower(), resourceLibrary, forDSL);
         } else if (mathSign instanceof RadicalMath) {
            RadicalMath radicalMath = (RadicalMath)mathSign;
            this.rebuildValue(radicalMath.getValue(), resourceLibrary, forDSL);
         } else if (mathSign instanceof SigmaMath) {
            SigmaMath sigmaMath = (SigmaMath)mathSign;
            this.rebuildValue(sigmaMath.getExpr(), resourceLibrary, forDSL);
            this.rebuildValue(sigmaMath.getIvalue(), resourceLibrary, forDSL);
            this.rebuildValue(sigmaMath.getSuperior(), resourceLibrary, forDSL);
         } else if (mathSign instanceof TriangleFunctionMath) {
            TriangleFunctionMath triangleFunctionMath = (TriangleFunctionMath)mathSign;
            this.rebuildValue(triangleFunctionMath.getValue(), resourceLibrary, forDSL);
         } else if (mathSign instanceof UpRoundMath) {
            UpRoundMath upRoundMath = (UpRoundMath)mathSign;
            this.rebuildValue(upRoundMath.getValue(), resourceLibrary, forDSL);
         }
      }
   }

   public void setResourceLibraryBuilder(ResourceLibraryBuilder resourceLibraryBuilder) {
      this.resourceLibraryBuilder = resourceLibraryBuilder;
   }

   public ResourceLibraryBuilder getResourceLibraryBuilder() {
      return this.resourceLibraryBuilder;
   }
}
