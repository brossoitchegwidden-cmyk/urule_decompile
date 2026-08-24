package com.bstek.urule.builder.rete;

import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.library.ResourceLibrary;
import com.bstek.urule.model.library.variable.VariableCategory;
import com.bstek.urule.model.rete.ObjectTypeNode;
import com.bstek.urule.model.rule.CommonFunctionValue;
import com.bstek.urule.model.rule.ComplexArithmetic;
import com.bstek.urule.model.rule.MethodValue;
import com.bstek.urule.model.rule.Parameter;
import com.bstek.urule.model.rule.ParameterValue;
import com.bstek.urule.model.rule.ParenValue;
import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.VariableCategoryValue;
import com.bstek.urule.model.rule.VariableValue;
import com.bstek.urule.model.rule.lhs.AccumulateLeftPart;
import com.bstek.urule.model.rule.lhs.BaseCriteria;
import com.bstek.urule.model.rule.lhs.CalculateItem;
import com.bstek.urule.model.rule.lhs.CommonFunctionLeftPart;
import com.bstek.urule.model.rule.lhs.CommonFunctionParameter;
import com.bstek.urule.model.rule.lhs.ConditionItem;
import com.bstek.urule.model.rule.lhs.ConditionTemplateCriterion;
import com.bstek.urule.model.rule.lhs.Criteria;
import com.bstek.urule.model.rule.lhs.Criterion;
import com.bstek.urule.model.rule.lhs.FunctionLeftPart;
import com.bstek.urule.model.rule.lhs.Junction;
import com.bstek.urule.model.rule.lhs.LeftPart;
import com.bstek.urule.model.rule.lhs.MethodLeftPart;
import com.bstek.urule.model.rule.lhs.VariableLeftPart;
import java.util.ArrayList;
import java.util.List;

public class BuildContextImpl implements BuildContext {
   private ResourceLibrary resourceLibrary;
   private List<ObjectTypeNode> objectTypeNodes;
   private IdGenerator idGenerator;
   private Rule currentRule;

   public BuildContextImpl(ResourceLibrary resourceLibrary, List<ObjectTypeNode> objectTypeNodes) {
      this.resourceLibrary = resourceLibrary;
      this.objectTypeNodes = objectTypeNodes;
      this.idGenerator = new IdGenerator();
   }

   public BuildContextImpl(List<ObjectTypeNode> objectTypeNodes, BuildContext parentContent) {
      this.resourceLibrary = parentContent.getResourceLibrary();
      this.objectTypeNodes = objectTypeNodes;
      this.idGenerator = parentContent.getIdGenerator();
   }

   @Override
   public boolean assertSameType(BaseCriteria left, BaseCriteria right) {
      VariableCategory variableCategory = this.resolveVariableCategory(left);
      VariableCategory variableCategory2 = this.resolveVariableCategory(right);
      return variableCategory != null && variableCategory2 != null ? variableCategory.getClazz().equals(variableCategory2.getClazz()) : false;
   }

   private VariableCategory resolveVariableCategory(BaseCriteria baseCriteria) {
      VariableCategory variableCategory = null;
      if (baseCriteria instanceof Criteria) {
         Criteria criteria = (Criteria)baseCriteria;
         LeftPart leftPart = criteria.getLeft().getLeftPart();
         if (leftPart instanceof VariableLeftPart) {
            VariableLeftPart variableLeftPart = (VariableLeftPart)leftPart;
            variableCategory = this.resourceLibrary.getVariableCategoryByUuid(variableLeftPart.getCategoryUuid());
         }

         return variableCategory;
      } else {
         throw new RuleException("Unknow Criteria : " + baseCriteria);
      }
   }

   @Override
   public List<String> getObjectTypeByCriterions(List<Criterion> criterions) {
      ArrayList objectTypeByCriterions = new ArrayList();

      for (Criterion criterion : criterions) {
         if (criterion instanceof Criteria) {
            objectTypeByCriterions.addAll(this.getObjectType((Criteria)criterion));
         } else if (criterion instanceof Junction) {
            Junction junction = (Junction)criterion;
            objectTypeByCriterions.addAll(this.getObjectTypeByCriterions(junction.getCriterions()));
         } else if (criterion instanceof ConditionTemplateCriterion) {
            throw new RuleException("条件模版不能在N个条件中使用");
         }
      }

      return objectTypeByCriterions;
   }

   @Override
   public List<String> getObjectType(BaseCriteria criteria) {
      ArrayList objectType = new ArrayList();
      if (!(criteria instanceof Criteria)) {
         throw new RuleException("Unknow Criteria : " + criteria);
      }

      Criteria criteria2 = (Criteria)criteria;
      LeftPart leftPart = criteria2.getLeft().getLeftPart();
      if (leftPart instanceof VariableLeftPart) {
         VariableLeftPart variableLeftPart = (VariableLeftPart)leftPart;
         VariableCategory variableCategoryByUuid = this.resourceLibrary.getVariableCategoryByUuid(variableLeftPart.getCategoryUuid());
         if (variableCategoryByUuid == null) {
            variableCategoryByUuid = this.resourceLibrary.getVariableCategoryByCategoryName(variableLeftPart.getVariableCategory());
         }

         objectType.add(variableCategoryByUuid.getClazz());
      } else if (leftPart instanceof CommonFunctionLeftPart) {
         CommonFunctionLeftPart commonFunctionLeftPart = (CommonFunctionLeftPart)leftPart;
         CommonFunctionParameter parameter = commonFunctionLeftPart.getParameter();
         Value objectParameter = parameter.getObjectParameter();
         this.collectReferencedClasses(objectParameter, objectType);
      } else if (leftPart instanceof MethodLeftPart) {
         MethodLeftPart methodLeftPart = (MethodLeftPart)leftPart;
         List parameters = methodLeftPart.getParameters();
         if (parameters != null) {
            for (Parameter parameter2 : (Iterable<Parameter>)(Iterable<?>)(parameters)) {
               Value localValue = parameter2.getValue();
               this.collectReferencedClasses(localValue, objectType);
            }
         }
      } else if (leftPart instanceof FunctionLeftPart) {
         FunctionLeftPart functionLeftPart = (FunctionLeftPart)leftPart;
         List parameters2 = functionLeftPart.getParameters();
         if (parameters2 != null) {
            for (Parameter parameter3 : (Iterable<Parameter>)(Iterable<?>)(parameters2)) {
               Value localValue2 = parameter3.getValue();
               this.collectReferencedClasses(localValue2, objectType);
            }
         }
      } else if (leftPart instanceof AccumulateLeftPart) {
         AccumulateLeftPart accumulateLeftPart = (AccumulateLeftPart)leftPart;
         Value localValue3 = accumulateLeftPart.getLoopTarget().getValue();
         this.collectReferencedClasses(localValue3, objectType);

         for (ConditionItem conditionItem : accumulateLeftPart.getConditionItems()) {
            localValue3 = conditionItem.getValue();
            if (localValue3 != null) {
               this.collectReferencedClasses(localValue3, objectType);
            }
         }

         for (CalculateItem calculateItem : accumulateLeftPart.getCalculateItems()) {
            if (calculateItem.isEnableAssignment()) {
               VariableCategory variableCategoryByUuid2 = this.resourceLibrary.getVariableCategoryByUuid(calculateItem.getAssignCategoryUuid());
               String clazz = variableCategoryByUuid2.getClazz();
               if (!objectType.contains(clazz)) {
                  objectType.add(clazz);
               }
            }
         }
      }

      ComplexArithmetic arithmetic = criteria2.getLeft().getArithmetic();
      if (arithmetic != null) {
         this.collectReferencedClasses(arithmetic.getValue(), objectType);
      }

      this.collectReferencedClasses(criteria2.getValue(), objectType);
      if (objectType.size() == 0) {
         objectType.add("*");
      }

      if (criteria instanceof Criteria) {
         criteria2 = (Criteria)criteria;
         criteria2.addNecessaryClasses(objectType);
      }

      return objectType;
   }

   private void collectReferencedClasses(Value localValue, List<String> strings) {
      if (localValue != null) {
         if (localValue instanceof CommonFunctionValue) {
            CommonFunctionValue commonFunctionValue = (CommonFunctionValue)localValue;
            CommonFunctionParameter parameter = commonFunctionValue.getParameter();
            Value objectParameter = parameter.getObjectParameter();
            this.collectReferencedClasses(objectParameter, strings);
         } else if (localValue instanceof MethodValue) {
            MethodValue methodValue = (MethodValue)localValue;
            List parameters = methodValue.getParameters();
            if (parameters != null) {
               for (Parameter parameter2 : (Iterable<Parameter>)(Iterable<?>)(parameters)) {
                  Value localValue2 = parameter2.getValue();
                  this.collectReferencedClasses(localValue2, strings);
               }
            }
         } else if (localValue instanceof ParameterValue) {
            VariableCategory variableCategoryByUuid = this.resourceLibrary.getVariableCategoryByUuid("参数");
            String clazz = variableCategoryByUuid.getClazz();
            if (!strings.contains(clazz)) {
               strings.add(clazz);
            }
         } else if (localValue instanceof ParenValue) {
            ParenValue parenValue = (ParenValue)localValue;
            Value localValue3 = parenValue.getValue();
            this.collectReferencedClasses(localValue3, strings);
         } else if (localValue instanceof VariableCategoryValue) {
            VariableCategoryValue variableCategoryValue = (VariableCategoryValue)localValue;
            VariableCategory variableCategoryByUuid2 = this.resourceLibrary.getVariableCategoryByUuid(variableCategoryValue.getUuid());
            String clazz2 = variableCategoryByUuid2.getClazz();
            if (!strings.contains(clazz2)) {
               strings.add(clazz2);
            }
         } else if (localValue instanceof VariableValue) {
            VariableValue variableValue = (VariableValue)localValue;
            VariableCategory variableCategoryByUuid3 = this.resourceLibrary.getVariableCategoryByUuid(variableValue.getCategoryUuid());
            String clazz3 = variableCategoryByUuid3.getClazz();
            if (!strings.contains(clazz3)) {
               strings.add(clazz3);
            }
         }

         ComplexArithmetic arithmetic = localValue.getArithmetic();
         if (arithmetic != null) {
            Value localValue4 = arithmetic.getValue();
            this.collectReferencedClasses(localValue4, strings);
         }
      }
   }

   @Override
   public ObjectTypeNode buildObjectTypeNode(String className) {
      ObjectTypeNode objectTypeNode = null;

      for (ObjectTypeNode objectTypeNode2 : this.objectTypeNodes) {
         if (objectTypeNode2.support(className)) {
            objectTypeNode = objectTypeNode2;
            break;
         }
      }

      if (objectTypeNode == null) {
         objectTypeNode = new ObjectTypeNode(className, this.nextId());
         this.objectTypeNodes.add(objectTypeNode);
      }

      return objectTypeNode;
   }

   @Override
   public ResourceLibrary getResourceLibrary() {
      return this.resourceLibrary;
   }

   @Override
   public int nextId() {
      return this.idGenerator.nextId();
   }

   @Override
   public IdGenerator getIdGenerator() {
      return this.idGenerator;
   }

   @Override
   public void setCurrentRule(Rule rule) {
      this.currentRule = rule;
   }

   @Override
   public Rule currentRule() {
      return this.currentRule;
   }

   @Override
   public boolean currentRuleIsDebug() {
      return this.currentRule == null ? false : this.currentRule.getDebug() != null && this.currentRule.getDebug();
   }
}
