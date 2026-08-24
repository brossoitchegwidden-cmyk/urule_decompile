package com.bstek.urule.console.editor.ruleset;

import com.bstek.urule.model.library.ResourceLibrary;
import com.bstek.urule.model.library.action.ActionLibrary;
import com.bstek.urule.model.library.action.SpringBean;
import com.bstek.urule.model.library.constant.ConstantCategory;
import com.bstek.urule.model.library.variable.VariableCategory;
import com.bstek.urule.model.rule.LibraryType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LibraryIndex {
   private Map variableLibrariesByCategory = new HashMap();
   private Map constantLibrariesByCategory = new HashMap();
   private Map actionLibrariesByBeanName = new HashMap();
   private Map templatesById = new HashMap();

   public void buildIndex(ResourceLibrary library, Map libPath, LibraryType type) {
      List variableCategories = library.getVariableCategories();
      List constantCategories = library.getConstantCategories();
      List actionLibraries = library.getActionLibraries();
      if (!type.equals(LibraryType.Variable) && !type.equals(LibraryType.Parameter)) {
         if (type.equals(LibraryType.Constant)) {
            for(ConstantCategory constantCategory : (Iterable<ConstantCategory>)(Iterable<?>)(constantCategories)) {
               this.constantLibrariesByCategory.put(constantCategory.getLabel(), libPath);
            }
         } else if (type.equals(LibraryType.Action)) {
            for(ActionLibrary actionLibrary : (Iterable<ActionLibrary>)(Iterable<?>)(actionLibraries)) {
               for(SpringBean springBean : actionLibrary.getSpringBeans()) {
                  this.actionLibrariesByBeanName.put(springBean.getName(), libPath);
               }
            }
         }
      } else {
         for(VariableCategory variableCategory : (Iterable<VariableCategory>)(Iterable<?>)(variableCategories)) {
            this.variableLibrariesByCategory.put(variableCategory.getName(), libPath);
         }
      }

   }

   public void buildTemplateIndex(String id, Map map) {
      this.templatesById.put(id, map);
   }

   public LibInfo variableContains(String category) {
      Map valuesByKey = (Map)this.variableLibrariesByCategory.get(category);
      if (valuesByKey == null) {
         return null;
      } else {
         String text = valuesByKey.get("path").toString();
         return text.startsWith("ParameterLibrary") ? new LibInfo("parameters", valuesByKey) : new LibInfo("variables", valuesByKey);
      }
   }

   public LibInfo constantContains(String category) {
      Map valuesByKey = (Map)this.constantLibrariesByCategory.get(category);
      return valuesByKey == null ? null : new LibInfo("constants", valuesByKey);
   }

   public LibInfo actionContains(String category) {
      Map valuesByKey = (Map)this.actionLibrariesByBeanName.get(category);
      return valuesByKey == null ? null : new LibInfo("actions", valuesByKey);
   }

   public LibInfo templateContains(String id) {
      Map valuesByKey = (Map)this.templatesById.get(id);
      if (valuesByKey == null) {
         return null;
      } else {
         String text = valuesByKey.get("path").toString();
         return text.startsWith("ConditionTemplate") ? new LibInfo("conditionTemplates", valuesByKey) : new LibInfo("actionTemplates", valuesByKey);
      }
   }
}
