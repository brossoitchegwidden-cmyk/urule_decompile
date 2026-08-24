package com.bstek.urule.parse.table;

import com.bstek.urule.model.library.Datatype;
import com.bstek.urule.model.table.Cell;
import com.bstek.urule.parse.ActionParser;
import com.bstek.urule.parse.Parser;
import com.bstek.urule.parse.ValueParser;
import java.util.Collection;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class CellParser implements Parser<Cell>, ApplicationContextAware {
   private JointParser jointParser;
   private ValueParser valueParser;
   private Collection<ActionParser> actionParsers;

   public Cell parse(Element element) {
      Cell cell = new Cell();
      cell.setRow(Integer.valueOf(element.attributeValue("row")));
      cell.setCol(Integer.valueOf(element.attributeValue("col")));
      cell.setRowspan(Integer.valueOf(element.attributeValue("rowspan")));
      cell.setVariableLabel(element.attributeValue("var-label"));
      cell.setVariableName(element.attributeValue("var"));
      cell.setUuid(element.attributeValue("uuid"));
      cell.setKeyLabel(element.attributeValue("key-label"));
      cell.setKeyName(element.attributeValue("key-name"));
      cell.setKeyUuid(element.attributeValue("key-uuid"));
      cell.setKeyCategoryUuid(element.attributeValue("key-category-uuid"));
      String text = element.attributeValue("datatype");
      if (StringUtils.isNotBlank(text)) {
         cell.setDatatype(Datatype.valueOf(text));
      }

      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            String name = element2.getName();
            if (this.jointParser.support(name)) {
               cell.setJoint(this.jointParser.parse(element2));
            } else if (this.valueParser.support(name)) {
               cell.setValue(this.valueParser.parse(element2));
            } else {
               for (ActionParser actionParser : this.actionParsers) {
                  if (actionParser.support(name)) {
                     cell.setAction(actionParser.parse(element2));
                     break;
                  }
               }
            }
         }
      }

      return cell;
   }

   @Override
   public boolean support(String name) {
      return name.equals("cell");
   }

   public void setJointParser(JointParser jointParser) {
      this.jointParser = jointParser;
   }

   public void setValueParser(ValueParser valueParser) {
      this.valueParser = valueParser;
   }

   public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
      this.actionParsers = applicationContext.getBeansOfType(ActionParser.class).values();
   }
}
