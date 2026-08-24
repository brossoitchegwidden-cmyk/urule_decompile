package com.bstek.urule.model.rete;

import com.bstek.urule.model.Node;
import com.bstek.urule.runtime.rete.Path;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Map;

public class Line {
   private int fromNodeId;
   private int toNodeId;
   @JsonIgnore
   private ReteNode from;
   @JsonIgnore
   private ReteNode to;

   public Line() {
   }

   public Line(ReteNode from, ReteNode to) {
      this.from = from;
      this.to = to;
      this.fromNodeId = from.getId();
      this.toNodeId = to.getId();
   }

   public void setTo(ReteNode to) {
      this.to = to;
   }

   public Node getFrom() {
      return this.from;
   }

   public void setFrom(ReteNode from) {
      this.from = from;
   }

   public Node getTo() {
      return this.to;
   }

   public Path newPath(Map<Object, Object> context) {
      return new Path(this.to.newActivity(context));
   }

   public int getFromNodeId() {
      return this.fromNodeId;
   }

   public void setFromNodeId(int fromNodeId) {
      this.fromNodeId = fromNodeId;
   }

   public int getToNodeId() {
      return this.toNodeId;
   }

   public void setToNodeId(int toNodeId) {
      this.toNodeId = toNodeId;
   }
}
