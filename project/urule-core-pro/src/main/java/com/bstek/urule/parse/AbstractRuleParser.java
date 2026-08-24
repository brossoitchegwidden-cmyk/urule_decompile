package com.bstek.urule.parse;

import com.bstek.urule.Configure;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.rule.Rule;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

public abstract class AbstractRuleParser<T> implements Parser<T> {
   protected LhsParser lhsParser;
   protected RhsParser rhsParser;
   protected OtherParser otherParser;

   public void parseRule(Rule rule, Element element) {
      rule.setName(element.attributeValue("name"));
      String text = element.attributeValue("salience");
      if (StringUtils.isNotEmpty(text)) {
         rule.setSalience(Integer.valueOf(text));
      }

      String text2 = element.attributeValue("effective-date");
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Configure.getDateFormat());
      if (StringUtils.isNotEmpty(text2)) {
         try {
            rule.setEffectiveDate(simpleDateFormat.parse(text2));
         } catch (ParseException parseException) {
            throw new RuleException(parseException);
         }
      }

      String text3 = element.attributeValue("expires-date");
      if (StringUtils.isNotEmpty(text3)) {
         try {
            rule.setExpiresDate(simpleDateFormat.parse(text3));
         } catch (ParseException parseException2) {
            throw new RuleException(parseException2);
         }
      }

      String text4 = element.attributeValue("enabled");
      if (StringUtils.isNotEmpty(text4)) {
         rule.setEnabled(Boolean.valueOf(text4));
      }

      String text5 = element.attributeValue("debug");
      if (StringUtils.isNotEmpty(text5)) {
         rule.setDebug(Boolean.valueOf(text5));
      }

      String text6 = element.attributeValue("loop");
      if (StringUtils.isNotEmpty(text6)) {
         rule.setLoop(Boolean.valueOf(text6));
      }

      if (StringUtils.isNotBlank(element.attributeValue("activation-group"))) {
         rule.setMutexGroup(element.attributeValue("activation-group"));
      } else {
         rule.setMutexGroup(element.attributeValue("mutex-group"));
      }

      if (StringUtils.isNotBlank(element.attributeValue("agenda-group"))) {
         rule.setPendedGroup(element.attributeValue("agenda-group"));
      } else {
         rule.setPendedGroup(element.attributeValue("pended-group"));
      }

      String text7 = element.attributeValue("auto-focus");
      if (StringUtils.isNotEmpty(text7)) {
         rule.setAutoFocus(Boolean.valueOf(text7));
      }

      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            if (this.lhsParser.support(element2.getName())) {
               rule.setLhs(this.lhsParser.parse(element2));
            } else if (this.rhsParser.support(element2.getName())) {
               rule.setRhs(this.rhsParser.parse(element2));
            } else if (this.otherParser.support(element2.getName())) {
               rule.setOther(this.otherParser.parse(element2));
            } else if (element2.getName().equals("remark")) {
               rule.setRemark(element2.getText());
            }
         }
      }
   }

   public void setLhsParser(LhsParser lhsParser) {
      this.lhsParser = lhsParser;
   }

   public void setRhsParser(RhsParser rhsParser) {
      this.rhsParser = rhsParser;
   }

   public void setOtherParser(OtherParser otherParser) {
      this.otherParser = otherParser;
   }
}
