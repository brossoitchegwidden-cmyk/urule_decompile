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

   public static ArithmeticType parse(String type) {
      if (type.equals("+")) {
         return Add;
      } else if (type.equals("-")) {
         return Sub;
      } else if (type.equals("*")) {
         return Mul;
      } else if (type.equals("/")) {
         return Div;
      } else if (type.equals("%")) {
         return Mod;
      } else if (type.equals(">")) {
         return Gt;
      } else if (type.equals("<")) {
         return Lt;
      } else if (type.equals("≥")) {
         return Gte;
      } else if (type.equals("≤")) {
         return Lte;
      } else if (type.equals("==")) {
         return Eq;
      } else if (type.equals("≠")) {
         return NotEq;
      } else {
         throw new RuleException("Unsupport arithmetic type [" + type + "]");
      }
   }
}
