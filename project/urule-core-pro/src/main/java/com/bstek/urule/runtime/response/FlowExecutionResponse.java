package com.bstek.urule.runtime.response;

import java.util.List;

public interface FlowExecutionResponse extends ExecutionResponse {
   String getFlowId();

   List<RuleExecutionResponse> getRuleExecutionResponses();

   List<FlowExecutionResponse> getFlowExecutionResponses();
}
