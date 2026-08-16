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
   private Map a = new HashMap();
   private Map b = new HashMap();
   private Map c = new HashMap();
   private Map d = new HashMap();

   public void buildIndex(ResourceLibrary var1, Map var2, LibraryType var3) {
      List var4 = var1.getVariableCategories();
      List var5 = var1.getConstantCategories();
      List var6 = var1.getActionLibraries();
      if (!var3.equals(LibraryType.Variable) && !var3.equals(LibraryType.Parameter)) {
         if (var3.equals(LibraryType.Constant)) {
            for(ConstantCategory var13 : (Iterable<ConstantCategory>)(Iterable<?>)(var5)) {
               this.b.put(var13.getLabel(), var2);
            }
         } else if (var3.equals(LibraryType.Action)) {
            for(ActionLibrary var14 : (Iterable<ActionLibrary>)(Iterable<?>)(var6)) {
               for(SpringBean var10 : var14.getSpringBeans()) {
                  this.c.put(var10.getName(), var2);
               }
            }
         }
      } else {
         for(VariableCategory var8 : (Iterable<VariableCategory>)(Iterable<?>)(var4)) {
            this.a.put(var8.getName(), var2);
         }
      }

   }

   public void buildTemplateIndex(String var1, Map var2) {
      this.d.put(var1, var2);
   }

   public LibInfo variableContains(String var1) {
      Map var2 = (Map)this.a.get(var1);
      if (var2 == null) {
         return null;
      } else {
         String var3 = var2.get("path").toString();
         return var3.startsWith("ParameterLibrary") ? new LibInfo("parameters", var2) : new LibInfo("variables", var2);
      }
   }

   public LibInfo constantContains(String var1) {
      Map var2 = (Map)this.b.get(var1);
      return var2 == null ? null : new LibInfo("constants", var2);
   }

   public LibInfo actionContains(String var1) {
      Map var2 = (Map)this.c.get(var1);
      return var2 == null ? null : new LibInfo("actions", var2);
   }

   public LibInfo templateContains(String var1) {
      Map var2 = (Map)this.d.get(var1);
      if (var2 == null) {
         return null;
      } else {
         String var3 = var2.get("path").toString();
         return var3.startsWith("ConditionTemplate") ? new LibInfo("conditionTemplates", var2) : new LibInfo("actionTemplates", var2);
      }
   }
}
