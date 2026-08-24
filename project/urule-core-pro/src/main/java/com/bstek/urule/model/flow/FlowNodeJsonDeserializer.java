package com.bstek.urule.model.flow;

import com.bstek.urule.runtime.KnowledgePackageWrapper;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FlowNodeJsonDeserializer extends JsonDeserializer<List<FlowNode>> {
   public List<FlowNode> deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException, JsonProcessingException {
      ObjectMapper codec = (ObjectMapper)jsonParser.getCodec();
      JsonNode tree = (JsonNode)codec.readTree(jsonParser);
      ArrayList deserializeResult = new ArrayList();
      Iterator iterator = tree.elements();

      while (true) {
         JsonNode jsonNode;
         FlowNode flowNode;
         label82:
         while (true) {
            if (!iterator.hasNext()) {
               return deserializeResult;
            }

            jsonNode = (JsonNode)iterator.next();
            JsonNode type = jsonNode.get("type");
            if (type != null) {
               flowNode = null;
               FlowNodeType flowNodeType = FlowNodeType.valueOf(type.textValue());
               switch (flowNodeType) {
                  case Action:
                     ActionNode actionNode = new ActionNode();
                     JsonNode actionBean = jsonNode.get("actionBean");
                     if (actionBean != null) {
                        actionNode.setActionBean(actionBean.textValue());
                     }

                     flowNode = actionNode;
                     break label82;
                  case Exception:
                     ExceptionNode exceptionNode = new ExceptionNode();
                     JsonNode exception = jsonNode.get("exception");
                     if (exception != null) {
                        exceptionNode.setException(exception.textValue());
                     }

                     JsonNode exceptionBean = jsonNode.get("exceptionBean");
                     if (exceptionBean != null) {
                        exceptionNode.setExceptionBean(exceptionBean.textValue());
                     }

                     flowNode = exceptionNode;
                     break label82;
                  case Script:
                     ScriptNode scriptNode = new ScriptNode();
                     flowNode = scriptNode;
                     break label82;
                  case Decision:
                     DecisionNode decisionNode = new DecisionNode();
                     DecisionType decisionType = DecisionType.valueOf(jsonNode.get("decisionType").textValue());
                     decisionNode.setDecisionType(decisionType);
                     if (decisionType.equals(DecisionType.Percent)) {
                        JsonNode percentScope = jsonNode.get("percentScope");
                        if (percentScope != null) {
                           decisionNode.setPercentScope(PercentScope.valueOf(percentScope.textValue()));
                        }
                     }

                     JsonNode items = jsonNode.get("items");
                     Iterator iterator2 = items.elements();
                     ArrayList items2 = new ArrayList();

                     while (iterator2.hasNext()) {
                        JsonNode jsonNode2 = (JsonNode)iterator2.next();
                        DecisionItem decisionItem = new DecisionItem();
                        decisionItem.setTo(jsonNode2.get("to").textValue());
                        if (!decisionType.equals(DecisionType.Criteria)) {
                           decisionItem.setPercent(jsonNode2.get("percent").intValue());
                        }

                        items2.add(decisionItem);
                     }

                     decisionNode.setItems(items2);
                     flowNode = decisionNode;
                     break label82;
                  case End:
                     flowNode = new EndNode();
                     break label82;
                  case Fork:
                     ForkNode forkNode = new ForkNode();
                     flowNode = forkNode;
                     break label82;
                  case Join:
                     flowNode = new JoinNode();
                     break label82;
                  case Rule:
                     RuleNode ruleNode = new RuleNode();
                     JsonNode files = jsonNode.get("files");
                     Iterator iterator3 = files.elements();
                     ArrayList items3 = new ArrayList();

                     while (iterator3.hasNext()) {
                        JsonNode jsonNode3 = (JsonNode)iterator3.next();
                        long longValue = jsonNode3.get("id").asLong();
                        String text = jsonNode3.get("path").textValue();
                        String text2 = null;
                        if (jsonNode3.get("version") != null) {
                           text2 = jsonNode3.get("version").textValue();
                        }

                        BindingFile bindingFile = new BindingFile(longValue, text, text2);
                        items3.add(bindingFile);
                     }

                     ruleNode.setFiles(items3);
                     flowNode = ruleNode;
                     break label82;
                  case RulePackage:
                     RulePackageNode rulePackageNode = new RulePackageNode();
                     rulePackageNode.setPackageId(jsonNode.get("packageId").textValue());
                     if (jsonNode.get("project") != null) {
                        rulePackageNode.setProject(jsonNode.get("project").textValue());
                     }

                     JsonNode code = jsonNode.get("code");
                     if (code != null) {
                        rulePackageNode.setCode(code.textValue());
                     }

                     flowNode = rulePackageNode;
                     break label82;
                  case Start:
                     flowNode = new StartNode();
                  default:
                     break label82;
               }
            }
         }

         String text3 = jsonNode.get("name").textValue();
         flowNode.setName(text3);
         JsonNode eventBean = jsonNode.get("eventBean");
         if (eventBean != null) {
            flowNode.setEventBean(eventBean.textValue());
         }

         JsonNode connections = jsonNode.get("connections");
         if (connections != null) {
            ArrayList items4 = new ArrayList();
            Iterator iterator4 = connections.elements();

            while (iterator4.hasNext()) {
               JsonNode jsonNode4 = (JsonNode)iterator4.next();
               Connection connection = (Connection)codec.convertValue(jsonNode4, Connection.class);
               items4.add(connection);
            }

            for (Connection connection2 : (Iterable<Connection>)(Iterable<?>)(items4)) {
               connection2.buildDeserialize();
            }

            flowNode.setConnections(items4);
         }

         JsonNode knowledgePackageWrapper2 = jsonNode.get("knowledgePackageWrapper");
         if (knowledgePackageWrapper2 != null && !(flowNode instanceof RulePackageNode)) {
            KnowledgePackageWrapper knowledgePackageWrapper = (KnowledgePackageWrapper)codec.convertValue(knowledgePackageWrapper2, KnowledgePackageWrapper.class);
            knowledgePackageWrapper.buildDeserialize();
            BindingNode bindingNode = (BindingNode)flowNode;
            bindingNode.setKnowledgePackageWrapper(knowledgePackageWrapper);
         }

         deserializeResult.add(flowNode);
      }
   }
}
