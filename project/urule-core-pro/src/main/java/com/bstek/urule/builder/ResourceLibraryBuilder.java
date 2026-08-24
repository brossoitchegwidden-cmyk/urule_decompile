package com.bstek.urule.builder;

import com.bstek.urule.builder.resource.Resource;
import com.bstek.urule.builder.resource.ResourceBuilder;
import com.bstek.urule.builder.resource.ResourceType;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.library.ResourceLibrary;
import com.bstek.urule.model.library.action.ActionLibrary;
import com.bstek.urule.model.library.constant.ConstantLibrary;
import com.bstek.urule.model.library.variable.Variable;
import com.bstek.urule.model.library.variable.VariableCategory;
import com.bstek.urule.model.library.variable.VariableLibrary;
import com.bstek.urule.model.rule.Library;
import com.bstek.urule.model.rule.LibraryType;
import com.bstek.urule.model.rule.Predefine;
import com.bstek.urule.model.template.ActionTemplate;
import com.bstek.urule.model.template.ActionTemplateUnit;
import com.bstek.urule.model.template.ConditionTemplate;
import com.bstek.urule.model.template.ConditionTemplateUnit;
import com.bstek.urule.runtime.BuiltInActionLibraryBuilder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Element;

public class ResourceLibraryBuilder extends AbstractBuilder {
   private static final Log logger = LogFactory.getLog(ResourceLibraryBuilder.class);
   public static final String BEAN_ID = "urule.resourceLibraryBuilder";
   private BuiltInActionLibraryBuilder builtInActionLibraryBuilder;

   public ResourceLibrary buildResourceLibrary(Collection<Library> libraries, List<Predefine> predefines) {
      if (libraries == null) {
         libraries = Collections.EMPTY_LIST;
      }

      if (!ParsePhaseHolder.isParsePhase()) {
         this.registerTemplateResources(libraries);
      }

      ArrayList items = new ArrayList();
      ArrayList items2 = new ArrayList();
      ArrayList items3 = new ArrayList();
      ArrayList items4 = new ArrayList();
      ArrayList items5 = new ArrayList();
      ArrayList items6 = new ArrayList();
      ResourceBase resourceBase = this.newResourceBase();

      for (Library library : libraries) {
         try {
            resourceBase.addResource(library.getId(), library.getVersion());
         } catch (RuleException ruleException) {
            throw new RuleException(String.format("类库【%d，%s】加载失败，异常消息:\r\n%s", library.getId(), library.getPath(), ruleException.getMessage()));
         }
      }

      for (Resource resource : resourceBase.getResources()) {
         String content = resource.getContent();
         Element resource2 = this.parseResource(content);

         for (ResourceBuilder resourceBuilder : this.resourceBuilders) {
            if (resourceBuilder.support(resource2)) {
               String path = resource.getPath();
               if (resource.getVersion() != null) {
                  path = path + ":" + resource.getVersion();
               }

               if (ResourceLibraryBuilder.logger.isDebugEnabled()) {
                  ResourceLibraryBuilder.logger.debug("build resource... " + resource.toString());
               }

               Object objectValue = resourceBuilder.build(resource2, path);
               ResourceType type = resourceBuilder.getType();
               if (type.equals(ResourceType.ActionLibrary)) {
                  ActionLibrary actionLibrary = (ActionLibrary)objectValue;
                  items4.add(actionLibrary);
                  break;
               }

               if (type.equals(ResourceType.VariableLibrary)) {
                  VariableLibrary variableLibrary = (VariableLibrary)objectValue;
                  items5.add(variableLibrary);
               } else if (type.equals(ResourceType.ConstantLibrary)) {
                  ConstantLibrary constantLibrary = (ConstantLibrary)objectValue;
                  items3.add(constantLibrary);
               } else if (type.equals(ResourceType.ParameterLibrary)) {
                  VariableCategory variableCategory = (VariableCategory)objectValue;
                  items6.add(variableCategory);
               } else if (type.equals(ResourceType.ConditionTemplate)) {
                  ConditionTemplate conditionTemplate = (ConditionTemplate)objectValue;
                  List templates = conditionTemplate.getTemplates();
                  if (templates != null) {
                     for (ConditionTemplateUnit conditionTemplateUnit : (Iterable<ConditionTemplateUnit>)(Iterable<?>)(templates)) {
                        String text = String.valueOf(resource.getId());
                        if (resource.getVersion() != null) {
                           text = text + ":" + resource.getVersion();
                        }

                        conditionTemplateUnit.setPath(text);
                     }
                  }

                  items.add(conditionTemplate);
               } else if (type.equals(ResourceType.ActionTemplate)) {
                  ActionTemplate actionTemplate = (ActionTemplate)objectValue;
                  List templates2 = actionTemplate.getTemplates();
                  if (templates2 != null) {
                     for (ActionTemplateUnit actionTemplateUnit : (Iterable<ActionTemplateUnit>)(Iterable<?>)(templates2)) {
                        String text2 = String.valueOf(resource.getId());
                        if (resource.getVersion() != null) {
                           text2 = text2 + ":" + resource.getVersion();
                        }

                        actionTemplateUnit.setPath(text2);
                     }
                  }

                  items2.add(actionTemplate);
               }
               break;
            }
         }
      }

      if (items6.size() > 0) {
         VariableCategory variableCategory2 = (VariableCategory)items6.get(0);

         for (VariableCategory variableCategory3 : (Iterable<VariableCategory>)(Iterable<?>)(items6)) {
            if (!variableCategory3.equals(variableCategory2) && variableCategory3.getVariables() != null) {
               for (Variable variable : variableCategory3.getVariables()) {
                  variableCategory2.addVariable(variable);
               }
            }
         }

         VariableLibrary variableLibrary2 = new VariableLibrary();
         variableLibrary2.addVariableCategory(variableCategory2);
         items5.add(variableLibrary2);
      }

      if (!CopyLibPhaseHolder.isCopyLib()) {
         List builtInActions = this.builtInActionLibraryBuilder.getBuiltInActions();
         if (builtInActions.size() > 0) {
            ActionLibrary actionLibrary2 = new ActionLibrary();
            actionLibrary2.setSpringBeans(builtInActions);
            items4.add(actionLibrary2);
         }
      }

      return new ResourceLibrary(items5, items4, items3, items, items2, predefines);
   }

   private void registerTemplateResources(Collection<Library> libraries) {
      ResourceBase resourceBase = this.newResourceBase();

      for (Library library : libraries) {
         if (library.getType().equals(LibraryType.ConditionTemplate)) {
            resourceBase.addResource(library.getId(), library.getVersion());
         } else if (library.getType().equals(LibraryType.ActionTemplate)) {
            resourceBase.addResource(library.getId(), library.getVersion());
         }
      }

      ArrayList items = new ArrayList();

      for (Resource resource : resourceBase.getResources()) {
         String path = resource.getPath();
         if (resource.getVersion() != null) {
            path = path + ":" + resource.getVersion();
         }

         String content = resource.getContent();
         Element resource2 = this.parseResource(content);

         for (ResourceBuilder resourceBuilder : this.resourceBuilders) {
            if (resourceBuilder.support(resource2)) {
               Object objectValue = resourceBuilder.build(resource2, path);
               ResourceType type = resourceBuilder.getType();
               if (type.equals(ResourceType.ConditionTemplate)) {
                  ConditionTemplate conditionTemplate = (ConditionTemplate)objectValue;
                  if (conditionTemplate.getLibraries() != null) {
                     items.addAll(conditionTemplate.getLibraries());
                  }
               } else if (type.equals(ResourceType.ActionTemplate)) {
                  ActionTemplate actionTemplate = (ActionTemplate)objectValue;
                  if (actionTemplate.getLibraries() != null) {
                     items.addAll(actionTemplate.getLibraries());
                  }
               }
               break;
            }
         }
      }

      for (Library library2 : (Iterable<Library>)(Iterable<?>)(items)) {
         boolean flag = false;

         for (Library library3 : libraries) {
            if (library2.getId() == library3.getId()) {
               flag = true;
               break;
            }
         }

         if (!flag) {
            libraries.add(library2);
         }
      }
   }

   public void setBuiltInActionLibraryBuilder(BuiltInActionLibraryBuilder builtInActionLibraryBuilder) {
      this.builtInActionLibraryBuilder = builtInActionLibraryBuilder;
   }
}
