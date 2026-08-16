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
   private ReteNodeLayout e = new ReteNodeLayout();
   private final int f = 30;
   private final int g = 30;

   public void buildSingleRuleReteDiagram(HttpServletRequest var1, HttpServletResponse var2) throws ServletException, IOException {
      String var3 = var1.getParameter("content");
      ResourceBase var4 = ServiceUtils.getKnowledgeBuilder().newResourceBase();
      Resource var5 = new Resource(-1L, var3, "singlerule", (String)null);
      var4.getResources().add(var5);
      KnowledgeBase var6 = ServiceUtils.getKnowledgeBuilder().buildKnowledgeBase(var4);
      Rete var7 = var6.getPredefineRete();
      if (var7 == null) {
         var7 = var6.getRete();
      }

      Diagram var8 = this.a(var7);
      this.a(var2, var8);
   }

   public void loadDiagramData(HttpServletRequest var1, HttpServletResponse var2) throws ServletException, IOException {
      String var3 = var1.getParameter("files");
      var3 = Utils.decodeURL(var3);
      ResourceBase var4 = ServiceUtils.getKnowledgeBuilder().newResourceBase();
      String[] var5 = var3.split(";");
      Object var6 = null;
      Rete var12;
      if (StringUtils.isBlank(var3)) {
         KnowledgePackage var7 = this.c(var1);
         var12 = var7.getRete();
      } else {
         for(String var10 : var5) {
            var4.addResource(var10);
         }

         KnowledgeBase var14 = ServiceUtils.getKnowledgeBuilder().buildKnowledgeBase(var4);
         var12 = var14.getRete();
      }

      Diagram var15 = this.a(var12);
      this.a(var2, var15);
   }

   private Diagram a(Rete var1) {
      HashMap var2 = new HashMap();
      ArrayList var3 = new ArrayList();
      NodeInfo var4 = new NodeInfo();
      DiagramContext var5 = new DiagramContext(var3, var2);
      var4.setId(var5.nextId());
      var4.setLabel("入口");
      var4.setColor("#98AFC7");
      var4.setWidth(30);
      var4.setHeight(30);
      var4.setRoundCorner(10);
      List var6 = var1.getObjectTypeNodes();
      byte var7 = 1;

      for(ObjectTypeNode var9 : (Iterable<ObjectTypeNode>)(Iterable<?>)(var6)) {
         NodeInfo var10 = new NodeInfo();
         var10.setId(var5.nextId());
         var10.setLabel("T");
         String var11 = var9.getObjectTypeClass();
         if (var11.equals("__*__")) {
            var10.setTitle("否则");
         } else {
            var10.setTitle(var11);
         }

         var10.setColor("#97CBFF");
         var10.setLevel(var7);
         var10.setWidth(30);
         var10.setHeight(30);
         var10.setRoundCorner(5);
         var4.addChild(var10);
         List var12 = var9.getLines();
         if (var12 != null) {
            int var13 = var7 + 1;

            for(Line var15 : (Iterable<Line>)(Iterable<?>)(var12)) {
               Edge var16 = new Edge(var4.getId(), var10.getId());
               var3.add(var16);
               this.a(var15, var5, var10, var13);
            }
         }
      }

      Box var17 = this.e.layout(var4);
      Diagram var18 = new Diagram(var3, var4);
      if (var17 != null) {
         var18.setWidth(var17.getWidth() + 500);
         var18.setHeight(var17.getHeight() + 300);
      }

      return var18;
   }

   private void a(Line var1, DiagramContext var2, NodeInfo var3, int var4) {
      Node var5 = var1.getTo();
      if (var5 != null) {
         Map var6 = var2.getNodeMap();
         NodeInfo var7 = null;
         if (var6.containsKey(var5)) {
            var7 = (NodeInfo)var6.get(var5);
            var2.addEdge(new Edge(var3.getId(), var7.getId()));
         } else {
            List var8 = null;
            var7 = new NodeInfo();
            var7.setLevel(var4);
            var7.setId(var2.nextId());
            var7.setWidth(30);
            var7.setHeight(30);
            if (var5 instanceof CriteriaNode) {
               CriteriaNode var9 = (CriteriaNode)var5;
               var7.setColor("#B3D9D9");
               var7.setLabel("C");
               var7.setTitle(var9.getCriteriaInfo());
               var7.setRoundCorner(30);
               var8 = var9.getLines();
            } else if (var5 instanceof AndNode) {
               AndNode var14 = (AndNode)var5;
               var8 = var14.getLines();
               var7.setColor("#DAB1D5");
               var7.setLabel("AND");
               var7.setRoundCorner(15);
            } else if (var5 instanceof OrNode) {
               OrNode var15 = (OrNode)var5;
               var8 = var15.getLines();
               var7.setColor("#82D900");
               var7.setLabel("OR");
               var7.setRoundCorner(15);
            } else if (var5 instanceof MetNode) {
               MetNode var16 = (MetNode)var5;
               var8 = var16.getLines();
               var7.setColor("#82E888");
               var7.setLabel("Met");
               var7.setRoundCorner(15);
            } else if (var5 instanceof TerminalNode) {
               TerminalNode var17 = (TerminalNode)var5;
               var7.setColor("orange");
               var7.setLabel(var17.getRule().getName());
               var7.setTitle(var17.getRule().getName());
               var7.setRoundCorner(0);
            }

            var6.put(var5, var7);
            var3.addChild(var7);
            var2.addEdge(new Edge(var3.getId(), var7.getId()));
            if (var8 != null) {
               int var18 = var4 + 1;

               for(Line var11 : (Iterable<Line>)(Iterable<?>)(var8)) {
                  this.a(var11, var2, var7, var18);
               }

            }
         }
      }
   }

   private KnowledgePackage c(HttpServletRequest var1) {
      String var2 = var1.getParameter("packetId");
      if (StringUtils.isNotBlank(var2)) {
         Packet var3 = PacketManager.ins.load(Long.valueOf(var2));
         if (var3.getType().equals(PacketType.upload)) {
            PacketPackage var4 = var3.getPacketPackage();
            if (var4 != null && var4.getId() != 0L) {
               String var5 = PacketPackageManager.ins.loadContent(var4.getId());
               if (StringUtils.isBlank(var5)) {
                  throw new InfoException("请先上传知识包");
               }

               KnowledgePackage var6 = Utils.stringToKnowledgePackage(var5);
               return var6;
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
