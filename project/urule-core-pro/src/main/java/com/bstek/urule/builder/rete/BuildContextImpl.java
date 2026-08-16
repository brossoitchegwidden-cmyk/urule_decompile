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
   private ResourceLibrary a;
   private List<ObjectTypeNode> b;
   private IdGenerator c;
   private Rule d;

   public BuildContextImpl(ResourceLibrary var1, List<ObjectTypeNode> var2) {
      this.a = var1;
      this.b = var2;
      this.c = new IdGenerator();
   }

   public BuildContextImpl(List<ObjectTypeNode> var1, BuildContext var2) {
      this.a = var2.getResourceLibrary();
      this.b = var1;
      this.c = var2.getIdGenerator();
   }

   @Override
   public boolean assertSameType(BaseCriteria var1, BaseCriteria var2) {
      VariableCategory var3 = this.a(var1);
      VariableCategory var4 = this.a(var2);
      return var3 != null && var4 != null ? var3.getClazz().equals(var4.getClazz()) : false;
   }

   private VariableCategory a(BaseCriteria var1) {
      VariableCategory var2 = null;
      if (var1 instanceof Criteria) {
         Criteria var3 = (Criteria)var1;
         LeftPart var4 = var3.getLeft().getLeftPart();
         if (var4 instanceof VariableLeftPart) {
            VariableLeftPart var5 = (VariableLeftPart)var4;
            var2 = this.a.getVariableCategoryByUuid(var5.getCategoryUuid());
         }

         return var2;
      } else {
         throw new RuleException("Unknow Criteria : " + var1);
      }
   }

   @Override
   public List<String> getObjectTypeByCriterions(List<Criterion> var1) {
      ArrayList var2 = new ArrayList();

      for (Criterion var4 : var1) {
         if (var4 instanceof Criteria) {
            var2.addAll(this.getObjectType((Criteria)var4));
         } else if (var4 instanceof Junction) {
            Junction var5 = (Junction)var4;
            var2.addAll(this.getObjectTypeByCriterions(var5.getCriterions()));
         } else if (var4 instanceof ConditionTemplateCriterion) {
            throw new RuleException("条件模版不能在N个条件中使用");
         }
      }

      return var2;
   }

   @Override
   public List<String> getObjectType(BaseCriteria var1) {
      ArrayList var2 = new ArrayList();
      if (!(var1 instanceof Criteria)) {
         throw new RuleException("Unknow Criteria : " + var1);
      }

      Criteria var3 = (Criteria)var1;
      LeftPart var4 = var3.getLeft().getLeftPart();
      if (var4 instanceof VariableLeftPart) {
         VariableLeftPart var5 = (VariableLeftPart)var4;
         VariableCategory var6 = this.a.getVariableCategoryByUuid(var5.getCategoryUuid());
         if (var6 == null) {
            var6 = this.a.getVariableCategoryByCategoryName(var5.getVariableCategory());
         }

         var2.add(var6.getClazz());
      } else if (var4 instanceof CommonFunctionLeftPart) {
         CommonFunctionLeftPart var12 = (CommonFunctionLeftPart)var4;
         CommonFunctionParameter var17 = var12.getParameter();
         Value var7 = var17.getObjectParameter();
         this.a(var7, var2);
      } else if (var4 instanceof MethodLeftPart) {
         MethodLeftPart var13 = (MethodLeftPart)var4;
         List var18 = var13.getParameters();
         if (var18 != null) {
            for (Parameter var8 : (Iterable<Parameter>)(Iterable<?>)(var18)) {
               Value var9 = var8.getValue();
               this.a(var9, var2);
            }
         }
      } else if (var4 instanceof FunctionLeftPart) {
         FunctionLeftPart var14 = (FunctionLeftPart)var4;
         List var19 = var14.getParameters();
         if (var19 != null) {
            for (Parameter var26 : (Iterable<Parameter>)(Iterable<?>)(var19)) {
               Value var29 = var26.getValue();
               this.a(var29, var2);
            }
         }
      } else if (var4 instanceof AccumulateLeftPart) {
         AccumulateLeftPart var15 = (AccumulateLeftPart)var4;
         Value var20 = var15.getLoopTarget().getValue();
         this.a(var20, var2);

         for (ConditionItem var27 : var15.getConditionItems()) {
            var20 = var27.getValue();
            if (var20 != null) {
               this.a(var20, var2);
            }
         }

         for (CalculateItem var28 : var15.getCalculateItems()) {
            if (var28.isEnableAssignment()) {
               VariableCategory var30 = this.a.getVariableCategoryByUuid(var28.getAssignCategoryUuid());
               String var10 = var30.getClazz();
               if (!var2.contains(var10)) {
                  var2.add(var10);
               }
            }
         }
      }

      ComplexArithmetic var16 = var3.getLeft().getArithmetic();
      if (var16 != null) {
         this.a(var16.getValue(), var2);
      }

      this.a(var3.getValue(), var2);
      if (var2.size() == 0) {
         var2.add("*");
      }

      if (var1 instanceof Criteria) {
         var3 = (Criteria)var1;
         var3.addNecessaryClasses(var2);
      }

      return var2;
   }

   private void a(Value var1, List<String> var2) {
      if (var1 != null) {
         if (var1 instanceof CommonFunctionValue) {
            CommonFunctionValue var3 = (CommonFunctionValue)var1;
            CommonFunctionParameter var4 = var3.getParameter();
            Value var5 = var4.getObjectParameter();
            this.a(var5, var2);
         } else if (var1 instanceof MethodValue) {
            MethodValue var8 = (MethodValue)var1;
            List var14 = var8.getParameters();
            if (var14 != null) {
               for (Parameter var6 : (Iterable<Parameter>)(Iterable<?>)(var14)) {
                  Value var7 = var6.getValue();
                  this.a(var7, var2);
               }
            }
         } else if (var1 instanceof ParameterValue) {
            VariableCategory var9 = this.a.getVariableCategoryByUuid("参数");
            String var15 = var9.getClazz();
            if (!var2.contains(var15)) {
               var2.add(var15);
            }
         } else if (var1 instanceof ParenValue) {
            ParenValue var10 = (ParenValue)var1;
            Value var16 = var10.getValue();
            this.a(var16, var2);
         } else if (var1 instanceof VariableCategoryValue) {
            VariableCategoryValue var11 = (VariableCategoryValue)var1;
            VariableCategory var17 = this.a.getVariableCategoryByUuid(var11.getUuid());
            String var21 = var17.getClazz();
            if (!var2.contains(var21)) {
               var2.add(var21);
            }
         } else if (var1 instanceof VariableValue) {
            VariableValue var12 = (VariableValue)var1;
            VariableCategory var18 = this.a.getVariableCategoryByUuid(var12.getCategoryUuid());
            String var22 = var18.getClazz();
            if (!var2.contains(var22)) {
               var2.add(var22);
            }
         }

         ComplexArithmetic var13 = var1.getArithmetic();
         if (var13 != null) {
            Value var19 = var13.getValue();
            this.a(var19, var2);
         }
      }
   }

   @Override
   public ObjectTypeNode buildObjectTypeNode(String var1) {
      ObjectTypeNode var2 = null;

      for (ObjectTypeNode var4 : this.b) {
         if (var4.support(var1)) {
            var2 = var4;
            break;
         }
      }

      if (var2 == null) {
         var2 = new ObjectTypeNode(var1, this.nextId());
         this.b.add(var2);
      }

      return var2;
   }

   @Override
   public ResourceLibrary getResourceLibrary() {
      return this.a;
   }

   @Override
   public int nextId() {
      return this.c.nextId();
   }

   @Override
   public IdGenerator getIdGenerator() {
      return this.c;
   }

   @Override
   public void setCurrentRule(Rule var1) {
      this.d = var1;
   }

   @Override
   public Rule currentRule() {
      return this.d;
   }

   @Override
   public boolean currentRuleIsDebug() {
      return this.d == null ? false : this.d.getDebug() != null && this.d.getDebug();
   }
}
