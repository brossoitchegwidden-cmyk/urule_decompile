package com.bstek.urule.console.editor.file;

import com.bstek.urule.Utils;
import com.bstek.urule.action.Action;
import com.bstek.urule.action.ExecuteCommonFunctionAction;
import com.bstek.urule.action.ExecuteMethodAction;
import com.bstek.urule.action.InvokeFile;
import com.bstek.urule.action.InvokeKnowledgePackage;
import com.bstek.urule.builder.ParsePhaseHolder;
import com.bstek.urule.console.ApiServletHandler;
import com.bstek.urule.console.database.manager.file.FileManager;
import com.bstek.urule.console.database.manager.file.version.VersionFileManager;
import com.bstek.urule.console.database.manager.packet.PacketManager;
import com.bstek.urule.console.database.manager.project.ProjectManager;
import com.bstek.urule.console.database.model.Packet;
import com.bstek.urule.console.database.model.Project;
import com.bstek.urule.console.database.model.RuleFile;
import com.bstek.urule.console.database.model.VersionFile;
import com.bstek.urule.console.editor.FileDeserializer;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.crosstab.CrosstabDefinition;
import com.bstek.urule.model.decisiontree.ActionTreeNode;
import com.bstek.urule.model.decisiontree.ConditionTreeNode;
import com.bstek.urule.model.decisiontree.DecisionTree;
import com.bstek.urule.model.decisiontree.TreeNode;
import com.bstek.urule.model.decisiontree.VariableTreeNode;
import com.bstek.urule.model.flow.BindingFile;
import com.bstek.urule.model.flow.FlowDefinition;
import com.bstek.urule.model.flow.FlowNode;
import com.bstek.urule.model.flow.RuleNode;
import com.bstek.urule.model.function.FunctionDescriptor;
import com.bstek.urule.model.library.action.ActionLibrary;
import com.bstek.urule.model.library.action.Method;
import com.bstek.urule.model.library.action.SpringBean;
import com.bstek.urule.model.rule.Library;
import com.bstek.urule.model.rule.Other;
import com.bstek.urule.model.rule.Parameter;
import com.bstek.urule.model.rule.Rhs;
import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.model.rule.RuleSet;
import com.bstek.urule.model.rule.lhs.CommonFunctionParameter;
import com.bstek.urule.model.rule.loop.LoopRule;
import com.bstek.urule.model.rule.loop.LoopRuleUnit;
import com.bstek.urule.model.scorecard.ComplexScorecardDefinition;
import com.bstek.urule.model.scorecard.ScorecardDefinition;
import com.bstek.urule.model.table.Cell;
import com.bstek.urule.model.table.DecisionTable;
import com.bstek.urule.model.template.ActionTemplate;
import com.bstek.urule.model.template.ActionTemplateUnit;
import com.bstek.urule.model.template.ConditionTemplate;
import com.bstek.urule.model.template.ConditionTemplateUnit;
import com.bstek.urule.parse.RuleFileHolder;
import com.bstek.urule.parse.deserializer.ActionLibraryDeserializer;
import com.bstek.urule.parse.deserializer.ActionTemplateDeserializer;
import com.bstek.urule.parse.deserializer.ConditionTemplateDeserializer;
import com.bstek.urule.parse.deserializer.Deserializer;
import com.bstek.urule.parse.deserializer.RuleSetDeserializer;
import com.bstek.urule.runtime.BuiltInActionLibraryBuilder;
import com.bstek.urule.runtime.ProxyUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.dom4j.Element;
import org.springframework.context.ApplicationContext;

public class LoadServletHandler extends ApiServletHandler {
   private BuiltInActionLibraryBuilder e;
   private List f = new ArrayList();

   public void init() {
      super.init();
      ApplicationContext var1 = Utils.getApplicationContext();
      this.e = (BuiltInActionLibraryBuilder)var1.getBean("urule.builtInActionLibraryBuilder");

      for(FunctionDescriptor var4 : var1.getBeansOfType(FunctionDescriptor.class).values()) {
         if (!var4.isDisabled()) {
            this.f.add((FunctionDescriptor)ProxyUtils.getTargetObject(var4));
         }
      }

   }

   public void loadFunctions(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      this.a(var2, this.f);
   }

   public void loadBaseLibraries(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      HashMap var3 = new HashMap();
      ParsePhaseHolder.defineParsePhase();
      var3.put("functions", this.f);
      ArrayList var4 = new ArrayList();
      this.addBuiltinActions(var4);
      var3.put("springBeans", var4);
      FileManager var5 = FileManager.ins;
      VersionFileManager var6 = VersionFileManager.ins;
      String var7 = var1.getParameter("actionLibraries");
      if (StringUtils.isNotBlank(var7)) {
         var7 = var7.trim();
      }

      if (StringUtils.isNotBlank(var7)) {
         String[] var8 = var7.split(";");

         for(String var12 : var8) {
            String[] var13 = var12.split(":");
            String var14 = var13[0];
            String var15 = var13[1];
            Object var16 = null;
            RuleFile var17 = FileManager.ins.get(Long.valueOf(var14));
            RuleFileHolder.resetRuleFile(var17.getPath());
            String var22;
            if (var15.contentEquals("false")) {
               var22 = var5.loadContent(Long.valueOf(var14));
            } else {
               VersionFile var18 = var6.loadFile(Long.valueOf(var14), var15);
               var22 = var6.loadFileContent(var18.getId());
            }

            Element var23 = FileDeserializer.getInstance().parseXml(var22);
            Deserializer var19 = FileDeserializer.getInstance().getDeserializer(var23);
            Object var20 = var19.deserialize(var23);
            this.a(var20);
            RuleFileHolder.clean();
            if (var19 instanceof ActionLibraryDeserializer) {
               ActionLibrary var21 = (ActionLibrary)var20;
               var4.add(var21);
            }
         }
      }

      ParsePhaseHolder.cleanParsePhase();
      this.a(var2, var3);
   }

   public void loadFile(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      FileManager var3 = FileManager.ins;
      VersionFileManager var4 = VersionFileManager.ins;
      String var5 = var1.getParameter("files");
      String var6 = var1.getParameter("singleFile");
      ParsePhaseHolder.defineParsePhase();
      ArrayList var7 = new ArrayList();
      if (StringUtils.isBlank(var5)) {
         this.addBuiltinActions(var7);
      } else {
         String[] var8 = var5.split(";");

         for(String var12 : var8) {
            String[] var13 = var12.split(":");
            String var14 = var13[0];
            String var15 = var13[1];
            Object var16 = null;
            RuleFile var17 = FileManager.ins.get(Long.valueOf(var14));
            RuleFileHolder.resetRuleFile(var17.getPath());
            String var25;
            if (var15.contentEquals("false")) {
               var25 = var3.loadContent(Long.valueOf(var14));
            } else {
               VersionFile var18 = var4.loadFile(Long.valueOf(var14), var15);
               var25 = var4.loadFileContent(var18.getId());
            }

            Element var26 = FileDeserializer.getInstance().parseXml(var25);
            Deserializer var19 = FileDeserializer.getInstance().getDeserializer(var26);
            Object var20 = var19.deserialize(var26);
            this.a(var20);
            var7.add(var20);
            RuleFileHolder.clean();
            if (var19 instanceof ActionLibraryDeserializer && StringUtils.isBlank(var6)) {
               this.addBuiltinActions(var7);
            }

            if (var19 instanceof RuleSetDeserializer) {
               this.c(var7);
            }

            if (var19 instanceof ActionTemplateDeserializer) {
               ActionTemplate var21 = (ActionTemplate)var20;
               List var22 = var21.getTemplates();
               if (var8 != null) {
                  for(ActionTemplateUnit var24 : (Iterable<ActionTemplateUnit>)(Iterable<?>)(var22)) {
                     var24.setPath(var12);
                  }
               }
            }

            if (var19 instanceof ConditionTemplateDeserializer) {
               ConditionTemplate var27 = (ConditionTemplate)var20;
               List var28 = var27.getTemplates();
               if (var8 != null) {
                  for(ConditionTemplateUnit var30 : (Iterable<ConditionTemplateUnit>)(Iterable<?>)(var28)) {
                     var30.setPath(var12);
                  }
               }
            }
         }
      }

      ParsePhaseHolder.cleanParsePhase();
      this.a(var2, var7);
   }

   private void a(Object var1) throws Exception {
      if (var1 != null) {
         if (var1 instanceof RuleSet) {
            RuleSet var2 = (RuleSet)var1;
            this.b(var2.getLibraries());

            for(Rule var4 : var2.getRules()) {
               if (var4 instanceof LoopRule) {
                  LoopRule var24 = (LoopRule)var4;

                  for(LoopRuleUnit var8 : var24.getUnits()) {
                     Rhs var9 = var8.getRhs();
                     if (var9 != null) {
                        this.a(var9.getActions());
                     }

                     Other var10 = var8.getOther();
                     if (var10 != null) {
                        this.a(var10.getActions());
                     }
                  }

                  this.a(var24.getLoopEnd().getActions());
               } else {
                  Rhs var5 = var4.getRhs();
                  if (var5 != null) {
                     this.a(var5.getActions());
                  }

                  Other var6 = var4.getOther();
                  if (var6 != null) {
                     this.a(var6.getActions());
                  }
               }
            }
         } else if (var1 instanceof DecisionTable) {
            DecisionTable var11 = (DecisionTable)var1;
            this.b(var11.getLibraries());
            Map var19 = var11.getCellMap();
            this.a(var19);
         } else if (var1 instanceof CrosstabDefinition) {
            CrosstabDefinition var12 = (CrosstabDefinition)var1;
            this.b(var12.getLibraries());
         } else if (var1 instanceof DecisionTree) {
            DecisionTree var13 = (DecisionTree)var1;
            this.b(var13.getLibraries());
            VariableTreeNode var20 = var13.getVariableTreeNode();
            this.a((TreeNode)var20);
         } else if (var1 instanceof ScorecardDefinition) {
            ScorecardDefinition var14 = (ScorecardDefinition)var1;
            this.b(var14.getLibraries());
         } else if (var1 instanceof ComplexScorecardDefinition) {
            ComplexScorecardDefinition var15 = (ComplexScorecardDefinition)var1;
            this.b(var15.getLibraries());
            Map var21 = var15.getCellMap();
            this.a(var21);
         } else if (var1 instanceof FlowDefinition) {
            FlowDefinition var16 = (FlowDefinition)var1;
            this.b(var16.getLibraries());

            for(FlowNode var23 : var16.getNodes()) {
               if (var23 instanceof RuleNode) {
                  RuleNode var25 = (RuleNode)var23;

                  for(BindingFile var28 : var25.getFiles()) {
                     RuleFile var29 = FileManager.ins.get(var28.getId());
                     var28.setPath(var29.getPath());
                  }
               }
            }
         } else if (var1 instanceof ActionTemplate) {
            ActionTemplate var17 = (ActionTemplate)var1;
            this.b(var17.getLibraries());
         } else if (var1 instanceof ConditionTemplate) {
            ConditionTemplate var18 = (ConditionTemplate)var1;
            this.b(var18.getLibraries());
         }

      }
   }

   private void a(Map var1) throws Exception {
      if (var1 != null) {
         for(Cell var3 : (Iterable<Cell>)(Iterable<?>)(var1.values())) {
            Action var4 = var3.getAction();
            this.a(var4);
         }
      }

   }

   private void a(TreeNode var1) throws Exception {
      if (var1 instanceof VariableTreeNode) {
         VariableTreeNode var2 = (VariableTreeNode)var1;
         List var3 = var2.getConditionTreeNodes();
         if (var3 != null) {
            for(ConditionTreeNode var5 : (Iterable<ConditionTreeNode>)(Iterable<?>)(var3)) {
               this.a((TreeNode)var5);
            }
         }
      } else if (var1 instanceof ConditionTreeNode) {
         ConditionTreeNode var8 = (ConditionTreeNode)var1;
         List var10 = var8.getActionTreeNodes();
         if (var10 != null) {
            for(ActionTreeNode var14 : (Iterable<ActionTreeNode>)(Iterable<?>)(var10)) {
               this.a((TreeNode)var14);
            }
         }

         List var13 = var8.getConditionTreeNodes();
         if (var13 != null) {
            for(ConditionTreeNode var6 : (Iterable<ConditionTreeNode>)(Iterable<?>)(var13)) {
               this.a((TreeNode)var6);
            }
         }

         List var16 = var8.getVariableTreeNodes();
         if (var16 != null) {
            for(VariableTreeNode var7 : (Iterable<VariableTreeNode>)(Iterable<?>)(var16)) {
               this.a((TreeNode)var7);
            }
         }
      } else if (var1 instanceof ActionTreeNode) {
         ActionTreeNode var9 = (ActionTreeNode)var1;
         List var11 = var9.getActions();
         this.a(var11);
      }

   }

   private void a(List var1) throws Exception {
      if (var1 != null) {
         for(Action var3 : (Iterable<Action>)(Iterable<?>)(var1)) {
            this.a(var3);
         }

      }
   }

   private void a(ExecuteCommonFunctionAction var1) {
      FunctionDescriptor var2 = this.b(var1.getName());
      CommonFunctionParameter var3 = var1.getParameter();
      if (var3 != null) {
         String var4 = var3.getName();
         if (var2 != null && var2.getArgument() != null && StringUtils.isNotBlank(var2.getArgument().getEname())) {
            var4 = var2.getArgument().getEname();
         }

         var3.setEname(var4);
      }

   }

   private void a(Action var1) throws Exception {
      if (!(var1 instanceof ExecuteMethodAction)) {
         if (var1 instanceof ExecuteCommonFunctionAction) {
            this.a((ExecuteCommonFunctionAction)var1);
         }

      } else {
         ExecuteMethodAction var2 = (ExecuteMethodAction)var1;
         if (var2.getInvokeKnowledgePackage() != null) {
            InvokeKnowledgePackage var3 = var2.getInvokeKnowledgePackage();
            Packet var4 = null;
            if (StringUtils.isNotBlank(var3.getCode())) {
               var4 = PacketManager.ins.load(var3.getCode());
            }

            if (var4 == null) {
               var4 = PacketManager.ins.load(var3.getId());
            }

            if (var4 == null) {
               throw new RuleException(String.format("无法找到ID【%s】，CODE【%s】对应的知识包!", var3.getId(), var3.getCode()));
            }

            var3.setName(var4.getName());
            var3.setCode(var4.getCode());
            Project var5 = ProjectManager.ins.get(var4.getProjectId());
            var3.setProject(var5.getName());
         } else if (var2.getInvokeFile() != null) {
            InvokeFile var10 = var2.getInvokeFile();
            RuleFile var12 = FileManager.ins.get(var10.getId());
            var10.setPath(var12.getPath());
         }

         SpringBean var11 = this.a(var2.getBeanId());
         if (var11 != null) {
            var2.setBeanELabel(var11.getEname());

            for(Method var14 : var11.getMethods()) {
               if (var2.getMethodName().equals(var14.getMethodName())) {
                  for(Parameter var7 : var2.getParameters()) {
                     for(com.bstek.urule.model.library.action.Parameter var9 : var14.getParameters()) {
                        if (var9.getName().equals(var7.getName())) {
                           var7.setEname(var9.getEname());
                        }
                     }
                  }
               }
            }
         }

      }
   }

   private void b(List var1) {
      if (var1 != null) {
         for(Library var3 : (Iterable<Library>)(Iterable<?>)(var1)) {
            RuleFile var4 = FileManager.ins.get(var3.getId());
            var3.setPath(var4.getPath());
         }

      }
   }

   public void addBuiltinActions(List var1) throws Exception {
      List var2 = this.e.getBuiltInActions();
      if (var2.size() > 0) {
         ActionLibrary var3 = new ActionLibrary();
         var3.setSpringBeans(var2);
         var1.add(var3);
      }

   }

   private SpringBean a(String var1) {
      SpringBean var2 = null;
      List var3 = this.e.getBuiltInActions();
      if (var3.size() > 0) {
         for(SpringBean var5 : (Iterable<SpringBean>)(Iterable<?>)(var3)) {
            if (var1.equals(var5.getId())) {
               var2 = var5;
               break;
            }
         }
      }

      return var2;
   }

   private FunctionDescriptor b(String var1) {
      FunctionDescriptor var2 = null;
      List var3 = this.f;
      if (var3.size() > 0) {
         for(FunctionDescriptor var5 : (Iterable<FunctionDescriptor>)(Iterable<?>)(var3)) {
            if (var1.equals(var5.getName())) {
               var2 = var5;
               break;
            }
         }
      }

      return var2;
   }

   private void c(List var1) {
      if (var1.size() != 0) {
         RuleSet var2 = (RuleSet)var1.get(0);

         for(Rule var4 : var2.getRules()) {
            if (var4.isDebugFromGlobal()) {
               var4.setDebug((Boolean)null);
            }
         }

      }
   }

   public String url() {
      return "/load";
   }
}
