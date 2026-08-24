package com.bstek.urule.console.editor.diagram;

public class Edge {
   private int from;
   private int to;

   public Edge(int from, int to) {
      this.from = from;
      this.to = to;
   }

   public int getFrom() {
      return this.from;
   }

   public void setFrom(int from) {
      this.from = from;
   }

   public int getTo() {
      return this.to;
   }

   public void setTo(int to) {
      this.to = to;
   }
}
