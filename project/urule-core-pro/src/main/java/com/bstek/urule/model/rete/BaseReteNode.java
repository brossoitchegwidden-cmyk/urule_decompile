package com.bstek.urule.model.rete;

import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.runtime.rete.Context;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.BeanUtils;

public abstract class BaseReteNode extends ReteNode {
   @JsonIgnore
   private List<ReteNode> childrenNodes = new ArrayList<>();
   protected List<Line> lines;

   public BaseReteNode(int id) {
      super(id);
   }

   public List<ReteNode> getChildrenNodes() {
      return this.childrenNodes;
   }

   public void setChildrenNodes(List<ReteNode> childrenNodes) {
      this.childrenNodes = childrenNodes;
   }

   protected boolean buildVariables(Context context, Value value, Map<String, Object> variableMap) {
      return true;
   }

   protected Object fetchData(Object object, String property) {
      try {
         return BeanUtils.getProperty(object, property);
      } catch (Exception exception) {
         throw new RuleException(exception);
      }
   }

   public Line addLine(ReteNode toNode) {
      if (this.childrenNodes == null) {
         this.childrenNodes = new ArrayList<>();
      }

      this.childrenNodes.add(toNode);
      Line line = new Line(this, toNode);
      if (this.lines == null) {
         this.lines = new ArrayList<>();
      }

      this.lines.add(line);
      if (toNode instanceof JunctionNode) {
         JunctionNode junctionNode = (JunctionNode)toNode;
         junctionNode.addToConnection(line);
      }

      return line;
   }

   public List<Line> getLines() {
      return this.lines;
   }

   public void setLines(List<Line> lines) {
      this.lines = lines;
   }
}
