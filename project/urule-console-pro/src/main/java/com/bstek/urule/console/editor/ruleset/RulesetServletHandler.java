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
   private static final String e = "_loop_rule_for_copy_";
   private static final String f = "_rule_for_copy_";
   private RuleParser g;
   private LoopRuleParser h;
   private ValueParser i;
   private ExecuteMethodActionParser j;
   private JointParser k;
   private JunctionParser l;
   private Collection m;

   public void init() {
      super.init();
      ApplicationContext var1 = Utils.getApplicationContext();
      this.g = (RuleParser)var1.getBean(RuleParser.class);
      this.h = (LoopRuleParser)var1.getBean(LoopRuleParser.class);
      this.i = (ValueParser)var1.getBean(ValueParser.class);
      this.j = (ExecuteMethodActionParser)var1.getBean(ExecuteMethodActionParser.class);
      this.k = (JointParser)var1.getBean(JointParser.class);
      this.l = (JunctionParser)var1.getBean(JunctionParser.class);
      this.m = var1.getBeansOfType(ActionParser.class).values();
   }

   public void copyRule(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("type");
      String var4 = Utils.decodeContent(var1.getParameter("xml"));
      String var5 = Utils.decodeContent(var1.getParameter("libs"));
      Document var6 = DocumentHelper.parseText(var4);
      Element var7 = var6.getRootElement();
      Object var8 = null;
      if (var3.equals("loop")) {
         LoopRule var11 = this.h.parse(var7);
         var5 = CopyLibsAnalysis.ins.doAnalysis(var5, var11);
         StoreTools.setAttribute("_loop_rule_for_copy_", new CopyRule(var11, var5));
      } else {
         Rule var12 = this.g.parse(var7);
         var5 = CopyLibsAnalysis.ins.doAnalysis(var5, var12);
         StoreTools.setAttribute("_rule_for_copy_", new CopyRule(var12, var5));
      }

   }

   public void pasteRule(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("type");
      if (var3.equals("loop")) {
         Object var4 = StoreTools.getAttribute("_loop_rule_for_copy_");
         this.a(var2, var4);
      } else {
         Object var5 = StoreTools.getAttribute("_rule_for_copy_");
         this.a(var2, var5);
      }

   }

   public void parseRuleData(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("type");
      String var4 = Utils.decodeContent(var1.getParameter("xml"));
      String var5 = Utils.decodeContent(var1.getParameter("libs"));
      Document var6 = DocumentHelper.parseText(var4);
      if (var3.equals("condition")) {
         Element var7 = var6.getRootElement();
         if (this.l.support(var7.getName())) {
            Criterion var8 = this.l.parse(var7);
            var5 = CopyLibsAnalysis.ins.doAnalysis(var5, var8);
            StoreTools.setAttribute("_condition_rule_data_", new CopyCriterion(var8, var5));
         } else {
            List var15 = this.l.parseCriterion(var7);
            var5 = CopyLibsAnalysis.ins.doAnalysis(var5, var15);
            StoreTools.setAttribute("_condition_rule_data_", new CopyCriterion((Criterion)var15.get(0), var5));
         }
      } else if (var3.equals("action")) {
         Element var14 = var6.getRootElement();

         for(ActionParser var9 : (Iterable<ActionParser>)(Iterable<?>)(this.m)) {
            if (var9.support(var14.getName())) {
               Action var10 = (Action)var9.parse(var14);
               var5 = CopyLibsAnalysis.ins.doAnalysis(var5, var10);
               StoreTools.setAttribute("__action_rule_data_", new CopyAction(var10, var5));
               break;
            }
         }
      }

   }

   public void loadRuleData(HttpServletRequest var1, HttpServletResponse var2) throws ServletException, IOException {
      String var3 = var1.getParameter("type");
      Object var4 = null;
      if (var3.equals("condition")) {
         var4 = StoreTools.getAttribute("_condition_rule_data_");
      } else if (var3.equals("action")) {
         var4 = StoreTools.getAttribute("__action_rule_data_");
      }

      if (var4 != null) {
         this.a(var2, var4);
      }

   }

   public void parseCellData(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("type");
      String var4 = Utils.decodeContent(var1.getParameter("xml"));

      Document var5;
      try {
         var5 = DocumentHelper.parseText(var4);
      } catch (DocumentException var7) {
         throw new ServletException(var7);
      }

      if (var3.equals("condition")) {
         Joint var6 = this.k.parse(var5.getRootElement());
         StoreTools.setAttribute("_condition_cell_data_", var6);
      } else if (var3.equals("value")) {
         Value var8 = this.i.parse(var5.getRootElement());
         StoreTools.setAttribute("_value_cell_data_", var8);
      } else if (var3.equals("action")) {
         Action var9 = this.j.parse(var5.getRootElement());
         StoreTools.setAttribute("__action_cell_data_", var9);
      }

   }

   public void loadCellData(HttpServletRequest var1, HttpServletResponse var2) throws ServletException, IOException {
      String var3 = var1.getParameter("type");
      Object var4 = null;
      if (var3.equals("condition")) {
         var4 = StoreTools.getAttribute("_condition_cell_data_");
      } else if (var3.equals("value")) {
         var4 = StoreTools.getAttribute("_value_cell_data_");
      } else if (var3.equals("action")) {
         var4 = StoreTools.getAttribute("__action_cell_data_");
      }

      if (var4 != null) {
         this.a(var2, var4);
      }

   }

   public void loadSimpleData(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      HashMap var3 = new HashMap();

      for(SimpleDataProvider var6 : Utils.getApplicationContext().getBeansOfType(SimpleDataProvider.class).values()) {
         var3.put(var6.name(), var6.data());
      }

      this.a(var2, var3);
   }

   public void loadPackets(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      long var3 = Long.valueOf(var1.getParameter("projectId"));
      ArrayList var5 = new ArrayList();
      Project var6 = ProjectManager.ins.get(var3);
      Map var7 = this.a(var6);
      if (var7 != null) {
         var5.add(var7);
      }

      for(Project var10 : (Iterable<Project>)(Iterable<?>)(ProjectManager.ins.newQuery().type(ProjectType.common.name()).groupId(var6.getGroupId()).list())) {
         var7 = this.a(var10);
         if (var7 != null) {
            var5.add(var7);
         }
      }

      this.a(var2, var5);
   }

   private Map a(Project var1) throws Exception {
      HashMap var2 = new HashMap();
      ArrayList var3 = new ArrayList();
      var2.put("project", var1.getName());
      var2.put("packets", var3);

      for(Packet var6 : (Iterable<Packet>)(Iterable<?>)(PacketManager.ins.newQuery().projectId(var1.getId()).enable(true).list())) {
         HashMap var7 = new HashMap();
         var7.put("name", var6.getName());
         var7.put("code", var6.getCode());
         var7.put("id", var6.getId());
         var3.add(var7);
      }

      return var3.size() > 0 ? var2 : null;
   }

   public void pendedGroups(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("groupName");
      String var4 = var1.getParameter("fileName");
      long var5 = Long.valueOf(var1.getParameter("projectId"));
      List var7 = FileManager.ins.newQuery().nameLike(var4).deleted(false).type(ResourceType.RuleSet.name()).asc("NAME_").list(var5);
      HashMap var8 = new HashMap();

      for(RuleFile var10 : (Iterable<RuleFile>)(Iterable<?>)(var7)) {
         this.a(var10, var8);
      }

      ArrayList var13 = new ArrayList();

      for(String var11 : (Iterable<String>)(Iterable<?>)(var8.keySet())) {
         HashMap var12 = new HashMap();
         if (!StringUtils.isNotBlank(var3) || var11.toLowerCase().indexOf(var3.toLowerCase()) != -1) {
            var12.put("name", var11);
            var12.put("path", var8.get(var11));
            var13.add(var12);
         }
      }

      this.a(var2, var13);
   }

   protected void a(RuleFile var1, Map var2) throws Exception {
      String var3 = FileManager.ins.loadContent(var1.getId());
      ByteArrayInputStream var4 = new ByteArrayInputStream(var3.getBytes("utf-8"));
      XXESAXReader var5 = new XXESAXReader();
      Document var6 = ((SAXReader)var5).read(var4);
      Element var7 = var6.getRootElement();

      for(Object var9 : var7.elements()) {
         if (var9 != null && var9 instanceof Element) {
            Element var10 = (Element)var9;
            String var11 = var10.getName();
            if (var11.equals("rule") || var11.equals("loop-rule")) {
               String var12 = var10.attributeValue("pended-group");
               String var13 = var1.getPath() + "(" + var1.getId() + ")";
               if (StringUtils.isNotBlank(var12)) {
                  if (var2.containsKey(var12)) {
                     String var14 = (String)var2.get(var12);
                     if (var14.indexOf(var13) == -1) {
                        var2.put(var12, var14 + "，" + var13);
                     }
                  } else {
                     var2.put(var12, var13);
                  }
               }
            }
         }
      }

      IOUtils.closeQuietly(var4);
   }

   public String url() {
      return "/ruleset";
   }
}
