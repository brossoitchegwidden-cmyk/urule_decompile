package com.bstek.urule.parse;

import com.bstek.urule.model.rule.loop.LoopEnd;
import com.bstek.urule.model.rule.loop.LoopRule;
import com.bstek.urule.model.rule.loop.LoopRuleUnit;
import com.bstek.urule.model.rule.loop.LoopStart;
import com.bstek.urule.model.rule.loop.LoopTarget;
import com.bstek.urule.model.rule.loop.LoopTargetType;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

/** Parses a loop-rule XML element, including its target and execution units. */
public class LoopRuleParser extends AbstractRuleParser<LoopRule> {
   private ValueParser valueParser;

   public LoopRule parse(Element element) {
      LoopRule loopRule = new LoopRule();
      this.parseRule(loopRule, element);
      LoopStart loopStart = new LoopStart();
      loopRule.setLoopStart(loopStart);
      LoopEnd loopEnd = new LoopEnd();
      loopRule.setLoopEnd(loopEnd);
      String text = element.attributeValue("loop-target-type");
      if (StringUtils.isNotBlank(text)) {
         loopRule.setLoopTargetType(LoopTargetType.valueOf(text));
      }

      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element childElement = (Element)objectValue;
            String name = childElement.getName();
            if (name.equals("loop-start")) {
               loopStart.setActions(this.rhsParser.parseActions(childElement));
            } else if (name.equals("loop-end")) {
               loopEnd.setActions(this.rhsParser.parseActions(childElement));
            } else if (name.equals("loop-target")) {
               LoopTarget loopTarget = new LoopTarget();
               loopRule.setLoopTarget(loopTarget);

               for (Object targetChild : childElement.elements()) {
                  if (targetChild != null && targetChild instanceof Element) {
                     Element valueElement = (Element)targetChild;
                     if (this.valueParser.support(valueElement.getName())) {
                        loopTarget.setValue(this.valueParser.parse(valueElement));
                        break;
                     }
                  }
               }
            } else if (name.equals("units")) {
               loopRule.setUnits(this.parseUnits(childElement));
            }
         }
      }

      this.ensureDefaultUnit(loopRule);
      return loopRule;
   }

   private void ensureDefaultUnit(LoopRule loopRule) {
      if (loopRule.getUnits() == null || loopRule.getUnits().size() <= 0) {
         LoopRuleUnit loopRuleUnit = new LoopRuleUnit();
         loopRuleUnit.setLhs(loopRule.getLhs());
         loopRuleUnit.setOther(loopRule.getOther());
         loopRuleUnit.setRhs(loopRule.getRhs());
         loopRule.setLhs(null);
         loopRule.setRhs(null);
         loopRule.setOther(null);
         List<LoopRuleUnit> units = new ArrayList<>();
         units.add(loopRuleUnit);
         loopRule.setUnits(units);
      }
   }

   private List<LoopRuleUnit> parseUnits(Element element) {
      List<LoopRuleUnit> units = new ArrayList<>();

      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element unitElement = (Element)objectValue;
            if (unitElement.getName().equals("unit")) {
               LoopRuleUnit loopRuleUnit = this.parseUnit(unitElement);
               units.add(loopRuleUnit);
            }
         }
      }

      return units;
   }

   private LoopRuleUnit parseUnit(Element element) {
      LoopRuleUnit loopRuleUnit = new LoopRuleUnit();
      loopRuleUnit.setName(element.attributeValue("name"));

      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element childElement = (Element)objectValue;
            if (this.lhsParser.support(childElement.getName())) {
               loopRuleUnit.setLhs(this.lhsParser.parse(childElement));
            } else if (this.rhsParser.support(childElement.getName())) {
               loopRuleUnit.setRhs(this.rhsParser.parse(childElement));
            } else if (this.otherParser.support(childElement.getName())) {
               loopRuleUnit.setOther(this.otherParser.parse(childElement));
            }
         }
      }

      return loopRuleUnit;
   }

   public void setValueParser(ValueParser valueParser) {
      this.valueParser = valueParser;
   }

   @Override
   public boolean support(String name) {
      return name.equals("loop-rule");
   }
}
