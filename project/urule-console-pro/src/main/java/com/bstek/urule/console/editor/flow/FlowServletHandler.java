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
   public void loadProjects(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("groupId");
      long longValue = Long.valueOf(req.getParameter("projectId"));
      List items = ProjectManager.ins.newQuery().groupId(parameter).type(ProjectType.common.name()).list();
      Project project = ProjectManager.ins.get(longValue);
      boolean flag = false;

      for(Project project2 : (Iterable<Project>)(Iterable<?>)(items)) {
         if (project2.getId() == project.getId()) {
            flag = true;
            break;
         }
      }

      if (!flag) {
         items.add(0, project);
      }

      this.writeObjectToJson(resp, items);
   }

   public void loadPackets(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long longValue = Long.valueOf(req.getParameter("id"));
      List items = PacketManager.ins.newQuery().enable(true).projectId(longValue).list();
      this.writeObjectToJson(resp, items);
   }

   public void parseJoint(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("content");
      parameter = Utils.decodeContent(parameter);
      Element element = this.parseRootElement(parameter);
      Lhs lhs = ServiceUtils.getLhsParser().parse(element);
      this.writeObjectToJson(resp, lhs);
   }

   public void parseActions(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("content");
      parameter = Utils.decodeContent(parameter);
      Element element = this.parseRootElement(parameter);
      ArrayList items = new ArrayList();

      for(Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            String name = element2.getName();

            for(ActionParser actionParser : (Iterable<ActionParser>)(Iterable<?>)(ServiceUtils.getActionParsers())) {
               if (actionParser.support(name)) {
                  items.add(actionParser.parse(element2));
                  break;
               }
            }
         }
      }

      this.writeObjectToJson(resp, items);
   }

   private Element parseRootElement(String text2) {
      try {
         Document text = DocumentHelper.parseText(text2);
         Element rootElement = text.getRootElement();
         return rootElement;
      } catch (DocumentException documentException) {
         throw new RuleException(documentException);
      }
   }

   public String url() {
      return "/flow";
   }
}
