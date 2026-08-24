package com.bstek.urule.parse;

import com.bstek.urule.model.rule.Predefine;
import com.bstek.urule.model.rule.PredefineGroupDefinition;
import com.bstek.urule.model.rule.PredefineValueType;
import com.bstek.urule.model.rule.Value;
import com.bstek.urule.model.rule.lhs.Junction;
import java.util.ArrayList;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

public class PredefinesParser implements Parser<PredefineGroupDefinition> {
   protected ValueParser valueParser;
   private JunctionParser junctionParser;

   @Override
   public boolean support(String name) {
      return name.contentEquals("predefine-group");
   }

   public PredefineGroupDefinition parse(Element element) {
      PredefineGroupDefinition predefineGroupDefinition = new PredefineGroupDefinition();
      ArrayList items = new ArrayList();
      predefineGroupDefinition.setPredefines(items);
      String text = element.attributeValue("priority");
      if (StringUtils.isNotBlank(text)) {
         predefineGroupDefinition.setPriority(Integer.valueOf(text));
      }

      for (Object objectValue : element.elements()) {
         if (objectValue != null && objectValue instanceof Element) {
            Element element2 = (Element)objectValue;
            String name = element2.getName();
            if (name.contentEquals("predefine")) {
               Predefine predefine = new Predefine();
               items.add(predefine);
               predefine.setUuid(element2.attributeValue("uuid"));
               predefine.setName(element2.attributeValue("name"));
               predefine.setType(element2.attributeValue("type"));
               predefine.setValueType(PredefineValueType.valueOf(element2.attributeValue("value-type")));
               if (!predefine.getValueType().equals(PredefineValueType.none)) {
                  Element element3 = element2.element("value");
                  if (element3 != null) {
                     Value localValue = this.valueParser.parse(element3);
                     predefine.setValue(localValue);
                  }
               }

               for (Object objectValue2 : element2.elements()) {
                  if (objectValue2 != null && objectValue2 instanceof Element) {
                     Element element4 = (Element)objectValue2;
                     if (element4.getName().equals("and") || element4.getName().equals("or")) {
                        Junction junction = (Junction)this.junctionParser.parse(element4);
                        predefine.setJunction(junction);
                        break;
                     }
                  }
               }
            }
         }
      }

      return predefineGroupDefinition;
   }

   public void setValueParser(ValueParser valueParser) {
      this.valueParser = valueParser;
   }

   public void setJunctionParser(JunctionParser junctionParser) {
      this.junctionParser = junctionParser;
   }
}
