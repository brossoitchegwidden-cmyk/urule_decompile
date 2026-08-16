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
   public List<ReteNode> deserialize(JsonParser var1, DeserializationContext var2) throws IOException, JsonProcessingException {
      ObjectCodec var3 = var1.getCodec();
      JsonNode var4 = (JsonNode)var3.readTree(var1);
      ArrayList var5 = new ArrayList();
      Iterator var6 = var4.elements();

      while (var6.hasNext()) {
         JsonNode var7 = (JsonNode)var6.next();
         int var8 = var7.get("id").intValue();
         JsonNode var9 = var7.get("nodeType");
         if (var9 != null) {
            String var10 = var9.textValue();
            NodeType var11 = NodeType.valueOf(var10);
            ReteNode var12 = NodeType.newReteNodeInstance(var11);
            if (var12 instanceof ObjectTypeNode) {
               ObjectTypeNode var13 = (ObjectTypeNode)var12;
               var13.setObjectTypeClass(var7.get("objectTypeClass").textValue());
               var13.setId(var8);
            } else if (var12 instanceof AndNode) {
               AndNode var16 = (AndNode)var12;
               var16.setId(var8);
               var16.setToLineCount(var7.get("toLineCount").intValue());
               var16.setLines(this.parseLines(var7));
            } else if (var12 instanceof MetNode) {
               MetNode var17 = (MetNode)var12;
               var17.setId(var8);
               var17.setLines(this.parseLines(var7));
               JsonNode var14 = var7.get("criterions");
               if (var14 != null) {
                  var17.setCriterions(JsonUtils.parseCriterions(var14));
               }

               JsonNode var15 = var7.get("debug");
               if (var15 != null) {
                  var17.setDebug(var15.asBoolean());
               }

               var17.setMet(Integer.parseInt(JsonUtils.getJsonValue(var7, "met")));
               var17.setOnly(Boolean.parseBoolean(JsonUtils.getJsonValue(var7, "only")));
            } else if (var12 instanceof OrNode) {
               OrNode var18 = (OrNode)var12;
               var18.setId(var8);
               var18.setLines(this.parseLines(var7));
            } else if (var12 instanceof CriteriaNode) {
               CriteriaNode var19 = (CriteriaNode)var12;
               var19.setId(var8);
               JsonNode var21 = var7.get("debug");
               if (var21 != null) {
                  var19.setDebug(var21.asBoolean());
               }

               JsonNode var22 = var7.get("criteria");
               var19.setCriteria(JsonUtils.parseCriteria(var22));
               var19.setLines(this.parseLines(var7));
            } else if (var12 instanceof TerminalNode) {
               TerminalNode var20 = (TerminalNode)var12;
               var20.setId(var8);
               var20.setRule(this.parseRule(var1, var7));
            }

            var5.add(var12);
         }
      }

      return var5;
   }

   private List<Line> parseLines(JsonNode var1) {
      JsonNode var2 = var1.get("lines");
      if (var2 == null) {
         return null;
      }

      ArrayList var3 = new ArrayList();

      for (JsonNode var5 : var2) {
         Line var6 = new Line();
         var6.setFromNodeId(var5.get("fromNodeId").intValue());
         var6.setToNodeId(var5.get("toNodeId").intValue());
         var3.add(var6);
      }

      return var3;
   }
}
