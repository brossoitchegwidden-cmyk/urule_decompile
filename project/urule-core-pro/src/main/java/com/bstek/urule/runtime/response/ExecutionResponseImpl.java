package com.bstek.urule.runtime.response;

import java.util.ArrayList;
import java.util.List;

public class ExecutionResponseImpl implements FlowExecutionResponse, RuleExecutionResponse {
   private long duration;
   private String flowId;
   private List<RuleExecutionResponse> ruleExecutionResponses = new ArrayList<>();
   private List<FlowExecutionResponse> flowExecutionResponses = new ArrayList<>();

   @Override
   public String getFlowId() {
      return this.flowId;
   }

   public void setFlowId(String flowId) {
      this.flowId = flowId;
   }

   public void addFlowExecutionResponse(FlowExecutionResponse response) {
      this.flowExecutionResponses.add(response);
   }

   @Override
   public List<FlowExecutionResponse> getFlowExecutionResponses() {
      return this.flowExecutionResponses;
   }

   @Override
   public List<RuleExecutionResponse> getRuleExecutionResponses() {
      return this.ruleExecutionResponses;
   }

   public void addRuleExecutionResponse(RuleExecutionResponse response) {
      this.ruleExecutionResponses.add(response);
   }

   @Override
   public long getDuration() {
      return this.duration;
   }

   public void setDuration(long duration) {
      this.duration = duration;
   }
}
