package com.bstek.urule.model.flow;

import com.bstek.urule.model.rule.lhs.Lhs;

public class DecisionItem {
   public static final String RETURN_VALUE_KEY = "return_to__";
   private String conditionType = "script";
   private String script;
   private Lhs lhs;
   private String lhsXml;
   private int percent;
   private String to;

   public String getScript() {
      return this.script;
   }

   public void setScript(String script) {
      this.script = script;
   }

   public String getTo() {
      return this.to;
   }

   public void setTo(String to) {
      this.to = to;
   }

   public int getPercent() {
      return this.percent;
   }

   public void setPercent(int percent) {
      this.percent = percent;
   }

   public String getConditionType() {
      return this.conditionType;
   }

   public void setConditionType(String conditionType) {
      this.conditionType = conditionType;
   }

   public void setLhs(Lhs lhs) {
      this.lhs = lhs;
   }

   public Lhs getLhs() {
      return this.lhs;
   }

   public String getLhsXml() {
      return this.lhsXml;
   }

   public void setLhsXml(String lhsXml) {
      this.lhsXml = lhsXml;
   }

   public String buildDSLScript(int index, boolean debug, String flowId, String nodeName) {
      StringBuffer stringBuffer = new StringBuffer();
      stringBuffer.append("rule \"" + flowId + "-" + nodeName + "-decision" + index + "\"");
      if (debug) {
         stringBuffer.append(" debug=true ");
      }

      stringBuffer.append(" ");
      stringBuffer.append("if");
      stringBuffer.append(" ");
      stringBuffer.append(this.script);
      stringBuffer.append(" ");
      stringBuffer.append("then");
      stringBuffer.append(" ");
      stringBuffer.append("parameter.return_to__=\"" + this.to + "\"");
      stringBuffer.append(" ");
      stringBuffer.append("end");
      return stringBuffer.toString();
   }
}
