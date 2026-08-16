package com.bstek.urule.parse.table;

import com.bstek.urule.model.table.ScriptCell;
import com.bstek.urule.parse.Parser;
import org.dom4j.Element;

public class ScriptCellParser implements Parser<ScriptCell> {
   public ScriptCell parse(Element var1) {
      ScriptCell var2 = new ScriptCell();
      var2.setRow(Integer.valueOf(var1.attributeValue("row")));
      var2.setCol(Integer.valueOf(var1.attributeValue("col")));
      var2.setRowspan(Integer.valueOf(var1.attributeValue("rowspan")));
      var2.setScript(var1.getStringValue());
      return var2;
   }

   @Override
   public boolean support(String var1) {
      return var1.equals("script-cell");
   }
}
