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

public class CopyLibsAnalysis {
   private ResourceLibraryBuilder a = (ResourceLibraryBuilder)Utils.getApplicationContext().getBean("urule.resourceLibraryBuilder");
   private ActionTemplateDeserializer b = (ActionTemplateDeserializer)Utils.getApplicationContext().getBean("urule.actionTemplateDeserializer");
   private ConditionTemplateDeserializer c = (ConditionTemplateDeserializer)Utils.getApplicationContext().getBean("urule.conditionTemplateDeserializer");
   public static final CopyLibsAnalysis ins = new CopyLibsAnalysis();

   private CopyLibsAnalysis() {
   }

   public String doAnalysis(String var1, Object var2) throws Exception {
      HashMap var3 = new HashMap();
      var3.put("parameters", new ArrayList());
      var3.put("variables", new ArrayList());
      var3.put("constants", new ArrayList());
      var3.put("actions", new ArrayList());
      var3.put("conditionTemplates", new ArrayList());
      var3.put("actionTemplates", new ArrayList());
      LibraryIndex var4 = this.b(var1);
      if (var2 instanceof List) {
         List var5 = (List)var2;
         this.a((Criterion)((Criterion)var5.get(0)), (Map)var3, (LibraryIndex)var4);
      } else if (var2 instanceof Criterion) {
         Criterion var13 = (Criterion)var2;
         this.a((Criterion)var13, (Map)var3, (LibraryIndex)var4);
      } else if (var2 instanceof Action) {
         Action var14 = (Action)var2;
         this.a((Action)var14, (Map)var3, (LibraryIndex)var4);
      } else {
         if (!(var2 instanceof Rule)) {
            throw new RuleException("Unknow object :" + var2.getClass().getName());
         }

         Rule var15 = (Rule)var2;
         if (var15 instanceof LoopRule) {
            LoopRule var6 = (LoopRule)var15;
            LoopTarget var7 = var6.getLoopTarget();
            Value var8 = var7.getValue();
            this.a((Value)var8, (Map)var3, (LibraryIndex)var4);

            for(LoopRuleUnit var11 : var6.getUnits()) {
               this.a((Map)var3, (LibraryIndex)var4, (Lhs)var11.getLhs());
               this.a((Map)var3, (LibraryIndex)var4, (Rhs)var11.getRhs());
               Other var12 = var11.getOther();
               if (var12 != null) {
                  this.a((Map)var3, (LibraryIndex)var4, (List)var12.getActions());
               }
            }
         } else if (var15 instanceof ScoreRule) {
            ScoreRule var17 = (ScoreRule)var15;
            String var18 = var17.getVariableCategory();
            LibInfo var19 = var4.variableContains(var18);
            this.a(var3, var19);
         } else {
            this.a((Rule)var15, (Map)var3, (LibraryIndex)var4);
         }
      }

      ObjectMapper var16 = JsonMapper.builder().build();
      return var16.writeValueAsString(var3);
   }

   private void a(Rule var1, Map var2, LibraryIndex var3) {
      Lhs var4 = var1.getLhs();
      this.a(var2, var3, var4);
      Rhs var5 = var1.getRhs();
      this.a(var2, var3, var5);
      Other var6 = var1.getOther();
      if (var6 != null) {
         List var7 = var6.getActions();
         this.a(var2, var3, var7);
      }

   }

   private void a(Map var1, LibraryIndex var2, List var3) {
      if (var3 != null) {
         for(Action var5 : (Iterable<Action>)(Iterable<?>)(var3)) {
            this.a(var5, var1, var2);
         }

      }
   }

   private void a(Map var1, LibraryIndex var2, Rhs var3) {
      if (var3 != null) {
         List var4 = var3.getActions();
         this.a(var1, var2, var4);
      }
   }

   private void a(Map var1, LibraryIndex var2, Lhs var3) {
      if (var3 != null) {
         Criterion var4 = var3.getCriterion();
         this.a(var4, var1, var2);
      }
   }

   private void a(Action var1, Map var2, LibraryIndex var3) {
      if (var1 instanceof ConsolePrintAction) {
         ConsolePrintAction var4 = (ConsolePrintAction)var1;
         Value var5 = var4.getValue();
         this.a(var5, var2, var3);
      } else if (var1 instanceof ExecuteCommonFunctionAction) {
         ExecuteCommonFunctionAction var11 = (ExecuteCommonFunctionAction)var1;
         CommonFunctionParameter var16 = var11.getParameter();
         this.a(var2, var3, var16);
      } else if (var1 instanceof ExecuteMethodAction) {
         ExecuteMethodAction var12 = (ExecuteMethodAction)var1;
         String var17 = var12.getBeanLabel();
         LibInfo var6 = var3.actionContains(var17);
         this.a(var2, var6);
         List var7 = var12.getParameters();
         if (var7 != null) {
            for(Parameter var9 : (Iterable<Parameter>)(Iterable<?>)(var7)) {
               Value var10 = var9.getValue();
               this.a(var10, var2, var3);
            }
         }
      } else if (var1 instanceof ScoringAction) {
         ScoringAction var13 = (ScoringAction)var1;
         Value var18 = var13.getValue();
         this.a(var18, var2, var3);
      } else if (var1 instanceof TemplateAction) {
         TemplateAction var14 = (TemplateAction)var1;
         String var19 = var14.getId();
         LibInfo var21 = var3.templateContains(var19);
         this.a(var2, var21);
      } else if (var1 instanceof VariableAssignAction) {
         VariableAssignAction var15 = (VariableAssignAction)var1;
         String var20 = var15.getVariableCategory();
         LibInfo var22 = var3.variableContains(var20);
         this.a(var2, var22);
         Value var23 = var15.getValue();
         this.a(var23, var2, var3);
      }

   }

   private void a(Criterion var1, Map var2, LibraryIndex var3) {
      if (var1 != null) {
         if (var1 instanceof Junction) {
            Junction var4 = (Junction)var1;
            if (var4.getCriterions() != null) {
               for(Criterion var6 : var4.getCriterions()) {
                  this.a(var6, var2, var3);
               }
            }
         } else if (var1 instanceof ConditionTemplateCriterion) {
            ConditionTemplateCriterion var7 = (ConditionTemplateCriterion)var1;
            LibInfo var9 = var3.templateContains(var7.getId());
            this.a(var2, var9);
         } else if (var1 instanceof Criteria) {
            Criteria var8 = (Criteria)var1;
            Left var10 = var8.getLeft();
            if (var10 != null) {
               LeftPart var11 = var10.getLeftPart();
               this.a(var11, var2, var3);
            }

            this.a(var8.getValue(), var2, var3);
         }

      }
   }

   private void a(LeftPart var1, Map var2, LibraryIndex var3) {
      if (var1 != null) {
         if (var1 instanceof AccumulateLeftPart) {
            AccumulateLeftPart var4 = (AccumulateLeftPart)var1;
            List var5 = var4.getCalculateItems();
            if (var5 != null) {
               for(CalculateItem var7 : (Iterable<CalculateItem>)(Iterable<?>)(var5)) {
                  String var8 = var7.getAssignVariableCategory();
                  if (var8 != null) {
                     LibInfo var9 = var3.variableContains(var8);
                     this.a(var2, var9);
                     Value var10 = var7.getValue();
                     this.a(var10, var2, var3);
                  }
               }
            }

            List var19 = var4.getConditionItems();
            if (var19 != null) {
               for(ConditionItem var27 : (Iterable<ConditionItem>)(Iterable<?>)(var19)) {
                  Value var31 = var27.getValue();
                  this.a(var31, var2, var3);
               }
            }

            Junction var24 = var4.getJunction();
            this.a((Criterion)var24, (Map)var2, (LibraryIndex)var3);
            LoopTarget var28 = var4.getLoopTarget();
            if (var28 != null) {
               this.a(var28.getValue(), var2, var3);
            }
         } else if (var1 instanceof CommonFunctionLeftPart) {
            CommonFunctionLeftPart var11 = (CommonFunctionLeftPart)var1;
            CommonFunctionParameter var15 = var11.getParameter();
            this.a(var2, var3, var15);
         } else if (var1 instanceof FunctionLeftPart) {
            FunctionLeftPart var12 = (FunctionLeftPart)var1;
            List var16 = var12.getParameters();
            if (var16 != null) {
               for(Parameter var25 : (Iterable<Parameter>)(Iterable<?>)(var16)) {
                  Value var29 = var25.getValue();
                  this.a(var29, var2, var3);
               }
            }
         } else if (var1 instanceof MethodLeftPart) {
            MethodLeftPart var13 = (MethodLeftPart)var1;
            String var17 = var13.getBeanLabel();
            LibInfo var21 = var3.actionContains(var17);
            this.a(var2, var21);
            List var26 = var13.getParameters();
            if (var26 != null) {
               for(Parameter var32 : (Iterable<Parameter>)(Iterable<?>)(var26)) {
                  Value var33 = var32.getValue();
                  this.a(var33, var2, var3);
               }
            }
         } else if (var1 instanceof VariableLeftPart) {
            VariableLeftPart var14 = (VariableLeftPart)var1;
            String var18 = var14.getVariableCategory();
            LibInfo var22 = var3.variableContains(var18);
            this.a(var2, var22);
         }

      }
   }

   private void a(Map var1, LibraryIndex var2, CommonFunctionParameter var3) {
      if (var3 != null) {
         Value var4 = var3.getObjectParameter();
         this.a(var4, var1, var2);
      }

   }

   private void a(Value var1, Map var2, LibraryIndex var3) {
      if (var1 != null) {
         if (var1 instanceof CommonFunctionValue) {
            CommonFunctionValue var4 = (CommonFunctionValue)var1;
            String var5 = var4.getLabel();
            LibInfo var6 = var3.actionContains(var5);
            this.a(var2, var6);
            CommonFunctionParameter var7 = var4.getParameter();
            this.a(var2, var3, var7);
         } else if (var1 instanceof ConstantValue) {
            ConstantValue var11 = (ConstantValue)var1;
            String var18 = var11.getConstantCategory();
            LibInfo var24 = var3.constantContains(var18);
            this.a(var2, var24);
         } else if (var1 instanceof MethodValue) {
            MethodValue var12 = (MethodValue)var1;
            String var19 = var12.getBeanLabel();
            LibInfo var25 = var3.actionContains(var19);
            this.a(var2, var25);
            List var27 = var12.getParameters();
            if (var27 != null) {
               for(Parameter var9 : (Iterable<Parameter>)(Iterable<?>)(var27)) {
                  Value var10 = var9.getValue();
                  this.a(var10, var2, var3);
               }
            }
         } else if (var1 instanceof ParameterValue) {
            LibInfo var13 = var3.variableContains("参数");
            this.a(var2, var13);
         } else if (var1 instanceof ParenValue) {
            ParenValue var14 = (ParenValue)var1;
            Value var20 = var14.getValue();
            this.a(var20, var2, var3);
         } else if (var1 instanceof VariableCategoryValue) {
            VariableCategoryValue var15 = (VariableCategoryValue)var1;
            LibInfo var21 = var3.variableContains(var15.getVariableCategory());
            this.a(var2, var21);
         } else if (var1 instanceof VariableValue) {
            VariableValue var16 = (VariableValue)var1;
            String var22 = var16.getVariableCategory();
            LibInfo var26 = var3.variableContains(var22);
            this.a(var2, var26);
         }

         ComplexArithmetic var17 = var1.getArithmetic();
         if (var17 != null) {
            Value var23 = var17.getValue();
            this.a(var23, var2, var3);
         }

      }
   }

   private void a(Map var1, LibInfo var2) {
      if (var2 != null) {
         Object var3 = null;
         if (var1.containsKey(var2.getType())) {
            var3 = (List)var1.get(var2.getType());
         } else {
            var3 = new ArrayList();
            var1.put(var2.getType(), var3);
         }

         if (!((List)var3).contains(var2.getMap())) {
            ((List)var3).add(var2.getMap());
         }
      }
   }

   private LibraryIndex b(String var1) throws Exception {
      ObjectMapper var2 = JsonMapper.builder().build();
      LibraryIndex var3 = new LibraryIndex();
      Map var4 = (Map)var2.readValue(var1, HashMap.class);
      Iterator var5 = var4.keySet().iterator();

      while(true) {
         String var6;
         boolean var8;
         LibraryType var24;
         while(true) {
            if (!var5.hasNext()) {
               return var3;
            }

            var6 = (String)var5.next();
            var24 = null;
            var8 = false;
            if (var6.contentEquals("parameters")) {
               var24 = LibraryType.Parameter;
               break;
            }

            if (var6.contentEquals("variables")) {
               var24 = LibraryType.Variable;
               break;
            }

            if (var6.contentEquals("constants")) {
               var24 = LibraryType.Constant;
               break;
            }

            if (var6.contentEquals("actions")) {
               var24 = LibraryType.Action;
               break;
            }

            if (var6.contentEquals("conditionTemplates")) {
               var24 = LibraryType.ActionTemplate;
               var8 = true;
               break;
            }

            if (var6.contentEquals("actionTemplates")) {
               var24 = LibraryType.ConditionTemplate;
               var8 = true;
               break;
            }
         }

         for(Map var11 : (Iterable<Map>)(Iterable<?>)((List)var4.get(var6))) {
            long var12 = (long)(Integer)var11.get("id");
            Library var14 = new Library();
            var14.setType(var24);
            var14.setId(var12);
            var14.setPath(var11.get("path").toString());
            var14.setVersion((String)var11.get("version"));
            if (var8) {
               KnowledgeBuilder var25 = ServiceUtils.getKnowledgeBuilder();
               ResourceBase var26 = var25.newResourceBase();
               var26.addResource(var12, var14.getVersion());
               Element var17 = this.a(((Resource)var26.getResources().get(0)).getContent());
               if (var24.equals(LibraryType.ActionTemplate)) {
                  ActionTemplate var27 = this.b.deserialize(var17);

                  for(ActionTemplateUnit var29 : var27.getTemplates()) {
                     var3.buildTemplateIndex(var29.getId(), var11);
                  }
               } else {
                  ConditionTemplate var18 = this.c.deserialize(var17);

                  for(ConditionTemplateUnit var20 : var18.getTemplates()) {
                     var3.buildTemplateIndex(var20.getId(), var11);
                  }
               }
            } else {
               ArrayList var15 = new ArrayList();
               var15.add(var14);
               CopyLibPhaseHolder.set();

               try {
                  ResourceLibrary var16 = this.a.buildResourceLibrary(var15, (List)null);
                  var3.buildIndex(var16, var11, var24);
               } finally {
                  CopyLibPhaseHolder.clean();
               }
            }
         }
      }
   }

   protected Element a(String var1) {
      try {
         Document var2 = DocumentHelper.parseText(var1);
         Element var3 = var2.getRootElement();
         return var3;
      } catch (DocumentException var4) {
         throw new RuleException(var4);
      }
   }
}
