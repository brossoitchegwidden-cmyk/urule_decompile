package com.bstek.urule.runtime.response;

import java.util.List;

public interface RuleExecutionResponse extends ExecutionResponse {
   List<FlowExecutionResponse> getFlowExecutionResponses();
}
