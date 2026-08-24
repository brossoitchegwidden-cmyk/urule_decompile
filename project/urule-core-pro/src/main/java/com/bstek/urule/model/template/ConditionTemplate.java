package com.bstek.urule.model.template;

import com.bstek.urule.model.rule.Library;
import java.util.ArrayList;
import java.util.List;

public class ConditionTemplate {
   private List<Library> libraries;
   private List<ConditionTemplateUnit> templates;

   public void addLibrary(Library library) {
      if (this.libraries == null) {
         this.libraries = new ArrayList<>();
      }

      this.libraries.add(library);
   }

   public List<Library> getLibraries() {
      return this.libraries;
   }

   public void setLibraries(List<Library> libraries) {
      this.libraries = libraries;
   }

   public List<ConditionTemplateUnit> getTemplates() {
      return this.templates;
   }

   public void setTemplates(List<ConditionTemplateUnit> templates) {
      this.templates = templates;
   }
}
