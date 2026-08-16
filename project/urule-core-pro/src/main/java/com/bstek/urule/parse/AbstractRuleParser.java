package com.bstek.urule.parse;

import com.bstek.urule.Configure;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.rule.Rule;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

public abstract class AbstractRuleParser<T> implements Parser<T> {
   protected LhsParser a;
   protected RhsParser b;
   protected OtherParser c;

   public void parseRule(Rule var1, Element var2) {
      var1.setName(var2.attributeValue("name"));
      String var3 = var2.attributeValue("salience");
      if (StringUtils.isNotEmpty(var3)) {
         var1.setSalience(Integer.valueOf(var3));
      }

      String var4 = var2.attributeValue("effective-date");
      SimpleDateFormat var5 = new SimpleDateFormat(Configure.getDateFormat());
      if (StringUtils.isNotEmpty(var4)) {
         try {
            var1.setEffectiveDate(var5.parse(var4));
         } catch (ParseException var15) {
            throw new RuleException(var15);
         }
      }

      String var6 = var2.attributeValue("expires-date");
      if (StringUtils.isNotEmpty(var6)) {
         try {
            var1.setExpiresDate(var5.parse(var6));
         } catch (ParseException var14) {
            throw new RuleException(var14);
         }
      }

      String var7 = var2.attributeValue("enabled");
      if (StringUtils.isNotEmpty(var7)) {
         var1.setEnabled(Boolean.valueOf(var7));
      }

      String var8 = var2.attributeValue("debug");
      if (StringUtils.isNotEmpty(var8)) {
         var1.setDebug(Boolean.valueOf(var8));
      }

      String var9 = var2.attributeValue("loop");
      if (StringUtils.isNotEmpty(var9)) {
         var1.setLoop(Boolean.valueOf(var9));
      }

      if (StringUtils.isNotBlank(var2.attributeValue("activation-group"))) {
         var1.setMutexGroup(var2.attributeValue("activation-group"));
      } else {
         var1.setMutexGroup(var2.attributeValue("mutex-group"));
      }

      if (StringUtils.isNotBlank(var2.attributeValue("agenda-group"))) {
         var1.setPendedGroup(var2.attributeValue("agenda-group"));
      } else {
         var1.setPendedGroup(var2.attributeValue("pended-group"));
      }

      String var10 = var2.attributeValue("auto-focus");
      if (StringUtils.isNotEmpty(var10)) {
         var1.setAutoFocus(Boolean.valueOf(var10));
      }

      for (Object var12 : var2.elements()) {
         if (var12 != null && var12 instanceof Element) {
            Element var13 = (Element)var12;
            if (this.a.support(var13.getName())) {
               var1.setLhs(this.a.parse(var13));
            } else if (this.b.support(var13.getName())) {
               var1.setRhs(this.b.parse(var13));
            } else if (this.c.support(var13.getName())) {
               var1.setOther(this.c.parse(var13));
            } else if (var13.getName().equals("remark")) {
               var1.setRemark(var13.getText());
            }
         }
      }
   }

   public void setLhsParser(LhsParser var1) {
      this.a = var1;
   }

   public void setRhsParser(RhsParser var1) {
      this.b = var1;
   }

   public void setOtherParser(OtherParser var1) {
      this.c = var1;
   }
}
