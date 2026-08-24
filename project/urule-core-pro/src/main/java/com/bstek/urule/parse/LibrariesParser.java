package com.bstek.urule.parse;

import com.bstek.urule.model.rule.Library;
import com.bstek.urule.model.rule.LibraryType;
import org.dom4j.Element;

public abstract class LibrariesParser<T> implements Parser<T> {
   protected Library parseLibrary(Element ele) {
      String name = ele.getName();
      LibraryType libraryType = null;
      if (name.equals("import-variable-library")) {
         libraryType = LibraryType.Variable;
      } else if (name.equals("import-constant-library")) {
         libraryType = LibraryType.Constant;
      } else if (name.equals("import-action-library")) {
         libraryType = LibraryType.Action;
      } else if (name.equals("import-parameter-library")) {
         libraryType = LibraryType.Parameter;
      } else if (name.contentEquals("condition-template-library")) {
         libraryType = LibraryType.ConditionTemplate;
      } else if (name.contentEquals("action-template-library")) {
         libraryType = LibraryType.ActionTemplate;
      }

      if (libraryType == null) {
         return null;
      }

      String text = ele.attributeValue("path");
      String text2 = ele.attributeValue("version");
      String text3 = ele.attributeValue("id");
      long longValue = Long.valueOf(text3);
      return new Library(longValue, text, text2, libraryType);
   }
}
