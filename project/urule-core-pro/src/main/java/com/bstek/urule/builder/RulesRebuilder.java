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
   private ResourceLibraryBuilder a;

   public void rebuildRules(List<Library> var1, List<Rule> var2, List<Predefine> var3) {
      if (var1 != null) {
         if (var2 != null) {
            ResourceLibrary var4 = this.a.buildResourceLibrary(var1, var3);
            if (var3 != null) {
               for (Predefine var6 : var3) {
                  Junction var7 = var6.getJunction();
                  if (var7 != null) {
                     this.rebuildCriterion(var7, var4, false);
                  }

                  Value var8 = var6.getValue();
                  if (var8 != null) {
                     this.rebuildValue(var8, var4, false);
                  }
               }
            }

            for (Rule var21 : var2) {
               if (var21.getLhs() != null) {
                  Criterion var22 = var21.getLhs().getCriterion();
                  this.rebuildCriterion(var22, var4, false);
               }

               Rhs var23 = var21.getRhs();
               List var25 = null;
               if (var23 != null) {
                  var25 = var23.getActions();
               }

               if (var25 != null) {
                  for (Action var10 : (Iterable<Action>)(Iterable<?>)(var25)) {
                     this.rebuildAction(var10, var4, false);
                     this.a(var10, var4);
                  }
               }

               Other var26 = var21.getOther();
               if (var26 != null) {
                  List var28 = var26.getActions();
                  if (var28 != null) {
                     for (Action var12 : (Iterable<Action>)(Iterable<?>)(var28)) {
                        this.rebuildAction(var12, var4, false);
                        this.a(var12, var4);
                     }
                  }
               }

               if (var21 instanceof LoopRule) {
                  LoopRule var29 = (LoopRule)var21;
                  LoopTarget var30 = var29.getLoopTarget();
                  if (var30 != null) {
                     Value var31 = var30.getValue();
                     this.rebuildValue(var31, var4, false);
                  }

                  LoopStart var32 = var29.getLoopStart();
                  if (var32 != null && var32.getActions() != null) {
                     for (Action var14 : var32.getActions()) {
                        this.rebuildAction(var14, var4, false);
                        this.a(var14, var4);
                     }
                  }

                  LoopEnd var33 = var29.getLoopEnd();
                  if (var33 != null && var33.getActions() != null) {
                     for (Action var15 : var33.getActions()) {
                        this.rebuildAction(var15, var4, false);
                        this.a(var15, var4);
                     }
                  }

                  for (LoopRuleUnit var16 : ((LoopRule)var21).getUnits()) {
                     if (var16.getLhs() != null) {
                        Criterion var17 = var16.getLhs().getCriterion();
                        this.rebuildCriterion(var17, var4, false);
                     }

                     var23 = var16.getRhs();
                     if (var23 != null) {
                        var25 = var23.getActions();
                     }

                     if (var25 != null) {
                        for (Action var18 : (Iterable<Action>)(Iterable<?>)(var25)) {
                           this.rebuildAction(var18, var4, false);
                           this.a(var18, var4);
                        }
                     }

                     var26 = var16.getOther();
                     if (var26 != null) {
                        List var38 = var26.getActions();
                        if (var38 != null) {
                           for (Action var19 : (Iterable<Action>)(Iterable<?>)(var38)) {
                              this.rebuildAction(var19, var4, false);
                              this.a(var19, var4);
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public void rebuildRulesForDSL(List<Library> var1, List<Rule> var2, List<Predefine> var3) {
      if (var1 != null) {
         if (var2 != null) {
            ResourceLibrary var4 = this.a.buildResourceLibrary(var1, var3);

            for (Rule var6 : var2) {
               if (var6.getLhs() != null) {
                  Criterion var7 = var6.getLhs().getCriterion();
                  this.rebuildCriterion(var7, var4, true);
               }

               Rhs var12 = var6.getRhs();
               if (var12 != null && var12.getActions() != null) {
                  for (Action var10 : var12.getActions()) {
                     this.rebuildAction(var10, var4, true);
                  }
               }

               Other var13 = var6.getOther();
               if (var13 != null) {
                  List var14 = var13.getActions();
                  if (var14 != null) {
                     for (Action var11 : (Iterable<Action>)(Iterable<?>)(var14)) {
                        this.rebuildAction(var11, var4, true);
                     }
                  }
               }

               if (var6 instanceof LoopRule) {
                  this.a(var4, var6);
               }
            }
         }
      }
   }

   private void a(Action var1, ResourceLibrary var2) {
      if (var1 instanceof TemplateAction) {
         TemplateAction var3 = (TemplateAction)var1;
         ActionTemplateUnit var4 = var2.getActionTemplateUnit(var3.getId());
         if (var4 == null) {
            throw new RuleException("文件【" + RuleFileHolder.getRuleFile() + "】中，引用动作模版【" + var3.getName() + "】不存在！");
         }

         var3.setName(var4.getName());
         var3.setPath(var4.getPath());
      }
   }

   private void a(ResourceLibrary var1, Rule var2) {
      LoopRule var3 = (LoopRule)var2;
      LoopTarget var4 = var3.getLoopTarget();
      if (var4 != null) {
         Value var5 = var4.getValue();
         this.rebuildValue(var5, var1, true);
      }

      LoopStart var16 = var3.getLoopStart();
      if (var16 != null && var16.getActions() != null) {
         for (Action var7 : var16.getActions()) {
            this.rebuildAction(var7, var1, true);
         }
      }

      LoopEnd var17 = var3.getLoopEnd();
      if (var17 != null && var17.getActions() != null) {
         for (Action var8 : var17.getActions()) {
            this.rebuildAction(var8, var1, true);
         }
      }

      for (LoopRuleUnit var9 : var3.getUnits()) {
         Lhs var10 = var9.getLhs();
         if (var10 != null) {
            Criterion var11 = var10.getCriterion();
            this.rebuildCriterion(var11, var1, true);
         }

         Rhs var21 = var9.getRhs();
         if (var21 != null) {
            for (Action var14 : var21.getActions()) {
               this.rebuildAction(var14, var1, true);
            }
         }

         Other var22 = var9.getOther();
         if (var22 != null) {
            List var23 = var22.getActions();
            if (var23 != null) {
               for (Action var15 : (Iterable<Action>)(Iterable<?>)(var23)) {
                  this.rebuildAction(var15, var1, true);
               }
            }
         }
      }
   }

   public void convertNamedJunctions(List<Rule> var1) {
      for (Rule var3 : var1) {
         if (var3.getLhs() != null) {
            Criterion var4 = var3.getLhs().getCriterion();
            Criterion var5 = this.a(var4);
            var3.getLhs().setCriterion(var5);
         }
      }
   }

   private Criterion a(Criterion var1) {
      if (!(var1 instanceof Junction)) {
         return var1;
      }

      if (var1 instanceof Junction) {
         this.a((Junction)var1);
      }

      return var1;
   }

   private void a(Junction var1) {
      List var2 = var1.getCriterions();
      ArrayList var3 = new ArrayList();

      for (Criterion var5 : (Iterable<Criterion>)(Iterable<?>)(var2)) {
         if (var5 instanceof Junction) {
            this.a((Junction)var5);
         }

         var3.add(var5);
      }

      var1.setCriterions(var3);
   }

   public void rebuildAction(Action var1, ResourceLibrary var2, boolean var3) {
      if (var1 != null) {
         if (var1 instanceof PredefineAssignAction) {
            PredefineAssignAction var4 = (PredefineAssignAction)var1;
            Predefine var5 = var2.getPredefine(var4.getUuid());
            if (var5 != null) {
               var4.setName(var5.getName());
               String var6 = var5.getType();
               if (Datatype.isType(var6)) {
                  var4.setDatatype(Datatype.valueOf(var6));
               } else {
                  String var7 = var4.getPropertyUuid();
                  if (var7 != null) {
                     VariableData var8 = var2.getVariableByUuid(var6, var7);
                     var4.setVariableCategory(var8.getCategory().getName());
                     var4.setPropertyName(var8.getVariable().getName());
                     var4.setPropertyDatatype(var8.getVariable().getType());
                     var4.setPropertyLabel(var8.getVariable().getLabel());
                     var4.setVariableCategoryUuid(var8.getCategory().getUuid());
                  } else {
                     VariableCategory var28 = var2.getVariableCategoryByUuid(var6);
                     var4.setVariableCategory(var28.getName());
                     var4.setVariableCategoryUuid(var28.getUuid());
                  }
               }
            }

            Value var20 = var4.getValue();
            this.rebuildValue(var20, var2, var3);
         } else if (var1 instanceof VariableAssignAction) {
            List var12 = var2.getVariableCategories();
            if (var12 == null) {
               return;
            }

            VariableAssignAction var16 = (VariableAssignAction)var1;
            VariableData var21 = var2.getVariableByUuid(var16.getCategoryUuid(), var16.getUuid());
            if (var21 == null) {
               throw new RuleException(
                  "文件【" + RuleFileHolder.getRuleFile() + "】中，变量赋值对象【" + var16.getVariableCategory() + "." + var16.getVariableLabel() + "】不存在"
               );
            }

            LeftType var25 = var16.getType();
            if (var25 != null) {
               Variable var29 = var21.getVariable();
               if (var16.getKeyUuid() == null) {
                  var16.setVariableName(var29.getName());
                  var16.setVariableLabel(var29.getLabel());
                  var16.setDatatype(var29.getType());
               } else {
                  var16.setKeyLabel(var29.getLabel());
                  var16.setKeyName(var29.getName());
                  VariableData var9 = var2.getVariableByUuid(var16.getKeyCategoryUuid(), var16.getKeyUuid());
                  if (var9 == null) {
                     throw new RuleException(
                        "文件【"
                           + RuleFileHolder.getRuleFile()
                           + "】中，对象【"
                           + var16.getVariableCategory()
                           + "."
                           + var16.getVariableLabel()
                           + "("
                           + var16.getVariableName()
                           + ")】对应的属性在库中不存在"
                     );
                  }

                  var16.setVariableName(var9.getVariable().getName());
                  var16.setVariableLabel(var9.getVariable().getLabel());
                  var16.setDatatype(var9.getVariable().getType());
               }

               var16.setDatatype(var29.getType());
               var16.setVariableCategory(var21.getCategory().getName());
            } else if (var25 == null) {
               String var30 = var16.getVariableCategory();
               String var34 = var16.getVariableLabel();
               if (var34 == null) {
                  throw new RuleException("文件【" + RuleFileHolder.getRuleFile() + "】中，赋值只能针对具体变量或参数，请检查规则中变量赋值操作中是否存在直接对对象赋值的操作.");
               }

               if (var34.equals("return_value__")) {
                  var16.setVariableName(var34);
                  var16.setDatatype(Datatype.Boolean);
               } else if (var34.equals("return_to__")) {
                  var16.setVariableName(var34);
                  var16.setDatatype(Datatype.String);
               } else {
                  VariableData var10 = var2.getVariableByUuid(var16.getCategoryUuid(), var16.getUuid());
                  if (var10 == null) {
                     throw new RuleException("文件【" + RuleFileHolder.getRuleFile() + "】中，变量[" + var30 + "." + var34 + "]不存在");
                  }

                  Variable var11 = var10.getVariable();
                  var16.setDatatype(var11.getType());
                  var16.setVariableLabel(var11.getLabel());
                  var16.setDatatype(var11.getType());
                  var16.setVariableName(var11.getName());
                  var16.setDatatype(var11.getType());
                  var16.setVariableCategory(var10.getCategory().getName());
               }
            }

            Value var31 = var16.getValue();
            this.rebuildValue(var31, var2, var3);
         } else if (var1 instanceof PredefineAssignAction) {
            PredefineAssignAction var13 = (PredefineAssignAction)var1;
            Predefine var17 = var2.getPredefine(var13.getUuid());
            if (var17 != null) {
               var13.setName(var17.getName());
               String var22 = var17.getType();
               if (Datatype.isType(var22)) {
                  var13.setDatatype(Datatype.valueOf(var22));
               } else {
                  String var26 = var13.getPropertyUuid();
                  if (var26 != null) {
                     VariableData var32 = var2.getVariableByUuid(var22, var26);
                     var13.setVariableCategory(var32.getCategory().getName());
                     var13.setPropertyName(var32.getVariable().getName());
                     var13.setPropertyLabel(var32.getVariable().getLabel());
                     var13.setVariableCategoryUuid(var32.getCategory().getUuid());
                  } else {
                     VariableCategory var33 = var2.getVariableCategoryByUuid(var22);
                     var13.setVariableCategory(var33.getName());
                     var13.setVariableCategoryUuid(var33.getUuid());
                  }
               }
            }
         } else if (var1 instanceof ConsolePrintAction) {
            ConsolePrintAction var14 = (ConsolePrintAction)var1;
            Value var18 = var14.getValue();
            this.rebuildValue(var18, var2, var3);
         } else if (var1 instanceof ExecuteMethodAction) {
            List var15 = var2.getActionLibraries();
            if (var15 == null) {
               return;
            }

            ExecuteMethodAction var19 = (ExecuteMethodAction)var1;
            ActionData var23 = null;
            if (var19.getCategoryUuid() == null) {
               var23 = var2.getActionByName(var19.getBeanLabel(), var19.getMethodLabel());
               if (var23 == null) {
                  var23 = var2.getActionByBean(var19.getBeanId(), var19.getMethodName());
               }
            } else {
               var23 = var2.getActionByUuid(var19.getCategoryUuid(), var19.getUuid());
            }

            if (var23 == null) {
               throw new RuleException("Bean [" + var19.getBeanLabel() + "] not define methods.");
            }

            var19.setBeanId(var23.getBean().getId());
            var19.setBeanLabel(var23.getBean().getName());
            var19.setMethodName(var23.getMethod().getMethodName());
            var19.setMethodLabel(var23.getMethod().getName());
            List var27 = var19.getParameters();
            this.a(var2, var27, var23.getMethod().getParameters(), var3);
         }
      }
   }

   private void a(CommonFunctionParameter var1, ResourceLibrary var2, boolean var3) {
      if (var1 != null) {
         String var4 = var1.getProperty();
         if (!StringUtils.isEmpty(var4)) {
            Value var5 = var1.getObjectParameter();
            this.rebuildValue(var5, var2, var3);
            String var6 = null;
            if (var5 instanceof VariableValue) {
               VariableValue var7 = (VariableValue)var5;
               var6 = var7.getCategoryUuid();
            } else if (var5 instanceof VariableCategoryValue) {
               VariableCategoryValue var13 = (VariableCategoryValue)var5;
               var6 = var13.getUuid();
            } else {
               if (!(var5 instanceof ParameterValue)) {
                  throw new RuleException("Function parameter is invalid.");
               }

               ParameterValue var14 = (ParameterValue)var5;
               var6 = var14.getUuid();
            }

            for (VariableCategory var9 : var2.getVariableCategories()) {
               if (var6.equals(var9.getUuid())) {
                  for (Variable var11 : var9.getVariables()) {
                     if (var11.getName().equals(var4) || var11.getLabel().equals(var4)) {
                        var1.setProperty(var11.getName());
                        var1.setPropertyLabel(var11.getLabel());
                        break;
                     }
                  }
               }
            }
         }
      }
   }

   private void a(ResourceLibrary var1, List<Parameter> var2, List<com.bstek.urule.model.library.action.Parameter> var3, boolean var4) {
      if (var2 != null && var3 != null) {
         for (int var5 = 0; var5 < var2.size() && var5 <= var3.size() - 1; var5++) {
            Parameter var6 = (Parameter)var2.get(var5);
            com.bstek.urule.model.library.action.Parameter var7 = (com.bstek.urule.model.library.action.Parameter)var3.get(var5);
            var6.setType(var7.getType());
            Value var8 = var6.getValue();
            this.rebuildValue(var8, var1, var4);
         }
      }
   }

   public void rebuildCriterion(Criterion var1, ResourceLibrary var2, boolean var3) {
      if (var1 != null) {
         if (var1 instanceof Criteria) {
            Criteria var4 = (Criteria)var1;
            this.a(var2, var4, var3);
         } else if (var1 instanceof Junction) {
            Junction var8 = (Junction)var1;
            List var5 = var8.getCriterions();
            if (var5 != null) {
               for (Criterion var7 : (Iterable<Criterion>)(Iterable<?>)(var5)) {
                  this.rebuildCriterion(var7, var2, var3);
               }
            }
         } else if (var1 instanceof ConditionTemplateCriterion) {
            ConditionTemplateCriterion var9 = (ConditionTemplateCriterion)var1;
            ConditionTemplateUnit var10 = var2.getConditionTemplateUnit(var9.getId());
            if (var10 == null) {
               throw new RuleException("文件【" + RuleFileHolder.getRuleFile() + "】中，引用条件模版【" + var9.getName() + "】不存在!");
            }

            var9.setName(var10.getName());
            var9.setPath(var10.getPath());
         }
      }
   }

   private void a(ResourceLibrary var1, Criteria var2, boolean var3) {
      Left var4 = var2.getLeft();
      LeftPart var5 = var4.getLeftPart();
      if (var5 instanceof VariableLeftPart) {
         VariableLeftPart var6 = (VariableLeftPart)var5;
         if (var6.getKeyCategoryUuid() != null) {
            VariableData var7 = var1.getVariableByUuid(var6.getKeyCategoryUuid(), var6.getKeyUuid());
            Variable var8 = var7.getVariable();
            var6.setDatatype(var8.getType());
            var6.setVariableLabel(var8.getLabel());
            var6.setDatatype(var8.getType());
            var6.setVariableName(var8.getName());
            var7 = var1.getVariableByUuid(var6.getCategoryUuid(), var6.getUuid());
            var6.setKeyName(var7.getVariable().getName());
            var6.setKeyLabel(var7.getVariable().getLabel());
         } else {
            VariableData var21 = null;
            if ("参数".equals(var6.getVariableCategory())) {
               Variable var30 = var1.getParameterByUuid(var6.getKeyUuid(), var6.getKeyName(), var6.getKeyLabel());
               if (var30 != null && var30.getType() == Datatype.Object) {
                  var21 = var1.getVariableByUuid(var30.getDataType(), var6.getUuid());
                  if (StringUtils.isBlank(var6.getKeyUuid())) {
                     var6.setKeyUuid(var30.getUuid());
                  }
               }

               if (var21 == null) {
                  var21 = var1.getVariableByName(var6.getCategoryUuid(), var6.getVariableName());
               }
            } else {
               var21 = var1.getVariableByUuid(var6.getCategoryUuid(), var6.getUuid());
            }

            if (var21 == null) {
               var21 = var1.getVariableByName(var6.getVariableCategory(), var6.getVariableName());
            }

            if (var21 == null) {
               throw new RuleException("文件【" + RuleFileHolder.getRuleFile() + "】中，变量[" + var6.getVariableCategory() + "." + var6.getVariableLabel() + "]不存在");
            }

            Variable var31 = var21.getVariable();
            var6.setDatatype(var31.getType());
            var6.setVariableLabel(var31.getLabel());
            var6.setDatatype(var31.getType());
            var6.setVariableName(var31.getName());
            if (!"参数".equals(var6.getVariableCategory())) {
               var6.setVariableCategory(var21.getCategory().getName());
            }
         }
      } else if (var5 instanceof PredefineLeftPart) {
         PredefineLeftPart var14 = (PredefineLeftPart)var5;
         Predefine var22 = var1.getPredefine(var14.getUuid());
         if (var22 != null) {
            var14.setName(var22.getName());
            String var32 = var22.getType();
            if (Datatype.isType(var32)) {
               var14.setDatatype(Datatype.valueOf(var32));
            } else {
               String var9 = var14.getPropertyUuid();
               if (var9 != null) {
                  VariableData var10 = var1.getVariableByUuid(var32, var9);
                  if (var10 == null) {
                     throw new RuleException("变量属性不存在:" + var9);
                  }

                  var14.setVariableCategory(var10.getCategory().getName());
                  var14.setPropertyName(var10.getVariable().getName());
                  var14.setPropertyLabel(var10.getVariable().getLabel());
                  var14.setVariableCategoryUuid(var10.getCategory().getUuid());
               } else {
                  VariableCategory var41 = var1.getVariableCategoryByUuid(var32);
                  if (var41 == null) {
                     throw new RuleException("变量不存在:" + var9);
                  }

                  var14.setVariableCategory(var41.getName());
                  var14.setVariableCategoryUuid(var41.getUuid());
               }
            }
         }
      } else if (var5 instanceof CommonFunctionLeftPart) {
         CommonFunctionLeftPart var15 = (CommonFunctionLeftPart)var5;
         CommonFunctionParameter var23 = var15.getParameter();
         this.a(var23, var1, var3);
      } else if (var5 instanceof MethodLeftPart) {
         MethodLeftPart var16 = (MethodLeftPart)var5;
         ActionData var24 = null;
         if (var16.getCategoryUuid() == null) {
            var24 = var1.getActionByName(var16.getBeanLabel(), var16.getMethodLabel());
            if (var24 == null) {
               var24 = var1.getActionByBean(var16.getBeanId(), var16.getMethodName());
            }
         } else {
            var24 = var1.getActionByUuid(var16.getCategoryUuid(), var16.getUuid());
         }

         if (var24 == null) {
            throw new RuleException("Bean[" + var16.getBeanLabel() + "] not exist.");
         }

         var16.setBeanId(var24.getBean().getId());
         var16.setBeanLabel(var24.getBean().getName());
         var16.setMethodName(var24.getMethod().getMethodName());
         var16.setMethodLabel(var24.getMethod().getName());
         List var33 = var16.getParameters();
         this.a(var1, var33, var24.getMethod().getParameters(), var3);
      } else if (var5 instanceof FunctionLeftPart) {
         FunctionLeftPart var17 = (FunctionLeftPart)var5;
         List var26 = var17.getParameters();
         if (var26 != null && var26.size() > 0) {
            for (Parameter var37 : (Iterable<Parameter>)(Iterable<?>)(var26)) {
               Value var42 = var37.getValue();
               if (var42 != null) {
                  this.rebuildValue(var42, var1, var3);
               }
            }
         }
      } else if (var5 instanceof AccumulateLeftPart) {
         AccumulateLeftPart var18 = (AccumulateLeftPart)var5;
         List var27 = var18.getConditionItems();
         if (var27 != null) {
            for (ConditionItem var38 : (Iterable<ConditionItem>)(Iterable<?>)(var27)) {
               Value var43 = var38.getValue();
               this.rebuildValue(var43, var1, var3);
            }
         }

         List var36 = var18.getCalculateItems();
         if (var36 != null) {
            for (CalculateItem var44 : (Iterable<CalculateItem>)(Iterable<?>)(var36)) {
               String var11 = var44.getAssignCategoryUuid();
               String var12 = var44.getAssignVariableUuid();
               if (StringUtils.isNotBlank(var12) && StringUtils.isNotBlank(var11)) {
                  VariableData var13 = var1.getVariableByUuid(var11, var12);
                  if (var13 == null) {
                     throw new RuleException(
                        "文件【" + RuleFileHolder.getRuleFile() + "】中，引用变量[" + var44.getAssignVariableCategory() + "." + var44.getAssignVariableLabel() + "]不存在"
                     );
                  }

                  var44.setAssignDatatype(var13.getVariable().getType());
                  var44.setAssignVariableCategory(var13.getCategory().getName());
                  var44.setAssignVariable(var13.getVariable().getName());
                  var44.setAssignVariableLabel(var13.getVariable().getLabel());
               }
            }
         }

         LoopTarget var40 = var18.getLoopTarget();
         Value var45 = var40.getValue();
         this.rebuildValue(var45, var1, var3);
      }

      ComplexArithmetic var19 = var4.getArithmetic();
      if (var19 != null) {
         Value var28 = var19.getValue();
         this.rebuildValue(var28, var1, var3);
      }

      Value var29 = var2.getValue();
      this.rebuildValue(var29, var1, var3);
   }

   public void rebuildValue(Value var1, ResourceLibrary var2, boolean var3) {
      if (var1 != null) {
         if (var1 instanceof ParenValue) {
            ParenValue var4 = (ParenValue)var1;
            Value var5 = var4.getValue();
            this.rebuildValue(var5, var2, var3);
         } else if (var1 instanceof ConstantValue) {
            ConstantValue var10 = (ConstantValue)var1;
            ConstantData var19 = var2.getConstantByUuid(var10.getCategoryUuid(), var10.getUuid());
            if (var19 == null) {
               throw new RuleException(
                  "文件【" + RuleFileHolder.getRuleFile() + "】中，引用常量[" + var10.getConstantCategory() + "." + var10.getConstantLabel() + "]不存在"
               );
            }

            var10.setConstantCategory(var19.getCategory().getLabel());
            var10.setConstantName(var19.getConstant().getName());
            var10.setConstantLabel(var19.getConstant().getLabel());
            var10.setDatatype(var19.getConstant().getType());
         } else if (var1 instanceof VariableValue) {
            VariableValue var11 = (VariableValue)var1;
            VariableData var20 = var2.getVariableByUuid(var11.getCategoryUuid(), var11.getUuid());
            if (var20 == null) {
               throw new RuleException(
                  "文件【" + RuleFileHolder.getRuleFile() + "】中，引用变量[" + var11.getVariableCategory() + "." + var11.getVariableLabel() + "]不存在"
               );
            }

            Variable var6 = var20.getVariable();
            var11.setDatatype(var6.getType());
            var11.setVariableLabel(var6.getLabel());
            var11.setDatatype(var6.getType());
            var11.setVariableName(var6.getName());
            var11.setDatatype(var6.getType());
            var11.setVariableCategory(var20.getCategory().getName());
         } else if (var1 instanceof PredefineValue) {
            PredefineValue var12 = (PredefineValue)var1;
            String var21 = var12.getUuid();
            Predefine var30 = var2.getPredefine(var21);
            if (var30 == null) {
               throw new RuleException("文件【" + RuleFileHolder.getRuleFile() + "】中，引用预定义对象[" + var21 + "]不存在");
            }

            var12.setName(var30.getName());
            String var7 = var30.getType();
            if (Datatype.isType(var7)) {
               var12.setDatatype(Datatype.valueOf(var7));
            } else {
               String var8 = var12.getPropertyUuid();
               if (var8 != null) {
                  VariableData var9 = var2.getVariableByUuid(var7, var8);
                  var12.setVariableCategory(var9.getCategory().getName());
                  var12.setPropertyName(var9.getVariable().getName());
                  var12.setPropertyLabel(var9.getVariable().getLabel());
                  var12.setVariableCategoryUuid(var9.getCategory().getUuid());
               } else {
                  VariableCategory var34 = var2.getVariableCategoryByUuid(var7);
                  var12.setVariableCategory(var34.getName());
                  var12.setVariableCategoryUuid(var34.getUuid());
               }
            }
         } else if (var1 instanceof VariableCategoryValue) {
            VariableCategoryValue var13 = (VariableCategoryValue)var1;
            VariableCategory var22 = var2.getVariableCategoryByUuid(var13.getUuid());
            if (var22 != null) {
               var13.setVariableCategory(var22.getName());
            }
         } else if (var1 instanceof ParameterValue) {
            ParameterValue var14 = (ParameterValue)var1;
            VariableData var23 = var2.getVariableByUuid("参数", var14.getUuid());
            if (var23 == null) {
               throw new RuleException("文件【" + RuleFileHolder.getRuleFile() + "】中，引用的参数[" + "参数" + "." + var14.getVariableLabel() + "]不存在");
            }

            Variable var31 = var23.getVariable();
            if (var14.getKeyUuid() == null) {
               var14.setVariableLabel(var31.getLabel());
               var14.setVariableName(var31.getName());
               var14.setDatatype(var31.getType());
            } else {
               var14.setKeyLabel(var31.getLabel());
               var14.setKeyName(var31.getName());
               var23 = var2.getVariableByUuid(var14.getKeyCategoryUuid(), var14.getKeyUuid());
               if (var23 == null) {
                  throw new RuleException(
                     "文件【" + RuleFileHolder.getRuleFile() + "】中，引用的参数[" + "参数" + "." + var14.getKeyLabel() + "." + var14.getVariableLabel() + "]不存在"
                  );
               }

               var31 = var23.getVariable();
               var14.setVariableLabel(var31.getLabel());
               var14.setVariableName(var31.getName());
               var14.setDatatype(var31.getType());
            }
         } else if (var1 instanceof CommonFunctionValue) {
            CommonFunctionValue var15 = (CommonFunctionValue)var1;
            CommonFunctionParameter var25 = var15.getParameter();
            this.a(var25, var2, var3);
         } else if (var1 instanceof MethodValue) {
            MethodValue var16 = (MethodValue)var1;
            ActionData var26 = null;
            if (var16.getCategoryUuid() != null) {
               var26 = var2.getActionByUuid(var16.getCategoryUuid(), var16.getUuid());
            } else {
               var26 = var2.getActionByName(var16.getBeanLabel(), var16.getMethodLabel());
               if (var26 == null) {
                  var26 = var2.getActionByBean(var16.getBeanId(), var16.getMethodName());
               }
            }

            if (var26 == null) {
               throw new RuleException("Bean[" + var16.getBeanLabel() + "] not exist.");
            }

            var16.setBeanId(var26.getBean().getId());
            var16.setBeanLabel(var26.getBean().getName());
            var16.setMethodName(var26.getMethod().getMethodName());
            var16.setMethodLabel(var26.getMethod().getName());
            List var33 = var16.getParameters();
            this.a(var2, var33, var26.getMethod().getParameters(), var3);
         } else if (var1 instanceof MathValue) {
            MathValue var17 = (MathValue)var1;
            MathSign var28 = var17.getMathSign();
            this.a(var28, var2, var3);
         }

         ComplexArithmetic var18 = var1.getArithmetic();
         if (var18 != null) {
            Value var29 = var18.getValue();
            this.rebuildValue(var29, var2, var3);
         }
      }
   }

   private void a(MathSign var1, ResourceLibrary var2, boolean var3) {
      if (var1 != null) {
         if (var1 instanceof AbsoluteMath) {
            AbsoluteMath var4 = (AbsoluteMath)var1;
            this.rebuildValue(var4.getValue(), var2, var3);
         } else if (var1 instanceof DownRoundMath) {
            DownRoundMath var5 = (DownRoundMath)var1;
            this.rebuildValue(var5.getValue(), var2, var3);
         } else if (var1 instanceof ExtremumMath) {
            ExtremumMath var6 = (ExtremumMath)var1;
            this.rebuildValue(var6.getValue1(), var2, var3);
            this.rebuildValue(var6.getValue2(), var2, var3);
         } else if (var1 instanceof FractionMath) {
            FractionMath var7 = (FractionMath)var1;
            this.rebuildValue(var7.getDenominator(), var2, var3);
            this.rebuildValue(var7.getNumerator(), var2, var3);
         } else if (var1 instanceof LnMath) {
            LnMath var8 = (LnMath)var1;
            this.rebuildValue(var8.getValue(), var2, var3);
         } else if (var1 instanceof LogMath) {
            LogMath var9 = (LogMath)var1;
            this.rebuildValue(var9.getValue(), var2, var3);
            this.rebuildValue(var9.getBaseValue(), var2, var3);
         } else if (var1 instanceof NRadicalMath) {
            NRadicalMath var10 = (NRadicalMath)var1;
            this.rebuildValue(var10.getValue(), var2, var3);
            this.rebuildValue(var10.getPower(), var2, var3);
         } else if (var1 instanceof PowerMath) {
            PowerMath var11 = (PowerMath)var1;
            this.rebuildValue(var11.getBase(), var2, var3);
            this.rebuildValue(var11.getPower(), var2, var3);
         } else if (var1 instanceof RadicalMath) {
            RadicalMath var12 = (RadicalMath)var1;
            this.rebuildValue(var12.getValue(), var2, var3);
         } else if (var1 instanceof SigmaMath) {
            SigmaMath var13 = (SigmaMath)var1;
            this.rebuildValue(var13.getExpr(), var2, var3);
            this.rebuildValue(var13.getIvalue(), var2, var3);
            this.rebuildValue(var13.getSuperior(), var2, var3);
         } else if (var1 instanceof TriangleFunctionMath) {
            TriangleFunctionMath var14 = (TriangleFunctionMath)var1;
            this.rebuildValue(var14.getValue(), var2, var3);
         } else if (var1 instanceof UpRoundMath) {
            UpRoundMath var15 = (UpRoundMath)var1;
            this.rebuildValue(var15.getValue(), var2, var3);
         }
      }
   }

   public void setResourceLibraryBuilder(ResourceLibraryBuilder var1) {
      this.a = var1;
   }

   public ResourceLibraryBuilder getResourceLibraryBuilder() {
      return this.a;
   }
}
