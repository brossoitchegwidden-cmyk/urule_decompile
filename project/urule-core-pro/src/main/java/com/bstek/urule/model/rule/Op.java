package com.bstek.urule.model.rule;

import com.bstek.urule.LocaleHolder;
import com.bstek.urule.exception.RuleException;

public enum Op {
   Equals {
      @Override
      public String toString() {
         return LocaleHolder.isEnglish() ? "Equals" : "等于";
      }
   },
   EqualsIgnoreCase {
      @Override
      public String toString() {
         return LocaleHolder.isEnglish() ? "EqualsIgnoreCase" : "等于(不分大小写)";
      }
   },
   NotEquals {
      @Override
      public String toString() {
         return LocaleHolder.isEnglish() ? "NotEquals" : "不等于";
      }
   },
   NotEqualsIgnoreCase {
      @Override
      public String toString() {
         return LocaleHolder.isEnglish() ? "NotEqualsIgnoreCase" : "不等于(不分大小写)";
      }
   },
   LessThen {
      @Override
      public String toString() {
         return LocaleHolder.isEnglish() ? "LessThen" : "小于";
      }
   },
   LessThenEquals {
      @Override
      public String toString() {
         return LocaleHolder.isEnglish() ? "LessThenEquals" : "小于等于";
      }
   },
   GreaterThen {
      @Override
      public String toString() {
         return LocaleHolder.isEnglish() ? "GreaterThen" : "大于";
      }
   },
   GreaterThenEquals {
      @Override
      public String toString() {
         return LocaleHolder.isEnglish() ? "GreaterThenEquals" : "大于等于";
      }
   },
   In {
      @Override
      public String toString() {
         return LocaleHolder.isEnglish() ? "In" : "在集合中";
      }
   },
   NotIn {
      @Override
      public String toString() {
         return LocaleHolder.isEnglish() ? "NotIn" : "不在集合中";
      }
   },
   StartWith {
      @Override
      public String toString() {
         return LocaleHolder.isEnglish() ? "StartWith" : "开始于";
      }
   },
   NotStartWith {
      @Override
      public String toString() {
         return LocaleHolder.isEnglish() ? "NotStartWith" : "不开始于";
      }
   },
   EndWith {
      @Override
      public String toString() {
         return LocaleHolder.isEnglish() ? "EndWith" : "结束于";
      }
   },
   NotEndWith {
      @Override
      public String toString() {
         return LocaleHolder.isEnglish() ? "NotEndWith" : "不结束于";
      }
   },
   Null {
      @Override
      public String toString() {
         return LocaleHolder.isEnglish() ? "Null" : "为空";
      }
   },
   NotNull {
      @Override
      public String toString() {
         return LocaleHolder.isEnglish() ? "NotNull" : "不为空";
      }
   },
   Match {
      @Override
      public String toString() {
         return LocaleHolder.isEnglish() ? "Match" : "匹配";
      }
   },
   NotMatch {
      @Override
      public String toString() {
         return LocaleHolder.isEnglish() ? "NotMatch" : "不匹配";
      }
   },
   Contain {
      @Override
      public String toString() {
         return LocaleHolder.isEnglish() ? "Contain" : "包含";
      }
   },
   NotContain {
      @Override
      public String toString() {
         return LocaleHolder.isEnglish() ? "NotContain" : "不包含";
      }
   },
   Between {
      @Override
      public String toString() {
         return LocaleHolder.isEnglish() ? "Between" : "等在值区间于";
      }
   },
   NotBetween {
      @Override
      public String toString() {
         return LocaleHolder.isEnglish() ? "NotBetween" : "不在值区间";
      }
   };

   Op() {
   }

   public static Op parse(String var0) {
      if (var0.equals(">")) {
         return GreaterThen;
      } else if (var0.equals(">=")) {
         return GreaterThenEquals;
      } else if (var0.equals("==")) {
         return Equals;
      } else if (var0.equals("EqualsIgnoreCase")) {
         return EqualsIgnoreCase;
      } else if (var0.equals("!=")) {
         return NotEquals;
      } else if (var0.equals("NotEqualsIgnoreCase")) {
         return NotEqualsIgnoreCase;
      } else if (var0.equals("<")) {
         return LessThen;
      } else if (var0.equals("<=")) {
         return LessThenEquals;
      } else if (var0.equals("In")) {
         return In;
      } else if (var0.equals("NotIn")) {
         return NotIn;
      } else if (var0.equals("StartWith")) {
         return StartWith;
      } else if (var0.equals("NotStartWidth")) {
         return NotStartWith;
      } else if (var0.equals("EndWith")) {
         return EndWith;
      } else if (var0.equals("NotEndWith")) {
         return NotEndWith;
      } else if (var0.equals("Null")) {
         return Null;
      } else if (var0.equals("Notnull")) {
         return NotNull;
      } else if (var0.equals("Match")) {
         return Match;
      } else if (var0.equals("NotMatch")) {
         return NotMatch;
      } else if (var0.equals("Contain")) {
         return Contain;
      } else if (var0.equals("NotContain")) {
         return NotContain;
      } else if (var0.equals("Between")) {
         return Between;
      } else if (var0.equals("NotBetween")) {
         return NotBetween;
      } else {
         throw new RuleException("Unsupport op " + var0 + "");
      }
   }
}
