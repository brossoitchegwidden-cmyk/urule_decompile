package com.bstek.urule.console.editor.ruleset;

import com.bstek.urule.Utils;
import com.bstek.urule.action.Action;
import com.bstek.urule.builder.resource.ResourceType;
import com.bstek.urule.console.ApiServletHandler;
import com.bstek.urule.console.SimpleDataProvider;
import com.bstek.urule.console.database.manager.file.FileManager;
import com.bstek.urule.console.database.manager.packet.PacketManager;
import com.bstek.urule.console.database.manager.project.ProjectManager;
import com.bstek.urule.console.database.model.Packet;
import com.bstek.urule.console.database.model.Project;
import com.bstek.urule.console.database.model.ProjectType;
import com.bstek.urule.console.database.model.RuleFile;
import com.bstek.urule.console.editor.store.StoreTools;
import com.bstek.urule.console.util.StringUtils;
import com.bstek.urule.console.xml.DocumentHelper;
import com.bstek.urule.console.xml.XXESAXReader;
import com.bstek.urule.model.rule.Rule;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.lhs.Criterion;
import com.bstek.urule.model.rule.loop.LoopRule;
import com.bstek.urule.model.table.Joint;
import com.bstek.urule.parse.ActionParser;
import com.bstek.urule.parse.ExecuteMethodActionParser;
import com.bstek.urule.parse.JunctionParser;
import com.bstek.urule.parse.LoopRuleParser;
import com.bstek.urule.parse.RuleParser;
import com.bstek.urule.parse.ValueParser;
import com.bstek.urule.parse.table.JointParser;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.context.ApplicationContext;

public class RulesetServletHandler extends ApiServletHandler {
   public static final String IMPORT_DATA = "_import_data_";
   public static final String ACTION_CELL_DATA = "__action_cell_data_";
   public static final String ACTION_RULE_DATA = "__action_rule_data_";
   public static final String CONDITION_RULE_DATA = "_condition_rule_data_";
   public static final String CONDITION_CELL_DATA = "_condition_cell_data_";
   public static final String VALUE_CELL_DATA = "_value_cell_data_";
   private static final String LOOP_RULE_FOR_COPY = "_loop_rule_for_copy_";
   private static final String RULE_FOR_COPY = "_rule_for_copy_";
   private RuleParser ruleParser;
   private LoopRuleParser loopRuleParser;
   private ValueParser valueParser;
   private ExecuteMethodActionParser executeMethodActionParser;
   private JointParser jointParser;
   private JunctionParser junctionParser;
   private Collection actionParsers;

   public void init() {
      super.init();
      ApplicationContext applicationContext = Utils.getApplicationContext();
      this.ruleParser = (RuleParser)applicationContext.getBean(RuleParser.class);
      this.loopRuleParser = (LoopRuleParser)applicationContext.getBean(LoopRuleParser.class);
      this.valueParser = (ValueParser)applicationContext.getBean(ValueParser.class);
      this.executeMethodActionParser = (ExecuteMethodActionParser)applicationContext.getBean(ExecuteMethodActionParser.class);
      this.jointParser = (JointParser)applicationContext.getBean(JointParser.class);
      this.junctionParser = (JunctionParser)applicationContext.getBean(JunctionParser.class);
      this.actionParsers = applicationContext.getBeansOfType(ActionParser.class).values();
   }

   public void copyRule(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("type");
      String text2 = Utils.decodeContent(req.getParameter("xml"));
      String text3 = Utils.decodeContent(req.getParameter("libs"));
      Document text = DocumentHelper.parseText(text2);
      Element rootElement = text.getRootElement();
      Object objectValue = null;
      if (parameter.equals("loop")) {
         LoopRule loopRule = this.loopRuleParser.parse(rootElement);
         text3 = CopyLibsAnalysis.ins.doAnalysis(text3, loopRule);
         StoreTools.setAttribute("_loop_rule_for_copy_", new CopyRule(loopRule, text3));
      } else {
         Rule rule = this.ruleParser.parse(rootElement);
         text3 = CopyLibsAnalysis.ins.doAnalysis(text3, rule);
         StoreTools.setAttribute("_rule_for_copy_", new CopyRule(rule, text3));
      }

   }

   public void pasteRule(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("type");
      if (parameter.equals("loop")) {
         Object attribute = StoreTools.getAttribute("_loop_rule_for_copy_");
         this.writeObjectToJson(resp, attribute);
      } else {
         Object attribute2 = StoreTools.getAttribute("_rule_for_copy_");
         this.writeObjectToJson(resp, attribute2);
      }

   }

   public void parseRuleData(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("type");
      String text2 = Utils.decodeContent(req.getParameter("xml"));
      String text3 = Utils.decodeContent(req.getParameter("libs"));
      Document text = DocumentHelper.parseText(text2);
      if (parameter.equals("condition")) {
         Element rootElement = text.getRootElement();
         if (this.junctionParser.support(rootElement.getName())) {
            Criterion criterion = this.junctionParser.parse(rootElement);
            text3 = CopyLibsAnalysis.ins.doAnalysis(text3, criterion);
            StoreTools.setAttribute("_condition_rule_data_", new CopyCriterion(criterion, text3));
         } else {
            List criterion2 = this.junctionParser.parseCriterion(rootElement);
            text3 = CopyLibsAnalysis.ins.doAnalysis(text3, criterion2);
            StoreTools.setAttribute("_condition_rule_data_", new CopyCriterion((Criterion)criterion2.get(0), text3));
         }
      } else if (parameter.equals("action")) {
         Element rootElement2 = text.getRootElement();

         for(ActionParser actionParser : (Iterable<ActionParser>)(Iterable<?>)(this.actionParsers)) {
            if (actionParser.support(rootElement2.getName())) {
               Action action = (Action)actionParser.parse(rootElement2);
               text3 = CopyLibsAnalysis.ins.doAnalysis(text3, action);
               StoreTools.setAttribute("__action_rule_data_", new CopyAction(action, text3));
               break;
            }
         }
      }

   }

   public void loadRuleData(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      String parameter = req.getParameter("type");
      Object attribute = null;
      if (parameter.equals("condition")) {
         attribute = StoreTools.getAttribute("_condition_rule_data_");
      } else if (parameter.equals("action")) {
         attribute = StoreTools.getAttribute("__action_rule_data_");
      }

      if (attribute != null) {
         this.writeObjectToJson(resp, attribute);
      }

   }

   public void parseCellData(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("type");
      String text = Utils.decodeContent(req.getParameter("xml"));

      Document document;
      try {
         document = DocumentHelper.parseText(text);
      } catch (DocumentException documentException) {
         throw new ServletException(documentException);
      }

      if (parameter.equals("condition")) {
         Joint joint = this.jointParser.parse(document.getRootElement());
         StoreTools.setAttribute("_condition_cell_data_", joint);
      } else if (parameter.equals("value")) {
         Value localValue = this.valueParser.parse(document.getRootElement());
         StoreTools.setAttribute("_value_cell_data_", localValue);
      } else if (parameter.equals("action")) {
         Action action = this.executeMethodActionParser.parse(document.getRootElement());
         StoreTools.setAttribute("__action_cell_data_", action);
      }

   }

   public void loadCellData(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      String parameter = req.getParameter("type");
      Object attribute = null;
      if (parameter.equals("condition")) {
         attribute = StoreTools.getAttribute("_condition_cell_data_");
      } else if (parameter.equals("value")) {
         attribute = StoreTools.getAttribute("_value_cell_data_");
      } else if (parameter.equals("action")) {
         attribute = StoreTools.getAttribute("__action_cell_data_");
      }

      if (attribute != null) {
         this.writeObjectToJson(resp, attribute);
      }

   }

   public void loadSimpleData(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      HashMap valuesByKey = new HashMap();

      for(SimpleDataProvider simpleDataProvider : Utils.getApplicationContext().getBeansOfType(SimpleDataProvider.class).values()) {
         valuesByKey.put(simpleDataProvider.name(), simpleDataProvider.data());
      }

      this.writeObjectToJson(resp, valuesByKey);
   }

   public void loadPackets(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      long longValue = Long.valueOf(req.getParameter("projectId"));
      ArrayList items = new ArrayList();
      Project project = ProjectManager.ins.get(longValue);
      Map valuesByKey = this.buildValuesByKey(project);
      if (valuesByKey != null) {
         items.add(valuesByKey);
      }

      for(Project project2 : (Iterable<Project>)(Iterable<?>)(ProjectManager.ins.newQuery().type(ProjectType.common.name()).groupId(project.getGroupId()).list())) {
         valuesByKey = this.buildValuesByKey(project2);
         if (valuesByKey != null) {
            items.add(valuesByKey);
         }
      }

      this.writeObjectToJson(resp, items);
   }

   private Map buildValuesByKey(Project project) throws Exception {
      HashMap valuesByKey = new HashMap();
      ArrayList items = new ArrayList();
      valuesByKey.put("project", project.getName());
      valuesByKey.put("packets", items);

      for(Packet packet : (Iterable<Packet>)(Iterable<?>)(PacketManager.ins.newQuery().projectId(project.getId()).enable(true).list())) {
         HashMap valuesByKey2 = new HashMap();
         valuesByKey2.put("name", packet.getName());
         valuesByKey2.put("code", packet.getCode());
         valuesByKey2.put("id", packet.getId());
         items.add(valuesByKey2);
      }

      return items.size() > 0 ? valuesByKey : null;
   }

   public void pendedGroups(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("groupName");
      String parameter2 = req.getParameter("fileName");
      long longValue = Long.valueOf(req.getParameter("projectId"));
      List items = FileManager.ins.newQuery().nameLike(parameter2).deleted(false).type(ResourceType.RuleSet.name()).asc("NAME_").list(longValue);
      HashMap valuesByKey = new HashMap();

      for(RuleFile ruleFile : (Iterable<RuleFile>)(Iterable<?>)(items)) {
         this.collectPendedGroups(ruleFile, valuesByKey);
      }

      ArrayList items2 = new ArrayList();

      for(String text : (Iterable<String>)(Iterable<?>)(valuesByKey.keySet())) {
         HashMap valuesByKey2 = new HashMap();
         if (!StringUtils.isNotBlank(parameter) || text.toLowerCase().indexOf(parameter.toLowerCase()) != -1) {
            valuesByKey2.put("name", text);
            valuesByKey2.put("path", valuesByKey.get(text));
            items2.add(valuesByKey2);
         }
      }

      this.writeObjectToJson(resp, items2);
   }

   protected void collectPendedGroups(RuleFile ruleFile, Map valuesByKey) throws Exception {
      String content = FileManager.ins.loadContent(ruleFile.getId());
      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(content.getBytes("utf-8"));
      XXESAXReader xXESAXReader = new XXESAXReader();
      Document document = ((SAXReader)xXESAXReader).read(byteArrayInputStream);
      Element rootElement = document.getRootElement();

      for(Object objectValue : rootElement.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element element = (Element)objectValue;
            String name = element.getName();
            if (name.equals("rule") || name.equals("loop-rule")) {
               String text = element.attributeValue("pended-group");
               String text2 = ruleFile.getPath() + "(" + ruleFile.getId() + ")";
               if (StringUtils.isNotBlank(text)) {
                  if (valuesByKey.containsKey(text)) {
                     String text3 = (String)valuesByKey.get(text);
                     if (text3.indexOf(text2) == -1) {
                        valuesByKey.put(text, text3 + "，" + text2);
                     }
                  } else {
                     valuesByKey.put(text, text2);
                  }
               }
            }
         }
      }

      IOUtils.closeQuietly(byteArrayInputStream);
   }

   public String url() {
      return "/ruleset";
   }
}
