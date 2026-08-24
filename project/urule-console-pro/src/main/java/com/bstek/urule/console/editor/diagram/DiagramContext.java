package com.bstek.urule.console.editor.diagram;

import java.util.List;
import java.util.Map;

public class DiagramContext {
   private int lastNodeId;
   private List edges;
   private Map nodeMap;

   public DiagramContext(List edges, Map nodeMap) {
      this.edges = edges;
      this.nodeMap = nodeMap;
   }

   public List getEdges() {
      return this.edges;
   }

   public void addEdge(Edge edge) {
      this.edges.add(edge);
   }

   public Map getNodeMap() {
      return this.nodeMap;
   }

   public void setNodeMap(Map nodeMap) {
      this.nodeMap = nodeMap;
   }

   public int nextId() {
      ++this.lastNodeId;
      return this.lastNodeId;
   }
}
