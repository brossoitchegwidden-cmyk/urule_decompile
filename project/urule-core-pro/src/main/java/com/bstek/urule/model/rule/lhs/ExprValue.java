package com.bstek.urule.model.rule.lhs;

import java.util.List;

public class ExprValue {
   private int total = 0;
   private int match = 0;
   private int notMatch = 0;
   private List<Object> facts;

   public int getTotal() {
      return this.total;
   }

   public void setTotal(int total) {
      this.total = total;
   }

   public int getMatch() {
      return this.match;
   }

   public void setMatch(int match) {
      this.match = match;
   }

   public int getNotMatch() {
      return this.notMatch;
   }

   public void setNotMatch(int notMatch) {
      this.notMatch = notMatch;
   }

   public List<Object> getFacts() {
      return this.facts;
   }

   public void setFacts(List<Object> facts) {
      this.facts = facts;
   }
}
