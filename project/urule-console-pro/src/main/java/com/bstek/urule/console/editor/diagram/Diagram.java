package com.bstek.urule.console.editor.diagram;

import java.util.List;

public class Diagram {
   private List edges;
   private NodeInfo rootNode;
   private int width;
   private int height;

   public Diagram(List edges, NodeInfo rootNode) {
      this.edges = edges;
      this.rootNode = rootNode;
   }

   public List getEdges() {
      return this.edges;
   }

   public void setEdges(List edges) {
      this.edges = edges;
   }

   public NodeInfo getRootNode() {
      return this.rootNode;
   }

   public void setRootNode(NodeInfo rootNode) {
      this.rootNode = rootNode;
   }

   public int getWidth() {
      return this.width;
   }

   public void setWidth(int width) {
      this.width = width;
   }

   public int getHeight() {
      return this.height;
   }

   public void setHeight(int height) {
      this.height = height;
   }
}
