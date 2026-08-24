package com.bstek.urule.model.rete;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;

public abstract class JunctionNode extends BaseReteNode {
   protected int toLineCount;
   @JsonIgnore
   protected List<Line> toConnections = new ArrayList<>();

   public JunctionNode(int id) {
      super(id);
   }

   public List<Line> getToConnections() {
      return this.toConnections;
   }

   public void addToConnection(Line connection) {
      this.toConnections.add(connection);
      this.toLineCount = this.toConnections.size();
   }

   public int getToLineCount() {
      return this.toLineCount;
   }
}
