package com.bstek.urule.console.editor.diagram;

import java.util.List;
import java.util.Map;

public class DiagramContext {
   private int a;
   private List b;
   private Map c;

   public DiagramContext(List var1, Map var2) {
      this.b = var1;
      this.c = var2;
   }

   public List getEdges() {
      return this.b;
   }

   public void addEdge(Edge var1) {
      this.b.add(var1);
   }

   public Map getNodeMap() {
      return this.c;
   }

   public void setNodeMap(Map var1) {
      this.c = var1;
   }

   public int nextId() {
      ++this.a;
      return this.a;
   }
}
