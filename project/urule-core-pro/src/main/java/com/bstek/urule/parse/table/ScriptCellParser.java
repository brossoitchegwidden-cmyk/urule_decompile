package com.bstek.urule.parse.table;

import com.bstek.urule.model.table.ScriptCell;
import com.bstek.urule.parse.Parser;
import org.dom4j.Element;

public class ScriptCellParser implements Parser<ScriptCell> {
   public ScriptCell parse(Element element) {
      ScriptCell scriptCell = new ScriptCell();
      scriptCell.setRow(Integer.valueOf(element.attributeValue("row")));
      scriptCell.setCol(Integer.valueOf(element.attributeValue("col")));
      scriptCell.setRowspan(Integer.valueOf(element.attributeValue("rowspan")));
      scriptCell.setScript(element.getStringValue());
      return scriptCell;
   }

   @Override
   public boolean support(String name) {
      return name.equals("script-cell");
   }
}
