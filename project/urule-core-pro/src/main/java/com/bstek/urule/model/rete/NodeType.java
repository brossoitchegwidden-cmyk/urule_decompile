package com.bstek.urule.model.rete;

public enum NodeType {
   and,
   or,
   met,
   criteria,
   objectType,
   terminal;

   public static ReteNode newReteNodeInstance(NodeType var0) {
      switch (var0) {
         case and:
            return new AndNode();
         case or:
            return new OrNode();
         case met:
            return new MetNode();
         case criteria:
            return new CriteriaNode();
         case objectType:
            return new ObjectTypeNode();
         case terminal:
            return new TerminalNode();
         default:
            return null;
      }
   }
}
