package com.bstek.urule.parse;

import com.bstek.urule.model.rule.Library;
import com.bstek.urule.model.rule.LibraryType;
import org.dom4j.Element;

public abstract class LibrariesParser<T> implements Parser<T> {
   protected Library a(Element var1) {
      String var2 = var1.getName();
      LibraryType var3 = null;
      if (var2.equals("import-variable-library")) {
         var3 = LibraryType.Variable;
      } else if (var2.equals("import-constant-library")) {
         var3 = LibraryType.Constant;
      } else if (var2.equals("import-action-library")) {
         var3 = LibraryType.Action;
      } else if (var2.equals("import-parameter-library")) {
         var3 = LibraryType.Parameter;
      } else if (var2.contentEquals("condition-template-library")) {
         var3 = LibraryType.ConditionTemplate;
      } else if (var2.contentEquals("action-template-library")) {
         var3 = LibraryType.ActionTemplate;
      }

      if (var3 == null) {
         return null;
      }

      String var4 = var1.attributeValue("path");
      String var5 = var1.attributeValue("version");
      String var6 = var1.attributeValue("id");
      long var7 = Long.valueOf(var6);
      return new Library(var7, var4, var5, var3);
   }
}
