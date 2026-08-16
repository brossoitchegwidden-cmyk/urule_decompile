package com.bstek.urule.model.rule;

import com.bstek.urule.exception.RuleException;

public enum ArithmeticType {
   Add {
      @Override
      public String toString() {
         return "+";
      }
   },
   Sub {
      @Override
      public String toString() {
         return "-";
      }
   },
   Mul {
      @Override
      public String toString() {
         return "*";
      }
   },
   Div {
      @Override
      public String toString() {
         return "/";
      }
   },
   Mod {
      @Override
      public String toString() {
         return "%";
      }
   },
   Gt {
      @Override
      public String toString() {
         return ">";
      }
   },
   Lt {
      @Override
      public String toString() {
         return "<";
      }
   },
   Gte {
      @Override
      public String toString() {
         return "≥";
      }
   },
   Lte {
      @Override
      public String toString() {
         return "≤";
      }
   },
   Eq {
      @Override
      public String toString() {
         return "==";
      }
   },
   NotEq {
      @Override
      public String toString() {
         return "≠";
      }
   };

   ArithmeticType() {
   }

   public static ArithmeticType parse(String var0) {
      if (var0.equals("+")) {
         return Add;
      } else if (var0.equals("-")) {
         return Sub;
      } else if (var0.equals("*")) {
         return Mul;
      } else if (var0.equals("/")) {
         return Div;
      } else if (var0.equals("%")) {
         return Mod;
      } else if (var0.equals(">")) {
         return Gt;
      } else if (var0.equals("<")) {
         return Lt;
      } else if (var0.equals("≥")) {
         return Gte;
      } else if (var0.equals("≤")) {
         return Lte;
      } else if (var0.equals("==")) {
         return Eq;
      } else if (var0.equals("≠")) {
         return NotEq;
      } else {
         throw new RuleException("Unsupport arithmetic type [" + var0 + "]");
      }
   }
}
