package com.bstek.urule.parse;

import com.bstek.urule.builder.RulesRebuilder;
import com.bstek.urule.model.library.ResourceLibrary;
import com.bstek.urule.model.rule.Library;
import com.bstek.urule.model.template.ConditionTemplate;
import com.bstek.urule.model.template.ConditionTemplateUnit;
import java.util.ArrayList;
import java.util.List;
import org.dom4j.Element;

public class ConditionTemplateParser extends LibrariesParser<ConditionTemplate> {
   private RulesRebuilder rulesRebuilder;
   private LhsParser lhsParser;

   public ConditionTemplate parse(Element element) {
      ConditionTemplate conditionTemplate = new ConditionTemplate();
      ArrayList items = new ArrayList();
      conditionTemplate.setTemplates(items);

      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            String name = element2.getName();
            Library library = this.parseLibrary(element2);
            if (library != null) {
               conditionTemplate.addLibrary(library);
            } else if (name.equals("template")) {
               items.add(this.resolveConditionTemplateUnit(element2));
            }
         }
      }

      List libraries = conditionTemplate.getLibraries();
      if (libraries != null) {
         ResourceLibrary resourceLibrary = this.rulesRebuilder.getResourceLibraryBuilder().buildResourceLibrary(libraries, null);

         for (ConditionTemplateUnit conditionTemplateUnit : conditionTemplate.getTemplates()) {
            this.rulesRebuilder.rebuildCriterion(conditionTemplateUnit.getCriterion(), resourceLibrary, false);
         }
      }

      return conditionTemplate;
   }

   private ConditionTemplateUnit resolveConditionTemplateUnit(Element element) {
      ConditionTemplateUnit conditionTemplateUnit = new ConditionTemplateUnit();
      conditionTemplateUnit.setId(element.attributeValue("id"));
      conditionTemplateUnit.setName(element.attributeValue("name"));
      conditionTemplateUnit.setCriterion(this.lhsParser.parseCriterion(element));
      return conditionTemplateUnit;
   }

   @Override
   public boolean support(String name) {
      return name.equals("templates");
   }

   public void setLhsParser(LhsParser lhsParser) {
      this.lhsParser = lhsParser;
   }

   public void setRulesRebuilder(RulesRebuilder rulesRebuilder) {
      this.rulesRebuilder = rulesRebuilder;
   }
}
