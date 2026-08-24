package com.bstek.urule.parse;

import com.bstek.urule.builder.RulesRebuilder;
import com.bstek.urule.model.rule.Library;
import com.bstek.urule.model.rule.ParentFile;
import com.bstek.urule.model.rule.PredefineGroupDefinition;
import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.model.rule.RuleSet;
import java.util.ArrayList;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

public class RuleSetParser extends LibrariesParser<RuleSet> {
   private RuleParser ruleParser;
   private PredefinesParser predefinesParser;
   private LoopRuleParser loopRuleParser;
   private RulesRebuilder rulesRebuilder;

   public RuleSet parse(Element element) {
      RuleSet ruleSet = new RuleSet();
      String text = element.attributeValue("alone");
      if (StringUtils.isNotBlank(text)) {
         ruleSet.setAlone(Boolean.valueOf(text));
      }

      String text2 = element.attributeValue("debug");
      if (StringUtils.isNotBlank(text2)) {
         ruleSet.setDebug(Boolean.valueOf(text2));
      }

      ArrayList items = new ArrayList();
      ruleSet.setParents(items);
      String text3 = element.attributeValue("parent");
      if (StringUtils.isNotBlank(text3)) {
         long longValue = Long.valueOf(text3);
         items.add(new ParentFile(longValue, "", null));
      }

      ArrayList items2 = new ArrayList();

      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            String name = element2.getName();
            Library library = this.parseLibrary(element2);
            if (library != null) {
               ruleSet.addLibrary(library);
            } else if (this.ruleParser.support(name)) {
               items2.add(this.ruleParser.parse(element2));
            } else if (name.equals("quick-test-data")) {
               String textTrim = element2.getTextTrim();
               ruleSet.setQuickTestData(textTrim);
            } else if (this.loopRuleParser.support(name)) {
               items2.add(this.loopRuleParser.parse(element2));
            } else if (this.predefinesParser.support(name)) {
               PredefineGroupDefinition predefineGroupDefinition = this.predefinesParser.parse(element2);
               ruleSet.setPredefineGroup(predefineGroupDefinition);
            }

            if (name.equals("remark")) {
               ruleSet.setRemark(element2.getText());
            } else if (name.contentEquals("extends-file") || name.contentEquals("parent")) {
               String text4 = element2.attributeValue("path");
               long longValue2 = Long.valueOf(element2.attributeValue("id"));
               String text5 = element2.attributeValue("version");
               items.add(new ParentFile(longValue2, text4, text5));
            }
         }
      }

      if (ruleSet.isDebug()) {
         for (Rule rule : (Iterable<Rule>)(Iterable<?>)(items2)) {
            if (rule.getDebug() == null) {
               rule.setDebug(true);
               rule.setDebugFromGlobal(true);
            }
         }
      }

      ruleSet.setRules(items2);
      PredefineGroupDefinition predefineGroup = ruleSet.getPredefineGroup();
      if (predefineGroup != null) {
         this.rulesRebuilder.rebuildRules(ruleSet.getLibraries(), items2, predefineGroup.getPredefines());
      } else {
         this.rulesRebuilder.rebuildRules(ruleSet.getLibraries(), items2, null);
      }

      return ruleSet;
   }

   public void setRulesRebuilder(RulesRebuilder rulesRebuilder) {
      this.rulesRebuilder = rulesRebuilder;
   }

   @Override
   public boolean support(String name) {
      return name.equals("rule-set");
   }

   public void setPredefinesParser(PredefinesParser predefinesParser) {
      this.predefinesParser = predefinesParser;
   }

   public void setRuleParser(RuleParser ruleParser) {
      this.ruleParser = ruleParser;
   }

   public void setLoopRuleParser(LoopRuleParser loopRuleParser) {
      this.loopRuleParser = loopRuleParser;
   }
}
