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
   private static final String b = ">>>进入：%s：%s";
   private static final String c = ">>>enter：%s：%s";
   private static final String d = ">>>离开：%s：%s";
   private static final String e = ">>>leave：%s：%s";
   private String f;
   private String g;
   private String h;
   private boolean i;

   public FlowNodeLog(FlowNode var1, String var2, boolean var3) {
      this.i = var3;
      this.g = var1.getName();
      this.f = var2;
      if (var1 instanceof ActionNode) {
         this.h = this.a() ? "ActionNode" : "动作节点";
      } else if (var1 instanceof DecisionNode) {
         this.h = this.a() ? "DecisionNode" : "决策节点";
      } else if (var1 instanceof ScriptNode) {
         this.h = this.a() ? "ScriptNode" : "脚本节点";
      } else if (var1 instanceof RuleNode) {
         this.h = this.a() ? "RuleNode" : "规则节点";
      } else if (var1 instanceof RulePackageNode) {
         this.h = this.a() ? "RulePackageNode" : "知识包节点";
      } else if (var1 instanceof EndNode) {
         this.h = this.a() ? "EndNode" : "结束节点";
      } else if (var1 instanceof StartNode) {
         this.h = this.a() ? "StartNode" : "开始节点";
      } else if (var1 instanceof ForkNode) {
         this.h = this.a() ? "ForkNode" : "分支节点";
      } else if (var1 instanceof JoinNode) {
         this.h = this.a() ? "JoinNode" : "聚合节点";
      } else if (var1 instanceof ExceptionNode) {
         this.h = this.a() ? "ExceptionNode" : "异常捕获节点";
      }

      if (var3) {
         this.a = this.a() ? ">>>enter：%s：%s" : ">>>进入：%s：%s";
      } else {
         this.a = this.a() ? ">>>leave：%s：%s" : ">>>离开：%s：%s";
      }

      this.a = String.format(this.a, this.h, this.g);
   }

   public String getNodeName() {
      return this.g;
   }

   public String getNodeType() {
      return this.h;
   }

   public String getFile() {
      return this.f;
   }

   public boolean isEnter() {
      return this.i;
   }
}
