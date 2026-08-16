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
   public List<FlowNode> deserialize(JsonParser var1, DeserializationContext var2) throws IOException, JsonProcessingException {
      ObjectMapper var3 = (ObjectMapper)var1.getCodec();
      JsonNode var4 = (JsonNode)var3.readTree(var1);
      ArrayList var5 = new ArrayList();
      Iterator var6 = var4.elements();

      while (true) {
         JsonNode var7;
         FlowNode var9;
         label82:
         while (true) {
            if (!var6.hasNext()) {
               return var5;
            }

            var7 = (JsonNode)var6.next();
            JsonNode var8 = var7.get("type");
            if (var8 != null) {
               var9 = null;
               FlowNodeType var10 = FlowNodeType.valueOf(var8.textValue());
               switch (var10) {
                  case Action:
                     ActionNode var11 = new ActionNode();
                     JsonNode var12 = var7.get("actionBean");
                     if (var12 != null) {
                        var11.setActionBean(var12.textValue());
                     }

                     var9 = var11;
                     break label82;
                  case Exception:
                     ExceptionNode var13 = new ExceptionNode();
                     JsonNode var14 = var7.get("exception");
                     if (var14 != null) {
                        var13.setException(var14.textValue());
                     }

                     JsonNode var15 = var7.get("exceptionBean");
                     if (var15 != null) {
                        var13.setExceptionBean(var15.textValue());
                     }

                     var9 = var13;
                     break label82;
                  case Script:
                     ScriptNode var16 = new ScriptNode();
                     var9 = var16;
                     break label82;
                  case Decision:
                     DecisionNode var17 = new DecisionNode();
                     DecisionType var18 = DecisionType.valueOf(var7.get("decisionType").textValue());
                     var17.setDecisionType(var18);
                     if (var18.equals(DecisionType.Percent)) {
                        JsonNode var19 = var7.get("percentScope");
                        if (var19 != null) {
                           var17.setPercentScope(PercentScope.valueOf(var19.textValue()));
                        }
                     }

                     JsonNode var45 = var7.get("items");
                     Iterator var20 = var45.elements();
                     ArrayList var21 = new ArrayList();

                     while (var20.hasNext()) {
                        JsonNode var46 = (JsonNode)var20.next();
                        DecisionItem var47 = new DecisionItem();
                        var47.setTo(var46.get("to").textValue());
                        if (!var18.equals(DecisionType.Criteria)) {
                           var47.setPercent(var46.get("percent").intValue());
                        }

                        var21.add(var47);
                     }

                     var17.setItems(var21);
                     var9 = var17;
                     break label82;
                  case End:
                     var9 = new EndNode();
                     break label82;
                  case Fork:
                     ForkNode var22 = new ForkNode();
                     var9 = var22;
                     break label82;
                  case Join:
                     var9 = new JoinNode();
                     break label82;
                  case Rule:
                     RuleNode var23 = new RuleNode();
                     JsonNode var24 = var7.get("files");
                     Iterator var25 = var24.elements();
                     ArrayList var26 = new ArrayList();

                     while (var25.hasNext()) {
                        JsonNode var48 = (JsonNode)var25.next();
                        long var49 = var48.get("id").asLong();
                        String var30 = var48.get("path").textValue();
                        String var31 = null;
                        if (var48.get("version") != null) {
                           var31 = var48.get("version").textValue();
                        }

                        BindingFile var32 = new BindingFile(var49, var30, var31);
                        var26.add(var32);
                     }

                     var23.setFiles(var26);
                     var9 = var23;
                     break label82;
                  case RulePackage:
                     RulePackageNode var27 = new RulePackageNode();
                     var27.setPackageId(var7.get("packageId").textValue());
                     if (var7.get("project") != null) {
                        var27.setProject(var7.get("project").textValue());
                     }

                     JsonNode var28 = var7.get("code");
                     if (var28 != null) {
                        var27.setCode(var28.textValue());
                     }

                     var9 = var27;
                     break label82;
                  case Start:
                     var9 = new StartNode();
                  default:
                     break label82;
               }
            }
         }

         String var33 = var7.get("name").textValue();
         var9.setName(var33);
         JsonNode var34 = var7.get("eventBean");
         if (var34 != null) {
            var9.setEventBean(var34.textValue());
         }

         JsonNode var35 = var7.get("connections");
         if (var35 != null) {
            ArrayList var36 = new ArrayList();
            Iterator var38 = var35.elements();

            while (var38.hasNext()) {
               JsonNode var40 = (JsonNode)var38.next();
               Connection var43 = (Connection)var3.convertValue(var40, Connection.class);
               var36.add(var43);
            }

            for (Connection var44 : (Iterable<Connection>)(Iterable<?>)(var36)) {
               var44.buildDeserialize();
            }

            var9.setConnections(var36);
         }

         JsonNode var37 = var7.get("knowledgePackageWrapper");
         if (var37 != null && !(var9 instanceof RulePackageNode)) {
            KnowledgePackageWrapper var39 = (KnowledgePackageWrapper)var3.convertValue(var37, KnowledgePackageWrapper.class);
            var39.buildDeserialize();
            BindingNode var42 = (BindingNode)var9;
            var42.setKnowledgePackageWrapper(var39);
         }

         var5.add(var9);
      }
   }
}
