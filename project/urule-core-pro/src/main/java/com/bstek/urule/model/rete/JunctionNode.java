package com.bstek.urule.model.rete;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;

public abstract class JunctionNode extends BaseReteNode {
   protected int toLineCount;
   @JsonIgnore
   protected List<Line> toConnections = new ArrayList<>();

   public JunctionNode(int var1) {
      super(var1);
   }

   public List<Line> getToConnections() {
      return this.toConnections;
   }

   public void addToConnection(Line var1) {
      this.toConnections.add(var1);
      this.toLineCount = this.toConnections.size();
   }

   public int getToLineCount() {
      return this.toLineCount;
   }
}
