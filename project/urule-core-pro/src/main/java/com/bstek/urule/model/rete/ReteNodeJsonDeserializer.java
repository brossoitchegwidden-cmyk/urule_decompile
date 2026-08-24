package com.bstek.urule.model.rete;

import com.bstek.urule.model.AbstractJsonDeserializer;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ReteNodeJsonDeserializer extends AbstractJsonDeserializer<List<ReteNode>> {
   public List<ReteNode> deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
      ObjectCodec codec = jsonParser.getCodec();
      JsonNode tree = (JsonNode)codec.readTree(jsonParser);
      ArrayList deserializeResult = new ArrayList();
      Iterator iterator = tree.elements();

      while (iterator.hasNext()) {
         JsonNode jsonNode = (JsonNode)iterator.next();
         int number = jsonNode.get("id").intValue();
         JsonNode nodeType2 = jsonNode.get("nodeType");
         if (nodeType2 != null) {
            String text = nodeType2.textValue();
            NodeType nodeType = NodeType.valueOf(text);
            ReteNode reteNode = NodeType.newReteNodeInstance(nodeType);
            if (reteNode instanceof ObjectTypeNode) {
               ObjectTypeNode objectTypeNode = (ObjectTypeNode)reteNode;
               objectTypeNode.setObjectTypeClass(jsonNode.get("objectTypeClass").textValue());
               objectTypeNode.setId(number);
            } else if (reteNode instanceof AndNode) {
               AndNode andNode = (AndNode)reteNode;
               andNode.setId(number);
               andNode.setToLineCount(jsonNode.get("toLineCount").intValue());
               andNode.setLines(this.parseLines(jsonNode));
            } else if (reteNode instanceof MetNode) {
               MetNode metNode = (MetNode)reteNode;
               metNode.setId(number);
               metNode.setLines(this.parseLines(jsonNode));
               JsonNode criterions = jsonNode.get("criterions");
               if (criterions != null) {
                  metNode.setCriterions(JsonUtils.parseCriterions(criterions));
               }

               JsonNode debug = jsonNode.get("debug");
               if (debug != null) {
                  metNode.setDebug(debug.asBoolean());
               }

               metNode.setMet(Integer.parseInt(JsonUtils.getJsonValue(jsonNode, "met")));
               metNode.setOnly(Boolean.parseBoolean(JsonUtils.getJsonValue(jsonNode, "only")));
            } else if (reteNode instanceof OrNode) {
               OrNode orNode = (OrNode)reteNode;
               orNode.setId(number);
               orNode.setLines(this.parseLines(jsonNode));
            } else if (reteNode instanceof CriteriaNode) {
               CriteriaNode criteriaNode = (CriteriaNode)reteNode;
               criteriaNode.setId(number);
               JsonNode debug2 = jsonNode.get("debug");
               if (debug2 != null) {
                  criteriaNode.setDebug(debug2.asBoolean());
               }

               JsonNode criteria = jsonNode.get("criteria");
               criteriaNode.setCriteria(JsonUtils.parseCriteria(criteria));
               criteriaNode.setLines(this.parseLines(jsonNode));
            } else if (reteNode instanceof TerminalNode) {
               TerminalNode terminalNode = (TerminalNode)reteNode;
               terminalNode.setId(number);
               terminalNode.setRule(this.parseRule(jsonParser, jsonNode));
            }

            deserializeResult.add(reteNode);
         }
      }

      return deserializeResult;
   }

   private List<Line> parseLines(JsonNode jsonNode) {
      JsonNode lines = jsonNode.get("lines");
      if (lines == null) {
         return null;
      }

      ArrayList lines2 = new ArrayList();

      for (JsonNode jsonNode2 : lines) {
         Line line = new Line();
         line.setFromNodeId(jsonNode2.get("fromNodeId").intValue());
         line.setToNodeId(jsonNode2.get("toNodeId").intValue());
         lines2.add(line);
      }

      return lines2;
   }
}
