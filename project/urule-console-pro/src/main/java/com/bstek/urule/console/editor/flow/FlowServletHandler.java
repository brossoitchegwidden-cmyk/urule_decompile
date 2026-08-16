package com.bstek.urule.console.editor.flow;

import com.bstek.urule.Utils;
import com.bstek.urule.console.ApiServletHandler;
import com.bstek.urule.console.database.manager.packet.PacketManager;
import com.bstek.urule.console.database.manager.project.ProjectManager;
import com.bstek.urule.console.database.model.Project;
import com.bstek.urule.console.database.model.ProjectType;
import com.bstek.urule.console.util.ServiceUtils;
import com.bstek.urule.console.xml.DocumentHelper;
import com.bstek.urule.exception.RuleException;
import com.bstek.urule.model.rule.lhs.Lhs;
import com.bstek.urule.parse.ActionParser;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

public class FlowServletHandler extends ApiServletHandler {
   public void loadProjects(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("groupId");
      long var4 = Long.valueOf(var1.getParameter("projectId"));
      List var6 = ProjectManager.ins.newQuery().groupId(var3).type(ProjectType.common.name()).list();
      Project var7 = ProjectManager.ins.get(var4);
      boolean var8 = false;

      for(Project var10 : (Iterable<Project>)(Iterable<?>)(var6)) {
         if (var10.getId() == var7.getId()) {
            var8 = true;
            break;
         }
      }

      if (!var8) {
         var6.add(0, var7);
      }

      this.a(var2, var6);
   }

   public void loadPackets(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = Long.valueOf(var1.getParameter("id"));
      List var5 = PacketManager.ins.newQuery().enable(true).projectId(var3).list();
      this.a(var2, var5);
   }

   public void parseJoint(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("content");
      var3 = Utils.decodeContent(var3);
      Element var4 = this.a(var3);
      Lhs var5 = ServiceUtils.getLhsParser().parse(var4);
      this.a(var2, var5);
   }

   public void parseActions(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("content");
      var3 = Utils.decodeContent(var3);
      Element var4 = this.a(var3);
      ArrayList var5 = new ArrayList();

      for(Object var7 : var4.elements()) {
         if (var7 != null && var7 instanceof Element) {
            Element var8 = (Element)var7;
            String var9 = var8.getName();

            for(ActionParser var11 : (Iterable<ActionParser>)(Iterable<?>)(ServiceUtils.getActionParsers())) {
               if (var11.support(var9)) {
                  var5.add(var11.parse(var8));
                  break;
               }
            }
         }
      }

      this.a(var2, var5);
   }

   private Element a(String var1) {
      try {
         Document var2 = DocumentHelper.parseText(var1);
         Element var3 = var2.getRootElement();
         return var3;
      } catch (DocumentException var4) {
         throw new RuleException(var4);
      }
   }

   public String url() {
      return "/flow";
   }
}
