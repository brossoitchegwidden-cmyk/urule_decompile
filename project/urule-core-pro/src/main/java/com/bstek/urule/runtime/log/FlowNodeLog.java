package com.bstek.urule.runtime.log;

import com.bstek.urule.model.flow.ActionNode;
import com.bstek.urule.model.flow.DecisionNode;
import com.bstek.urule.model.flow.EndNode;
import com.bstek.urule.model.flow.ExceptionNode;
import com.bstek.urule.model.flow.FlowNode;
import com.bstek.urule.model.flow.ForkNode;
import com.bstek.urule.model.flow.JoinNode;
import com.bstek.urule.model.flow.RuleNode;
import com.bstek.urule.model.flow.RulePackageNode;
import com.bstek.urule.model.flow.ScriptNode;
import com.bstek.urule.model.flow.StartNode;

public class FlowNodeLog extends DataLog {
   private String file;
   private String nodeName;
   private String nodeType;
   private boolean enter;

   public FlowNodeLog(FlowNode node, String file, boolean enter) {
      this.enter = enter;
      this.nodeName = node.getName();
      this.file = file;
      if (node instanceof ActionNode) {
         this.nodeType = this.isEnglishLanguage() ? "ActionNode" : "动作节点";
      } else if (node instanceof DecisionNode) {
         this.nodeType = this.isEnglishLanguage() ? "DecisionNode" : "决策节点";
      } else if (node instanceof ScriptNode) {
         this.nodeType = this.isEnglishLanguage() ? "ScriptNode" : "脚本节点";
      } else if (node instanceof RuleNode) {
         this.nodeType = this.isEnglishLanguage() ? "RuleNode" : "规则节点";
      } else if (node instanceof RulePackageNode) {
         this.nodeType = this.isEnglishLanguage() ? "RulePackageNode" : "知识包节点";
      } else if (node instanceof EndNode) {
         this.nodeType = this.isEnglishLanguage() ? "EndNode" : "结束节点";
      } else if (node instanceof StartNode) {
         this.nodeType = this.isEnglishLanguage() ? "StartNode" : "开始节点";
      } else if (node instanceof ForkNode) {
         this.nodeType = this.isEnglishLanguage() ? "ForkNode" : "分支节点";
      } else if (node instanceof JoinNode) {
         this.nodeType = this.isEnglishLanguage() ? "JoinNode" : "聚合节点";
      } else if (node instanceof ExceptionNode) {
         this.nodeType = this.isEnglishLanguage() ? "ExceptionNode" : "异常捕获节点";
      }

      if (enter) {
         this.msg = this.isEnglishLanguage() ? ">>>enter：%s：%s" : ">>>进入：%s：%s";
      } else {
         this.msg = this.isEnglishLanguage() ? ">>>leave：%s：%s" : ">>>离开：%s：%s";
      }

      this.msg = String.format(this.msg, this.nodeType, this.nodeName);
   }

   public String getNodeName() {
      return this.nodeName;
   }

   public String getNodeType() {
      return this.nodeType;
   }

   public String getFile() {
      return this.file;
   }

   public boolean isEnter() {
      return this.enter;
   }
}
