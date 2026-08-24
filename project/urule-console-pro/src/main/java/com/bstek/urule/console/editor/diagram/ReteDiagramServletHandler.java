package com.bstek.urule.console.editor.diagram;

import com.bstek.urule.Utils;
import com.bstek.urule.builder.KnowledgeBase;
import com.bstek.urule.builder.ResourceBase;
import com.bstek.urule.builder.resource.Resource;
import com.bstek.urule.console.ApiServletHandler;
import com.bstek.urule.console.InfoException;
import com.bstek.urule.console.database.manager.packet.PacketManager;
import com.bstek.urule.console.database.manager.packet.packge.PacketPackageManager;
import com.bstek.urule.console.database.model.Packet;
import com.bstek.urule.console.database.model.PacketPackage;
import com.bstek.urule.console.database.model.PacketType;
import com.bstek.urule.console.util.ServiceUtils;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.model.Node;
import com.bstek.urule.model.rete.AndNode;
import com.bstek.urule.model.rete.CriteriaNode;
import com.bstek.urule.model.rete.Line;
import com.bstek.urule.model.rete.MetNode;
import com.bstek.urule.model.rete.ObjectTypeNode;
import com.bstek.urule.model.rete.OrNode;
import com.bstek.urule.model.rete.Rete;
import com.bstek.urule.model.rete.TerminalNode;
import com.bstek.urule.runtime.KnowledgePackage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ReteDiagramServletHandler extends ApiServletHandler {
   private ReteNodeLayout reteNodeLayout = new ReteNodeLayout();
   public void buildSingleRuleReteDiagram(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      String parameter = req.getParameter("content");
      ResourceBase resourceBase = ServiceUtils.getKnowledgeBuilder().newResourceBase();
      Resource resource = new Resource(-1L, parameter, "singlerule", (String)null);
      resourceBase.getResources().add(resource);
      KnowledgeBase knowledgeBase = ServiceUtils.getKnowledgeBuilder().buildKnowledgeBase(resourceBase);
      Rete predefineRete = knowledgeBase.getPredefineRete();
      if (predefineRete == null) {
         predefineRete = knowledgeBase.getRete();
      }

      Diagram diagram = this.buildDiagram(predefineRete);
      this.writeObjectToJson(resp, diagram);
   }

   public void loadDiagramData(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      String parameter = req.getParameter("files");
      parameter = Utils.decodeURL(parameter);
      ResourceBase resourceBase = ServiceUtils.getKnowledgeBuilder().newResourceBase();
      String[] parts = parameter.split(";");
      Object objectValue = null;
      Rete rete;
      if (StringUtils.isBlank(parameter)) {
         KnowledgePackage knowledgePackage = this.resolveKnowledgePackage(req);
         rete = knowledgePackage.getRete();
      } else {
         for(String text : parts) {
            resourceBase.addResource(text);
         }

         KnowledgeBase knowledgeBase = ServiceUtils.getKnowledgeBuilder().buildKnowledgeBase(resourceBase);
         rete = knowledgeBase.getRete();
      }

      Diagram diagram = this.buildDiagram(rete);
      this.writeObjectToJson(resp, diagram);
   }

   private Diagram buildDiagram(Rete rete) {
      HashMap valuesByKey = new HashMap();
      ArrayList items = new ArrayList();
      NodeInfo nodeInfo = new NodeInfo();
      DiagramContext diagramContext = new DiagramContext(items, valuesByKey);
      nodeInfo.setId(diagramContext.nextId());
      nodeInfo.setLabel("入口");
      nodeInfo.setColor("#98AFC7");
      nodeInfo.setWidth(30);
      nodeInfo.setHeight(30);
      nodeInfo.setRoundCorner(10);
      List objectTypeNodes = rete.getObjectTypeNodes();
      byte byteValue = 1;

      for(ObjectTypeNode objectTypeNode : (Iterable<ObjectTypeNode>)(Iterable<?>)(objectTypeNodes)) {
         NodeInfo nodeInfo2 = new NodeInfo();
         nodeInfo2.setId(diagramContext.nextId());
         nodeInfo2.setLabel("T");
         String objectTypeClass = objectTypeNode.getObjectTypeClass();
         if (objectTypeClass.equals("__*__")) {
            nodeInfo2.setTitle("否则");
         } else {
            nodeInfo2.setTitle(objectTypeClass);
         }

         nodeInfo2.setColor("#97CBFF");
         nodeInfo2.setLevel(byteValue);
         nodeInfo2.setWidth(30);
         nodeInfo2.setHeight(30);
         nodeInfo2.setRoundCorner(5);
         nodeInfo.addChild(nodeInfo2);
         List lines = objectTypeNode.getLines();
         if (lines != null) {
            int number = byteValue + 1;

            for(Line line : (Iterable<Line>)(Iterable<?>)(lines)) {
               Edge edge = new Edge(nodeInfo.getId(), nodeInfo2.getId());
               items.add(edge);
               this.appendLineNodes(line, diagramContext, nodeInfo2, number);
            }
         }
      }

      Box box = this.reteNodeLayout.layout(nodeInfo);
      Diagram diagram = new Diagram(items, nodeInfo);
      if (box != null) {
         diagram.setWidth(box.getWidth() + 500);
         diagram.setHeight(box.getHeight() + 300);
      }

      return diagram;
   }

   private void appendLineNodes(Line line, DiagramContext diagramContext, NodeInfo nodeInfo, int number) {
      Node to = line.getTo();
      if (to != null) {
         Map nodeMap = diagramContext.getNodeMap();
         NodeInfo nodeInfo2 = null;
         if (nodeMap.containsKey(to)) {
            nodeInfo2 = (NodeInfo)nodeMap.get(to);
            diagramContext.addEdge(new Edge(nodeInfo.getId(), nodeInfo2.getId()));
         } else {
            List lines = null;
            nodeInfo2 = new NodeInfo();
            nodeInfo2.setLevel(number);
            nodeInfo2.setId(diagramContext.nextId());
            nodeInfo2.setWidth(30);
            nodeInfo2.setHeight(30);
            if (to instanceof CriteriaNode) {
               CriteriaNode criteriaNode = (CriteriaNode)to;
               nodeInfo2.setColor("#B3D9D9");
               nodeInfo2.setLabel("C");
               nodeInfo2.setTitle(criteriaNode.getCriteriaInfo());
               nodeInfo2.setRoundCorner(30);
               lines = criteriaNode.getLines();
            } else if (to instanceof AndNode) {
               AndNode andNode = (AndNode)to;
               lines = andNode.getLines();
               nodeInfo2.setColor("#DAB1D5");
               nodeInfo2.setLabel("AND");
               nodeInfo2.setRoundCorner(15);
            } else if (to instanceof OrNode) {
               OrNode orNode = (OrNode)to;
               lines = orNode.getLines();
               nodeInfo2.setColor("#82D900");
               nodeInfo2.setLabel("OR");
               nodeInfo2.setRoundCorner(15);
            } else if (to instanceof MetNode) {
               MetNode metNode = (MetNode)to;
               lines = metNode.getLines();
               nodeInfo2.setColor("#82E888");
               nodeInfo2.setLabel("Met");
               nodeInfo2.setRoundCorner(15);
            } else if (to instanceof TerminalNode) {
               TerminalNode terminalNode = (TerminalNode)to;
               nodeInfo2.setColor("orange");
               nodeInfo2.setLabel(terminalNode.getRule().getName());
               nodeInfo2.setTitle(terminalNode.getRule().getName());
               nodeInfo2.setRoundCorner(0);
            }

            nodeMap.put(to, nodeInfo2);
            nodeInfo.addChild(nodeInfo2);
            diagramContext.addEdge(new Edge(nodeInfo.getId(), nodeInfo2.getId()));
            if (lines != null) {
               int number2 = number + 1;

               for(Line line2 : (Iterable<Line>)(Iterable<?>)(lines)) {
                  this.appendLineNodes(line2, diagramContext, nodeInfo2, number2);
               }

            }
         }
      }
   }

   private KnowledgePackage resolveKnowledgePackage(HttpServletRequest httpServletRequest) {
      String parameter = httpServletRequest.getParameter("packetId");
      if (StringUtils.isNotBlank(parameter)) {
         Packet packet = PacketManager.ins.load(Long.valueOf(parameter));
         if (packet.getType().equals(PacketType.upload)) {
            PacketPackage packetPackage = packet.getPacketPackage();
            if (packetPackage != null && packetPackage.getId() != 0L) {
               String content = PacketPackageManager.ins.loadContent(packetPackage.getId());
               if (StringUtils.isBlank(content)) {
                  throw new InfoException("请先上传知识包");
               }

               KnowledgePackage knowledgePackage = Utils.stringToKnowledgePackage(content);
               return knowledgePackage;
            }

            throw new InfoException("请先上传知识包");
         }
      }

      return null;
   }

   public String url() {
      return "/rete";
   }
}
