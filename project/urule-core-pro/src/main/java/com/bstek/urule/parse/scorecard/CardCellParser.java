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
   private ValueParser valueParser;
   private JointParser jointParser;

   public CardCell parse(Element element) {
      CardCell cardCell = new CardCell();
      cardCell.setType(CellType.valueOf(element.attributeValue("type")));
      cardCell.setCol(Integer.valueOf(element.attributeValue("col")));
      cardCell.setRow(Integer.valueOf(element.attributeValue("row")));
      String text = element.attributeValue("datatype");
      if (StringUtils.isNotBlank(text)) {
         cardCell.setDatatype(Datatype.valueOf(text));
      }

      cardCell.setVariableName(element.attributeValue("var"));
      cardCell.setVariableLabel(element.attributeValue("var-label"));
      cardCell.setUuid(element.attributeValue("uuid"));
      cardCell.setKeyLabel(element.attributeValue("key-label"));
      cardCell.setKeyName(element.attributeValue("key-name"));
      cardCell.setKeyUuid(element.attributeValue("key-uuid"));
      cardCell.setWeight(element.attributeValue("weight"));

      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            if (this.valueParser.support(element2.getName())) {
               cardCell.setValue(this.valueParser.parse(element2));
            } else if (this.jointParser.support(element2.getName())) {
               cardCell.setJoint(this.jointParser.parse(element2));
            }
         }
      }

      return cardCell;
   }

   public void setJointParser(JointParser jointParser) {
      this.jointParser = jointParser;
   }

   public void setValueParser(ValueParser valueParser) {
      this.valueParser = valueParser;
   }

   @Override
   public boolean support(String name) {
      return name.equals("card-cell");
   }
}
