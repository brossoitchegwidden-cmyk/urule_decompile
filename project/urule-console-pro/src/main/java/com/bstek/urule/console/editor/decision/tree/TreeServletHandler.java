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
   private VariableTreeNodeParser e;
   private ConditionTreeNodeParser f;
   private ActionTreeNodeParser g;

   public void init() {
      super.init();
      this.e = (VariableTreeNodeParser)Utils.getApplicationContext().getBean("urule.variableTreeNodeParser");
      this.f = (ConditionTreeNodeParser)Utils.getApplicationContext().getBean("urule.conditionTreeNodeParser");
      this.g = (ActionTreeNodeParser)Utils.getApplicationContext().getBean("urule.actionTreeNodeParser");
   }

   public void parseTreeNode(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      String var3 = var1.getParameter("xml");
      var3 = URLDecoder.decode(var3, "utf-8");
      Document var4 = DocumentHelper.parseText(var3);
      Element var5 = var4.getRootElement();
      if (this.e.support(var5.getName())) {
         VariableTreeNode var6 = this.e.parse(var5);
         StoreTools.setAttribute("_decision_tree_data", var6);
      } else if (this.f.support(var5.getName())) {
         StoreTools.setAttribute("_decision_tree_data", this.f.parse(var5));
      } else if (this.g.support(var5.getName())) {
         StoreTools.setAttribute("_decision_tree_data", this.g.parse(var5));
      }

   }

   public void loadTreeNode(HttpServletRequest var1, HttpServletResponse var2) throws Exception {
      Object var3 = StoreTools.getAttribute("_decision_tree_data");
      this.a(var2, var3);
   }

   public String url() {
      return "/tree";
   }
}
