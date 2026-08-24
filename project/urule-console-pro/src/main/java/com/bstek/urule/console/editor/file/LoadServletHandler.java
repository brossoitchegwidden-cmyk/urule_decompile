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

/** Loads editor resources and enriches their cross-file/action references. */
public class LoadServletHandler extends ApiServletHandler {
   private BuiltInActionLibraryBuilder builtInActionLibraryBuilder;
   private List<FunctionDescriptor> functionDescriptors = new ArrayList<>();

   public void init() {
      super.init();
      ApplicationContext applicationContext = Utils.getApplicationContext();
      this.builtInActionLibraryBuilder = (BuiltInActionLibraryBuilder)applicationContext.getBean("urule.builtInActionLibraryBuilder");

      for(FunctionDescriptor functionDescriptor : applicationContext.getBeansOfType(FunctionDescriptor.class).values()) {
         if (!functionDescriptor.isDisabled()) {
            this.functionDescriptors.add((FunctionDescriptor)ProxyUtils.getTargetObject(functionDescriptor));
         }
      }

   }

   public void loadFunctions(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      this.writeObjectToJson(resp, this.functionDescriptors);
   }

   public void loadBaseLibraries(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      HashMap valuesByKey = new HashMap();
      ParsePhaseHolder.defineParsePhase();
      valuesByKey.put("functions", this.functionDescriptors);
      ArrayList items = new ArrayList();
      this.addBuiltinActions(items);
      valuesByKey.put("springBeans", items);
      FileManager fileManager = FileManager.ins;
      VersionFileManager versionFileManager = VersionFileManager.ins;
      String parameter = req.getParameter("actionLibraries");
      if (StringUtils.isNotBlank(parameter)) {
         parameter = parameter.trim();
      }

      if (StringUtils.isNotBlank(parameter)) {
         String[] parts = parameter.split(";");

         for(String text : parts) {
            String[] parts2 = text.split(":");
            String text2 = parts2[0];
            String text3 = parts2[1];
            Object objectValue = null;
            RuleFile ruleFile = FileManager.ins.get(Long.valueOf(text2));
            RuleFileHolder.resetRuleFile(ruleFile.getPath());
            String text4;
            if (text3.contentEquals("false")) {
               text4 = fileManager.loadContent(Long.valueOf(text2));
            } else {
               VersionFile file = versionFileManager.loadFile(Long.valueOf(text2), text3);
               text4 = versionFileManager.loadFileContent(file.getId());
            }

            Element xml = FileDeserializer.getInstance().parseXml(text4);
            Deserializer deserializer = FileDeserializer.getInstance().getDeserializer(xml);
            Object objectValue2 = deserializer.deserialize(xml);
            this.resolveResourceReferences(objectValue2);
            RuleFileHolder.clean();
            if (deserializer instanceof ActionLibraryDeserializer) {
               ActionLibrary actionLibrary = (ActionLibrary)objectValue2;
               items.add(actionLibrary);
            }
         }
      }

      ParsePhaseHolder.cleanParsePhase();
      this.writeObjectToJson(resp, valuesByKey);
   }

   public void loadFile(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      FileManager fileManager = FileManager.ins;
      VersionFileManager versionFileManager = VersionFileManager.ins;
      String parameter = req.getParameter("files");
      String parameter2 = req.getParameter("singleFile");
      ParsePhaseHolder.defineParsePhase();
      ArrayList items = new ArrayList();
      if (StringUtils.isBlank(parameter)) {
         this.addBuiltinActions(items);
      } else {
         String[] parts = parameter.split(";");

         for(String text : parts) {
            String[] parts2 = text.split(":");
            String text2 = parts2[0];
            String text3 = parts2[1];
            Object objectValue = null;
            RuleFile ruleFile = FileManager.ins.get(Long.valueOf(text2));
            RuleFileHolder.resetRuleFile(ruleFile.getPath());
            String text4;
            if (text3.contentEquals("false")) {
               text4 = fileManager.loadContent(Long.valueOf(text2));
            } else {
               VersionFile file = versionFileManager.loadFile(Long.valueOf(text2), text3);
               text4 = versionFileManager.loadFileContent(file.getId());
            }

            Element xml = FileDeserializer.getInstance().parseXml(text4);
            Deserializer deserializer = FileDeserializer.getInstance().getDeserializer(xml);
            Object objectValue2 = deserializer.deserialize(xml);
            this.resolveResourceReferences(objectValue2);
            items.add(objectValue2);
            RuleFileHolder.clean();
            if (deserializer instanceof ActionLibraryDeserializer && StringUtils.isBlank(parameter2)) {
               this.addBuiltinActions(items);
            }

            if (deserializer instanceof RuleSetDeserializer) {
               this.clearInheritedDebugFlag(items);
            }

            if (deserializer instanceof ActionTemplateDeserializer) {
               ActionTemplate actionTemplate = (ActionTemplate)objectValue2;
               List templates = actionTemplate.getTemplates();
               if (parts != null) {
                  for(ActionTemplateUnit actionTemplateUnit : (Iterable<ActionTemplateUnit>)(Iterable<?>)(templates)) {
                     actionTemplateUnit.setPath(text);
                  }
               }
            }

            if (deserializer instanceof ConditionTemplateDeserializer) {
               ConditionTemplate conditionTemplate = (ConditionTemplate)objectValue2;
               List templates2 = conditionTemplate.getTemplates();
               if (parts != null) {
                  for(ConditionTemplateUnit conditionTemplateUnit : (Iterable<ConditionTemplateUnit>)(Iterable<?>)(templates2)) {
                     conditionTemplateUnit.setPath(text);
                  }
               }
            }
         }
      }

      ParsePhaseHolder.cleanParsePhase();
      this.writeObjectToJson(resp, items);
   }

   private void resolveResourceReferences(Object objectValue) throws Exception {
      if (objectValue != null) {
         if (objectValue instanceof RuleSet) {
            RuleSet ruleSet = (RuleSet)objectValue;
            this.resolveLibraryPaths(ruleSet.getLibraries());

            for(Rule rule : ruleSet.getRules()) {
               if (rule instanceof LoopRule) {
                  LoopRule loopRule = (LoopRule)rule;

                  for(LoopRuleUnit loopRuleUnit : loopRule.getUnits()) {
                     Rhs rhs = loopRuleUnit.getRhs();
                     if (rhs != null) {
                        this.enrichActions(rhs.getActions());
                     }

                     Other other = loopRuleUnit.getOther();
                     if (other != null) {
                        this.enrichActions(other.getActions());
                     }
                  }

                  this.enrichActions(loopRule.getLoopEnd().getActions());
               } else {
                  Rhs rhs2 = rule.getRhs();
                  if (rhs2 != null) {
                     this.enrichActions(rhs2.getActions());
                  }

                  Other other2 = rule.getOther();
                  if (other2 != null) {
                     this.enrichActions(other2.getActions());
                  }
               }
            }
         } else if (objectValue instanceof DecisionTable) {
            DecisionTable decisionTable = (DecisionTable)objectValue;
            this.resolveLibraryPaths(decisionTable.getLibraries());
            Map cellMap = decisionTable.getCellMap();
            this.enrichCellActions(cellMap);
         } else if (objectValue instanceof CrosstabDefinition) {
            CrosstabDefinition crosstabDefinition = (CrosstabDefinition)objectValue;
            this.resolveLibraryPaths(crosstabDefinition.getLibraries());
         } else if (objectValue instanceof DecisionTree) {
            DecisionTree decisionTree = (DecisionTree)objectValue;
            this.resolveLibraryPaths(decisionTree.getLibraries());
            VariableTreeNode variableTreeNode = decisionTree.getVariableTreeNode();
            this.enrichTreeActions(variableTreeNode);
         } else if (objectValue instanceof ScorecardDefinition) {
            ScorecardDefinition scorecardDefinition = (ScorecardDefinition)objectValue;
            this.resolveLibraryPaths(scorecardDefinition.getLibraries());
         } else if (objectValue instanceof ComplexScorecardDefinition) {
            ComplexScorecardDefinition complexScorecardDefinition = (ComplexScorecardDefinition)objectValue;
            this.resolveLibraryPaths(complexScorecardDefinition.getLibraries());
            Map cellMap2 = complexScorecardDefinition.getCellMap();
            this.enrichCellActions(cellMap2);
         } else if (objectValue instanceof FlowDefinition) {
            FlowDefinition flowDefinition = (FlowDefinition)objectValue;
            this.resolveLibraryPaths(flowDefinition.getLibraries());

            for(FlowNode flowNode : flowDefinition.getNodes()) {
               if (flowNode instanceof RuleNode) {
                  RuleNode ruleNode = (RuleNode)flowNode;

                  for(BindingFile bindingFile : ruleNode.getFiles()) {
                     RuleFile ruleFile = FileManager.ins.get(bindingFile.getId());
                     bindingFile.setPath(ruleFile.getPath());
                  }
               }
            }
         } else if (objectValue instanceof ActionTemplate) {
            ActionTemplate actionTemplate = (ActionTemplate)objectValue;
            this.resolveLibraryPaths(actionTemplate.getLibraries());
         } else if (objectValue instanceof ConditionTemplate) {
            ConditionTemplate conditionTemplate = (ConditionTemplate)objectValue;
            this.resolveLibraryPaths(conditionTemplate.getLibraries());
         }

      }
   }

   private void enrichCellActions(Map valuesByKey) throws Exception {
      if (valuesByKey != null) {
         for(Cell cell : (Iterable<Cell>)(Iterable<?>)(valuesByKey.values())) {
            Action action = cell.getAction();
            this.enrichAction(action);
         }
      }

   }

   private void enrichTreeActions(TreeNode treeNode) throws Exception {
      if (treeNode instanceof VariableTreeNode) {
         VariableTreeNode variableTreeNode2 = (VariableTreeNode)treeNode;
         List conditionTreeNodes = variableTreeNode2.getConditionTreeNodes();
         if (conditionTreeNodes != null) {
            for(ConditionTreeNode conditionTreeNode : (Iterable<ConditionTreeNode>)(Iterable<?>)(conditionTreeNodes)) {
               this.enrichTreeActions(conditionTreeNode);
            }
         }
      } else if (treeNode instanceof ConditionTreeNode) {
         ConditionTreeNode conditionTreeNode2 = (ConditionTreeNode)treeNode;
         List actionTreeNodes = conditionTreeNode2.getActionTreeNodes();
         if (actionTreeNodes != null) {
            for(ActionTreeNode actionTreeNode : (Iterable<ActionTreeNode>)(Iterable<?>)(actionTreeNodes)) {
               this.enrichTreeActions(actionTreeNode);
            }
         }

         List conditionTreeNodes2 = conditionTreeNode2.getConditionTreeNodes();
         if (conditionTreeNodes2 != null) {
            for(ConditionTreeNode conditionTreeNode3 : (Iterable<ConditionTreeNode>)(Iterable<?>)(conditionTreeNodes2)) {
               this.enrichTreeActions(conditionTreeNode3);
            }
         }

         List variableTreeNodes = conditionTreeNode2.getVariableTreeNodes();
         if (variableTreeNodes != null) {
            for(VariableTreeNode variableTreeNode : (Iterable<VariableTreeNode>)(Iterable<?>)(variableTreeNodes)) {
               this.enrichTreeActions(variableTreeNode);
            }
         }
      } else if (treeNode instanceof ActionTreeNode) {
         ActionTreeNode actionTreeNode2 = (ActionTreeNode)treeNode;
         List actions = actionTreeNode2.getActions();
         this.enrichActions(actions);
      }

   }

   private void enrichActions(List items) throws Exception {
      if (items != null) {
         for(Action action : (Iterable<Action>)(Iterable<?>)(items)) {
            this.enrichAction(action);
         }

      }
   }

   private void enrichCommonFunctionAction(ExecuteCommonFunctionAction executeCommonFunctionAction) {
      FunctionDescriptor functionDescriptor = this.findFunctionDescriptor(executeCommonFunctionAction.getName());
      CommonFunctionParameter parameter = executeCommonFunctionAction.getParameter();
      if (parameter != null) {
         String name = parameter.getName();
         if (functionDescriptor != null && functionDescriptor.getArgument() != null && StringUtils.isNotBlank(functionDescriptor.getArgument().getEname())) {
            name = functionDescriptor.getArgument().getEname();
         }

         parameter.setEname(name);
      }

   }

   private void enrichAction(Action action) throws Exception {
      if (!(action instanceof ExecuteMethodAction)) {
         if (action instanceof ExecuteCommonFunctionAction) {
            this.enrichCommonFunctionAction((ExecuteCommonFunctionAction)action);
         }

      } else {
         ExecuteMethodAction executeMethodAction = (ExecuteMethodAction)action;
         if (executeMethodAction.getInvokeKnowledgePackage() != null) {
            InvokeKnowledgePackage invokeKnowledgePackage = executeMethodAction.getInvokeKnowledgePackage();
            Packet packet = null;
            if (StringUtils.isNotBlank(invokeKnowledgePackage.getCode())) {
               packet = PacketManager.ins.load(invokeKnowledgePackage.getCode());
            }

            if (packet == null) {
               packet = PacketManager.ins.load(invokeKnowledgePackage.getId());
            }

            if (packet == null) {
               throw new RuleException(String.format("无法找到ID【%s】，CODE【%s】对应的知识包!", invokeKnowledgePackage.getId(), invokeKnowledgePackage.getCode()));
            }

            invokeKnowledgePackage.setName(packet.getName());
            invokeKnowledgePackage.setCode(packet.getCode());
            Project project = ProjectManager.ins.get(packet.getProjectId());
            invokeKnowledgePackage.setProject(project.getName());
         } else if (executeMethodAction.getInvokeFile() != null) {
            InvokeFile invokeFile = executeMethodAction.getInvokeFile();
            RuleFile ruleFile = FileManager.ins.get(invokeFile.getId());
            invokeFile.setPath(ruleFile.getPath());
         }

         SpringBean springBean = this.findSpringBean(executeMethodAction.getBeanId());
         if (springBean != null) {
            executeMethodAction.setBeanELabel(springBean.getEname());

            for(Method method : springBean.getMethods()) {
               if (executeMethodAction.getMethodName().equals(method.getMethodName())) {
                  for(Parameter parameter : executeMethodAction.getParameters()) {
                     for(com.bstek.urule.model.library.action.Parameter parameter2 : method.getParameters()) {
                        if (parameter2.getName().equals(parameter.getName())) {
                           parameter.setEname(parameter2.getEname());
                        }
                     }
                  }
               }
            }
         }

      }
   }

   private void resolveLibraryPaths(List items) {
      if (items != null) {
         for(Library library : (Iterable<Library>)(Iterable<?>)(items)) {
            RuleFile ruleFile = FileManager.ins.get(library.getId());
            library.setPath(ruleFile.getPath());
         }

      }
   }

   public void addBuiltinActions(List result) throws Exception {
      List builtInActions = this.builtInActionLibraryBuilder.getBuiltInActions();
      if (builtInActions.size() > 0) {
         ActionLibrary actionLibrary = new ActionLibrary();
         actionLibrary.setSpringBeans(builtInActions);
         result.add(actionLibrary);
      }

   }

   private SpringBean findSpringBean(String beanId) {
      SpringBean springBean = null;
      List builtInActions = this.builtInActionLibraryBuilder.getBuiltInActions();
      if (builtInActions.size() > 0) {
         for(SpringBean springBean2 : (Iterable<SpringBean>)(Iterable<?>)(builtInActions)) {
            if (beanId.equals(springBean2.getId())) {
               springBean = springBean2;
               break;
            }
         }
      }

      return springBean;
   }

   private FunctionDescriptor findFunctionDescriptor(String functionName) {
      FunctionDescriptor functionDescriptor = null;
      if (this.functionDescriptors.size() > 0) {
         for(FunctionDescriptor descriptor : this.functionDescriptors) {
            if (functionName.equals(descriptor.getName())) {
               functionDescriptor = descriptor;
               break;
            }
         }
      }

      return functionDescriptor;
   }

   private void clearInheritedDebugFlag(List items) {
      if (items.size() != 0) {
         RuleSet ruleSet = (RuleSet)items.get(0);

         for(Rule rule : ruleSet.getRules()) {
            if (rule.isDebugFromGlobal()) {
               rule.setDebug((Boolean)null);
            }
         }

      }
   }

   public String url() {
      return "/load";
   }
}
