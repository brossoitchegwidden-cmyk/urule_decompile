package com.bstek.urule.console.editor.decision.tree;

import com.bstek.urule.Utils;
import com.bstek.urule.console.ApiServletHandler;
import com.bstek.urule.console.editor.store.StoreTools;
import com.bstek.urule.console.xml.DocumentHelper;
import com.bstek.urule.model.decisiontree.VariableTreeNode;
import com.bstek.urule.parse.decisiontree.ActionTreeNodeParser;
import com.bstek.urule.parse.decisiontree.ConditionTreeNodeParser;
import com.bstek.urule.parse.decisiontree.VariableTreeNodeParser;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.dom4j.Document;
import org.dom4j.Element;

public class TreeServletHandler extends ApiServletHandler {
   public static final String DECISION_TREE_DATA = "_decision_tree_data";
   private VariableTreeNodeParser variableTreeNodeParser;
   private ConditionTreeNodeParser conditionTreeNodeParser;
   private ActionTreeNodeParser actionTreeNodeParser;

   public void init() {
      super.init();
      this.variableTreeNodeParser = (VariableTreeNodeParser)Utils.getApplicationContext().getBean("urule.variableTreeNodeParser");
      this.conditionTreeNodeParser = (ConditionTreeNodeParser)Utils.getApplicationContext().getBean("urule.conditionTreeNodeParser");
      this.actionTreeNodeParser = (ActionTreeNodeParser)Utils.getApplicationContext().getBean("urule.actionTreeNodeParser");
   }

   public void parseTreeNode(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      String parameter = req.getParameter("xml");
      parameter = URLDecoder.decode(parameter, "utf-8");
      Document text = DocumentHelper.parseText(parameter);
      Element rootElement = text.getRootElement();
      if (this.variableTreeNodeParser.support(rootElement.getName())) {
         VariableTreeNode variableTreeNode = this.variableTreeNodeParser.parse(rootElement);
         StoreTools.setAttribute("_decision_tree_data", variableTreeNode);
      } else if (this.conditionTreeNodeParser.support(rootElement.getName())) {
         StoreTools.setAttribute("_decision_tree_data", this.conditionTreeNodeParser.parse(rootElement));
      } else if (this.actionTreeNodeParser.support(rootElement.getName())) {
         StoreTools.setAttribute("_decision_tree_data", this.actionTreeNodeParser.parse(rootElement));
      }

   }

   public void loadTreeNode(HttpServletRequest req, HttpServletResponse resp) throws Exception {
      Object attribute = StoreTools.getAttribute("_decision_tree_data");
      this.writeObjectToJson(resp, attribute);
   }

   public String url() {
      return "/tree";
   }
}
