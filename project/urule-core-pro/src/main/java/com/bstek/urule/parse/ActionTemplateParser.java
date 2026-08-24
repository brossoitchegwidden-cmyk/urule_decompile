package com.bstek.urule.parse;

import com.bstek.urule.action.Action;
import com.bstek.urule.builder.RulesRebuilder;
import com.bstek.urule.model.library.ResourceLibrary;
import com.bstek.urule.model.rule.Library;
import com.bstek.urule.model.template.ActionTemplate;
import com.bstek.urule.model.template.ActionTemplateUnit;
import java.util.ArrayList;
import java.util.List;
import org.dom4j.Element;

public class ActionTemplateParser extends LibrariesParser<ActionTemplate> {
   private RhsParser rhsParser;
   private RulesRebuilder rulesRebuilder;

   public ActionTemplate parse(Element element) {
      ActionTemplate actionTemplate = new ActionTemplate();
      ArrayList items = new ArrayList();
      actionTemplate.setTemplates(items);

      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            String name = element2.getName();
            Library library = this.parseLibrary(element2);
            if (library != null) {
               actionTemplate.addLibrary(library);
            } else if (name.equals("template")) {
               items.add(this.resolveActionTemplateUnit(element2));
            }
         }
      }

      List libraries = actionTemplate.getLibraries();
      if (libraries != null) {
         ResourceLibrary resourceLibrary = this.rulesRebuilder.getResourceLibraryBuilder().buildResourceLibrary(libraries, null);

         for (ActionTemplateUnit actionTemplateUnit : actionTemplate.getTemplates()) {
            List actions = actionTemplateUnit.getActions();
            if (actions != null) {
               for (Action action : (Iterable<Action>)(Iterable<?>)(actions)) {
                  this.rulesRebuilder.rebuildAction(action, resourceLibrary, false);
               }
            }
         }
      }

      return actionTemplate;
   }

   private ActionTemplateUnit resolveActionTemplateUnit(Element element) {
      ActionTemplateUnit actionTemplateUnit = new ActionTemplateUnit();
      actionTemplateUnit.setId(element.attributeValue("id"));
      actionTemplateUnit.setName(element.attributeValue("name"));
      actionTemplateUnit.setActions(this.rhsParser.parseActions(element));
      return actionTemplateUnit;
   }

   @Override
   public boolean support(String name) {
      return name.equals("action-templates");
   }

   public void setRhsParser(RhsParser rhsParser) {
      this.rhsParser = rhsParser;
   }

   public void setRulesRebuilder(RulesRebuilder rulesRebuilder) {
      this.rulesRebuilder = rulesRebuilder;
   }
}
