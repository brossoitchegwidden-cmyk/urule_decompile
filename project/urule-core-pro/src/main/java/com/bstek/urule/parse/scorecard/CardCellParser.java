package com.bstek.urule.parse.scorecard;

import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.scorecard.CardCell;
import com.bstek.urule.model.scorecard.CellType;
import com.bstek.urule.parse.Parser;
import com.bstek.urule.parse.ValueParser;
import com.bstek.urule.parse.table.JointParser;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

public class CardCellParser implements Parser<CardCell> {
   private ValueParser a;
   private JointParser b;

   public CardCell parse(Element var1) {
      CardCell var2 = new CardCell();
      var2.setType(CellType.valueOf(var1.attributeValue("type")));
      var2.setCol(Integer.valueOf(var1.attributeValue("col")));
      var2.setRow(Integer.valueOf(var1.attributeValue("row")));
      String var3 = var1.attributeValue("datatype");
      if (StringUtils.isNotBlank(var3)) {
         var2.setDatatype(Datatype.valueOf(var3));
      }

      var2.setVariableName(var1.attributeValue("var"));
      var2.setVariableLabel(var1.attributeValue("var-label"));
      var2.setUuid(var1.attributeValue("uuid"));
      var2.setKeyLabel(var1.attributeValue("key-label"));
      var2.setKeyName(var1.attributeValue("key-name"));
      var2.setKeyUuid(var1.attributeValue("key-uuid"));
      var2.setWeight(var1.attributeValue("weight"));

      for (Object var5 : var1.elements()) {
         if (var5 != null && var5 instanceof Element) {
            Element var6 = (Element)var5;
            if (this.a.support(var6.getName())) {
               var2.setValue(this.a.parse(var6));
            } else if (this.b.support(var6.getName())) {
               var2.setJoint(this.b.parse(var6));
            }
         }
      }

      return var2;
   }

   public void setJointParser(JointParser var1) {
      this.b = var1;
   }

   public void setValueParser(ValueParser var1) {
      this.a = var1;
   }

   @Override
   public boolean support(String var1) {
      return var1.equals("card-cell");
   }
}
